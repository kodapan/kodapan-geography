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

/**
 * @author kalle
 * @since 2010-jul-20 01:21:11
 */
public abstract class CoordinateDecorator implements Coordinate {

  private static final long serialVersionUID = 1l;

  public abstract Coordinate getDecoratedCoordinate();

  @Override
  public Double getLatitude() {
    return getDecoratedCoordinate().getLatitude();
  }

  @Override
  public void setLatitude(Double v) {
    getDecoratedCoordinate().setLatitude(v);
  }

  @Override
  public Double getLongitude() {
    return getDecoratedCoordinate().getLongitude();
  }

  @Override
  public void setLongitude(Double v) {
    getDecoratedCoordinate().setLongitude(v);
  }

  @Override
  public double archDistance(Coordinate coordinate) {
    return getDecoratedCoordinate().archDistance(coordinate);
  }

  @Override
  public int hashCode() {
    return getDecoratedCoordinate().hashCode();
  }

  @Override
  public boolean equals(Object o) {
    return getDecoratedCoordinate().equals(o);
  }

  @Override
  public String toString() {
    return getDecoratedCoordinate().toString();
  }
}
