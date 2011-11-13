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

import se.kodapan.geography.domain.Coordinate;
import se.kodapan.geography.domain.Envelope;
import se.kodapan.geography.domain.AddressComponents;
import se.kodapan.geography.domain.Precision;

import java.io.Serializable;
import java.util.*;

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


  
  public abstract <T> T accept(ResultVisitor<T> visitor);

  public abstract Envelope getBounds();

  public abstract void setBounds(Envelope bounds);

  public abstract Envelope getViewPort();

  public abstract void setViewPort(Envelope viewPort);

  public abstract Coordinate getLocation();

  public abstract void setLocation(Coordinate location);

  public abstract Precision getPrecision();

  public abstract void setPrecision(Precision precision);

  public abstract double getScore();

  public abstract void setScore(double score);

  public abstract AddressComponents getAddressComponents();

  public abstract void setAddressComponents(AddressComponents addressComponents);

  public abstract String getLicence();

  public abstract void setLicence(String licence);

  public abstract String getSource();

  public abstract void setSource(String source);

}
