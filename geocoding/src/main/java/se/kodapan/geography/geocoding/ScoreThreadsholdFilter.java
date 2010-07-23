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
public class ScoreThreadsholdFilter extends ResultsScorer {

  public static final double defaultThreadshold = 0.7d;

  private double threadshold;

  public ScoreThreadsholdFilter() {
    this(defaultThreadshold);
  }

  public ScoreThreadsholdFilter(double threadshold) {
    this.threadshold = threadshold;
  }

  @Override
  public void score(Geocoding geocoding) throws Exception {
    int threadsholdIndex = geocoding.getResults().size();
    if (geocoding.getResults().size() > 1 && geocoding.getResults().get(0).getScore() > 0d) {
      double topScore = geocoding.getResults().get(0).getScore();
      java.util.List<Result> results = geocoding.getResults();
      for (int i = 1, resultsSize = results.size(); i < resultsSize; i++) {
        Result result = results.get(i);
        if (result.getScore() / topScore <= threadshold) {
          threadsholdIndex = i;
          break;
        }
      }
      if (threadsholdIndex == 1) {
        geocoding.setSuccess(true);
      }      
    }
  }

}
