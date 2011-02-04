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

import se.kodapan.geography.domain.AddressComponent;
import se.kodapan.geography.domain.AddressComponents;
import se.kodapan.geography.polygon.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * decorates the successful result.
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

  public double kmDiagonal() {
    Envelope envelope = new EnvelopeImpl();
    for (Result result : results) {
      envelope.addBounds(result.getLocation());
    }
    return envelope.getSouthwest().archDistance(envelope.getNortheast());

  }

  public void mergeResults(Geocoding geocoding) {
    for (Result result : geocoding.getResults()) {
      mergeResult(result);
    }
  }

  /**
   * results that equal to something already in the geocoding will not be replaced.
   *
   * @param result the instance to be added
   * @return the instance now bound to this geocoding
   */
  public Result mergeResult(Result result) {
    int index = results.indexOf(result);
    if (index > -1) {
      result = results.get(index);
    } else {
      results.add(result);
    }
    return result;
  }

  public AddressComponents gatherCommonDenominatorsFromResults() {
    HashSet<AddressComponent> allHas = new LinkedHashSet<AddressComponent>();
    HashSet<AddressComponent> allHasNot = new HashSet<AddressComponent>();
    for (Result result : getResults()) {
      for (AddressComponent component : result.getAddressComponents()) {
        if (allHas.contains(component) || allHasNot.contains(component)) {
          continue;
        }
        boolean allHasThis = true;
        for (Result result2 : getResults()) {
          if (result == result2) {
            continue;
          }
          if (!result2.getAddressComponents().contains(component)) {
            allHasThis = false;
            break;
          }
        }
        if (allHasThis) {
          allHas.add(component);
        }
      }
    }
    AddressComponents result = new AddressComponents();
    result.addAll(allHas);
    return result;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null) return false;

    if (getClass() == o.getClass()) {
      Geocoding geocoding = (Geocoding) o;
      if (!success && !geocoding.success) return true;
      if (success != geocoding.success) return false;

      return geocoding.getDecoratedResult().equals(getDecoratedResult());
    } else if (Result.class.isAssignableFrom(o.getClass())){
      if (isSuccess()) {
        return o.equals(this);
      }
    }

    return false;
  }

  @Override
  public int hashCode() {
    return (success ? 1 : 0);
  }

  public boolean contains(Geocoding geocoding) {
    return contains((Polygon) geocoding);
  }

  public double archDistance(Geocoding that) {
    return archDistance((Polygon) that);
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
  public Polygon getDecoratedPolygon() {
    return getDecoratedResult().accept(new ResultVisitor<Polygon>() {
      @Override
      public Polygon visit(Coordinate location) {
        return new AbstractSingleCoordinatePolygon(location) {
          @Override
          public String getPolygonName() {
            return getDecoratedResult().getAddressComponents().getFormattedAddress();
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
      return results.get(0).getAddressComponents().getFormattedAddress();
    } else {
      return "Unsuccessful unique geocoding";
    }
  }

  @Override
  public AddressComponents getAddressComponents() {
    return getDecoratedResult().getAddressComponents();
  }

  @Override
  public void setAddressComponents(AddressComponents addressComponents) {
    getDecoratedResult().setAddressComponents(addressComponents);
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

}


