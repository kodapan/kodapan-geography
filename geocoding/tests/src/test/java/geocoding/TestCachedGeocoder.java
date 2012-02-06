package geocoding;

import se.kodapan.geography.geocoding.Nominatim;
import se.kodapan.geography.domain.CoordinateImpl;
import se.kodapan.geography.geocoding.CachedGeocoder;
import se.kodapan.geography.geocoding.CoordinateQuery;

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

    geocoder.setCachingExceptions(true);
    geocoder.setUseCachedExceptions(true);

    // todo test some
    geocoder.setUseCacheOnly(true);
    // todo now run again with and make sure it's all cached.
    geocoder.setUseCacheOnly(false);

    try {
      geocoder.reverse(new CoordinateQuery().setCoordinate(new CoordinateImpl(333, 444)));
      fail();
    } catch (Exception e) {
      // all good
      e.printStackTrace();
    }
    geocoder.setUseCacheOnly(true);
    try {
      geocoder.reverse(new CoordinateQuery().setCoordinate(new CoordinateImpl(333, 444)));
      fail();
    } catch (Exception e) {
      // all good
      e.printStackTrace();
    }
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
