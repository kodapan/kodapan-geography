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

import java.util.*;

/**
 * @author kalle
 * @since 2010-jun-25 22:30:19
 */
public class PolygonTools {

  /**
   * @see #findSmallestEnclosingBounds(java.util.Collection)
   */
  public static Polygon findSmallestEnclosingBounds(Polygon... input) {
    return findSmallestEnclosingBounds(Arrays.asList(input));
  }


  /**
   * @return the smallest polygon that contain all smaller polygons.
   */
  public static Polygon findSmallestEnclosingBounds(Collection<? extends Polygon> input) {
    if (input.size() == 0) {
      return null;
    }
    if (input.size() == 1) {
      return input.iterator().next();
    }
    List<Polygon> list = new ArrayList<Polygon>(input);
    Collections.sort(list, new Comparator<Polygon>() {
      @Override
      public int compare(Polygon polygon, Polygon polygon1) {
        return polygon.equals(polygon1) ? 0 : polygon.contains(polygon1) ? -1 : 1;
      }
    });

    // find the chain where next link is still contained by previous link.
    boolean found = false;
    int i = 0;
    while (i < list.size()) {
      for (int i2 = i + 1; i2 < list.size(); i2++) {
        if (!list.get(i).contains(list.get(i2))) {
          found = true;
          break;
        }
      }
      if (found) {
        break;
      }
      i++;
    }
    if (i == 0) {
      return null;
    }
    list = list.subList(0, i);
    return list.get(i-1);
  }


}
