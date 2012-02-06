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
 * <p>Java class for GeocodeResponseType complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="GeocodeResponseType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="status" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="result" type="{}resultType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GeocodeResponse", propOrder = {
    "status",
    "results"
})

public class GeocodeResponse implements Serializable {

  private static final long serialVersionUID = 1l;  

  private transient String serverResponse;

  @XmlElement(name = "status", required = true)
  protected String status;

  @XmlElement(name = "result")
  protected List<Result> results;

  /**
   * Gets the value of the status property.
   *
   * @return possible object is
   *         {@link String }
   */

  public String getStatus() {
    return status;
  }

  /**
   * Sets the value of the status property.
   *
   * @param value allowed object is
   *              {@link String }
   */

  public void setStatus(String value) {
    this.status = value;
  }

  /**
   * Gets the value of the result property.
   * <p/>
   * <p/>
   * This accessor method returns a reference to the live list,
   * not a snapshot. Therefore any modification you make to the
   * returned list will be present inside the JAXB object.
   * This is why there is not a <CODE>set</CODE> method for the result property.
   * <p/>
   * <p/>
   * For example, to add a new item, do as follows:
   * <pre>
   *    getResult().add(newItem);
   * </pre>
   * <p/>
   * <p/>
   * <p/>
   * Objects of the following type(s) are allowed in the list
   * {@link Result }
   */

  public List<Result> getResults() {
    if (results == null) {
      results = new ArrayList<Result>();
    }
    return this.results;
  }

  public String getServerResponse() {
    return serverResponse;
  }

  public void setServerResponse(String serverResponse) {
    this.serverResponse = serverResponse;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    GeocodeResponse that = (GeocodeResponse) o;

    if (results != null ? !results.equals(that.results) : that.results != null) return false;
    if (status != null ? !status.equals(that.status) : that.status != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result1 = status != null ? status.hashCode() : 0;
    result1 = 31 * result1 + (results != null ? results.hashCode() : 0);
    return result1;
  }

  @Override
  public String toString() {
    return "GeocodeResponse{" +
        "status='" + status + '\'' +
        ", result=" + results +
        ", serverResponse=" + getServerResponse() +
        '}';
  }
}
