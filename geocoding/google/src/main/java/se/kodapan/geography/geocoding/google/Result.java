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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for resultType complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="resultType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="type" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="formatted_address" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="address_component" type="{}address_componentType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="geometry" type="{}geometryType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "resultType", propOrder = {
    "partialMatch",
    "type",
    "formattedAddress",
    "addressComponents",
    "geometry"
})

public class Result implements Serializable {

  private static final long serialVersionUID = 1l;


  @XmlElement(name = "type", required = true)
  protected String type;

  @XmlElement(name = "partial_match")
  protected boolean partialMatch;


  @XmlElement(name = "formatted_address", required = true)
  protected String formattedAddress;

  @XmlElement(name = "address_component")
  protected List<AddressComponent> addressComponents;

  @XmlElement(name = "geometry", required = true)
  protected Geometry geometry;

  /**
   * Gets the value of the type property.
   *
   * @return possible object is
   *         {@link String }
   */

  public String getType() {
    return type;
  }

  /**
   * Sets the value of the type property.
   *
   * @param value allowed object is
   *              {@link String }
   */

  public void setType(String value) {
    this.type = value;
  }

  /**
   * Gets the value of the formattedAddress property.
   *
   * @return possible object is
   *         {@link String }
   */

  public String getFormattedAddress() {
    return formattedAddress;
  }

  /**
   * Sets the value of the formattedAddress property.
   *
   * @param value allowed object is
   *              {@link String }
   */

  public void setFormattedAddress(String value) {
    this.formattedAddress = value;
  }

  /**
   * Gets the value of the addressComponent property.
   * <p/>
   * <p/>
   * This accessor method returns a reference to the live list,
   * not a snapshot. Therefore any modification you make to the
   * returned list will be present inside the JAXB object.
   * This is why there is not a <CODE>set</CODE> method for the addressComponent property.
   * <p/>
   * <p/>
   * For example, to add a new item, do as follows:
   * <pre>
   *    getAddressComponent().add(newItem);
   * </pre>
   * <p/>
   * <p/>
   * <p/>
   * Objects of the following type(s) are allowed in the list
   * {@link AddressComponent }
   */

  public List<AddressComponent> getAddressComponents() {
    if (addressComponents == null) {
      addressComponents = new ArrayList<AddressComponent>();
    }
    return this.addressComponents;
  }

  /**
   * Gets the value of the geometry property.
   *
   * @return possible object is
   *         {@link Geometry }
   */

  public Geometry getGeometry() {
    return geometry;
  }

  /**
   * Sets the value of the geometry property.
   *
   * @param value allowed object is
   *              {@link Geometry }
   */

  public void setGeometry(Geometry value) {
    this.geometry = value;
  }

  public boolean isPartialMatch() {
    return partialMatch;
  }

  public void setPartialMatch(boolean partialMatch) {
    this.partialMatch = partialMatch;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Result that = (Result) o;

    if (addressComponents != null ? !addressComponents.equals(that.addressComponents) : that.addressComponents != null)
      return false;
    if (formattedAddress != null ? !formattedAddress.equals(that.formattedAddress) : that.formattedAddress != null)
      return false;
    if (geometry != null ? !geometry.equals(that.geometry) : that.geometry != null) return false;
    if (type != null ? !type.equals(that.type) : that.type != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = type != null ? type.hashCode() : 0;
    result = 31 * result + (formattedAddress != null ? formattedAddress.hashCode() : 0);
    result = 31 * result + (addressComponents != null ? addressComponents.hashCode() : 0);
    result = 31 * result + (geometry != null ? geometry.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "Result{" +
        "type='" + type + '\'' +
        ", formattedAddress='" + formattedAddress + '\'' +
        ", addressComponent=" + addressComponents +
        ", geometry=" + geometry +
        '}';
  }
}
