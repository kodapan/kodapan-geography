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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.kodapan.geography.core.Coordinate;
import se.kodapan.geography.core.Polygon;

/**
 * @author kalle
 * @since 2010-jun-23 21:21:04
 */
public abstract class Geocoder {

  protected static final Logger log = LoggerFactory.getLogger(Geocoder.class);

  /**
   * reverse geocoding
   * 
   * @param coordinate
   * @return
   * @throws Exception
   */
  public abstract Geocoding geocode(Coordinate coordinate, String preferredLanguage) throws Exception;


  public Geocoding geocode(Coordinate coordinate) throws Exception {
    return geocode(coordinate, null);
  }

  public Geocoding geocode(String textQuery) throws Exception {
    return geocode(new Request(textQuery));
  }

  public Geocoding geocode(String textQuery, Polygon bounds) throws Exception {
    return geocode(new Request(textQuery, bounds));
  }

  public final Geocoding geocode(Request request) throws Exception {
    Geocoding geocoding = doGeocode(request);
    // todo add debug level (test scope) checking that this geocoding has not been touched by the filter!
    if (request.getAugmenter() != null) {
      Geocoding response = request.getAugmenter().filter(this, request, geocoding);
      if (response != null) {
        geocoding = response;
      }
    }
    return geocoding;
  }

  /**
   * @param request
   * @return
   * @throws Exception
   */
  protected abstract Geocoding doGeocode(Request request) throws Exception;

}
