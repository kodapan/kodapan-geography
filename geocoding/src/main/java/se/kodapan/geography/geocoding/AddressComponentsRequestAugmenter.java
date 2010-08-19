package se.kodapan.geography.geocoding;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.kodapan.geography.domain.AddressComponent;
import se.kodapan.geography.domain.AddressComponentType;
import se.kodapan.geography.domain.AddressComponents;

import java.util.*;

/**
 * If missing in text query this filter adds registered components as comma separated suffix.
 * eg:
 * <p/>
 * storgatan ->
 * <p/>
 * storgatan, sweden
 * storgatan, jönköpings län, sweden
 * storgatan, tranås, jönsköpings län, sweden
 * <p/>
 * Increases score the more narrow the query gets.
 *
 * @author kalle
 * @since 2010-jul-21 18:02:51
 */
public class AddressComponentsRequestAugmenter implements RequestAugmenter {

  private static final Logger log = LoggerFactory.getLogger(AddressComponentsRequestAugmenter.class);

  private AddressComponents components = new AddressComponents();

  public AddressComponentsRequestAugmenter() {
  }

  public AddressComponentsRequestAugmenter(AddressComponents components) {
    this.components.addAll(components);
  }

  @Override
  public Geocoding filter(Geocoder geocoder, Request request) throws Exception {


    LinkedList<AddressComponent> components = new LinkedList<AddressComponent>();

    AddressComponent country = this.components.get(AddressComponentType.country);
    if (country != null) {
      components.addLast(country);
    }

    AddressComponent administrativeArea1 = this.components.get(AddressComponentType.political, AddressComponentType.administrative_area_level_1);
    if (administrativeArea1 != null) {
      components.addLast(administrativeArea1);
    }

    for (AddressComponent district : this.components.list(AddressComponentType.political, AddressComponentType.locality)) {
      components.addLast(district);
    }

    AddressComponent postalTown = this.components.get(AddressComponentType.political, AddressComponentType.postal_town);
    if (postalTown != null) {
      components.addLast(postalTown);
    } else {
      AddressComponent city = this.components.get(AddressComponentType.political, AddressComponentType.locality);
      if (city != null) {
        components.addLast(city);
      }
    }

    // sweden
    // jönköpings län, sweden
    // tranås, jönsköpings län, sweden
    Geocoding merged = new Geocoding();
    Map<Result, Integer> levelAdded = new HashMap<Result, Integer>();

    LinkedList<AddressComponent> componentsUsed = new LinkedList<AddressComponent>();
    for (int i = 0, max = components.size(); i <= max; i++) {
      componentsUsed.clear();
      for (int i2 = 0; i2 < i; i2++) {
        componentsUsed.addFirst(components.get(i2));
      }
      Geocoding geocoding = filter(geocoder, request, componentsUsed);
      if (geocoding.getResults().size() == 0) {
        // if we dont find anything for the text query we should not look any deeper!
        break;
      }

      for (Result result : geocoding.getResults()) {
        result = merged.mergeResult(result);
        levelAdded.put(result, i);
      }

      if (geocoding.isSuccess()) {
        break;
      }
    }
    for (Result result : merged.getResults()) {
      double factor = 1d + levelAdded.get(result);
      factor *= factor;
      result.setScore(result.getScore() * factor);
    }
    Collections.sort(merged.getResults(), Result.scoreComparator);
    merged = new MergeSameHouseResultsFilter(merged).filter();
    new ThreadsholdScorer().score(merged);
    return merged;

  }

  private Geocoding filter(Geocoder geocoder, Request request, List<AddressComponent> components) throws Exception {
    StringBuilder sb = new StringBuilder();
    sb.append(request.getTextQuery());
    String text = request.getTextQuery().toLowerCase();
    for (AddressComponent component : components) {
      if (!(text.matches("(^|.*\\W)" + component.getLongName().toLowerCase() + "($|\\W.*)")
          || (component.hasShortName() && text.matches("(^|.*\\W)" + component.getShortName().toLowerCase() + "($|\\W.*)")))) {
        sb.append(", ").append(component.getLongName());
      } else {
        System.currentTimeMillis();
      }
    }
    Request tmp = new Request();
    tmp.setBounds(request.getBounds());
    tmp.setLanguage(request.getLanguage());
    tmp.setTextQuery(sb.toString());
    Geocoding geocoding = geocoder.geocode(tmp);

    // ensure the results contain the request text
    // this avoids picking up items that are just the bias area
    String[] parts = request.getTextQuery().toLowerCase().split("\\s+");
    for (Iterator<Result> it = geocoding.getResults().iterator(); it.hasNext();) {
      Result result = it.next();
      boolean found = false;
      for (String part : parts) {
        for (AddressComponent component : result.getAddressComponents()) {
          if (component.getLongName().toLowerCase().contains(part)
              || component.getShortName().toLowerCase().contains(part)) {
            found = true;
          }
        }
      }
      if (!found) {
        it.remove();
        if (log.isDebugEnabled()) {
          log.debug("Removing " + result + " as no component match any token of " + request.getTextQuery());
        }
      }
    }
    if (geocoding.getResults().size() == 0) {
      geocoding.setSuccess(false);
    }
    return geocoding;
  }

  public final AddressComponents getComponents() {
    return components;
  }

  public final void setComponents(AddressComponents components) {
    this.components = components;
  }
}
