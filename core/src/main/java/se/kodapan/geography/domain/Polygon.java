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

import java.io.Serializable;
import java.util.Iterator;

/**
 * @author kalle
 * @since 2010-maj-30 23:14:04
 */
public interface Polygon extends Serializable {

//  private static final long serialVersionUID = 1l;

//  public String getPolygonName();

  /**
   * for now only used to draw polygons, not yet for functionality.
   * @return ordered coordinates in polygon.
   */
  public abstract Iterator<Coordinate> iterateCoordinates();

  public abstract boolean contains(Coordinate coordinate);

  public abstract boolean contains(Polygon polygon);

  public Coordinate getCentroid();

  /**
   * @param that
   * @return distance in km between two closest point of the two polygons
   */
  public abstract double archDistance(Polygon that);

  public abstract double archDistance(Coordinate that);

}
