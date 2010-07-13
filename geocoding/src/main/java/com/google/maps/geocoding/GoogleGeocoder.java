/*
   Copyright 2010 Kodapan

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package com.google.maps.geocoding;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLFilterImpl;
import org.xml.sax.helpers.XMLReaderFactory;
import se.kodapan.collections.MapSet;
import se.kodapan.io.HttpGetReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.sax.SAXSource;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * http://code.google.com/intl/sv-SE/apis/maps/documentation/geocoding/index.html#XMLParsing
 * <p/>
 * http://code.google.com/intl/sv-SE/apis/maps/documentation/geocoding/index.html
 *
 * @author kalle
 * @since 2010-maj-09 05:19:50
 */
public class GoogleGeocoder {

  private static final Logger log = LoggerFactory.getLogger(GoogleGeocoder.class);

  private static final String BASE_URL = "http://maps.google.com/maps/api/geocode/xml?";


  private long millisecondsBetweenQueries = 1000;
  private long lastRequest = System.currentTimeMillis();

  private long queryOverLimitTimeStamp = 0l;

  private File cachePath;

  public GoogleGeocoder() {
  }


  public GoogleGeocoder(File cachePath) {
    this.cachePath = cachePath;
    open();
  }

  private boolean opened;

  public synchronized void open() {
    if (opened) {
      return;
    }
    opened = true;
    if (cachePath == null) {
      cachePath = new File("cache/" + getClass().getName());
    }
    if (!cachePath.exists()) {
      log.warn("Creating path " + cachePath.getAbsolutePath());
      if (!cachePath.mkdirs()) {
        log.error("Could not create path " + cachePath.getAbsolutePath());
      }
    }

  }


  private synchronized void delay() {

    long now = System.currentTimeMillis();
    if (queryOverLimitTimeStamp + 60000 > now) {
      throw new RuntimeException("QUERY_OVER_LIMIT since " + new Date(queryOverLimitTimeStamp));
    }
    if (now - millisecondsBetweenQueries < lastRequest) {
      try {
        log.info("Sleeping for " + (lastRequest + millisecondsBetweenQueries - now) + " milliseconds.");
        Thread.sleep(lastRequest + millisecondsBetweenQueries - now);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
    lastRequest = System.currentTimeMillis();
  }


  public GeocodeResponse geocode(String address) throws IOException {
    return geocode(new Request(address));
  }

  public GeocodeResponse geocode(Request request) throws IOException {
    if (!opened) {
      open();
    }

    log.info("Geocoding \"" + request + "\"");

    StringBuilder query = new StringBuilder();
    if (request.getAddress() != null) {
      query.append("address=").append(URLEncoder.encode(request.getAddress(), "UTF-8"));
    } else if (request.getLatLng() != null) {
      query.append("latlng=");
      query.append(String.valueOf(request.getLatLng().getLat()));
      query.append(",");
      query.append(String.valueOf(request.getLatLng().getLng()));
    } else {
      throw new RuntimeException();
    }

    query.append("&sensor=").append(String.valueOf(request.isSensor()));

    if (request.getLanguage() != null) {
      query.append("&language=").append(URLEncoder.encode(request.getLanguage(), "UTF-8"));
    }
    if (request.getRegion() != null) {
      query.append("&region=").append(URLEncoder.encode(request.getRegion(), "UTF-8"));
    }
    if (request.getBounds() != null) {
      query.append("&bounds=");
      query.append(String.valueOf(request.getBounds().getNortheast().getLat()));
      query.append(",");
      query.append(String.valueOf(request.getBounds().getNortheast().getLng()));
      query.append(URLEncoder.encode("|", "UTF-8"));
      query.append(String.valueOf(request.getBounds().getSouthwest().getLat()));
      query.append(",");
      query.append(String.valueOf(request.getBounds().getSouthwest().getLng()));
    }

    if (log.isInfoEnabled()) {
      log.info("query=" + query.toString());
    }


    String url = BASE_URL + query.toString();

    boolean createdNewCache = false;
    File cachedFile = new File(cachePath, query.toString());
    if (!cachedFile.exists()) {

      createdNewCache = true;

      delay();

      try {
        BufferedReader br = new BufferedReader(new HttpGetReader(url));

        Writer cached = new OutputStreamWriter(new GZIPOutputStream(new FileOutputStream(cachedFile)), "UTF-8");
        cached.write(String.valueOf(System.currentTimeMillis()));
        cached.write("\n");

        String line;
        while ((line = br.readLine()) != null) {
          cached.write(line);
          cached.write("\n");
        }
        cached.close();
        br.close();

      } catch (URISyntaxException e) {
        throw new RuntimeException(e);
      }

    }

    BufferedReader kml = new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(cachedFile)), "UTF-8"));
    kml.readLine(); // date created. todo allow for max age. in future perhaps try to keep fresh if there are resources left over.

