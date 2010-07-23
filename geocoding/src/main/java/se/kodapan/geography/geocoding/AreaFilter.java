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
package se.kodapan.geography.geocoding;

import se.kodapan.geography.core.Coordinate;
import se.kodapan.geography.core.Polygon;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * removes any result that does not match
 *
 * @author kalle
 * @since 2010-jun-23 00:43:01
 */
public class AreaFilter extends ResponseFilter {

  private Set<Polygon> areas;


  public AreaFilter(Geocoding input) {
    this(input, new HashSet<Polygon>());
  }

  public AreaFilter(Geocoding input, Polygon... areas) {
    this(input, new HashSet<Polygon>(Arrays.asList(areas)));
  }

  public AreaFilter(Geocoding input, Set<Polygon> areas) {
    super(input);
    this.areas = areas;
  }

  @Override
  public Geocoding filter() throws Exception {
    Logic logic = new Logic();
    for (Iterator<Result> it = geocoding.getResults().iterator(); it.hasNext();) {
      Result result = it.next();
      boolean found = false;
      for (Polygon area : areas) {
        logic.setArea(area);
        if (result.accept(logic)) {
          found = true;
          break;
        }
      }
      if (!found) {
        it.remove();
      }
    }

    geocoding.setSuccess(geocoding.getResults().size() > 0);

    return geocoding;
  }

  public class Logic implements ResultVisitor<Boolean> {

    private Polygon area;

    public Polygon getArea() {
      return area;
    }

    public void setArea(Polygon area) {
      this.area = area;
    }

    @Override
    public Boolean visit(Coordinate location) {
      return area.contains(location);
    }

    @Override
    public Boolean visit(Polygon bounds) {
      return area.contains(bounds);
    }
  }

  public Set<Polygon> getAreas() {
    return areas;
  }

  public void setAreas(Set<Polygon> areas) {
    this.areas = areas;
  }
}
