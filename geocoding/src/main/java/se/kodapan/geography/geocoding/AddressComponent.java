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

import se.kodapan.geography.core.AbstractEnvelope;
import se.kodapan.geography.core.AbstractPolygonDecorator;
import se.kodapan.geography.core.Polygon;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * @author kalle
 * @since 2010-jun-22 21:18:12
 */
public class AddressComponent implements Serializable {

  private static final long serialVersionUID = 1l;
  
  private String longName;
  private String shortName;
  private Set<AddressComponentType> types = new HashSet<AddressComponentType>();


  @Override
  public String toString() {
    return "AddressComponent{" +
        "longName='" + longName + '\'' +
        ", shortName='" + shortName + '\'' +
        ", types=" + types +
        '}';
  }

  public String getLongName() {
    return longName;
  }

  public void setLongName(String longName) {
    this.longName = longName;
  }

  public String getShortName() {
    return shortName;
  }

  public void setShortName(String shortName) {
    this.shortName = shortName;
  }


  public Set<AddressComponentType> getTypes() {
    return types;
  }

  public void setTypes(Set<AddressComponentType> types) {
    this.types = types;
  }

}
