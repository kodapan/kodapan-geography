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

import se.kodapan.geography.domain.AddressComponents;
import se.kodapan.geography.polygon.Coordinate;
import se.kodapan.geography.polygon.CoordinateImpl;
import se.kodapan.geography.polygon.Envelope;
import se.kodapan.geography.polygon.EnvelopeImpl;

import java.io.Serializable;

/**
 * @author kalle
 * @since 2010-jun-22 20:03:23
 */
public class ResultImpl extends AbstractResult implements Serializable {

  private static final long serialVersionUID = 1l;


  private double score = 1d;

  @Override
  public <T> T accept(ResultVisitor<T> visitor) {
    if (getPrecision() == null) {
      return visitor.visit(getBounds());
    }
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

  private Envelope bounds;
  private Envelope viewPort;
  private Coordinate location;
  private Precision precision;


  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || !Result.class.isAssignableFrom(o.getClass())) return false;

    Result result = (Result) o;

    String formattedAddress = addressComponents != null ? addressComponents.getFormattedAddress() : null;
    String formattedAddress2 = result.getAddressComponents() != null ? result.getAddressComponents().getFormattedAddress() : null;
    return !(formattedAddress != null ? !formattedAddress.equalsIgnoreCase(formattedAddress2) : formattedAddress2 != null);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + (addressComponents != null ? addressComponents.getFormattedAddress() != null ? addressComponents.getFormattedAddress().hashCode() : 0 : 0);
    return result;
  }

  @Override
  public Envelope getBounds() {
    return bounds;
  }

  @Override
  public void setBounds(Envelope bounds) {
    if (bounds == null) {
      this.bounds = null;
    } else {
      EnvelopeImpl envelope = new EnvelopeImpl(addressComponents.getFormattedAddress());
      envelope.setNorthEast(new CoordinateImpl(bounds.getNorthEast()));
      envelope.setSouthWest(new CoordinateImpl(bounds.getSouthWest()));
      this.bounds = envelope;
    }
  }

  @Override
  public Envelope getViewPort() {
    return viewPort;
  }

  @Override
  public void setViewPort(Envelope viewPort) {
    if (viewPort == null) {
      this.viewPort = null;
    } else {
      EnvelopeImpl envelope = new EnvelopeImpl(addressComponents.getFormattedAddress());
      envelope.setNorthEast(new CoordinateImpl(viewPort.getNorthEast()));
      envelope.setSouthWest(new CoordinateImpl(viewPort.getSouthWest()));
      this.viewPort = envelope;
    }
  }

  @Override
  public Coordinate getLocation() {
    return location;
  }

  @Override
  public void setLocation(Coordinate location) {
    if (location == null) {
      this.location = null;
    } else {
      this.location = new CoordinateImpl(location);
    }
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
        ", addressComponents=" + addressComponents +
        ", precision=" + precision +
        ", location=" + location +
        ", bounds=" + bounds +
        ", viewPort=" + viewPort +
        '}';
  }
}
