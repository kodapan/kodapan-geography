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
import se.kodapan.geography.core.Polygon;

import java.util.HashMap;
import java.util.Map;

/**
 * @author kalle
 * @since 2010-jun-23 21:21:04
 */
public abstract class Geocoder {

  protected static final Logger log = LoggerFactory.getLogger(Geocoder.class);

  public Geocoding geocode(String textQuery) throws Exception {
    return geocode(new Request(textQuery));
  }

  public Geocoding geocode(String textQuery, Polygon bounds) throws Exception {
    return geocode(new Request(textQuery, bounds));
  }



  public Geocoding geocode(Request request) throws Exception {
    return geocode(request, new HashMap<Request, Geocoding>());
  }

  /**
   *
   * @param request
   * @param cache avoids eternal loops when executing sub-geocoding on address components
   * @return
   * @throws Exception
   */
  public abstract Geocoding geocode(Request request, Map<Request, Geocoding> cache) throws Exception;

}
