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
package com.google.maps.geocoding;

import java.io.Serializable;

/**
 * @author kalle
 * @since 2010-jun-23 20:19:18
 */
public enum AddressComponentType implements Serializable {


  /**
   * indicates a precise street address.
   */
  street_address,
  /**
   * indicates a named route (such as "US 101").
   */
  route,
  /**
   * indicates a major intersection, usually of two major roads.
   */
  intersection,
  /**
   * indicates a political entity. Usually, this type indicates a polygon of some civil administration.
   */
  political,
  /**
   * indicates the national political entity, and is typically the highest order type returned by the Geocoder.
   */
  country,
  /**
   * indicates a first-order civil entity below the country level. Within the United States, these administrative levels are states. Not all nations exhibit these administrative levels.
   */
  administrative_area_level_1,
  /**
   * indicates a second-order civil entity below the country level. Within the United States, these administrative levels are counties. Not all nations exhibit these administrative levels.
   */
  administrative_area_level_2,
  /**
   * indicates a third-order civil entity below the country level. This type indicates a minor civil division. Not all nations exhibit these administrative levels.
   */
  administrative_area_level_3,
  /**
   * indicates a commonly-used alternative name for the entity.
   */
  colloquial_area,
  /**
   * indicates an incorporated city or town political entity.
   */
  locality,
  /**
   * indicates an first-order civil entity below a locality
   */
  sublocality,
  /**
   * indicates a named neighborhood
   */
  neighborhood,
  /**
   * indicates a named location, usually a building or collection of buildings with a common name
   */
  premise,
  /**
   * indicates a first-order entity below a named location, usually a singular building within a collection of buildings with a common name
   */
  subpremise,
  /**
   * indicates a postal code as used to address postal mail within the country.
   */
  postal_code,
  /**
   * indicates a prominent natural feature.
   */
  natural_feature,
  /**
   * indicates an airport.
   */
  airport,
  /**
   * indicates a named park.
   */
  park,


  // In addition to the above, address components may exhibit the following types


  /**
   * indicates a specific postal box.
   */
  post_box,
  /**
   * indicates the precise street number.
   */
  street_number,
  /**
   * indicates the floor of a building address.
   */
  floor,
  /**
   * indicates the room of a building address.
   */
  room;

  private static final long serialVersionUID = 1l;

}
