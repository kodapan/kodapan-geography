package se.kodapan.geography.geocoding;

import org.apache.commons.io.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.kodapan.geography.domain.*;
import se.kodapan.geography.geocoding.geocoding.Geocoding;
import se.kodapan.geography.geocoding.geocoding.Result;
import se.kodapan.geography.geocoding.geocoding.ResultImpl;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author kalle
 * @since 2011-11-13 03:39
 */
public class Nominatim extends Geocoder {

  private static final Logger log = LoggerFactory.getLogger(Nominatim.class);
  private String emailAddress;

  @Override
  public String getDefaultLicense() {
    return "Data Copyright OpenStreetMap Contributors, Some Rights Reserved. CC-BY-SA 2.0.";
  }

  @Override
  public String getName() {
    return "nominatim.openstreetmap.org";
  }

  @Override
  public String getVersion() {
    return "1";
  }

  @Override
  public int getMaximumResultsReturned() {
    return 10;  // todo  setting
  }

  @Override
  public Geocoding reverse(CoordinateQuery query) throws Exception {


    DecimalFormat df = new DecimalFormat("0.##############");

    StringBuilder urlFactory = new StringBuilder(512);
    urlFactory.append("http://nominatim.openstreetmap.org/reverse?format=json&addressdetails=1");
    urlFactory.append("&lat=").append(df.format(query.getCoordinate().getLatitude()));
    urlFactory.append("&lon=").append(df.format(query.getCoordinate().getLongitude()));
    if (getEmailAddress() != null) {
      urlFactory.append("&email=").append(URLEncoder.encode(emailAddress, "UTF8"));
    }
    if (query.getPreferredResponseLanguage() != null) {
      urlFactory.append("&acceptLanguage=").append(query.getPreferredResponseLanguage());
    }

    URLConnection connection = new URL(urlFactory.toString()).openConnection();


    InputStream inputStream = connection.getInputStream();
    try {
      return parseReverseServerResponse(inputStream);
    } finally {
      inputStream.close();
    }

  }

  @Override
  public Geocoding geocode(TextQuery query) throws Exception {

    StringBuilder urlFactory = new StringBuilder(512);
    urlFactory.append("http://nominatim.openstreetmap.org/search?format=json&addressdetails=1&polygon=1");
    if (query.getBounds() != null) {
      urlFactory.append("&viewboxlbrt=");
      Envelope envelope = new EnvelopeImpl();
      envelope.addBounds(query.getBounds());
      urlFactory.append(envelope.getSouthwest().getLatitude()).append(",");
      urlFactory.append(envelope.getSouthwest().getLongitude()).append(",");
      urlFactory.append(envelope.getNortheast().getLatitude()).append(",");
      urlFactory.append(envelope.getNortheast().getLongitude());
    }
    urlFactory.append("&q=").append(URLEncoder.encode(query.getText(), "UTF8"));


    if (getEmailAddress() != null) {
      urlFactory.append("&email=").append(URLEncoder.encode(emailAddress, "UTF8"));
    }
    if (query.getPreferredResponseLanguage() != null) {
      urlFactory.append("&acceptLanguage=").append(query.getPreferredResponseLanguage());
    }

    URLConnection connection = new URL(urlFactory.toString()).openConnection();

    Geocoding geocoding = new Geocoding();

    Reader reader = new InputStreamReader(connection.getInputStream(), "UTF8");
    StringWriter jsonFactory = new StringWriter(49152);
    IOUtils.copy(reader, jsonFactory);
    reader.close();
    String json = jsonFactory.toString();
    reader = new StringReader(json);

    geocoding.setServerResponse(jsonFactory.toString());

    JSONParser parser = new JSONParser();


    JSONArray results = (JSONArray) parser.parse(reader);
    reader.close();

    if (results.size() == 0) {
      geocoding.setSuccess(false);
    } else {
      geocoding.setSuccess(results.size() == 1);
      for (Object result : results) {
        geocoding.getResults().add(resultFactory((JSONObject) result));
      }


    }


    return geocoding;
  }


