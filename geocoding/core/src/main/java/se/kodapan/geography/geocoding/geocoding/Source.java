package se.kodapan.geography.geocoding.geocoding;

import java.io.Serializable;

/**
 * @author kalle
 * @since 2011-11-16 01:54
 */
public class Source implements Serializable {

  private static final long serialVersionUID = 1l;

  public Source(){
  }

  private String name;
  private String version;
  private String license;

  public Source(String name, String version, String license) {
    this.name = name;
    this.version = version;
    this.license = license;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public String getLicense() {
    return license;
  }

  public void setLicense(String license) {
    this.license = license;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Source source = (Source) o;

    if (license != null ? !license.equals(source.license) : source.license != null) return false;
    if (name != null ? !name.equals(source.name) : source.name != null) return false;
    if (version != null ? !version.equals(source.version) : source.version != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = name != null ? name.hashCode() : 0;
    result = 31 * result + (version != null ? version.hashCode() : 0);
    result = 31 * result + (license != null ? license.hashCode() : 0);
    return result;
  }
}
