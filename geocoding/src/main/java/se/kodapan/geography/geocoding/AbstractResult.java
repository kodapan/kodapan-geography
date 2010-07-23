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

/**
 * @author kalle
 * @since 2010-jun-26 01:42:41
 */
public abstract class AbstractResult implements Result {

  private static final long serialVersionUID = 1l;  


  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || !Result.class.isAssignableFrom(o.getClass())) return false;

    Result result = (Result) o;

    return !(getFormattedAddress() != null ? !getFormattedAddress().equals(result.getFormattedAddress()) : result.getFormattedAddress() != null);

  }

  @Override
  public int hashCode() {
    return getFormattedAddress() != null ? getFormattedAddress().hashCode() : 0;
  }

  
}
