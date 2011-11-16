package se.kodapan.geography.geocoding;

import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.PrimaryIndex;
import com.sleepycat.persist.SecondaryIndex;
import com.sleepycat.persist.StoreConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
/**
 * @author kalle
 * @since 2011-11-15 23:39
 */
public class CachedGeocodingStore {

  private static final Logger log = LoggerFactory.getLogger(CachedGeocodingStore.class);

  private File path;

  private String storeName = "geocoding";

  private Environment environment;
  private EntityStore entityStore;
  private int cacheMB;
  private boolean readOnly;

  private PrimaryIndex<String, CachedGeocoding> cachedGeocodings;
  private SecondaryIndex<String, String, CachedGeocoding> queryIndex;

  public void open() throws IOException {

    log.info("Opening BDB...");

    cacheMB = 5;
    readOnly = false;

    if (!path.exists()) {
      log.info("Creating directory " + path.getAbsolutePath());
      if (!path.mkdirs()) {
        throw new IOException("Could not create directory " + path.getAbsolutePath());
      }

      EnvironmentConfig envConfig = new EnvironmentConfig();
      envConfig.setAllowCreate(true);
      envConfig.setTransactional(false);
      envConfig.setLocking(true);
      envConfig.setReadOnly(false);


      log.info("Creating environment " + envConfig.toString());

      environment = new Environment(path, envConfig);

      StoreConfig storeConfig = new StoreConfig();
      storeConfig.setAllowCreate(true);
      storeConfig.setTransactional(false);
      storeConfig.setReadOnly(false);

      log.info("Creating store '" + storeName + "' " + storeConfig.toString());

      entityStore = new EntityStore(environment, storeName, storeConfig);

      entityStore.close();
      environment.close();

      log.info("BDB has been created");

    }

    // open

    EnvironmentConfig envConfig = new EnvironmentConfig();
    envConfig.setAllowCreate(true);
    envConfig.setTransactional(false);
    envConfig.setLocking(false);
    envConfig.setReadOnly(readOnly);
    envConfig.setCacheSize(cacheMB * 1024 * 1024); //


    log.info("Opening environment " + envConfig.toString());

    environment = new Environment(path, envConfig);

    StoreConfig storeConfig = new StoreConfig();
    storeConfig.setAllowCreate(true);
    storeConfig.setTransactional(false);
    storeConfig.setReadOnly(readOnly);

    log.info("Opening store '" + storeName + "' " + storeConfig.toString());

    entityStore = new EntityStore(environment, storeName, storeConfig);

    cachedGeocodings = entityStore.getPrimaryIndex(String.class, CachedGeocoding.class);
    queryIndex = entityStore.getSecondaryIndex(cachedGeocodings, String.class, "query");

    log.info("BDB has been opened.");


  }

  public void close() throws IOException {

    log.info("Closing BDB...");

    if (entityStore != null) {
      entityStore.close();
    }
    if (environment != null) {
      environment.close();
    }

    entityStore = null;
    environment = null;

    log.info("BDB has been closed");

  }


  public File getPath() {
    return path;
  }

  public void setPath(File path) {
    this.path = path;
  }

  public int getCacheMB() {
    return cacheMB;
  }

  public void setCacheMB(int cacheMB) {
    this.cacheMB = cacheMB;
  }

  public boolean isReadOnly() {
    return readOnly;
  }

  public void setReadOnly(boolean readOnly) {
    this.readOnly = readOnly;
  }

  public String getStoreName() {
    return storeName;
  }

  public void setStoreName(String storeName) {
    this.storeName = storeName;
  }

  public EntityStore getEntityStore() {
    return entityStore;
  }

  public void setEntityStore(EntityStore entityStore) {
    this.entityStore = entityStore;
  }

  public Environment getEnvironment() {
    return environment;
  }

  public void setEnvironment(Environment environment) {
    this.environment = environment;
  }

  public PrimaryIndex<String, CachedGeocoding> getCachedGeocodings() {
    return cachedGeocodings;
  }

  public void setCachedGeocodings(PrimaryIndex<String, CachedGeocoding> cachedGeocodings) {
    this.cachedGeocodings = cachedGeocodings;
  }

  public SecondaryIndex<String, String, CachedGeocoding> getQueryIndex() {
    return queryIndex;
  }

  public void setQueryIndex(SecondaryIndex<String, String, CachedGeocoding> queryIndex) {
    this.queryIndex = queryIndex;
  }
}
