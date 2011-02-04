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
 * An envelope that holds two CoordinateImpls, one for southwest and one for northeast.
 *
 * @author kalle
 * @since 2010-jun-23 19:38:46
 */
public abstract class CoordinatedEnvelope extends AbstractEnvelope {

  private static final long serialVersionUID = 1l;


  private Coordinate southwest;
  private Coordinate northeast;

  @Override
  protected void coordinateFactory() {
    southwest = new CoordinateImpl();
    northeast = new CoordinateImpl();
  }

  public Coordinate getSouthwest() {
    return southwest;
  }

  public void setSouthwest(Coordinate southwest) {
    this.southwest = southwest;
  }

  public Coordinate getNortheast() {
    return northeast;
  }

  public void setNortheast(Coordinate northeast) {
    this.northeast = northeast;
  }
}
