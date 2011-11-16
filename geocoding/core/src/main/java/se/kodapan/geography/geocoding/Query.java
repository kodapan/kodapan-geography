package se.kodapan.geography.geocoding;

/**
 * @author kalle
 * @since 2011-11-15 19:43
 */
public class Query {

  private String preferredResponseLanguage;

  public String getPreferredResponseLanguage() {
    return preferredResponseLanguage;
  }

  public Query setPreferredResponseLanguage(String preferredResponseLanguage) {
    this.preferredResponseLanguage = preferredResponseLanguage;
    return this;
  }
}
