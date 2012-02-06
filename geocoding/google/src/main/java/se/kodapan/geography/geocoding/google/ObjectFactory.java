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

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the com.google.maps.geocoding.g2 package.
 * <p>An ObjectFactory allows you to programatically
 * construct new instances of the Java representation
 * for XML content. The Java representation of XML
 * content can consist of schema derived interfaces
 * and classes representing the binding of schema
 * type definitions, element declarations and model
 * groups.  Factory methods for each of these are
 * provided in this class.
 */
@XmlRegistry
public class ObjectFactory {

  private final static QName _GeocodeResponse_QNAME = new QName("", "GeocodeResponse");

  /**
   * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.google.maps.geocoding.g2
   */
  public ObjectFactory() {
  }

  /**
   * Create an instance of {@link GeocodeResponse }
   */
  public GeocodeResponse createGeocodeResponseType() {
    return new GeocodeResponse();
  }

  /**
   * Create an instance of {@link Envelope }
   */
  public Envelope createEnvelopeType() {
    return new Envelope();
  }

  /**
   * Create an instance of {@link Geometry }
   */
  public Geometry createGeometryType() {
    return new Geometry();
  }

  /**
   * Create an instance of {@link AddressComponent }
   */
  public AddressComponent createAddressComponentType() {
    return new AddressComponent();
  }

  /**
   * Create an instance of {@link LatLng }
   */
  public LatLng createLatLngType() {
    return new LatLng();
  }

  /**
   * Create an instance of {@link Result }
   */
  public Result createResultType() {
    return new Result();
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}{@link GeocodeResponse }{@code >}}
   */
  @XmlElementDecl(namespace = "", name = "GeocodeResponse")
  public JAXBElement<GeocodeResponse> createGeocodeResponse(GeocodeResponse value) {
    return new JAXBElement<GeocodeResponse>(_GeocodeResponse_QNAME, GeocodeResponse.class, null, value);
  }

}
