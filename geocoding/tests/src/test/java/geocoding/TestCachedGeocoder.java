package geocoding;

import org.apache.commons.io.IOUtils;
import org.osm.nominatim.Nominatim;
import se.kodapan.geography.geocoding.CachedGeocoder;
import se.kodapan.geography.geocoding.Geocoder;
import se.kodapan.io.IOUtil;

import java.io.File;

/**
 * todo it would be nice if the test suite ran all geocoders via this.
 *
 * @author kalle
 * @since 2011-11-16 02:03
 */
public class TestCachedGeocoder extends GeocoderTest {

  private CachedGeocoder geocoder;

  @Override
  protected void setUp() throws Exception {
    geocoder = new CachedGeocoder(new Nominatim(), new File("target/TestCachedGeocoder/" + System.currentTimeMillis()));
    geocoder.open();
    super.setUp();
  }

  @Override
  public void test() throws Exception {
    super.test();
    // now run again with and make sure it's all cached.
    geocoder.setUseCacheOnly(true);
    super.test();
  }

  @Override
  protected void tearDown() throws Exception {
    super.tearDown();
    geocoder.close();
  }

  @Override
  protected CachedGeocoder getGeocoder() {
    return geocoder;
  }
}
