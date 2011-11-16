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
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for address_componentType complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="address_componentType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="long_name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="short_name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="type" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "address_componentType", propOrder = {
    "longName",
    "shortName",
    "types"
})

public class AddressComponent implements Serializable {

  private static final long serialVersionUID = 1l;

  @XmlElement(name = "long_name", required = true)
  protected String longName;

  @XmlElement(name = "short_name", required = true)
  protected String shortName;

  @XmlElement(name = "type")
  protected List<String> types;

  /**
   * Gets the value of the longName property.
   *
   * @return possible object is
   *         {@link String }
   */

  public String getLongName() {
    return longName;
  }

  /**
   * Sets the value of the longName property.
   *
   * @param value allowed object is
   *              {@link String }
   */

  public void setLongName(String value) {
    this.longName = value;
  }

  /**
   * Gets the value of the shortName property.
   *
   * @return possible object is
   *         {@link String }
   */

  public String getShortName() {
    return shortName;
  }

  /**
   * Sets the value of the shortName property.
   *
   * @param value allowed object is
   *              {@link String }
   */

  public void setShortName(String value) {
    this.shortName = value;
  }

  /**
   * Gets the value of the type property.
   * <p/>
   * <p/>
   * This accessor method returns a reference to the live list,
   * not a snapshot. Therefore any modification you make to the
   * returned list will be present inside the JAXB object.
   * This is why there is not a <CODE>set</CODE> method for the type property.
   * <p/>
   * <p/>
   * For example, to add a new item, do as follows:
   * <pre>
   *    getType().add(newItem);
   * </pre>
   * <p/>
   * <p/>
   * <p/>
   * Objects of the following type(s) are allowed in the list
   * {@link String }
   */

  public List<String> getTypes() {
    if (types == null) {
      types = new ArrayList<String>();
    }
    return this.types;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    AddressComponent that = (AddressComponent) o;

    if (longName != null ? !longName.equals(that.longName) : that.longName != null) return false;
    if (shortName != null ? !shortName.equals(that.shortName) : that.shortName != null) return false;
    if (types != null ? !types.equals(that.types) : that.types != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = longName != null ? longName.hashCode() : 0;
    result = 31 * result + (shortName != null ? shortName.hashCode() : 0);
    result = 31 * result + (types != null ? types.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "AddressComponent{" +
        "longName='" + longName + '\'' +
        ", shortName='" + shortName + '\'' +
        ", type=" + types +
        '}';
  }
}
