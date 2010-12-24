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
package se.kodapan.geography.geocoding;

import com.google.maps.geocoding.*;
import com.google.maps.geocoding.AddressComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.kodapan.geography.polygon.*;
import se.kodapan.geography.polygon.Envelope;

import java.io.IOException;

/**
 * @author kalle
 * @since 2010-jun-22 19:38:40
 */
public class GoogleGeocoder extends Geocoder {

  private static final Logger log = LoggerFactory.getLogger(GoogleGeocoder.class);
  private static com.google.maps.geocoding.GoogleGeocoder defaultGeocoder = new com.google.maps.geocoding.GoogleGeocoder();

  private com.google.maps.geocoding.GoogleGeocoder geocoder;

  public GoogleGeocoder() {
    this.geocoder = defaultGeocoder;
  }

  public GoogleGeocoder(com.google.maps.geocoding.GoogleGeocoder geocoder) {
    this.geocoder = geocoder;
  }

  @Override
  public int getMaximumResultsReturned() {
    return 10;
  }

  public Geocoding geocode(Coordinate coordinate, String preferredLanguage) throws Exception {

    Geocoding geocoding = new Geocoding();

    // create request
    com.google.maps.geocoding.Request googleRequest = new com.google.maps.geocoding.Request();
    googleRequest.setLatLng(new LatLng(coordinate.getLatitude(), coordinate.getLongitude()));
    googleRequest.setLanguage(preferredLanguage);

    sendRequest(geocoding, googleRequest);

    return geocoding;

  }

  @Override
  protected Geocoding doGeocode(Request request) throws Exception {

    Geocoding geocoding = new Geocoding();

    // create request
    com.google.maps.geocoding.Request googleRequest = new com.google.maps.geocoding.Request();
    googleRequest.setAddress(request.getTextQuery());
    googleRequest.setLanguage(request.getLanguage());
    if (request.getBounds() != null) {
      EnvelopeImpl envelope = new EnvelopeImpl();
      envelope.addBounds(request.getBounds());
      googleRequest.setBounds(new com.google.maps.geocoding.Envelope(
          new LatLng(envelope.getSouthWest().getLatitude(), envelope.getSouthWest().getLongitude()),
          new LatLng(envelope.getNorthEast().getLatitude(), envelope.getNorthEast().getLongitude())));
    }

    sendRequest(geocoding, googleRequest);

    return geocoding;

  }

  private void sendRequest(Geocoding geocoding, com.google.maps.geocoding.Request googleRequest) throws IOException {
    GeocodeResponse googleResponse = geocoder.geocode(googleRequest);
    geocoding.setServerResponse(googleResponse);


    // parse response
    if (googleResponse.getResults().size() == 0
        || !"OK".equals(googleResponse.getStatus())) {
      geocoding.setSuccess(false);

    } else {
      parseResponse(geocoding, googleResponse);
    }

    // reverse geocoding return multiple responses. merge these to a single response.
    if (googleRequest.getLatLng() != null
        && geocoding.getResults() != null
        && !geocoding.getResults().isEmpty()) {
      Result endProduct = new ResultImpl();
      for (Result result : geocoding.getResults()) {
        for (se.kodapan.geography.domain.AddressComponent component : result.getAddressComponents()) {
          endProduct.getAddressComponents().add(component);
        }
        endProduct.setScore(endProduct.getScore() + result.getScore());
      }
      geocoding.getResults().add(0, endProduct);
      geocoding.setSuccess(true);
    }

  }

