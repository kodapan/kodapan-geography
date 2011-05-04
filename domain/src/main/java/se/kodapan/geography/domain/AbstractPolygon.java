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

import java.util.Iterator;

/**
 * @author kalle
 * @since 2010-jun-23 00:15:39
 */
public abstract class AbstractPolygon implements Polygon {

  private static final long serialVersionUID = 1l;



  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || !(o instanceof Polygon)) return false;

    Polygon polygon = (Polygon) o;

    Iterator<Coordinate> it = iterateCoordinates();
    Iterator<Coordinate> itThat = polygon.iterateCoordinates();

    if (it != null && itThat == null) {
      return false;
    } else if (itThat != null && it == null) {
      return false;
    } else if (it == null && itThat == null) {
      return false;
    } else {

      while (it.hasNext() && itThat.hasNext()) {
        if (!it.next().equals(itThat.next())) {
          return false;
        }
      }

      return !(it.hasNext() || itThat.hasNext());
    }
  }

  @Override
  public int hashCode() {
    int hashCode = 0;
    Iterator<Coordinate> it = iterateCoordinates();
    if (it != null) {
      while (it.hasNext()) {
        hashCode *= 0.5 + (it.next().hashCode() * 0.5);
      }
    }
    return hashCode;


  }

  @Override
  public String toString() {
    Iterator<Coordinate> it = iterateCoordinates();
    StringBuilder sb = new StringBuilder(it == null ? 50 : 1000);
    sb.append("Polygon{coordinates=");
    if (it == null) {
      sb.append("null");
    } else {
      sb.append("[");
      while (it.hasNext()) {
        sb.append(it.next().toString());
        if (it.hasNext()) {
          sb.append(",");
        }
      }
      sb.append("]");
    }
    sb.append("}");
    return sb.toString();
  }


  @Override
  public double arcDistance(Coordinate that) {
    if (contains(that)) {
      return 0d;
    }
    double closest = Double.MAX_VALUE;
    for (Iterator<Coordinate> it = iterateCoordinates(); it.hasNext();) {
      Coordinate coordinate = it.next();
      double distance = Coordinate.CoordinateTools.arcDistance(coordinate, that);
      if (distance < closest) {
        closest = distance;
      }
    }
    return closest;
  }

  @Override
  public boolean contains(Polygon that) {
    for (Iterator<Coordinate> it = that.iterateCoordinates(); it.hasNext();) {
      if (!this.contains(it.next())) {
        return false;
      }
    }
    return true;
  }

  public final double arcDistance(Polygon that) {
    if (contains(that) || that.contains(this)) {
      return 0d;
    }
    double closest = Double.MAX_VALUE;
    for (Iterator<Coordinate> it = iterateCoordinates(); it.hasNext();) {
      Coordinate coordinate = it.next();
      for (Iterator<Coordinate> itThat = that.iterateCoordinates(); itThat.hasNext();) {
        Coordinate coordinateThat = itThat.next();
        double distance = Coordinate.CoordinateTools.arcDistance(coordinate, coordinateThat);
        if (distance < closest) {
          closest = distance;
        }
      }
    }
    return closest;
  }


}
