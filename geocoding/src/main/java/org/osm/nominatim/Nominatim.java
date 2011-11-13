package org.osm.nominatim;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import se.kodapan.geography.domain.AddressComponent;
import se.kodapan.geography.domain.Coordinate;
import se.kodapan.geography.domain.CoordinateImpl;
import se.kodapan.geography.domain.EnvelopeImpl;
import se.kodapan.geography.geocoding.*;

import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author kalle
 * @since 2011-11-13 03:39
 */
public class Nominatim extends Geocoder {

  @Override
  public int getMaximumResultsReturned() {
    return 10;  // todo  setting
  }

  @Override
  public Geocoding geocode(Coordinate coordinate, String preferredLanguage) throws Exception {
    return null;  // todo  reverse
  }

  @Override
  protected Geocoding doGeocode(Request request) throws Exception {

    StringBuilder urlFactory = new StringBuilder(512);
    urlFactory.append("http://nominatim.openstreetmap.org/search/");
    urlFactory.append(URLEncoder.encode(request.getTextQuery(), "UTF8"));
    urlFactory.append("?format=json&addressdetails=1");

    URL url = new URL(urlFactory.toString());
    URLConnection connection = url.openConnection();

    Geocoding geocoding = new Geocoding();

    JSONParser parser = new JSONParser();

    Reader reader = new InputStreamReader(connection.getInputStream(), "UTF8");
    JSONArray results = (JSONArray) parser.parse(reader);
    reader.close();

    if (results.size() == 0) {
      geocoding.setSuccess(false);
    } else {
      geocoding.setSuccess(results.size() == 1);
      for (int i = 0; i < results.size(); i++) {

        Result result = new ResultImpl();

        JSONObject jsonResult = (JSONObject) results.get(i);

        result.setSource("osm.org nominatim");

        String licence = ((String) jsonResult.remove("licence"));
        result.setLicence(licence);

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


        JSONArray boundingBox = (JSONArray) jsonResult.remove("boundingbox");
        if (boundingBox != null) {
          result.setViewPort(new EnvelopeImpl(new CoordinateImpl(Double.valueOf((String) boundingBox.get(0)), Double.valueOf((String) boundingBox.get(2))), new CoordinateImpl(Double.valueOf((String) boundingBox.get(1)), Double.valueOf((String) boundingBox.get(3)))));
        }

        JSONObject address = (JSONObject) jsonResult.remove("address");
        if (address != null) {
          for (Map.Entry<String, String> ae : ((Set<Map.Entry<String, String>>) address.entrySet())) {
            result.getAddressComponents().add(new AddressComponent(ae.getValue(), ae.getKey()));
          }
        }

        JSONArray polygonPoints = (JSONArray) jsonResult.remove("polygonPoints");
        if (polygonPoints != null) {
          List<Coordinate> polygonCoordinates = new ArrayList<Coordinate>(polygonPoints.size());
          for (int ppsi = 0; ppsi < polygonPoints.size(); ppsi++) {
            JSONArray polygonPoint = (JSONArray) polygonPoints.get(i);
            polygonCoordinates.add(new CoordinateImpl((Number) polygonPoint.get(1), (Number) polygonPoint.get(0)));
          }
          if (!polygonCoordinates.isEmpty()) {
            EnvelopeImpl envelope = new EnvelopeImpl();
            envelope.addBounds(polygonCoordinates);
            geocoding.setViewPort(envelope);
          }

        }

        if (!jsonResult.isEmpty()) {
          log.error("Unknown data left in result: " + jsonResult.toJSONString());
        }


        geocoding.getResults().add(result);
      }


    }


    return geocoding;

  }


}
