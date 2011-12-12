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
 * @since 2010-jun-23 15:44:59
 */
public abstract class AbstractCoordinate implements Coordinate {

  private static final long serialVersionUID = 1l;

  public double arcDistance(Coordinate that) {
    return CoordinateTools.arcDistance(this, that);
  }

  @Override
  public String toString() {
    return getClass().toString() + "{" +
        "latitude=" + getLatitude() +
        ", longitude=" + getLongitude() +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || !(o instanceof Coordinate)) return false;
    if (!super.equals(o)) return false;

    Coordinate that = (Coordinate) o;

    if (getLatitude() != null ? !getLatitude().equals(that.getLatitude()) : that.getLatitude() != null) return false;
    if (getLongitude() != null ? !getLongitude().equals(that.getLongitude()) : that.getLongitude() != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = getLatitude() != null ? getLatitude().hashCode() : 0;
    result = 31 * result + (getLongitude() != null ? getLongitude().hashCode() : 0);
    return result;
  }
}
