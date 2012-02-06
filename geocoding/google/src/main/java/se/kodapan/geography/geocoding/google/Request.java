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

/**
 * The Geocoding API defines a geocoding request using the following URL parameters:
 * address (required) — The address that you want to geocode.*
 * OR
 * latlng (required) — The textual latitude/longitude value for which you wish to obtain the closest, human-readable address.*
 * <p/>
 * Note: You may pass either an address or a latlng to lookup. (If you pass a latlng, the geocoder performs what is known as a reverse geocode. See Reverse Geocoding for more information.)
 * The bounds and region parameters will only influence, not fully restrict, results from the geocoder.
 * <p/>
 * bounds (optional) — The bounding box of the viewport within which to bias geocode results more prominently. (For more information see Viewport Biasing below.)
 * region (optional) — The region code, specified as a ccTLD ("top-level domain") two-character value. (For more information see Region Biasing below.)
 * language (optional) — The language in which to return results. See the supported list of domain languages. Note that we often update supported languages so this list may not be exhaustive. If language is not supplied, the geocoder will attempt to use the native language of the domain from which the request is sent wherever possible.
 * sensor (required) — Indicates whether or not the geocoding request comes from a device with a location sensor. This value must be either true or false.
 *
 * @author kalle
 * @since 2010-jun-23 20:46:10
 */
public class Request {

  private String address;
  private LatLng latLng;

  private String language = null;
  private String region = null;
  private Envelope bounds = null;
  private boolean sensor = false;

  public Request() {
  }

  public Request(LatLng latLng) {
    this.latLng = latLng;
  }

  public Request(String address) {
    this.address = address;
  }

  public Request(String address, Envelope bounds) {
    this.address = address;
    this.bounds = bounds;
  }

  public Request(String address, Envelope bounds, String language, String region, boolean sensor) {
    this.address = address;
    this.bounds = bounds;
    this.language = language;
    this.region = region;
    this.sensor = sensor;
  }

  @Override
  public String toString() {
    return "Request{" +
        "address='" + address + '\'' +
        ", bounds=" + bounds +
        ", region='" + region + '\'' +
        ", language='" + language + '\'' +
        ", sensor=" + sensor +
        '}';
  }

  public LatLng getLatLng() {
    return latLng;
  }

  public void setLatLng(LatLng latLng) {
    this.latLng = latLng;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public String getRegion() {
    return region;
  }

  public void setRegion(String region) {
    this.region = region;
  }

  public Envelope getBounds() {
    return bounds;
  }

  public void setBounds(Envelope bounds) {
    this.bounds = bounds;
  }

  public boolean isSensor() {
    return sensor;
  }

  public void setSensor(boolean sensor) {
    this.sensor = sensor;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Request request = (Request) o;

    if (sensor != request.sensor) return false;
    if (address != null ? !address.equals(request.address) : request.address != null) return false;
    if (bounds != null ? !bounds.equals(request.bounds) : request.bounds != null) return false;
    if (language != null ? !language.equals(request.language) : request.language != null) return false;
    if (region != null ? !region.equals(request.region) : request.region != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = address != null ? address.hashCode() : 0;
    result = 31 * result + (language != null ? language.hashCode() : 0);
    result = 31 * result + (region != null ? region.hashCode() : 0);
    result = 31 * result + (bounds != null ? bounds.hashCode() : 0);
    result = 31 * result + (sensor ? 1 : 0);
    return result;
  }


}
