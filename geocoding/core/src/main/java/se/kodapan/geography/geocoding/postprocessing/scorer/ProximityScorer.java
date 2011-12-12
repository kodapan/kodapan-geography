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
package se.kodapan.geography.geocoding.postprocessing.scorer;

import se.kodapan.geography.domain.Coordinate;
import se.kodapan.geography.domain.Polygon;
import se.kodapan.geography.geocoding.geocoding.Geocoding;
import se.kodapan.geography.geocoding.geocoding.Result;
import se.kodapan.geography.geocoding.geocoding.ResultVisitor;
import se.kodapan.geography.geocoding.geocoding.ResultsScorer;

import java.util.*;

/**
 * @author kalle
 * @since 2010-jun-22 19:38:40
 */
public class ProximityScorer extends ResultsScorer {

  private Map<Polygon, Double> areaWeights;

  public void addAll(Collection<? extends Polygon> polygons) {
    for (Polygon polygon : polygons) {
      add(polygon);
    }
  }


  public void add(Polygon polygon) {
    areaWeights.put(polygon, 1d);
  }

  public ProximityScorer() {
    this(new HashMap<Polygon, Double>());
  }

  public ProximityScorer(Polygon... areas) {
    this(Arrays.asList(areas));
  }

  public ProximityScorer(Iterable<? extends Polygon> areaWeights) {
    this.areaWeights = new HashMap<Polygon, Double>();
    for (Polygon polygon : areaWeights) {
      this.areaWeights.put(polygon, 1d);
    }
  }

  public ProximityScorer(Map<Polygon, Double> areaWeights) {
    this.areaWeights = areaWeights;
  }


  @Override
  public void score(Geocoding geocoding) throws Exception {

    if (geocoding.getResults().size() > 0) {
      Map<Result, Double> distances = new HashMap<Result, Double>();
      double kmFurthestAway = Double.MIN_VALUE;
      double kmClosest = Double.MAX_VALUE;

      for (final Result result : geocoding.getResults()) {

        double kmClosestArea = Double.MAX_VALUE;

        for (final Map.Entry<Polygon, Double> areaWeight : getAreaWeights().entrySet()) {
          double distance = result.accept(new ResultVisitor<Double>(){
            @Override
            public Double visit(Coordinate location) {
              if (areaWeight.getKey().contains(location)) {
                return 0d;
              }
              return areaWeight.getKey().arcDistance(location) * areaWeight.getValue();
            }

            @Override
            public Double visit(Polygon bounds) {
              if (areaWeight.getKey().contains(bounds) || bounds.contains(areaWeight.getKey())) {
                return 0d;
              }
              return areaWeight.getKey().arcDistance(bounds) * areaWeight.getValue();
            }
          });
          
          if (distance < kmClosestArea) {
            kmClosestArea = distance;
            if (distance == 0d) {
              break;
            }
          }
        }

        distances.put(result, kmClosestArea);

        if (kmClosestArea < kmClosest) {
          kmClosest = kmClosestArea;
        }
        if (kmClosestArea > kmFurthestAway
            || kmFurthestAway == Double.MIN_VALUE) {
          kmFurthestAway = kmClosestArea;
        }

      }
        
      if (kmFurthestAway > 0) {

        double factor = 1d / kmFurthestAway;

        for (Result result : geocoding.getResults()) {
          double distance = distances.get(result);
          double score = kmFurthestAway - distance;
          score *= factor;
          result.setScore(score);
        }
      }

    }

    Collections.sort(geocoding.getResults(), Result.scoreComparator);
  }


  public Map<Polygon, Double> getAreaWeights() {
    return areaWeights;
  }

  public void setAreaWeights(Map<Polygon, Double> areaWeights) {
    this.areaWeights = areaWeights;
  }
}