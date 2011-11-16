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
import se.kodapan.geography.domain.Coordinate;

import java.io.InputStream;

/**
 * @author kalle
 * @since 2010-jun-23 21:21:04
 */
public abstract class Geocoder {

  protected static final Logger log = LoggerFactory.getLogger(Geocoder.class);

  /**
   * @return the number of results possible to get using this geocoder.
   */
  public abstract int getMaximumResultsReturned();

  public abstract Geocoding reverse(CoordinateQuery query) throws Exception;
  public abstract Geocoding geocode(TextQuery query) throws Exception;

  public abstract Geocoding parseReverseServerResponse(InputStream inputStream) throws Exception;
  public abstract Geocoding parseGeocodeServerResponse(InputStream inputStream) throws Exception;

  public abstract String getName();
  public abstract String getVersion();
  public abstract String getDefaultLicense();

  public Source sourceFactory() {
    Source source = new Source();
    source.setName(getName());
    source.setVersion(getVersion());
    source.setLicense(getDefaultLicense());
    return source;
  }
}
