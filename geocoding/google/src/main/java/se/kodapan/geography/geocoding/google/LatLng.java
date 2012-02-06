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
package se.kodapan.geography.geocoding.google;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;


/**
 * <p>Java class for latLngType complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="latLngType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="lat" type="{http://www.w3.org/2001/XMLSchema}float"/>
 *         &lt;element name="lng" type="{http://www.w3.org/2001/XMLSchema}float"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "latLngType", propOrder = {
    "lat",
    "lng"
})

public class LatLng implements Serializable {

  private static final long serialVersionUID = 1l;



  protected float lat;

  protected float lng;


  public LatLng() {
  }

  public LatLng(float lat, float lng) {
    this.lat = lat;
    this.lng = lng;
  }

  public LatLng(Number lat, Number lng) {
    this.lat = lat.floatValue();
    this.lng = lng.floatValue();
  }

  /**
   * Gets the value of the lat property.
   */

  public float getLat() {
    return lat;
  }

  /**
   * Sets the value of the lat property.
   */

  public void setLat(float value) {
    this.lat = value;
  }

  /**
   * Gets the value of the lng property.
   */

  public float getLng() {
    return lng;
  }

  /**
   * Sets the value of the lng property.
   */

  public void setLng(float value) {
    this.lng = value;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    LatLng that = (LatLng) o;

    if (Float.compare(that.lat, lat) != 0) return false;
    if (Float.compare(that.lng, lng) != 0) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = (lat != +0.0f ? Float.floatToIntBits(lat) : 0);
    result = 31 * result + (lng != +0.0f ? Float.floatToIntBits(lng) : 0);
    return result;
  }

  @Override
  public String toString() {
    return "LatLng{" +
        "lat=" + lat +
        ", lng=" + lng +
        '}';
  }
}
