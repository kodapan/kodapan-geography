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
package se.kodapan.geography.tools;

import se.kodapan.geography.domain.Coordinate;
import se.kodapan.geography.domain.Polygon;

import java.io.Serializable;
import java.util.Set;

/**
 * @author kalle
 * @since 2010-sep-04 21:32:57
 */
public abstract class ConvexHull implements Serializable {

  private static final long serialVersionUID = 1l;

  public abstract Polygon factory(Set<Coordinate> coordinates);


}
