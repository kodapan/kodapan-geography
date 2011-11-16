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

import java.util.Iterator;

/**
 * @author kalle
 * @since 2010-jun-24 00:08:57
 */
public class ThreadsholdFilter extends ResponseFilter {

  public static final double defaultThreadshold = 0.7d;

  private double threadshold;

  public ThreadsholdFilter(Geocoding geocoding) {
    this(geocoding, defaultThreadshold);
  }

  public ThreadsholdFilter(Geocoding geocoding, double threadshold) {
    super(geocoding);
    this.threadshold = threadshold;
  }

  @Override
  public Geocoding filter() throws Exception {
    Integer threadsholdIndex = null;
    if (geocoding.getResults().size() > 1 && geocoding.getResults().get(0).getScore() > 0d) {
      int index = 0;
      Iterator<Result> it = geocoding.getResults().iterator();
      double topScore = it.next().getScore();
      while (it.hasNext()) {
        index++;
        Result result = it.next();
        if (result.getScore() / topScore <= threadshold) {
          if (threadsholdIndex == null) {
            threadsholdIndex = index;
          }
          it.remove();
        }
      }
      if (threadsholdIndex != null && threadsholdIndex == 1) {
        geocoding.setSuccess(true);
      }
    }
    return geocoding;
  }

}
