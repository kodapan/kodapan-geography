package org.osm.nominatim;

import se.kodapan.geography.geocoding.Geocoder;
import se.kodapan.geography.geocoding.GeocoderTest;

/**
 * todo this test fails!
 *
 * @author kalle
 * @since 2011-11-13 05:20
 */
public class TestNominatim extends GeocoderTest {

  @Override
  protected Geocoder geocoderFactory() {
    return new Nominatim();
  }
}
