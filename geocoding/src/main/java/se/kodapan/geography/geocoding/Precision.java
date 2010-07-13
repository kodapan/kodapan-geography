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

import java.io.Serializable;

/**
 * @author kalle
 * @since 2010-jun-24 14:26:57
 */
public enum Precision implements Serializable {

  // from google

  /** indicates that the returned result is a precise geocode for which we have location information accurate down to street address precision. */
  ROOFTOP,

  /** indicates that the returned result reflects an approximation (usually on a road) interpolated between two precise points (such as intersections). Interpolated results are generally returned when rooftop geocodes are unavailable for a street address. */
  RANGE_INTERPOLATED,

  /**  indicates that the returned result is the geometric center of a result such as a polyline (for example, a street) or polygon (region). */
  GEOMETRIC_CENTER,

  /** indicates that the returned result is approximate. */
  APPROXIMATE;

  private static final long serialVersionUID = 1l;
  




}
