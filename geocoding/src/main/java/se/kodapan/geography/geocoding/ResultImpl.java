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
import java.util.ArrayList;
import java.util.List;
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

    switch(getPrecision()) {
      case ROOFTOP: return visitor.visit(getLocation());
      case RANGE_INTERPOLATED: return visitor.visit(getLocation());
      default: return visitor.visit(getBounds());
    }

  }


  private String formattedAddress;
  private List<AddressComponent> addressComponents = new ArrayList<AddressComponent>();
  private MapSet<AddressComponentType, AddressComponent> addressComponentsByType = new MapSet<AddressComponentType, AddressComponent>();

  private Envelope bounds;
  private Envelope viewPort;
  private Coordinate location;  
  private Precision precision;


  @Override
  public Envelope getBounds() {
    return bounds;
  }

  @Override
  public void setBounds(Envelope bounds) {
    this.bounds = bounds;
  }

  @Override
  public Envelope getViewPort() {
    return viewPort;
  }

  @Override
  public void setViewPort(Envelope viewPort) {
    this.viewPort = viewPort;
  }

  @Override
  public Coordinate getLocation() {
    return location;
  }

  @Override
  public void setLocation(Coordinate location) {
    this.location = location;
  }

  @Override
  public MapSet<AddressComponentType, AddressComponent> getAddressComponentsByType() {
    return addressComponentsByType;
  }

  @Override
  public void setAddressComponentsByType(MapSet<AddressComponentType, AddressComponent> addressComponentsByType) {
    this.addressComponentsByType = addressComponentsByType;
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
  public List<AddressComponent> getAddressComponents() {
    return addressComponents;
  }

  @Override
  public void setAddressComponents(List<AddressComponent> addressComponents) {
    this.addressComponents = addressComponents;
  }

  @Override
  public String toString() {
    return "Result{" +
        "score=" + score +
        ", type='" + precision + '\'' +
        ", formattedAddress='" + formattedAddress + '\'' +
        ", addressComponents=" + addressComponents +
        '}';
  }
}
