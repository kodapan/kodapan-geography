package se.kodapan.geography.geocoding;


import java.util.Collections;

/**
 * If missing in text query this filter adds
 * country
 * administrative area 1 (USA: state, Sweden: län)
 * and locality (city) or postal town
 * in that order or breaks if it yield a successful response.
 *
 * @author kalle
 * @since 2010-jul-21 18:02:51
 */
public class AddressComponentsScorer extends ResultsScorer {

  private AddressComponents components;

  public AddressComponentsScorer() {
    this.components = new AddressComponents();
  }

  public AddressComponentsScorer(AddressComponents components) {
    this.components = components;
  }

  @Override
  public void score(Geocoding geocoding) throws Exception {

    if (geocoding.getResults().size() > 1) {

      for (Result result : geocoding.getResults()) {
        result.setScore(score(result));
      }

      Collections.sort(geocoding.getResults(), Result.scoreComparator);
      if (geocoding.getResults().get(0).getScore() > geocoding.getResults().get(1).getScore()) {
        geocoding.setSuccess(true);
      }

    }
  }

  private double score(Result result) {
    int score = 0;
    for (AddressComponent component : components) {
      if (result.getAddressComponents().contains(component)) {
        score++;
      }
    }
    return score;
  }

  public final AddressComponents getComponents() {
    return components;
  }

  public final void setComponents(AddressComponents components) {
    this.components = components;
  }
}