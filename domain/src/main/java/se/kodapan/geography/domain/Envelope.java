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
 * @since 2010-jun-23 19:06:33
 */
public interface Envelope extends Polygon {

  public abstract Coordinate getSouthwest();
  public abstract Coordinate getSoutheast();
  public abstract Coordinate getNortheast();
  public abstract Coordinate getNorthwest();

  public abstract void addBounds(Polygon polygon);

  public abstract void addBounds(Coordinate... coordinates);

  public abstract void addBounds(double latitude, double longitude);

  public abstract double arcDistanceDiagonal();

}
