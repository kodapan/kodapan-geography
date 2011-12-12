package se.kodapan.geography.geocoding;

import com.sleepycat.persist.EntityCursor;
import se.kodapan.collections.ScoreMap;
import se.kodapan.geography.domain.Coordinate;
import se.kodapan.geography.geocoding.geocoding.Geocoding;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Iterator;

/**
 * @author kalle
 * @since 2011-11-15 20:05
 */
public class CachedGeocoder extends Geocoder {

  private Geocoder decorated;
  private File path;

  public CachedGeocoder(Geocoder decorated, File path) {
    this.decorated = decorated;
    this.path = path;
  }

  private CachedGeocodingStore cacheStore;


  public void open() throws Exception {
    if (cacheStore != null) {
      return;
    }
    cacheStore = new CachedGeocodingStore();
    cacheStore.setPath(path);
    cacheStore.open();
  }

  public void close() throws Exception {
    if (cacheStore != null) {
      try {
        cacheStore.close();
      } finally {
        cacheStore = null;
      }
    }
  }

  @Override
  protected void finalize() throws Throwable {
    super.finalize();
    close();
  }



  public String queryFactory(CoordinateQuery query) {
    StringBuilder sb = new StringBuilder(1024);
    sb.append("coordinate");
    appendCoordinate(sb, query.getCoordinate());
    sb.append("/").append(String.valueOf(query.getPreferredResponseLanguage()));
    return sb.toString();
  }

  public String queryFactory(TextQuery query) {
    StringBuilder sb = new StringBuilder(1024);
    sb.append("text");
    if (query.getBounds() != null) {
      for (Iterator<Coordinate> it = query.getBounds().iterateCoordinates(); it.hasNext(); ) {
        appendCoordinate(sb, it.next());
      }
    } else {
      sb.append("/null");
    }
    sb.append("/").append(String.valueOf(query.getPreferredResponseLanguage()));
    sb.append("/").append(query.getTextLanguage());
    sb.append("/").append(query.getText());
    return sb.toString();
  }

  private void appendCoordinate(StringBuilder sb, Coordinate coordinate) {
    sb.append("/").append(coordinate.getLatitude()).append("/").append(coordinate.getLongitude());
  }

  @Override
  public int getMaximumResultsReturned() {
    return decorated.getMaximumResultsReturned();
  }

  private boolean useCacheOnly = false;

  public boolean isUseCacheOnly() {
    return useCacheOnly;
  }

  public void setUseCacheOnly(boolean useCacheOnly) {
    this.useCacheOnly = useCacheOnly;
  }

  @Override
  public Geocoding reverse(CoordinateQuery query) throws Exception {

    long started = System.currentTimeMillis();

    String stringQuery = queryFactory(query);
    EntityCursor<CachedGeocoding> cursor = cacheStore.getQueryIndex().entities(stringQuery, true, stringQuery, true);
    ScoreMap<CachedGeocoding> scoreMap = new ScoreMap<CachedGeocoding>();
    try {
      CachedGeocoding cachedGeocoding;
      while ((cachedGeocoding = cursor.next()) != null) {
        scoreMap.increase(cachedGeocoding, cachedGeocoding.getCreated().getTime());
      }
    } finally {
      cursor.close();
    }

    if (!scoreMap.isEmpty()) {
      CachedGeocoding cachedGeocoding = scoreMap.getHits()[0].getKey();
      Geocoding geocoding = decorated.parseReverseServerResponse(new ByteArrayInputStream(cachedGeocoding.getServerResponse().getBytes("UTF8")));
      log.info("Returning cached geocoding after " + (System.currentTimeMillis() - started) + " milliseconds.");
      return geocoding;
    }

    if (useCacheOnly) {
      return null;
    }

    Geocoding geocoding = decorated.reverse(query);

    CachedGeocoding cachedGeocoding = new CachedGeocoding();
    cachedGeocoding.setGeocoder(geocoding.getSource().getName());
    cachedGeocoding.setGeocoderVersion(geocoding.getSource().getVersion());
    cachedGeocoding.setLicense(geocoding.getSource().getLicense());
    cachedGeocoding.setServerResponse(geocoding.getServerResponse());
    cachedGeocoding.setQuery(stringQuery);

    cacheStore.getCachedGeocodings().put(cachedGeocoding);
    log.info("Returning brand new geocoding after " + (System.currentTimeMillis() - started) + " milliseconds.");

    return geocoding;
  }

  @Override
  public Geocoding geocode(TextQuery query) throws Exception {

    long started = System.currentTimeMillis();

    String stringQuery = queryFactory(query);
    EntityCursor<CachedGeocoding> cursor = cacheStore.getQueryIndex().entities(stringQuery, true, stringQuery, true);
    ScoreMap<CachedGeocoding> scoreMap = new ScoreMap<CachedGeocoding>();
    try {
      CachedGeocoding cachedGeocoding;
      while ((cachedGeocoding = cursor.next()) != null) {
        scoreMap.increase(cachedGeocoding, cachedGeocoding.getCreated().getTime());
      }
    } finally {
      cursor.close();
    }

    if (!scoreMap.isEmpty()) {
      CachedGeocoding cachedGeocoding = scoreMap.getHits()[0].getKey();
      Geocoding geocoding = decorated.parseGeocodeServerResponse(new ByteArrayInputStream(cachedGeocoding.getServerResponse().getBytes("UTF8")));
      log.info("Returning cached geocoding after " + (System.currentTimeMillis() - started) + " milliseconds.");
      return geocoding;
    }

    if (useCacheOnly) {
      return null;
    }

    Geocoding geocoding = decorated.geocode(query);

    CachedGeocoding cachedGeocoding = new CachedGeocoding();
    cachedGeocoding.setGeocoder(getName());
    cachedGeocoding.setGeocoderVersion(getVersion());
    cachedGeocoding.setServerResponse(geocoding.getServerResponse());
    cachedGeocoding.setQuery(stringQuery);

    cacheStore.getCachedGeocodings().put(cachedGeocoding);
    log.info("Returning brand new geocoding after " + (System.currentTimeMillis() - started) + " milliseconds.");
    return geocoding;

  }

  @Override
  public Geocoding parseReverseServerResponse(InputStream inputStream) throws Exception {
    return decorated.parseReverseServerResponse(inputStream);
  }

  @Override
  public Geocoding parseGeocodeServerResponse(InputStream inputStream) throws Exception {
    return decorated.parseGeocodeServerResponse(inputStream);
  }

  @Override
  public String getName() {
    return decorated.getName();
  }

  @Override
  public String getVersion() {
    return decorated.getVersion();
  }

  @Override
  public String getDefaultLicense() {
    return decorated.getDefaultLicense();
  }

  public CachedGeocodingStore getCacheStore() {
    return cacheStore;
  }


}
