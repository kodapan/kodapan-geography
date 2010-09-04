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

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * @author kalle
 * @since 2010-mar-16 06:42:18
 */
public class CoordinateImpl extends AbstractCoordinate implements Externalizable {

  private static final long serialVersionUID = 1l;

  private double latitude;
  private double longitude;

  public CoordinateImpl() {
  }

  @Override
  public void writeExternal(ObjectOutput objectOutput) throws IOException {
    objectOutput.writeInt(1);
    objectOutput.writeDouble(latitude);
    objectOutput.writeDouble(longitude);
  }

  @Override
  public void readExternal(ObjectInput objectInput) throws IOException, ClassNotFoundException {
    int version = objectInput.readInt();
    if (version == 1) {
      latitude = objectInput.readDouble();
      longitude = objectInput.readDouble();
    } else {
      throw new ClassNotFoundException("Unknown local version " + version + " of " + getClass().getName());
    }
  }

  public CoordinateImpl(Coordinate coordinate) {
    this(coordinate.getLatitude(), coordinate.getLongitude());
  }

  public CoordinateImpl(double latitude, double longitude) {
    this.latitude = latitude;
    this.longitude = longitude;
  }

  public double getLatitude() {
    return latitude;
  }

  public void setLatitude(double latitude) {
    this.latitude = latitude;
  }

  public double getLongitude() {
    return longitude;
  }

  public void setLongitude(double longitude) {
    this.longitude = longitude;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || !Coordinate.class.isAssignableFrom(o.getClass())) return false;

    Coordinate that = (Coordinate) o;

    if (Double.compare(that.getLatitude(), latitude) != 0) return false;
    if (Double.compare(that.getLongitude(), longitude) != 0) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result;
    long temp;
    temp = latitude != +0.0d ? Double.doubleToLongBits(latitude) : 0L;
    result = (int) (temp ^ (temp >>> 32));
    temp = longitude != +0.0d ? Double.doubleToLongBits(longitude) : 0L;
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    return result;
  }

  @Override
  public String toString() {
    return "CoordinateImpl{" +
        "latitude=" + latitude +
        ", longitude=" + longitude +
        '}';
  }
}