    try {

      JAXBContext jc = JAXBContext.newInstance(getClass().getPackage().getName());

      Unmarshaller u = jc.createUnmarshaller();

      XMLReader reader = XMLReaderFactory.createXMLReader();

      NamespaceFilter inFilter = new NamespaceFilter(null, false);
      inFilter.setParent(reader);

      InputSource is = new InputSource(kml);
      SAXSource source = new SAXSource(inFilter, is);

      Object o = u.unmarshal(source);
      GeocodeResponse response = ((JAXBElement<GeocodeResponse>) o).getValue();

      if ("OVER_QUERY_LIMIT".equals(response.getStatus())) {
        // remove cache file, this is an invalid response!
        if (!cachedFile.delete()) {
          log.error("Could node delete file " + cachedFile.getAbsolutePath());
        }
        throw new RuntimeException("OVER_QUERY_LIMIT");
      }

      if (response.getResults() == null || response.getResults().size() == 0) {
        log.info("Failed to geocode \"" + request + "\": " + response.getStatus());
      } else {
        log.info(response.getResults().size() + " hits from " + request + "");
      }

      if (createdNewCache
          && !"OK".equals(response.getStatus())
          && !"ZERO_RESULTS".equals(response.getStatus())) {
        // remove cache file, this is an invalid response!
        if (!cachedFile.delete()) {
          log.error("Could node delete file " + cachedFile.getAbsolutePath());
        }
      }


      return response;
    } catch (JAXBException e) {
      throw new RuntimeException(e);
    } catch (SAXException e) {
      throw new RuntimeException(e);
    } finally {
      kml.close();
    }

  }


  public class NamespaceFilter extends XMLFilterImpl {

    private String usedNamespaceUri;
    private boolean addNamespace;

    //State variable
    private boolean addedNamespace = false;

    public NamespaceFilter(String namespaceUri,
                           boolean addNamespace) {
      super();

      if (addNamespace)
        this.usedNamespaceUri = namespaceUri;
      else
        this.usedNamespaceUri = "";
      this.addNamespace = addNamespace;
    }


    @Override
    public void startDocument() throws SAXException {
      super.startDocument();
      if (addNamespace) {
        startControlledPrefixMapping();
      }
    }


    @Override
    public void startElement(String arg0, String arg1, String arg2,
                             Attributes arg3) throws SAXException {

      super.startElement(this.usedNamespaceUri, arg1, arg2, arg3);
    }

    @Override
    public void endElement(String arg0, String arg1, String arg2)
        throws SAXException {

      super.endElement(this.usedNamespaceUri, arg1, arg2);
    }

    @Override
    public void startPrefixMapping(String prefix, String url)
        throws SAXException {


      if (addNamespace) {
        this.startControlledPrefixMapping();
      } else {
        //Remove the namespace, i.e. don´t call startPrefixMapping for parent!
      }

    }

    private void startControlledPrefixMapping() throws SAXException {

      if (this.addNamespace && !this.addedNamespace) {
        //We should add namespace since it is set and has not yet been done.
        super.startPrefixMapping("", this.usedNamespaceUri);

        //Make sure we dont do it twice
        this.addedNamespace = true;
      }
    }

  }


  public static MapSet<String, AddressComponent> getAddressComponentsByType(Result result) {
    MapSet<String, AddressComponent> ret = new MapSet<String, AddressComponent>();
    for (AddressComponent addressComponent : result.getAddressComponents()) {
      for (String type : addressComponent.getTypes()) {
        ret.add(type, addressComponent);
      }
    }
    return ret;
  }

  public long getMillisecondsBetweenQueries() {
    return millisecondsBetweenQueries;
  }

  public void setMillisecondsBetweenQueries(long millisecondsBetweenQueries) {
    this.millisecondsBetweenQueries = millisecondsBetweenQueries;
  }

  public long getLastRequest() {
    return lastRequest;
  }

  public void setLastRequest(long lastRequest) {
    this.lastRequest = lastRequest;
  }

  public long getQueryOverLimitTimeStamp() {
    return queryOverLimitTimeStamp;
  }

  public void setQueryOverLimitTimeStamp(long queryOverLimitTimeStamp) {
    this.queryOverLimitTimeStamp = queryOverLimitTimeStamp;
  }

  public File getCachePath() {
    return cachePath;
  }

  public void setCachePath(File cachePath) {
    this.cachePath = cachePath;
  }
}