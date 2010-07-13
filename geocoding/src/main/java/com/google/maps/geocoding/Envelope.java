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


/**
 * <p>Java class for envelopeType complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="envelopeType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="southwest" type="{}latLngType"/>
 *         &lt;element name="northeast" type="{}latLngType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "envelopeType", propOrder = {
    "southwest",
    "northeast"
})

public class Envelope {

  @XmlElement(required = true)

  protected LatLng southwest;
  @XmlElement(required = true)

  protected LatLng northeast;


  public Envelope() {
  }

  public Envelope(LatLng southwest, LatLng northeast) {
    this.southwest = southwest;
    this.northeast = northeast;
  }

  /**
   * Gets the value of the southwest property.
   *
   * @return possible object is
   *         {@link LatLng }
   */

  public LatLng getSouthwest() {
    return southwest;
  }

  /**
   * Sets the value of the southwest property.
   *
   * @param value allowed object is
   *              {@link LatLng }
   */

  public void setSouthwest(LatLng value) {
    this.southwest = value;
  }

  /**
   * Gets the value of the northeast property.
   *
   * @return possible object is
   *         {@link LatLng }
   */

  public LatLng getNortheast() {
    return northeast;
  }

  /**
   * Sets the value of the northeast property.
   *
   * @param value allowed object is
   *              {@link LatLng }
   */

  public void setNortheast(LatLng value) {
    this.northeast = value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Envelope that = (Envelope) o;

    if (northeast != null ? !northeast.equals(that.northeast) : that.northeast != null) return false;
    if (southwest != null ? !southwest.equals(that.southwest) : that.southwest != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = southwest != null ? southwest.hashCode() : 0;
    result = 31 * result + (northeast != null ? northeast.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "Envelope{" +
        "southwest=" + southwest +
        ", northeast=" + northeast +
        '}';
  }
}
