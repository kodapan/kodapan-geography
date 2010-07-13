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

import se.kodapan.geography.core.Polygon;

import java.io.Serializable;
import java.util.Set;

/**
 * @author kalle
 * @since 2010-jun-23 21:10:45
 */
public class Request implements Serializable {

  private static final long serialVersionUID = 1l;
  
  private String textQuery;

  private Polygon bounds;
  private String language;
  

  public Request() {
  }

  public Request(String textQuery) {
    this.textQuery = textQuery;
  }

  public Request(String textQuery, Polygon bounds) {
    this.textQuery = textQuery;
    this.bounds = bounds;
  }

  public Request(String textQuery, String language) {
    this.textQuery = textQuery;
    this.language = language;
  }

  public Request(String textQuery, Polygon bounds, String language) {
    this.textQuery = textQuery;
    this.bounds = bounds;
    this.language = language;
  }

  public String getTextQuery() {
    return textQuery;
  }

  public void setTextQuery(String textQuery) {
    this.textQuery = textQuery;
  }

  public Polygon getBounds() {
    return bounds;
  }

  public void setBounds(Polygon bounds) {
    this.bounds = bounds;
  }

  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Request request = (Request) o;

    if (bounds != null ? !bounds.equals(request.bounds) : request.bounds != null) return false;
    if (language != null ? !language.equals(request.language) : request.language != null) return false;
    if (textQuery != null ? !textQuery.equals(request.textQuery) : request.textQuery != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = textQuery != null ? textQuery.hashCode() : 0;
    result = 31 * result + (bounds != null ? bounds.hashCode() : 0);
    result = 31 * result + (language != null ? language.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "Request{" +
        "textQuery='" + textQuery + '\'' +
        ", bounds=" + bounds +
        ", language='" + language + '\'' +
        '}';
  }
}
