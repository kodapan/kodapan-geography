/*
   Copyright 2010 Kodapan

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package se.kodapan.geography.domain;

import java.io.Serializable;
import java.util.Random;

/**
 * @author kalle
 * @since 2010-mar-16 06:39:00
 */
public interface Coordinate extends Serializable {

  public abstract Double getLatitude();
  public abstract void setLatitude(Double latitude);

  public abstract Double getLongitude();
  public abstract void setLongitude(Double longitude);

  /// todo
//  public abstract String getName();

  /**
   *
   * @param that
   * @return distance in kilometers
   */
  public double archDistance(Coordinate that);

  public static class CoordinateTools {
    public static double euclideanDistance(Coordinate c, Coordinate c1) {

      double latDistance = c1.getLatitude() - c.getLatitude();
      double lngDistance = c1.getLongitude() - c1.getLongitude();
      double result = 0;
      result += latDistance * latDistance;
      result += lngDistance * lngDistance;
      return Math.sqrt(result);

    }

    /**
     * If any coordinates are too close to each other this will spread them out randomly
     *
     * @param coordinates value 0.00003 is the very least to use in sweden on a maxed zoomed in gmap. just a few meters.
     */
    public static void spreadOut(Iterable<Coordinate> coordinates, double minimumDistance) {
      Random random = new Random(0);

      long ms = System.currentTimeMillis();
      int iterations = 0;
      int distanceChecks = 0;

      boolean restart = true;
      while (restart) {
        iterations++;
        restart = false;
        for (Coordinate coordinateA : coordinates) {
          for (Coordinate coordinateB : coordinates) {
            if (coordinateA == coordinateB) {
              break;
            }
            distanceChecks++;
            double distance;
            distance = euclideanDistance(coordinateA, coordinateB);
            if (distance < minimumDistance) {
              if (random.nextBoolean()) {
                coordinateA.setLatitude(coordinateA.getLatitude() + minimumDistance);
              } else {
                coordinateA.setLatitude(coordinateA.getLatitude() - minimumDistance);
              }
              if (random.nextBoolean()) {
                coordinateA.setLongitude(coordinateA.getLongitude() + minimumDistance);
              } else {
                coordinateA.setLongitude(coordinateA.getLongitude() - minimumDistance);
              }
              restart = true;
              break;
            }
          }
          if (restart) {
            break;
          }
        }
      }

      ms = System.currentTimeMillis() - ms;
      //log.info(iterations + " iterations and " + distanceChecks + " distance calculations was required to remove collisions in " + ms + " milliseconds.");

    }


    /**
     * @param a
     * @param b
     * @return kilometers
     */
    public static double arcDistance(Coordinate a, Coordinate b) {
      return arcDistance(a.getLatitude(), a.getLongitude(), b.getLatitude(), b.getLongitude());
    }

    /**
     * copied from org.apache.lucene.spatial.base.LatLng
     *
     * @param latiduteA
     * @param longitudeA
     * @param latitudeB
     * @param longitudeB
     * @return kilometers
     */
    public static double arcDistance(double latiduteA, double longitudeA, double latitudeB, double longitudeB) {

      longitudeA = normalizeLongitude(longitudeA);
      longitudeB = normalizeLongitude(longitudeB);

      // Check for same position
      if (latiduteA == latitudeB && longitudeA == longitudeB)
        return 0.0;

      // Get the m_dLongitude diffeernce. Don't need to worry about
      // crossing 180 since cos(x) = cos(-x)
      double dLon = longitudeB - longitudeA;

      double a = radians(90.0 - latiduteA);
      double c = radians(90.0 - latitudeB);
      double cosB = (Math.cos(a) * Math.cos(c))
          + (Math.sin(a) * Math.sin(c) * Math.cos(radians(dLon)));

//    double radius = (lUnits == DistanceUnits.MILES) ? 3963.205/* MILERADIUSOFEARTH */
//        : 6378.160187/* KMRADIUSOFEARTH */;

      double radius = 6378.160187;

      // Find angle subtended (with some bounds checking) in radians and
      // multiply by earth radius to find the arc distance
      if (cosB < -1.0)
        return 3.14159265358979323846/* PI */ * radius;
      else if (cosB >= 1.0)
        return 0;
      else
        return Math.acos(cosB) * radius;
    }

    /**
     * copied from org.apache.lucene.spatial.base.LatLng
     *
     * @param a
     * @return
     */
    private static double radians(double a) {
      return a * 0.01745329251994;
    }

    /**
     * copied from org.apache.lucene.spatial.base.LatLng
     *
     * @param longitude
     * @return
     */
    private static double normalizeLongitude(double longitude) {
      double delta = 0;
      if (longitude < 0) delta = 360;
      if (longitude >= 0) delta = -360;

      double normalizedLongitude = longitude;
      while (normalizedLongitude <= -180 || normalizedLongitude >= 180) {
        normalizedLongitude += delta;
      }

      return normalizedLongitude;
    }

  }


}
