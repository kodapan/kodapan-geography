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
import java.util.List;

/**
 * @author kalle
 * @since 2010-sep-04 21:36:46
 */
public class PolygonImpl extends AbstractPolygon {

  private static final long serialVersionUID = 1l;

  private List<Coordinate> coordinates;

  public PolygonImpl() {
  }

  public PolygonImpl(List<Coordinate> coordinates) {
    this.coordinates = coordinates;
  }

  @Override
  public Iterator<Coordinate> iterateCoordinates() {
    return coordinates.iterator();
  }

  @Override
  public boolean contains(Coordinate coordinate) {
    return coordinates.contains(coordinate);
  }

  @Override
  public Coordinate getCentroid() {
    throw new UnsupportedOperationException();
  }

  public List<Coordinate> getCoordinates() {
    return coordinates;
  }

  public void setCoordinates(List<Coordinate> coordinates) {
    this.coordinates = coordinates;
  }
}