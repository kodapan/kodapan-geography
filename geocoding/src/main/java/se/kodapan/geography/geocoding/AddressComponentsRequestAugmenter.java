package se.kodapan.geography.geocoding;


import se.kodapan.io.SerializableTool;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * If missing in text query this filter adds
 * country
 * administrative area 1 (USA: state, Sweden: län)
 * and locality (city) or postal town
 * in that order or breaks if it yield a successful response.
 *
 *
 *
 * @author kalle
 * @since 2010-jul-21 18:02:51
 */
public class AddressComponentsRequestAugmenter implements RequestAugmenter {

  private AddressComponents components;

  public AddressComponentsRequestAugmenter() {
    this.components = new AddressComponents();
  }

  public AddressComponentsRequestAugmenter(AddressComponents components) {
    this.components = components;
  }

  @Override
  public Geocoding filter(Geocoder geocoder, Request request, Geocoding input) throws Exception {

    if (input.isSuccess() || input.getResults().size() < 2) {
      return input;
    }

    // a geocoding containing all the results found
    Geocoding response = SerializableTool.clone(input);


    Geocoding geocoding = input;

    LinkedList<AddressComponent> components = new LinkedList<AddressComponent>();

    AddressComponent country = this.components.get(AddressComponentType.country);
    if (country != null) {
      components.addFirst(country);

      geocoding = filter(geocoder, request, components);
      response.mergeResults(geocoding);
      for (Result result : geocoding.getResults()) {
        result.setScore(result.getScore() + 1d);
      }

    }

    if (!geocoding.isSuccess() && geocoding.getResults().size() > 1) {

      AddressComponent administrativeArea1 = this.components.get(AddressComponentType.political, AddressComponentType.administrative_area_level_1);
      if (administrativeArea1 != null) {
        components.addFirst(administrativeArea1);

        geocoding = filter(geocoder, request, components);
        response.mergeResults(geocoding);
        for (Result result : geocoding.getResults()) {
          result.setScore(result.getScore() + 1d);
        }

      }

      if (!geocoding.isSuccess() && geocoding.getResults().size() > 1) {

        AddressComponent locality = this.components.get(AddressComponentType.political, AddressComponentType.locality);
        if (locality != null) {
          components.addFirst(locality);
          geocoding = filter(geocoder, request, components);
          response.mergeResults(geocoding);
          for (Result result : geocoding.getResults()) {
            result.setScore(result.getScore() + 1d);
          }

        } else {
          AddressComponent postalTown = this.components.get(AddressComponentType.political, AddressComponentType.postal_town);
          if (postalTown != null) {
            components.addFirst(postalTown);
            geocoding = filter(geocoder, request, components);
            response.mergeResults(geocoding);
            for (Result result : geocoding.getResults()) {
              result.setScore(result.getScore() + 1d);
            }

          }
        }
      }
    }

    if (geocoding.isSuccess()) {

      int index = response.getResults().indexOf(geocoding.getDecoratedResult());
      Result successfulResult;
      if (index == -1) {
        throw new RuntimeException();
      } else {
        successfulResult = response.getResults().remove(index);
      }

      // add to top in results
      response.getResults().add(0, successfulResult);
      response.setSuccess(true);

    } else {

      Collections.sort(response.getResults(), Result.scoreComparator);
      new ScoreThreadsholdFilter().score(response);

    }

    return response;

  }

  private Geocoding filter(Geocoder geocoder, Request request, List<AddressComponent> components) throws Exception {
    StringBuilder sb = new StringBuilder();
    sb.append(request.getTextQuery());
    String text = request.getTextQuery().toLowerCase();
    for (AddressComponent component : components) {
      if (!(text.matches("(^|.*\\W)" + component.getLongName() + "($|\\W.*)")
          || (component.hasShortName() && text.matches("(^|.*\\W)" + component.getShortName() + "($|\\W.*)")))) {
        sb.append(", ").append(component.getLongName());
      } else {
        System.currentTimeMillis();
      }
    }
    Request tmp = new Request();
    tmp.setBounds(request.getBounds());
    tmp.setLanguage(request.getLanguage());
    tmp.setTextQuery(sb.toString());
    return geocoder.geocode(tmp);
  }

  public final AddressComponents getComponents() {
    return components;
  }

  public final void setComponents(AddressComponents components) {
    this.components = components;
  }
}