  private void parseResponse(Geocoding geocoding, GeocodeResponse googleResponse) {
    for (com.google.maps.geocoding.Result googleResult : googleResponse.getResults()) {

      // todo do something with partial matches
//      if (googleResult.isPartialMatch()) {
//        if (log.isInfoEnabled()) {
//          log.info("Skipping partial match result " + googleResult);
//        }
//        continue;
//      }

      final ResultImpl result = new ResultImpl();

      result.getAddressComponents().setFormattedAddress(googleResult.getFormattedAddress());
      result.setPrecision(Precision.valueOf(googleResult.getGeometry().getLocationType()));

      if (googleResult.getGeometry().getLocation() != null) {
        result.setLocation(new CoordinateImpl(googleResult.getGeometry().getLocation().getLat(), googleResult.getGeometry().getLocation().getLng()));
      }

      if (googleResult.getGeometry().getBounds() != null) {
        Envelope envelope = new CoordinatedEnvelope(){
          @Override
          public String getPolygonName() {
            return result.getAddressComponents().getFormattedAddress();
          }
        };
        envelope.addBounds(googleResult.getGeometry().getBounds().getNortheast().getLat(), googleResult.getGeometry().getBounds().getNortheast().getLng());
        envelope.addBounds(googleResult.getGeometry().getBounds().getSouthwest().getLat(), googleResult.getGeometry().getBounds().getSouthwest().getLng());
        result.setBounds(envelope);
      } else {
        result.setBounds(new AbstractSingleCoordinatePolygon(result.getLocation()){
          @Override
          public String getPolygonName() {
            return result.getAddressComponents().getFormattedAddress();
          }
        });
      }

      if (googleResult.getGeometry().getViewport() != null) {
        EnvelopeImpl envelope = new EnvelopeImpl();
        envelope.addBounds(googleResult.getGeometry().getViewport().getNortheast().getLat(), googleResult.getGeometry().getViewport().getNortheast().getLng());
        envelope.addBounds(googleResult.getGeometry().getViewport().getSouthwest().getLat(), googleResult.getGeometry().getViewport().getSouthwest().getLng());
        result.setViewPort(envelope);
      }

      if (result.getViewPort() == null && result.getBounds() != null) {
        result.setViewPort(result.getBounds());
      }


      // this should be handled with a filter!
//      if (request.getBounds() != null) {
//        boolean keep;
//        if (result.getLocation() != null) {
//          keep = request.getBounds().contains(result.getLocation());
//        } else if (result.getBounds() != null) {
//          keep = request.getBounds().contains(result.getBounds());
//        } else if (result.getViewPort() != null) {
//          keep = request.getBounds().contains(result.getViewPort());
//        } else {
//          throw new RuntimeException();
//        }
//
//        if (!keep) {
//          if (log.isInfoEnabled()) {
//            log.info("Filtering out " + result + " as it does not match " + request.getBounds());
//          }
//          continue;
//        }
//      }


      for (AddressComponent googleComponent : googleResult.getAddressComponents()) {
        se.kodapan.geography.domain.AddressComponent addressComponent = new se.kodapan.geography.domain.AddressComponent();
        addressComponent.setLongName(googleComponent.getLongName());
        addressComponent.setShortName(googleComponent.getShortName());
        for (String type : googleComponent.getTypes()) {
          se.kodapan.geography.domain.AddressComponentType enumType = null;
          try {
            enumType = se.kodapan.geography.domain.AddressComponentType.valueOf(type);
          } catch (Exception e) {
            enumType = se.kodapan.geography.domain.AddressComponentType.unknown;
          }
          addressComponent.getTypes().add(enumType);
        }
        result.getAddressComponents().add(addressComponent);
      }


      // todo well, this is not really needed, but it would be nice.
//    // geocode address components
//
//
//    for (int startIndex = result.getAddressComponents().size() - 1; startIndex >= 0; startIndex--) {
//      se.kodapan.geography.geocoding.AddressComponent addressComponent = result.getAddressComponents().get(startIndex);
//      StringBuilder addressTextQuery = new StringBuilder();
//      for (int i = startIndex; i < result.getAddressComponents().size(); i++) {
//        if (addressTextQuery.length() > 0) {
//          addressTextQuery.append(", ");
//        }
//        addressTextQuery.append(result.getAddressComponents().get(i).getLongName());
//      }
//
//
//      Geocoding addressComponentGeocoding = new GoogleGeocoder(geocoder, addressTextQuery.toString(), cache).geocode();
//      addressComponent.setGeocoding(addressComponentGeocoding);
//
//      if (addressComponentGeocoding != geocoding) {
//        // filter out any response that has nothing to do with the parent address component
//        ProximityScorer filter = new ProximityScorer(addressComponentGeocoding);
//        for (int i = startIndex + 1; i < result.getAddressComponents().size(); i++) {
//          filter.getAreas().add(result.getAddressComponents().get(i));
//        }
//
//        addressComponentGeocoding = filter.geocode();
//
//        System.currentTimeMillis();
//      }
//
//
//    }


      geocoding.getResults().add(result);
    }

    geocoding.setSuccess(geocoding.getResults().size() == 1);
  }


  private void addBoundsToEnvelope(AbstractEnvelope envelope, com.google.maps.geocoding.Result result) {
    com.google.maps.geocoding.Envelope resultGeometryEnvelope;
    if (result.getGeometry().getBounds() != null) {
      resultGeometryEnvelope = result.getGeometry().getBounds();
    } else if (result.getGeometry().getViewport() != null) {
      resultGeometryEnvelope = result.getGeometry().getViewport();
    } else {
      throw new RuntimeException();
    }

    envelope.addBounds(resultGeometryEnvelope.getNortheast().getLat(), resultGeometryEnvelope.getNortheast().getLng());
    envelope.addBounds(resultGeometryEnvelope.getSouthwest().getLat(), resultGeometryEnvelope.getSouthwest().getLng());

  }

//  public Envelope findArea(String address, double maximumKilometersDiagonal, double maximumKilometersDiagonalOnEstimated) throws Exception {
//    GeocodeResponseType response = geocoder.geocode(address);
//
//    Envelope envelope = new Envelope();
//    envelope.setName(address);
//
//    for (ResultType result : response.getResult()) {
//
//      addBoundsToEnvelope(envelope, result);
//
//
//    }
//
//    if (response.getResult().size() == 0) {
//      return null;
//
//    }
//
//    double kilometersDiagonal = GeoUtil.arcDistance(envelope.getNorthEast(), envelope.getSouthWest());
//    if (response.getResult().size() == 1) {
//
//      if (maximumKilometersDiagonal < kilometersDiagonal) {
//        return null;
//      }
//
//    } else /*if (response.getResult().size() > 1)*/ {
//
//      // the envelope is representing multiple hits.
//
//      if (kilometersDiagonal > maximumKilometersDiagonalOnEstimated) {
//        // todo setting;
//        return null;
//      }
//
//    }
//
//    // use this envelope
//
//    return envelope;
//
//
//  }


  public com.google.maps.geocoding.GoogleGeocoder getGeocoder() {
    return geocoder;
  }
}
