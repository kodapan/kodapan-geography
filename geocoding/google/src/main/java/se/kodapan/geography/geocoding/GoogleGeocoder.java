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

import se.kodapan.geography.geocoding.google.AddressComponent;
import se.kodapan.geography.geocoding.google.GeocodeResponse;
import se.kodapan.geography.geocoding.google.LatLng;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.kodapan.geography.domain.*;
import se.kodapan.geography.geocoding.geocoding.Geocoding;
import se.kodapan.geography.geocoding.geocoding.ResultImpl;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author kalle
 * @since 2010-jun-22 19:38:40
 */
public class GoogleGeocoder extends Geocoder {

  private static final Logger log = LoggerFactory.getLogger(GoogleGeocoder.class);
  private static se.kodapan.geography.geocoding.google.GoogleGeocoder defaultGeocoder = new se.kodapan.geography.geocoding.google.GoogleGeocoder();

  private se.kodapan.geography.geocoding.google.GoogleGeocoder geocoder;

  public GoogleGeocoder() {
    this.geocoder = defaultGeocoder;
  }

  public GoogleGeocoder(se.kodapan.geography.geocoding.google.GoogleGeocoder geocoder) {
    this.geocoder = geocoder;
  }

  @Override
  public int getMaximumResultsReturned() {
    return 10;
  }

  @Override
  public String getName() {
    return geocoder.getName();
  }

  @Override
  public String getVersion() {
    return geocoder.getVersion();
  }

  @Override
  public String getDefaultLicense() {
    return "Only for use on a Google map";
  }

  public Geocoding reverse(CoordinateQuery query) throws Exception {

    // create request
    se.kodapan.geography.geocoding.google.Request googleRequest = new se.kodapan.geography.geocoding.google.Request();
    googleRequest.setLatLng(new LatLng(query.getCoordinate().getLatitude(), query.getCoordinate().getLongitude()));
    googleRequest.setLanguage(query.getPreferredResponseLanguage());

    Geocoding geocoding = sendRequest(googleRequest);
    if (!geocoding.getResults().isEmpty()) {
      geocoding.setSuccess(true);
    }
    return geocoding;


  }

  @Override
  public Geocoding geocode(TextQuery query) throws Exception {


    // create request
    se.kodapan.geography.geocoding.google.Request googleRequest = new se.kodapan.geography.geocoding.google.Request();
    googleRequest.setAddress(query.getText());
    googleRequest.setLanguage(query.getText());
    if (query.getBounds() != null) {
      EnvelopeImpl envelope = new EnvelopeImpl();
      envelope.addBounds(query.getBounds());
      googleRequest.setBounds(new se.kodapan.geography.geocoding.google.Envelope(
          new LatLng(envelope.getSouthwest().getLatitude(), envelope.getSouthwest().getLongitude()),
          new LatLng(envelope.getNortheast().getLatitude(), envelope.getNortheast().getLongitude())));
    }

    return sendRequest(googleRequest);

  }

  private Geocoding sendRequest(se.kodapan.geography.geocoding.google.Request googleRequest) throws IOException {
    return parseServerResponse(geocoder.geocode(googleRequest));
  }


  private Geocoding parseServerResponse(GeocodeResponse googleResponse) {
    Geocoding geocoding = new Geocoding();
    geocoding.setServerResponse(googleResponse.getServerResponse());
    // parse response
    if (googleResponse.getResults().size() == 0
        || !"OK".equals(googleResponse.getStatus())) {
      geocoding.setSuccess(false);
    } else {
      parseResponse(geocoding, googleResponse);
    }
    return geocoding;
  }

  private void parseResponse(Geocoding geocoding, GeocodeResponse googleResponse) {
    for (se.kodapan.geography.geocoding.google.Result googleResult : googleResponse.getResults()) {

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
        EnvelopeImpl envelope = new EnvelopeImpl();
        envelope.setNortheast(new CoordinateImpl(googleResult.getGeometry().getBounds().getNortheast().getLat(), googleResult.getGeometry().getBounds().getNortheast().getLng()));
        envelope.setSouthwest(new CoordinateImpl(googleResult.getGeometry().getBounds().getSouthwest().getLat(), googleResult.getGeometry().getBounds().getSouthwest().getLng()));
        result.setBounds(envelope);
      } else {
        result.setBounds(new SingleCoordinatePolygonImpl(result.getLocation()));
      }

      if (googleResult.getGeometry().getViewport() != null) {
        EnvelopeImpl envelope = new EnvelopeImpl();
        envelope.setNortheast(new CoordinateImpl(googleResult.getGeometry().getViewport().getNortheast().getLat(), googleResult.getGeometry().getViewport().getNortheast().getLng()));
        envelope.setSouthwest(new CoordinateImpl(googleResult.getGeometry().getViewport().getSouthwest().getLat(), googleResult.getGeometry().getViewport().getSouthwest().getLng()));
        result.setViewPort(envelope);
      } else {
        result.setViewPort(result.getBounds());

      }


      for (AddressComponent googleComponent : googleResult.getAddressComponents()) {
        se.kodapan.geography.domain.AddressComponent addressComponent = new se.kodapan.geography.domain.AddressComponent();
        addressComponent.setLongName(googleComponent.getLongName());
        addressComponent.setShortName(googleComponent.getShortName());
        for (String type : googleComponent.getTypes()) {
          addressComponent.getTypes().add(type);
        }
        result.getAddressComponents().add(addressComponent);
      }

      geocoding.getResults().add(result);
    }

    geocoding.setSuccess(geocoding.getResults().size() == 1);
  }



  public se.kodapan.geography.geocoding.google.GoogleGeocoder getGeocoder() {
    return geocoder;
  }

  @Override
  public Geocoding parseReverseServerResponse(InputStream inputStream) throws Exception {
    Geocoding geocoding = parseServerResponse(geocoder.parseServerResponse(inputStream));
    if (!geocoding.getResults().isEmpty()) {
      geocoding.setSuccess(true);
    }
    return geocoding;

  }

  @Override
  public Geocoding parseGeocodeServerResponse(InputStream inputStream) throws Exception {
    return parseServerResponse(geocoder.parseServerResponse(inputStream));

  }
}
