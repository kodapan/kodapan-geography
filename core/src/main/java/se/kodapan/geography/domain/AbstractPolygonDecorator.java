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

import java.util.Iterator;

/**
 * @author kalle
 * @since 2010-jun-23 19:50:50
 */
public abstract class AbstractPolygonDecorator implements Polygon {

  private static final long serialVersionUID = 1l;


  @Override
  public double archDistance(Coordinate that) {
    return getDecoratedPolygon().archDistance(that);
  }

  @Override
  public Iterator<Coordinate> iterateCoordinates() {
    return getDecoratedPolygon().iterateCoordinates();
  }

  @Override
  public boolean contains(Coordinate coordinate) {
    return getDecoratedPolygon().contains(coordinate);
  }

  @Override
  public boolean contains(Polygon that) {
    return getDecoratedPolygon().contains(that);
  }

  @Override
  public Coordinate getCentroid() {
    return getDecoratedPolygon().getCentroid();
  }

  @Override
  public double archDistance(Polygon that) {
    return getDecoratedPolygon().archDistance(that);
  }

  public abstract Polygon getDecoratedPolygon();




}
