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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * decorates the successful result.
 *
 *
 * @author kalle
 * @since 2010-jun-22 19:58:24
 */
public class Geocoding extends AbstractPolygonDecorator implements Result, Coordinate, Serializable {

  private static final long serialVersionUID = 1l;
  
  /**
   * if true then this Response instance is the named geocoded polygon.
   */
  private boolean success;

  private Object serverResponse;

  private List<Result> results = new ArrayList<Result>();

  public boolean contains(Geocoding geocoding) {
    return contains((Polygon)geocoding);
  }

  public double archDistance(Geocoding that) {
    return archDistance((Polygon)that);
  }


  @Override
  public double getLatitude() {
    return getDecoratedPolygon().getCentroid().getLatitude();
  }

  @Override
  public void setLatitude(double latitude) {
    getDecoratedPolygon().getCentroid().setLatitude(latitude);
  }

  @Override
  public double getLongitude() {
    return getDecoratedPolygon().getCentroid().getLongitude();
  }

  @Override
  public void setLongitude(double longitude) {
    getDecoratedPolygon().getCentroid().setLongitude(longitude);
  }

  @Override
  public AddressComponent findAddressComponentByType(AddressComponentType... type) {
    return ResultTool.findAddressComponentByType(this, type);
  }
  
  @Override
  public Polygon getDecoratedPolygon() {
      return getDecoratedResult().accept(new ResultVisitor<Polygon>(){
        @Override
        public Polygon visit(Coordinate location) {
          return new AbstractSingleCoordinatePolygon(location){
            @Override
            public String getPolygonName() {
              return getDecoratedResult().getFormattedAddress();
            }
          };
        }

        @Override
        public Polygon visit(Polygon bounds) {
          return bounds;
        }
      });
  }

  @Override
  public String getPolygonName() {
    if (isSuccess()) {
      return results.get(0).getFormattedAddress();
    } else {
      return "Unsuccessful unique geocoding";
    }
  }



  public boolean isSuccess() {
    return success;
  }

  public void setSuccess(boolean success) {
    this.success = success;
  }

  public List<Result> getResults() {
    return results;
  }

  public void setResults(List<Result> results) {
    this.results = results;
  }

  public Object getServerResponse() {
    return serverResponse;
  }

  public void setServerResponse(Object serverResponse) {
    this.serverResponse = serverResponse;
  }

  @Override
  public String toString() {
    return "Geocoding{" +
        "success=" + success +
        ", results=" + results +
        ", serverResponse=" + serverResponse +
        '}';
  }

  // result decoration

  public Result getDecoratedResult() {
    if (!success) {
      throw new RuntimeException("Unsuccessful geocoding result");
    }
    return getResults().get(0);
  }

  @Override
  public <T> T accept(ResultVisitor<T> visitor) {
    return getDecoratedResult().accept(visitor);
  }

  @Override
  public Envelope getBounds() {
    return getDecoratedResult().getBounds();
  }

  @Override
  public void setBounds(Envelope bounds) {
    getDecoratedResult().setBounds(bounds);
  }

  @Override
  public Envelope getViewPort() {
    return getDecoratedResult().getViewPort();
  }

  @Override
  public void setViewPort(Envelope viewPort) {
    getDecoratedResult().setViewPort(viewPort);
  }

  @Override
  public Coordinate getLocation() {
    return getDecoratedResult().getLocation();
  }

  @Override
  public void setLocation(Coordinate location) {
    getDecoratedResult().setLocation(location);
  }

  @Override
  public MapSet<AddressComponentType, AddressComponent> getAddressComponentsByType() {
    return getDecoratedResult().getAddressComponentsByType();
  }

  @Override
  public void setAddressComponentsByType(MapSet<AddressComponentType, AddressComponent> addressComponentsByType) {
    getDecoratedResult().setAddressComponentsByType(addressComponentsByType);
  }

  @Override
  public Precision getPrecision() {
    return getDecoratedResult().getPrecision();
  }

  @Override
  public void setPrecision(Precision precision) {
    getDecoratedResult().setPrecision(precision);
  }

  @Override
  public double getScore() {
    return getDecoratedResult().getScore();
  }

  @Override
  public void setScore(double score) {
    getDecoratedResult().setScore(score);
  }

  @Override
  public String getFormattedAddress() {
    return getDecoratedResult().getFormattedAddress();
  }

  @Override
  public void setFormattedAddress(String formattedAddress) {
    getDecoratedResult().setFormattedAddress(formattedAddress);
  }

  @Override
  public List<AddressComponent> getAddressComponents() {
    return getDecoratedResult().getAddressComponents();
  }

  @Override
  public void setAddressComponents(List<AddressComponent> addressComponents) {
    getDecoratedResult().setAddressComponents(addressComponents);
  }
}


