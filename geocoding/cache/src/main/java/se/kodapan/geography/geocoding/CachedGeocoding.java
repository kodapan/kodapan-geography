package se.kodapan.geography.geocoding;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;
import com.sleepycat.persist.model.Relationship;
import com.sleepycat.persist.model.SecondaryKey;

import java.util.Date;
import java.util.UUID;

/**
 * @author kalle
 * @since 2011-11-15 23:40
 */
@Entity(version = 2)
public class CachedGeocoding {

  @PrimaryKey
  private String identity = UUID.randomUUID().toString();

  @SecondaryKey(relate = Relationship.MANY_TO_ONE)
  private String query;

  private String geocoder;
  private String geocoderVersion;
  private String license;

  @SecondaryKey(relate = Relationship.MANY_TO_ONE)
  private Date created = new Date();

  private String serverResponse;

  private CachedGeocoderException exception;

  @Override
  public String toString() {
    return "CachedGeocoding{" +
        "identity='" + identity + '\'' +
        ", created=" + created +
        ", license='" + license + '\'' +
        ", geocoder='" + geocoder + '\'' +
        ", geocoderVersion='" + geocoderVersion + '\'' +
        ", query='" + query + '\'' +
        ", exception=" + exception +
        ", serverResponse='" + serverResponse + '\'' +
        '}';
  }

  public CachedGeocoderException getException() {
    return exception;
  }

  public void setException(CachedGeocoderException exception) {
    this.exception = exception;
  }

  public String getQuery() {
    return query;
  }

  public void setQuery(String query) {
    this.query = query;
  }

  public String getIdentity() {
    return identity;
  }

  public void setIdentity(String identity) {
    this.identity = identity;
  }

  public String getGeocoder() {
    return geocoder;
  }

  public void setGeocoder(String geocoder) {
    this.geocoder = geocoder;
  }

  public String getGeocoderVersion() {
    return geocoderVersion;
  }

  public void setGeocoderVersion(String geocoderVersion) {
    this.geocoderVersion = geocoderVersion;
  }

  public String getLicense() {
    return license;
  }

  public void setLicense(String license) {
    this.license = license;
  }

  public Date getCreated() {
    return created;
  }

  public void setCreated(Date created) {
    this.created = created;
  }

  public String getServerResponse() {
    return serverResponse;
  }

  public void setServerResponse(String serverResponse) {
    this.serverResponse = serverResponse;
  }
}
