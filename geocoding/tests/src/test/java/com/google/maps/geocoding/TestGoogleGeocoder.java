package com.google.maps.geocoding;

import se.kodapan.geography.geocoding.CoordinateQuery;
import se.kodapan.geography.geocoding.Geocoder;
import geocoding.GeocoderTest;
import se.kodapan.geography.geocoding.geocoding.Geocoding;
import se.kodapan.geography.domain.CoordinateImpl;

/**
 * @author kalle
 * @since 2011-11-13 05:37
 */
public class TestGoogleGeocoder extends GeocoderTest {

  @Override
  protected Geocoder getGeocoder() {
    return new se.kodapan.geography.geocoding.GoogleGeocoder();
  }

  @Override
  public void testReverseGeocoding() throws Exception {
    Geocoding pilkingtonAvenue135 = getGeocoder().reverse(new CoordinateQuery().setCoordinate(new CoordinateImpl(52.5484767, -1.8166954)));
    assertFalse(pilkingtonAvenue135.getResults().isEmpty());
  }

}
