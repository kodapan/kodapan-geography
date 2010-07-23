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
import se.kodapan.geography.core.*;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * @author kalle
 * @since 2010-jun-22 20:03:23
 */
public class ResultImpl extends AbstractResult implements Serializable {

  private static final long serialVersionUID = 1l;


  private double score = 1d;

  @Override
  public <T> T accept(ResultVisitor<T> visitor) {

    switch (getPrecision()) {
      case ROOFTOP:
        return visitor.visit(getLocation());
      case RANGE_INTERPOLATED:
        return visitor.visit(getLocation());
      default:
        return visitor.visit(getBounds());
    }

  }

  private AddressComponents addressComponents = new AddressComponents();
  private String formattedAddress;

  private Envelope bounds;
  private Envelope viewPort;
  private Coordinate location;
  private Precision precision;


  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || !Result.class.isAssignableFrom(o.getClass())) return false;
    if (!super.equals(o)) return false;

    Result result = (Result) o;

    if (formattedAddress != null ? !formattedAddress.equalsIgnoreCase(result.getFormattedAddress()) : result.getFormattedAddress() != null)
      return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + (formattedAddress != null ? formattedAddress.hashCode() : 0);
    return result;
  }

  @Override
  public void setFormattedAddress(Locale locale) {
    StringBuilder sb = new StringBuilder();

    AddressComponent route = addressComponents.get(AddressComponentType.route);

    AddressComponent streetNumber = addressComponents.get(AddressComponentType.street_number);
    if (streetNumber != null) {
      if (locale.getLanguage().equals("en")) {

        sb.append(streetNumber.getLongName());
        sb.append(" ");
        sb.append(route.getLongName());
        sb.append(", ");


      } else {

        sb.append(route.getLongName());
        sb.append(" ");
        sb.append(streetNumber.getLongName());
        sb.append(", ");

      }

    } else {

      if (route != null) {
        sb.append(route.getLongName());

      } else {
        AddressComponent streetAddress = addressComponents.get(AddressComponentType.street_address);
        if (streetAddress != null) {
          sb.append(streetAddress.getLongName());
          sb.append(", ");
        }

      }

    }


    AddressComponent subLocality = addressComponents.get(AddressComponentType.sublocality, AddressComponentType.political);
    if (subLocality != null) {
      sb.append(subLocality.getLongName());
      sb.append(", ");

    }

    // todo find the most inner postal code, but only where there is one most inner postal code in the result. there might be several for a city, etc.
    List<AddressComponent> postalCodes = getAddressComponents().list(AddressComponentType.postal_code);
    if (postalCodes != null && postalCodes.size() == 1) {
      AddressComponent postalCode = postalCodes.iterator().next();
      sb.append(postalCode.getLongName());
      sb.append(", ");
    }
    
    AddressComponent city = addressComponents.get(AddressComponentType.locality, AddressComponentType.political);
    if (city != null) {
      sb.append(city.getLongName());
      sb.append(", ");
    }



    AddressComponent country = addressComponents.get(AddressComponentType.country);
    if (country != null) {
      sb.append(country.getLongName());
      sb.append(", ");
    }

    sb.delete(sb.length() - 2, sb.length());

    setFormattedAddress(sb.toString());

  }


  @Override
  public Envelope getBounds() {
    return bounds;
  }

  @Override
  public void setBounds(Envelope bounds) {
    EnvelopeImpl envelope = new EnvelopeImpl(getFormattedAddress());
    envelope.setNorthEast(new CoordinateImpl(bounds.getNorthEast()));
    envelope.setSouthWest(new CoordinateImpl(bounds.getSouthWest()));
    this.bounds = envelope;
  }

  @Override
  public Envelope getViewPort() {
    return viewPort;
  }

  @Override
  public void setViewPort(Envelope viewPort) {
    EnvelopeImpl envelope = new EnvelopeImpl(getFormattedAddress());
    envelope.setNorthEast(new CoordinateImpl(viewPort.getNorthEast()));
    envelope.setSouthWest(new CoordinateImpl(viewPort.getSouthWest()));
    this.viewPort = envelope;
  }

  @Override
  public Coordinate getLocation() {
    return location;
  }

  @Override
  public void setLocation(Coordinate location) {
    this.location = new CoordinateImpl(location);
  }


  @Override
  public Precision getPrecision() {
    return precision;
  }

  @Override
  public void setPrecision(Precision precision) {
    this.precision = precision;
  }

  @Override
  public double getScore() {
    return score;
  }

  @Override
  public void setScore(double score) {
    this.score = score;
  }

  @Override
  public String getFormattedAddress() {
    return formattedAddress;
  }

  @Override
  public void setFormattedAddress(String formattedAddress) {
    this.formattedAddress = formattedAddress;
  }

  @Override
  public AddressComponents getAddressComponents() {
    return addressComponents;
  }

  @Override
  public void setAddressComponents(AddressComponents addressComponents) {
    this.addressComponents = addressComponents;
  }


  @Override
  public String toString() {
    return "ResultImpl{" +
        "score=" + score +
        ", formattedAddress='" + formattedAddress + '\'' +
        ", precision=" + precision +
        ", location=" + location +
        ", bounds=" + bounds +
        ", viewPort=" + viewPort +
        ", addressComponents=" + addressComponents +
        '}';
  }
}
