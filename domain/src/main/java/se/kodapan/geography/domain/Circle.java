package se.kodapan.geography.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @author kalle
 * @since 2010-sep-11 20:58:40
 */
public class Circle extends AbstractPolygon {

  private static final long serialVersionUID = 1l;

  /**
   * @param coordinates
   * @param circumferenceResolution
   * @return A circle that includes all points in parameter coordinates
   */
  public static Circle factory(Collection<Coordinate> coordinates, int circumferenceResolution) {
    if (coordinates.size() < 2) {
      throw new IllegalArgumentException("Expected at least two coordinates");
    } else {
      EnvelopeImpl envelope = new EnvelopeImpl();
      for (Coordinate coordinate : coordinates) {
        envelope.addBounds(coordinate);
      }
      Coordinate centroid = new CoordinateImpl(envelope.getCentroid());
      double longestDistanceFromCentroid = Double.MIN_VALUE;
      for (Coordinate coordinate : coordinates) {
        double distance = centroid.arcDistance(coordinate);
        if (distance > longestDistanceFromCentroid) {
          longestDistanceFromCentroid = distance;
        }
      }
      return new Circle(longestDistanceFromCentroid, centroid, circumferenceResolution);
    }
  }

  private double radiusKilometers;
  private Coordinate centroid;
  private int circumferenceResolution = 8;

  private transient List<Coordinate> coordinates;

  public Circle() {
  }

  public Circle(double radiusKilometers, Coordinate centroid, int circumferenceResolution) {
    this.radiusKilometers = radiusKilometers;
    this.centroid = centroid;
    this.circumferenceResolution = circumferenceResolution;
  }

  private List<Coordinate> calculateCoordinates() {
    List<Coordinate> coordinates = new ArrayList<Coordinate>(circumferenceResolution);

    double radiusLatitude = (radiusKilometers / 6378.8d) * (180 / Math.PI);
    double radiusLongitude = radiusLatitude / Math.cos(centroid.getLatitude() * (Math.PI / 180));

    int step = (int) (360d / (double) circumferenceResolution);
//    for (int i = 0; i <= 361; i+= step) {
    for (int i = 0; i < 360; i += step) {
      double a = i * (Math.PI / 180);
      double latitude = centroid.getLatitude() + (radiusLatitude * Math.sin(a));
      double longitude = centroid.getLongitude() + (radiusLongitude * Math.cos(a));
      coordinates.add(new CoordinateImpl(latitude, longitude));
    }

    return coordinates;
  }


  public double getRadiusKilometers() {
    return radiusKilometers;
  }

  public void setRadiusKilometers(double radiusKilometers) {
    this.radiusKilometers = radiusKilometers;
  }

  @Override
  public Iterator<Coordinate> iterateCoordinates() {
    List<Coordinate> coordinates = this.coordinates;
    if (coordinates == null) {
      coordinates = calculateCoordinates();
      this.coordinates = coordinates;
    }
    return coordinates.iterator();
  }

  @Override
  public boolean contains(Coordinate coordinate) {
    return getCentroid().arcDistance(coordinate) <= getRadiusKilometers();
  }

  @Override
  public Coordinate getCentroid() {
    return centroid;
  }

  public void setCentroid(Coordinate centroid) {
    if (!centroid.equals(this.centroid)) {
      this.centroid = centroid;
      coordinates = null;
    }
  }

  public int getCircumferenceResolution() {
    return circumferenceResolution;
  }

  public void setCircumferenceResolution(int circumferenceResolution) {
    if (this.circumferenceResolution != circumferenceResolution) {
      this.circumferenceResolution = circumferenceResolution;
      coordinates = null;
    }
  }
}
