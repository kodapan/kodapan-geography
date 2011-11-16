package se.kodapan.geography.geocoding;

import se.kodapan.geography.domain.Polygon;

/**
 * @author kalle
 * @since 2011-11-15 19:42
 */
public class TextQuery extends Query {

  private String text;

  private String textLanguage;

  private Polygon bounds;

  public Polygon getBounds() {
    return bounds;
  }

  public TextQuery setBounds(Polygon bounds) {
    this.bounds = bounds;
    return this;
  }

  public String getText() {
    return text;
  }

  public TextQuery setText(String text) {
    this.text = text;
    return this;
  }

  public String getTextLanguage() {
    return textLanguage;
  }

  public TextQuery setTextLanguage(String textLanguage) {
    this.textLanguage = textLanguage;
    return this;
  }

  @Override
  public TextQuery setPreferredResponseLanguage(String preferredResponseLanguage) {
    super.setPreferredResponseLanguage(preferredResponseLanguage);
    return this;
  }
}
