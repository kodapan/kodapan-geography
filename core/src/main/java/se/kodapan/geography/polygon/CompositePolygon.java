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
package se.kodapan.geography.polygon;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * A list of polygons acting as a single polygon.
 *
 * @author kalle
 * @since 2010-jul-25 06:32:35
 */
public class CompositePolygon implements Polygon {

  private Set<Polygon> parts = new HashSet<Polygon>();

  public void add(Polygon part) {
    clearCache();
  }

  public void remove(Polygon part) {
    clearCache();
  }

  private void clearCache() {
    centroidLatitude = null;
    centroidLongitude = null;
  }

  @Override
  public String getPolygonName() {
    return null;
  }

  @Override
  public Iterator<Coordinate> iterateCoordinates() {
    return new Iterator<Coordinate>() {
      Iterator<Polygon> parts = CompositePolygon.this.parts.iterator();
      Iterator<Coordinate> it = null;

      @Override
      public boolean hasNext() {
        while (!it.hasNext()) {
          if (!parts.hasNext()) {
            return false;
          }
          it = parts.next().iterateCoordinates();
        }
        return true;
      }

      @Override
      public Coordinate next() {
        return it.next();
      }

      @Override
      public void remove() {
        it.remove();
      }
    };
  }

  @Override
  public boolean contains(Coordinate coordinate) {
    for (Polygon part : parts) {
      if (part.contains(coordinate)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean contains(Polygon polygon) {
    for (Polygon part : parts) {
      if (part.contains(polygon)) {
        return true;
      }
    }
    return false;
  }

  private Double centroidLatitude;
  private Double centroidLongitude;

  private Coordinate centroid = new Coordinate(){
    @Override
    public double getLatitude() {
      if (centroidLatitude == null) {
        set();
      }
      return centroidLatitude;
    }

    @Override
    public double getLongitude() {
      if (centroidLongitude == null) {
        set();
      }
      return centroidLongitude;

    }

    @Override
    public void setLatitude(double latitude) {
      throw new UnsupportedOperationException();
    }

    @Override
    public void setLongitude(double longitude) {
      throw new UnsupportedOperationException();
    }

    @Override
    public double archDistance(Coordinate that) {
      return CoordinateTools.arcDistance(this, that);
    }
  };

  private void set() {
    double latitude = 0d;
    double longitude = 0d;
    for (Polygon part : parts) {
      latitude += part.getCentroid().getLatitude();
      longitude += part.getCentroid().getLongitude();
    }
    this.centroidLatitude = latitude / (double)parts.size();
    this.centroidLongitude = longitude / (double)parts.size();
  }

  @Override
  public Coordinate getCentroid() {
    return centroid;
  }

  @Override
  public double archDistance(Polygon that) {
    double closest = Double.MAX_VALUE;
    for (Polygon part : parts) {
      double distance =part.archDistance(that);
      if (distance == 0) {
        return 0;
      }
      if (distance <= closest) {
        closest = distance;
      }
    }
    return closest;
  }

  @Override
  public double archDistance(Coordinate that) {
    double closest = Double.MAX_VALUE;
    for (Polygon part : parts) {
      double distance =part.archDistance(that);
      if (distance == 0) {
        return 0;
      }
      if (distance <= closest) {
        closest = distance;
      }
    }
    return closest;
  }
}
