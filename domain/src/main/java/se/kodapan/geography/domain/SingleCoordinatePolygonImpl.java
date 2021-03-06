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
 * A polygon that actually only is a single coordinate point,
 * represented as an envelope where all all corners points at the same point.
 *
 * @author kalle
 * @since 2010-jun-24 15:00:41
 */
public class SingleCoordinatePolygonImpl extends AbstractEnvelope implements SingleCoordinatePolygon {

  private static final long serialVersionUID = 1l;


  private Coordinate coordinate;

  public SingleCoordinatePolygonImpl(Coordinate coordinate) {
    this.coordinate = coordinate;
  }

  public SingleCoordinatePolygonImpl() {
  }

  @Override
  protected void coordinateFactory() {
    coordinate = new CoordinateImpl();
  }

  @Override
  public Coordinate getSouthwest() {
    return coordinate;
  }

  @Override
  public Coordinate getNortheast() {
    return coordinate;
  }

  @Override
  public Coordinate getCoordinate() {
    return coordinate;
  }

  @Override
  public void setCoordinate(Coordinate coordinate) {
    this.coordinate = coordinate;
  }


}
