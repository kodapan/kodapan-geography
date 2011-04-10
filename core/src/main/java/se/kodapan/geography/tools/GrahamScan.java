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
package se.kodapan.geography.tools;

import se.kodapan.geography.domain.Coordinate;
import se.kodapan.geography.domain.PolygonImpl;

import java.util.*;

/**
 * http://en.wikipedia.org/wiki/Graham_scan
 *
 * This is an adaptation of the CompGeom implementation by Bart Kiers, bart@big-o.nl
 *
 * @author kalle
 * @since 2010-sep-04 21:35:27
 */
public class GrahamScan extends ConvexHull {

  private static final long serialVersionUID = 1l;

  @Override
  public PolygonImpl factory(Set<Coordinate> coordinates) {

    long ms = System.currentTimeMillis();

    if (coordinates.size() < 3) {
      throw new IllegalArgumentException("Need at least 3 points to create a convex hull!");
    }

    List<Coordinate> sortedCoordinates = new ArrayList<Coordinate>(coordinates);
    final Coordinate lowestCoordinate = findCoordinateWithLowestLatitudeAndLongitude(sortedCoordinates);
    Collections.sort(sortedCoordinates, new Comparator<Coordinate>() {
      @Override
      public int compare(Coordinate coordinate, Coordinate coordinate1) {
        if (coordinate.equals(lowestCoordinate)) return -1;
        if (coordinate1.equals(lowestCoordinate)) return 1;

        Double slopeA = calculateSlope(lowestCoordinate, coordinate);
        Double slopeB = calculateSlope(lowestCoordinate, coordinate1);

        if (slopeA.equals(slopeB)) {
          Double distanceAToLowestY = coordinate.arcDistance(lowestCoordinate);
          Double distanceBToLowestY = coordinate1.arcDistance(lowestCoordinate);
          return distanceAToLowestY.compareTo(distanceBToLowestY);
        }

        if (isPositive(slopeA) && isNegative(slopeB)) {
          return -1;
        } else if (isNegative(slopeA) && isPositive(slopeB)) {
          return 1;
        } else {
          return slopeA.compareTo(slopeB);
        }

      }
    });

    // remove collinear coordinates
    // "slows down" the algorithm but might produce less points in the hull
    Coordinate lowestLatitude = sortedCoordinates.get(0);
    for (int i = sortedCoordinates.size() - 1; i > 1; i--) {
      Double slopeB = calculateSlope(lowestLatitude, sortedCoordinates.get(i));
      Double slopeA = calculateSlope(lowestLatitude, sortedCoordinates.get(i - 1));
      if (slopeB.equals(slopeA)) {
        sortedCoordinates.remove(i - 1);
      }
    }

    LinkedList<Coordinate> stack = new LinkedList<Coordinate>();
    for (int i = 0; i < 3; i++) {
      stack.push(sortedCoordinates.get(i));
    }

    for (int i = 3; i < sortedCoordinates.size(); i++) {
      Coordinate head = sortedCoordinates.get(i);
      Coordinate middle = stack.pop();
      Coordinate tail = stack.pop();
      if (formsLeftTurn(tail, middle, head)) {
        stack.push(tail);
        stack.push(middle);
        stack.push(head);
      } else {
        stack.push(tail);
        i--;
      }
    }
    stack.push(sortedCoordinates.get(0));

    return new PolygonImpl(stack);
  }

  public Coordinate findCoordinateWithLowestLatitudeAndLongitude(List<Coordinate> coordinates) {
    Coordinate result = coordinates.get(0);
    for (int i = 1; i < coordinates.size(); i++) {
      Coordinate coordinate = coordinates.get(i);
      if (result.getLatitude() > coordinate.getLatitude()) {
        result = coordinate;
      } else if (result.getLatitude() == coordinate.getLatitude() && result.getLongitude() > coordinate.getLongitude()) {
        result = coordinate;
      }
    }
    return result;
  }

  public boolean isPositive(Double value) {
    return value.equals(Double.POSITIVE_INFINITY) || value > 0;
  }

  public boolean isNegative(Double value) {
    return value.equals(Double.NEGATIVE_INFINITY) || value < 0;
  }

  public boolean formsLeftTurn(Coordinate a, Coordinate b, Coordinate c) {
    return crossProduct(a, b, c) > 0;
  }

  public boolean formsRightTurn(Coordinate a, Coordinate b, Coordinate c) {
    return crossProduct(a, b, c) < 0;
  }

  private double crossProduct(Coordinate a, Coordinate b, Coordinate c) {
    return (b.getLongitude() - a.getLongitude()) * (c.getLatitude() - a.getLatitude())
        - (b.getLatitude() - a.getLatitude())
        * (c.getLongitude() - a.getLongitude());
  }

  private Double calculateSlope(Coordinate coordinate, Coordinate coordinate1) {
    return coordinate.getLongitude() == coordinate1.getLongitude()
        ? Double.POSITIVE_INFINITY
        : (coordinate1.getLatitude() - coordinate.getLatitude()) / (coordinate1.getLongitude() - coordinate.getLongitude());
  }

}
