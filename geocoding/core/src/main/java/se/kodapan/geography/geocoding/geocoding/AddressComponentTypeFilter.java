package se.kodapan.geography.geocoding.geocoding;

import se.kodapan.geography.domain.*;
import se.kodapan.geography.geocoding.postprocessing.filter.ResponseFilter;


import java.util.Iterator;
import java.util.Set;

/**
 * Require a certain type or types of address components. Eg only routes and streets.
 *
 * @author kalle
 * @since 2010-aug-11 22:58:19
 */
public class AddressComponentTypeFilter extends ResponseFilter {

  private Set<String> allowedTypes;

  public AddressComponentTypeFilter(Geocoding geocoding) {
    super(geocoding);
  }

  public AddressComponentTypeFilter(Geocoding geocoding, Set<String> allowedTypes) {
    super(geocoding);
    this.allowedTypes = allowedTypes;
  }

  @Override
  public Geocoding filter() {
    for (Result result : geocoding.getResults()) {
      for (Iterator<AddressComponent> it = result.getAddressComponents().iterator(); it.hasNext();) {
        AddressComponent component = it.next();
        boolean allowed = false;
        for (String type : allowedTypes) {
          if (component.getTypes().contains(type)) {
            allowed = true;
            break;
          }
        }
        if (!allowed) {
          it.remove();
        }
      }
    }
    return geocoding;
  }

  public Set<String> getAllowedTypes() {
    return allowedTypes;
  }

  public void setAllowedTypes(Set<String> allowedTypes) {
    this.allowedTypes = allowedTypes;
  }
}
