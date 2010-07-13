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
public class ScoreThreadsholdFilter extends ResponseFilter {

  public static final double defaultThreadshold = 0.7d;

  private double threadshold;

  public ScoreThreadsholdFilter(Geocoding input) {
    this(input, defaultThreadshold);
  }

  public ScoreThreadsholdFilter(Geocoding input, double threadshold) {
    super(input);
    this.threadshold = threadshold;
  }

  @Override
  public Geocoding filter() throws Exception {
    if (input.getResults().size() > 1 && input.getResults().get(0).getScore() > 0d) {
      double factor = 1d / input.getResults().get(0).getScore();
      for (Iterator<Result> it = input.getResults().iterator(); it.hasNext();) {
        Result result = it.next();
        if (result.getScore() * factor < threadshold) {
          it.remove();
        }
      }
    }
    input.setSuccess(input.getResults().size() == 1);
    return input;
  }

}
