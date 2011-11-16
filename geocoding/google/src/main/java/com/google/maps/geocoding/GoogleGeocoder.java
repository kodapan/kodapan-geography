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

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLFilterImpl;
import org.xml.sax.helpers.XMLReaderFactory;
import se.kodapan.collections.SetMap;
import se.kodapan.io.http.HttpGetInputStream;
import se.kodapan.io.http.HttpGetReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.sax.SAXSource;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * http://code.google.com/intl/sv-SE/apis/maps/documentation/geocoding/index.html#XMLParsing
 * http://code.google.com/intl/sv-SE/apis/maps/documentation/geocoding/index.html
 *
 * @author kalle
 * @since 2010-maj-09 05:19:50
 */
public class GoogleGeocoder {

  private static final Logger log = LoggerFactory.getLogger(GoogleGeocoder.class);

  private static final String BASE_URL = "http://maps.google.com/maps/api/geocode/xml?";



  private long millisecondsBetweenQueries = TimeUnit.SECONDS.toMillis(1);
  private long lastRequest = System.currentTimeMillis();

  private long queryOverLimitTimeStamp = 0l;
  private long queryOverLimitDelay = TimeUnit.HOURS.toMillis(1);

  public GoogleGeocoder() {
    open();
  }

  public String getName() {
    return "maps.google.com";
  }

  public String getVersion() {
    return "1";
  }

  private boolean opened;

  public synchronized void open() {
    if (opened) {
      return;
    }
    opened = true;
  }


  private synchronized void delay(Request nextRequest) throws IOException {

    long now = System.currentTimeMillis();
    if (queryOverLimitTimeStamp + queryOverLimitDelay > now) {
      throw new OverQueryLimitException("Since " + new Date(queryOverLimitTimeStamp));
    }
    if (now - millisecondsBetweenQueries < lastRequest) {
      try {
        log.info("Sleeping for " + (lastRequest + millisecondsBetweenQueries - now) + " milliseconds. Next query: " + nextRequest);
        Thread.sleep(lastRequest + millisecondsBetweenQueries - now);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
    lastRequest = System.currentTimeMillis();
  }

  public GeocodeResponse geocode(Request request) throws IOException {
    if (!opened) {
      open();
    }

    if (log.isInfoEnabled()) {
      log.info("Geocoding " + request + "");
    }

    String url = urlFactory(request);

    delay(request);

    URI uri;
    try {
      uri = new URI(url);
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }

    return parseServerResponse(new HttpGetInputStream(uri));

  }

  public String urlFactory(Request request) throws UnsupportedEncodingException {
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

    if (log.isDebugEnabled()) {
      log.debug("query=" + query.toString());
    }


    return BASE_URL + query.toString();
  }

  public GeocodeResponse parseServerResponse(InputStream inputStream) throws IOException {
    try {

      JAXBContext jc = JAXBContext.newInstance(getClass().getPackage().getName());
      Unmarshaller u = jc.createUnmarshaller();
      XMLReader reader = XMLReaderFactory.createXMLReader();
      NamespaceFilter inFilter = new NamespaceFilter(null, false);
      inFilter.setParent(reader);

      ByteArrayOutputStream serverResponse = new ByteArrayOutputStream();
      IOUtils.copy(inputStream, serverResponse);
      StringWriter tmp = new StringWriter(serverResponse.size());
      IOUtils.copy(new InputStreamReader(new ByteArrayInputStream(serverResponse.toByteArray()), "UTF8"), tmp);

      SAXSource source = new SAXSource(inFilter, new InputSource(new ByteArrayInputStream(serverResponse.toByteArray())));

      Object o = u.unmarshal(source);
      GeocodeResponse response = ((JAXBElement<GeocodeResponse>) o).getValue();

      response.setServerResponse(tmp.toString());


      if ("OVER_QUERY_LIMIT".equals(response.getStatus())) {
        // remove cache file, this is an invalid response!
        queryOverLimitTimeStamp = System.currentTimeMillis();
        throw new OverQueryLimitException();
      }


      if (!"OK".equals(response.getStatus())
          && !"ZERO_RESULTS".equals(response.getStatus())) {
        // remove cache file, this is an invalid response!
      }


      return response;
    } catch (JAXBException e) {
      throw new RuntimeException(e);
    } catch (SAXException e) {
      throw new RuntimeException(e);
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


  public static SetMap<String, AddressComponent> getAddressComponentsByType(Result result) {
    SetMap<String, AddressComponent> ret = new SetMap<String, AddressComponent>();
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

}