package org.osm.nominatim;

import geocoding.GeocoderTest;
import se.kodapan.geography.domain.CoordinateImpl;
import se.kodapan.geography.geocoding.CoordinateQuery;
import se.kodapan.geography.geocoding.Geocoder;
import se.kodapan.geography.geocoding.Geocoding;

/**
 * @author kalle
 * @since 2011-11-13 05:20
 */
public class TestNominatim extends GeocoderTest {

  @Override
  protected Geocoder getGeocoder() {
    return new Nominatim();
  }

  @Override
  public void testReverseGeocoding() throws Exception {
    Geocoding pilkingtonAvenue135 = getGeocoder().reverse(new CoordinateQuery().setCoordinate(new CoordinateImpl(52.5484767, -1.8166954)).setPreferredResponseLanguage("en"));
    assertFalse(pilkingtonAvenue135.getResults().isEmpty());
  }

  @Override
  public void test() throws Exception {
    super.test();
  }

}
