package se.kodapan.geography.geocoding;

import se.kodapan.geography.domain.Coordinate;
import se.kodapan.geography.domain.CoordinateImpl;

/**
 * @author kalle
 * @since 2011-11-15 19:43
 */
public class CoordinateQuery extends Query {

  private Coordinate coordinate;

  public Coordinate getCoordinate() {
    return coordinate;
  }

  @Override
  public CoordinateQuery setPreferredResponseLanguage(String preferredResponseLanguage) {
    super.setPreferredResponseLanguage(preferredResponseLanguage);
    return this;
  }

  public CoordinateQuery setCoordinate(Coordinate coordinate) {
    if (coordinate == null) {
      this.coordinate = null;
    } else {
      this.coordinate = new CoordinateImpl(coordinate);
    }
    return this;
  }
}
