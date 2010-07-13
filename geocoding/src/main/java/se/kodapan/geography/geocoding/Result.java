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

import se.kodapan.collections.MapSet;
import se.kodapan.geography.core.Coordinate;
import se.kodapan.geography.core.Envelope;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

/**
 * @author kalle
 * @since 2010-jun-25 23:21:17
 */
public interface Result extends Serializable {

  public static Comparator<Result> scoreComparator = new Comparator<Result>() {
    @Override
    public int compare(Result minuend, Result subtrahend) {
      double difference = subtrahend.getScore() - minuend.getScore();
      if (difference == 0d) {
        return 0;
      } else if (difference < 0d) {
        return -1;
      } else {
        return 1;
      }
    }
  };

  public static class ResultTool {
    public static AddressComponent findAddressComponentByType(Result result, AddressComponentType... type) {
      List<AddressComponentType> list = Arrays.asList(type);
      Set<AddressComponent> set = result.getAddressComponentsByType().get(type[0]);
      if (set != null && set.size() > 0) {
        AddressComponent found = null;
        for (AddressComponent component : set) {
          if (component.getTypes().containsAll(list)) {
            if (found != null) {
              throw new RuntimeException("More than one AddressComponent matches " + list);
            }
            found = component;
          }
        }
        return found;
      } else {
        return null;
      }
    }

  }

  public abstract <T> T accept(ResultVisitor<T> visitor);

  public abstract Envelope getBounds();

  public abstract void setBounds(Envelope bounds);

  public abstract Envelope getViewPort();

  public abstract void setViewPort(Envelope viewPort);

  public abstract Coordinate getLocation();

  public abstract void setLocation(Coordinate location);

  public abstract AddressComponent findAddressComponentByType(AddressComponentType... type);

  /**
   * values are updated at geocoding time, consider this immutable.
   */
  public abstract MapSet<AddressComponentType, AddressComponent> getAddressComponentsByType();

  public abstract void setAddressComponentsByType(MapSet<AddressComponentType, AddressComponent> addressComponentsByType);

  public abstract Precision getPrecision();

  public abstract void setPrecision(Precision precision);

  public abstract double getScore();

  public abstract void setScore(double score);

  public abstract String getFormattedAddress();

  public abstract void setFormattedAddress(String formattedAddress);

  public abstract List<AddressComponent> getAddressComponents();

  public abstract void setAddressComponents(List<AddressComponent> addressComponents);
}
