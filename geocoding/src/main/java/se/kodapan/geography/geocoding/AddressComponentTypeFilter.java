package se.kodapan.geography.geocoding;

import se.kodapan.geography.domain.*;

import java.util.Iterator;
import java.util.Set;

/**
 * Require a certain type or types of address components. Eg only routes and streets.
 *
 * @author kalle
 * @since 2010-aug-11 22:58:19
 */
public class AddressComponentTypeFilter extends ResponseFilter {

  private Set<AddressComponentType> allowedTypes;

  public AddressComponentTypeFilter(Geocoding geocoding) {
    super(geocoding);
  }

  public AddressComponentTypeFilter(Geocoding geocoding, Set<AddressComponentType> allowedTypes) {
    super(geocoding);
    this.allowedTypes = allowedTypes;
  }

  @Override
  public Geocoding filter() throws Exception {
    for (Result result : geocoding.getResults()) {
      for (Iterator<AddressComponent> it = result.getAddressComponents().iterator(); it.hasNext();) {
        AddressComponent component = it.next();
        boolean allowed = false;
        for (AddressComponentType type : allowedTypes) {
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

  public Set<AddressComponentType> getAllowedTypes() {
    return allowedTypes;
  }

  public void setAllowedTypes(Set<AddressComponentType> allowedTypes) {
    this.allowedTypes = allowedTypes;
  }
}
