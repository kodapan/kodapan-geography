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

/**
 * @author kalle
 * @since 2010-jun-23 15:44:59
 */
public abstract class AbstractCoordinate implements Coordinate {

  private static final long serialVersionUID = 1l;

  public double archDistance(Coordinate that) {
    return CoordinateTools.arcDistance(this, that);
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + "{" +
        "latitude=" + getLatitude() +
        ", longitude=" + getLongitude() +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Coordinate.class.isAssignableFrom(o.getClass())) return false;

    Coordinate that = (Coordinate) o;

    if (Double.compare(that.getLatitude(), getLatitude()) != 0) return false;
    if (Double.compare(that.getLongitude(), getLongitude()) != 0) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result;
    long temp;
    temp = getLatitude() != +0.0d ? Double.doubleToLongBits(getLatitude()) : 0L;
    result = (int) (temp ^ (temp >>> 32));
    temp = getLongitude() != +0.0d ? Double.doubleToLongBits(getLongitude()) : 0L;
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    return result;
  }
}
