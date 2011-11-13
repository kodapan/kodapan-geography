package com.google.maps.geocoding;

import se.kodapan.geography.geocoding.*;

/**
 * @author kalle
 * @since 2011-11-13 05:37
 */
public class TestGoogleGeocoder extends GeocoderTest {

  @Override
  protected Geocoder geocoderFactory() {
    return new se.kodapan.geography.geocoding.GoogleGeocoder();
  }
}
