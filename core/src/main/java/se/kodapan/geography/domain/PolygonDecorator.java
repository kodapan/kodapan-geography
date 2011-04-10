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
 * @since 2010-jun-25 23:35:55
 */
public class PolygonDecorator implements Polygon {

  private static final long serialVersionUID = 1l;


  private Polygon decorated;

  public PolygonDecorator() {
  }

  public PolygonDecorator(Polygon decorated) {
    this.decorated = decorated;
  }

  public Polygon getDecorated() {
    return decorated;
  }

  public void setDecorated(Polygon decorated) {
    this.decorated = decorated;
  }

  @Override
  public Iterator<Coordinate> iterateCoordinates() {
    return getDecorated().iterateCoordinates();
  }

  @Override
  public boolean contains(Coordinate coordinate) {
    return getDecorated().contains(coordinate);
  }

  @Override
  public boolean contains(Polygon polygon) {
    return getDecorated().contains(polygon);
  }

  @Override
  public Coordinate getCentroid() {
    return getDecorated().getCentroid();
  }

  @Override
  public double archDistance(Polygon that) {
    return getDecorated().archDistance(that);
  }

  @Override
  public double archDistance(Coordinate that) {
    return getDecorated().archDistance(that);
  }
}
