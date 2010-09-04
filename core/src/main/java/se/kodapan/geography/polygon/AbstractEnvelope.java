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

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author kalle
 * @since 2010-jun-23 19:08:26
 */
public abstract class AbstractEnvelope
    extends AbstractPolygon
    implements Envelope {


  private static final long serialVersionUID = 1l;

  protected void coordinateFactory() {
    throw new UnsupportedOperationException("Not implemented in " + getClass().getName());
  }

  public void addBounds(Polygon polygon) {
    for (Iterator<Coordinate> it = polygon.iterateCoordinates(); it.hasNext();) {
      Coordinate coordinate = it.next();
      addBounds(coordinate.getLatitude(), coordinate.getLongitude());
    }
  }

  public final void addBounds(Coordinate... coordinates) {
    for (Coordinate coordinate : coordinates) {
      addBounds(coordinate.getLatitude(), coordinate.getLongitude());
    }
  }

  public final void addBounds(double latitude, double longitude) {

    if (getNorthEast() == null) {
      coordinateFactory();
      getNorthEast().setLatitude(latitude);
      getSouthWest().setLatitude(latitude);

      getNorthEast().setLongitude(longitude);
      getSouthWest().setLongitude(longitude);

    } else {
      if (latitude > getNorthEast().getLatitude()) {
        getNorthEast().setLatitude(latitude);
      } else if (latitude < getSouthWest().getLatitude()) {
        getSouthWest().setLatitude(latitude);
      }

      if (getNorthEast().getLongitude() > longitude) {
        getNorthEast().setLongitude(longitude);
      } else if (getSouthWest().getLongitude() < longitude) {
        getSouthWest().setLongitude(longitude);
      }
    }
  }

  
  @Override
  public Iterator<Coordinate> iterateCoordinates() {
    return new Iterator<Coordinate>(){
      int state = 0;
      @Override
      public boolean hasNext() {
        return state < 4;
      }

      @Override
      public Coordinate next() {
        Coordinate coordinate;
        switch (state) {
          case 0 : coordinate = getNorthEast(); break;
          case 1 : coordinate = getNorthWest(); break;
          case 2 : coordinate = getSouthWest(); break;
          case 3 : coordinate = getSouthEast(); break;
          default : throw new NoSuchElementException();
        }
        state++;
        return coordinate;
      }

      @Override
      public void remove() {
        throw new UnsupportedOperationException();
      }
    };
  }

  @Override
  public boolean contains(Coordinate coordinate) {
    return coordinate.getLatitude() <= getNorthEast().getLatitude()
        && coordinate.getLongitude() >= getNorthEast().getLongitude()
        && coordinate.getLatitude() >= getSouthWest().getLatitude()
        && coordinate.getLongitude() <= getSouthWest().getLongitude();
  }

  private final AbstractCoordinate centroid = new AbstractCoordinate() {
    @Override
    public double getLatitude() {
      return (getSouthWest().getLatitude() + getNorthEast().getLatitude()) / 2d;

    }

    @Override
    public double getLongitude() {
      return (getSouthWest().getLongitude() + getNorthEast().getLongitude()) / 2d;
    }

    @Override
    public void setLatitude(double latitude) {
      throw new RuntimeException();
    }

    @Override
    public void setLongitude(double longitude) {
      throw new RuntimeException();
    }

  };


  private final AbstractCoordinate southEast = new AbstractCoordinate() {
    @Override
    public double getLatitude() {
      return getSouthWest().getLatitude();

    }

    @Override
    public double getLongitude() {
      return getNorthEast().getLongitude();
    }

    @Override
    public void setLatitude(double latitude) {
      getSouthWest().setLatitude(latitude);
    }

    @Override
    public void setLongitude(double longitude) {
      getNorthEast().setLatitude(longitude);
    }

  };


  private final AbstractCoordinate northWest = new AbstractCoordinate() {
    @Override
    public double getLatitude() {
      return getNorthEast().getLatitude();

    }

    @Override
    public double getLongitude() {
      return getSouthWest().getLongitude();
    }

    @Override
    public void setLatitude(double latitude) {
      getNorthEast().setLatitude(latitude);
    }

    @Override
    public void setLongitude(double longitude) {
      getSouthWest().setLongitude(longitude);
    }

  };

    @Override
  public Coordinate getSouthEast() {
    return southEast;
  }

  @Override
  public AbstractCoordinate getNorthWest() {
    return northWest;
  }

  @Override
  public final AbstractCoordinate getCentroid() {
    return centroid;
  }

  @Override
  public double archDistanceDiagonal() {
    return getSouthWest().archDistance(getNorthEast());
  }
}
