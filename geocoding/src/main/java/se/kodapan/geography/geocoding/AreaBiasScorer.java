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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author kalle
 * @since 2010-jun-22 19:38:40
 */
public class AreaBiasScorer extends ResponseFilter {

  private Map<Polygon, Double> biasScores;

  public AreaBiasScorer(Geocoding input) {
    this(input, new HashMap<Polygon, Double>());
  }

  public AreaBiasScorer(Geocoding input, Collection<Polygon> bias) {
    this(input, new HashMap<Polygon, Double>());
    for (Polygon polygon : bias) {
      biasScores.put(polygon, 1d);
    }
  }

  public AreaBiasScorer(Geocoding input, Map<Polygon, Double> biasScores) {
    super(input);
    this.biasScores = biasScores;
  }

  @Override
  public Geocoding filter() throws Exception {
    Geocoding geocoding = input;

    Scorer scorer = new Scorer();
    if (geocoding.getResults().size() > 1) {
      for (Result result : geocoding.getResults()) {
        for (final Map.Entry<Polygon, Double> bias : biasScores.entrySet()) {

          scorer.setPolygon(bias.getKey());

          double score = result.getScore();
          score += result.accept(scorer);
          result.setScore(score);
        }
      }
    }

    return geocoding;
  }

  public class Scorer implements ResultVisitor<Double> {

    private Polygon polygon;

    public Polygon getPolygon() {
      return polygon;
    }

    public void setPolygon(Polygon polygon) {
      this.polygon = polygon;
    }

    @Override
    public Double visit(Coordinate location) {
      return polygon.contains(location) ? 1d : 0d;
    }

    @Override
    public Double visit(Polygon bounds) {
      return polygon.contains(bounds) ? 1d : 0d;
    }
  }


  public Map<Polygon, Double> getBiasScores() {
    return biasScores;
  }

  public void setBiasScores(Map<Polygon, Double> biasScores) {
    this.biasScores = biasScores;
  }
}