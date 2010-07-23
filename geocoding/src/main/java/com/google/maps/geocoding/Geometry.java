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
package com.google.maps.geocoding;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;


/**
 * <p>Java class for geometryType complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="geometryType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="location" type="{}latLngType"/>
 *         &lt;element name="location_type" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="viewport" type="{}envelopeType"/>
 *         &lt;element name="bounds" type="{}envelopeType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "geometryType", propOrder = {
    "location",
    "locationType",
    "viewport",
    "bounds"
})

public class Geometry implements Serializable {

  private static final long serialVersionUID = 1l;


  @XmlElement(required = true)

  protected LatLng location;
  @XmlElement(name = "location_type", required = true)

  protected String locationType;
  @XmlElement(required = true)

  protected Envelope viewport;

  protected Envelope bounds;

  /**
   * Gets the value of the location property.
   *
   * @return possible object is
   *         {@link LatLng }
   */

  public LatLng getLocation() {
    return location;
  }

  /**
   * Sets the value of the location property.
   *
   * @param value allowed object is
   *              {@link LatLng }
   */

  public void setLocation(LatLng value) {
    this.location = value;
  }

  /**
   * Gets the value of the locationType property.
   *
   * @return possible object is
   *         {@link String }
   */

  public String getLocationType() {
    return locationType;
  }

  /**
   * Sets the value of the locationType property.
   *
   * @param value allowed object is
   *              {@link String }
   */

  public void setLocationType(String value) {
    this.locationType = value;
  }

  /**
   * Gets the value of the viewport property.
   *
   * @return possible object is
   *         {@link Envelope }
   */

  public Envelope getViewport() {
    return viewport;
  }

  /**
   * Sets the value of the viewport property.
   *
   * @param value allowed object is
   *              {@link Envelope }
   */

  public void setViewport(Envelope value) {
    this.viewport = value;
  }

  /**
   * Gets the value of the bounds property.
   *
   * @return possible object is
   *         {@link Envelope }
   */

  public Envelope getBounds() {
    return bounds;
  }

  /**
   * Sets the value of the bounds property.
   *
   * @param value allowed object is
   *              {@link Envelope }
   */

  public void setBounds(Envelope value) {
    this.bounds = value;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Geometry that = (Geometry) o;

    if (bounds != null ? !bounds.equals(that.bounds) : that.bounds != null) return false;
    if (location != null ? !location.equals(that.location) : that.location != null) return false;
    if (locationType != null ? !locationType.equals(that.locationType) : that.locationType != null) return false;
    if (viewport != null ? !viewport.equals(that.viewport) : that.viewport != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = location != null ? location.hashCode() : 0;
    result = 31 * result + (locationType != null ? locationType.hashCode() : 0);
    result = 31 * result + (viewport != null ? viewport.hashCode() : 0);
    result = 31 * result + (bounds != null ? bounds.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "Geometry{" +
        "location=" + location +
        ", locationType='" + locationType + '\'' +
        ", viewport=" + viewport +
        ", bounds=" + bounds +
        '}';
  }
}
