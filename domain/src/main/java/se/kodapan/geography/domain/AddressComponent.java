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
package se.kodapan.geography.domain;

import java.io.Serializable;
import java.util.Arrays;
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
  private Set<String> types;

  public AddressComponent() {
    this (null, null, new HashSet<String>());
  }

  public AddressComponent(String longName, String type) {
    this (longName, null, new String[]{type});
  }

  public AddressComponent(String longName, String[] types) {
    this (longName, null, new HashSet<String>(Arrays.asList(types)));
  }

  public AddressComponent(String longName, String shortName, String[] types) {
    this (longName, shortName, new HashSet<String>(Arrays.asList(types)));
  }


  public AddressComponent(String longName, String shortName, Set<String> types) {
    if (longName == null) {
      longName = shortName;
    } else if (shortName == null) {
      shortName = longName;
    }
    this.longName = longName;
    this.shortName = shortName;
    this.types = types;
  }

  @Override
  public String toString() {
    return "AddressComponent{" +
        "longName='" + longName + '\'' +
        ", shortName='" + shortName + '\'' +
        ", types=" + types +
        '}';
  }

  public boolean hasShortName() {
    return shortName == null || shortName.equals(longName);
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


  public Set<String> getTypes() {
    return types;
  }

  public void setTypes(Set<String> types) {
    this.types = types;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    AddressComponent component = (AddressComponent) o;

    if (longName != null ? !longName.equals(component.longName) : component.longName != null) return false;
    if (shortName != null ? !shortName.equals(component.shortName) : component.shortName != null) return false;
    if (types != null ? !types.equals(component.types) : component.types != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = longName != null ? longName.hashCode() : 0;
    result = 31 * result + (shortName != null ? shortName.hashCode() : 0);
    result = 31 * result + (types != null ? types.hashCode() : 0);
    return result;
  }
}
