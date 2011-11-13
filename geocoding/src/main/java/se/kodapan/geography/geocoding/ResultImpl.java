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

import se.kodapan.geography.domain.*;
import se.kodapan.geography.domain.Coordinate;
import se.kodapan.geography.domain.Envelope;

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

  private String source;
  private String licence;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    ResultImpl result = (ResultImpl) o;

    if (Double.compare(result.score, score) != 0) return false;
    if (addressComponents != null ? !addressComponents.equals(result.addressComponents) : result.addressComponents != null)
      return false;
    if (bounds != null ? !bounds.equals(result.bounds) : result.bounds != null)
      return false;
    if (licence != null ? !licence.equals(result.licence) : result.licence != null)
      return false;
    if (location != null ? !location.equals(result.location) : result.location != null)
      return false;
    if (precision != result.precision) return false;
    if (source != null ? !source.equals(result.source) : result.source != null)
      return false;
    if (viewPort != null ? !viewPort.equals(result.viewPort) : result.viewPort != null)
      return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result;
    long temp;
    temp = score != +0.0d ? Double.doubleToLongBits(score) : 0L;
    result = (int) (temp ^ (temp >>> 32));
    result = 31 * result + (addressComponents != null ? addressComponents.hashCode() : 0);
    result = 31 * result + (bounds != null ? bounds.hashCode() : 0);
    result = 31 * result + (viewPort != null ? viewPort.hashCode() : 0);
    result = 31 * result + (location != null ? location.hashCode() : 0);
    result = 31 * result + (precision != null ? precision.hashCode() : 0);
    result = 31 * result + (source != null ? source.hashCode() : 0);
    result = 31 * result + (licence != null ? licence.hashCode() : 0);
    return result;
  }

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public String getLicence() {
    return licence;
  }

  public void setLicence(String licence) {
    this.licence = licence;
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
      EnvelopeImpl envelope = new EnvelopeImpl();
      envelope.setNortheast(new CoordinateImpl(bounds.getNortheast()));
      envelope.setSouthwest(new CoordinateImpl(bounds.getSouthwest()));
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
      EnvelopeImpl envelope = new EnvelopeImpl();
      envelope.setNortheast(new CoordinateImpl(viewPort.getNortheast()));
      envelope.setSouthwest(new CoordinateImpl(viewPort.getSouthwest()));
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
