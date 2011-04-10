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
 * @since 2010-mar-16 06:42:18
 */
public class CoordinateImpl extends AbstractCoordinate {

  private static final long serialVersionUID = 1l;

  private Double latitude;
  private Double longitude;

  public CoordinateImpl() {
  }

  public CoordinateImpl(Coordinate coordinate) {
    this(coordinate.getLatitude(), coordinate.getLongitude());
  }

  public CoordinateImpl(Number latitude, Number longitude) {
    this(latitude.doubleValue(), longitude.doubleValue());
  }

  public CoordinateImpl(Double latitude, Double longitude) {
    setLatitude(latitude);
    setLongitude(longitude);
  }

  public Double getLatitude() {
    if (latitude == null) {
      System.currentTimeMillis();
    }
    return latitude;
  }

  public void setLatitude(Double latitude) {
    if (latitude == null) {
      System.currentTimeMillis();
    }
    this.latitude = latitude;
  }

  public Double getLongitude() {
    return longitude;
  }

  public void setLongitude(Double longitude) {
    if (longitude == null) {
      System.currentTimeMillis();
    }

    this.longitude = longitude;
  }


}
