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
package se.kodapan.geography.geocoding.postprocessing.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.kodapan.geography.geocoding.geocoding.Geocoding;
import se.kodapan.io.SerializableTool;

/**
 * @author kalle
 * @since 2010-jun-22 23:10:11
 */
public abstract class ResponseFilter  {

  protected static final Logger log = LoggerFactory.getLogger(ResponseFilter.class);

  protected Geocoding geocoding;

  protected ResponseFilter(Geocoding geocoding) {
    this.geocoding = SerializableTool.clone(geocoding);
  }

  public abstract Geocoding filter() throws Exception;

}