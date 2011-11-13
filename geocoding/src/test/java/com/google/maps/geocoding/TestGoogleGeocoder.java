package com.google.maps.geocoding;

import se.kodapan.geography.domain.CoordinateImpl;
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

  @Override
  public void testReverseGeocoding() throws Exception {
    Geocoding pilkingtonAvenue135 = geocoderFactory().geocode(new CoordinateImpl(52.5484767, -1.8166954));
    assertFalse(pilkingtonAvenue135.getResults().isEmpty());
  }

  @Override
  public void test() throws Exception {
    Geocoding sverige = geocoderFactory().geocode("Sverige");
    assertFalse(sverige.getResults().isEmpty());
  }
}