  private Result resultFactory(JSONObject jsonResult) {
    Result result = new ResultImpl();
    result.setSource(sourceFactory());

    String licence = ((String) jsonResult.remove("licence"));
    result.getSource().setLicense(licence);
    // todo place_id can be a string error message, at least if reverse geocoding out of area
    Number placeIdentity = Long.valueOf(((String) jsonResult.remove("place_id")));
    String placeClass = ((String) jsonResult.remove("class"));
    String placeType = ((String) jsonResult.remove("type"));
    String osmIdentity = ((String) jsonResult.remove("osm_id"));

    String osmType = ((String) jsonResult.remove("osm_type"));

    String iconURL = ((String) jsonResult.remove("icon"));

    String displayName = ((String) jsonResult.remove("display_name"));
    result.getAddressComponents().setFormattedAddress(displayName);

    Number latitude = Double.valueOf((String) jsonResult.remove("lat"));
    Number longitude = Double.valueOf((String) jsonResult.remove("lon"));
    result.setLocation(new CoordinateImpl(latitude, longitude));


    JSONObject address = (JSONObject) jsonResult.remove("address");
    if (address != null) {
      for (Map.Entry<String, String> ae : ((Set<Map.Entry<String, String>>) address.entrySet())) {
        result.getAddressComponents().add(new AddressComponent(ae.getValue(), ae.getKey()));
      }
    }

    JSONArray boundingBox = (JSONArray) jsonResult.remove("boundingbox");
    if (boundingBox != null) {
      result.setBounds(new EnvelopeImpl(new CoordinateImpl(Double.valueOf((String) boundingBox.get(0)), Double.valueOf((String) boundingBox.get(2))), new CoordinateImpl(Double.valueOf((String) boundingBox.get(1)), Double.valueOf((String) boundingBox.get(3)))));
    }

    JSONArray polygonPoints = (JSONArray) jsonResult.remove("polygonpoints");
    if (polygonPoints != null) {
      if (result.getBounds() != null) {
        log.warn("Both boundingbox and polygon in response! What does this mean? Using polygon.");
      }
      List<Coordinate> polygonCoordinates = new ArrayList<Coordinate>(polygonPoints.size());
      for (int ppsi = 0; ppsi < polygonPoints.size(); ppsi++) {
        JSONArray polygonPoint = (JSONArray) polygonPoints.get(ppsi);
        polygonCoordinates.add(new CoordinateImpl(Double.valueOf(polygonPoint.get(1).toString()), Double.valueOf(polygonPoint.get(0).toString())));
      }
      if (!polygonCoordinates.isEmpty()) {
        EnvelopeImpl envelope = new EnvelopeImpl();
        envelope.addBounds(polygonCoordinates);
        result.setBounds(envelope);
      }

    }

    if (!jsonResult.isEmpty()) {
      log.error("Unknown data left in result: " + jsonResult.toJSONString());
    }

    return result;
  }

  @Override
  public Geocoding parseReverseServerResponse(InputStream inputStream) throws Exception {
    JSONParser parser = new JSONParser();
    Geocoding geocoding = new Geocoding();

    Reader reader = new InputStreamReader(inputStream, "UTF8");
    StringWriter jsonFactory = new StringWriter(49152);
    IOUtils.copy(reader, jsonFactory);
    reader.close();
    String json = jsonFactory.toString();
    reader = new StringReader(json);

    geocoding.setServerResponse(jsonFactory.toString());

    geocoding.getResults().add(resultFactory((JSONObject) parser.parse(reader)));

    if (!geocoding.getResults().isEmpty()) {
      geocoding.setSuccess(true);
    }

    return geocoding;
  }

  @Override
  public Geocoding parseGeocodeServerResponse(InputStream inputStream) throws Exception {

    JSONParser parser = new JSONParser();
    Geocoding geocoding = new Geocoding();

    Reader reader = new InputStreamReader(inputStream, "UTF8");
    StringWriter jsonFactory = new StringWriter(49152);
    IOUtils.copy(reader, jsonFactory);
    reader.close();
    String json = jsonFactory.toString();
    reader = new StringReader(json);

    geocoding.setServerResponse(jsonFactory.toString());

    JSONArray array = (JSONArray) parser.parse(reader);
    for (int i=0; i<array.size(); i++) {
      geocoding.getResults().add(resultFactory((JSONObject)array.get(i)));
    }

    if (!geocoding.getResults().isEmpty()) {
      geocoding.setSuccess(true);
    }

    return geocoding;

  }

  public String getEmailAddress() {
    return emailAddress;
  }

  public void setEmailAddress(String emailAddress) {
    this.emailAddress = emailAddress;
  }
}
