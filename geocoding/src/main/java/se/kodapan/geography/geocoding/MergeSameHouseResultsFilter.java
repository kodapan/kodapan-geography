package se.kodapan.geography.geocoding;

import se.kodapan.geography.domain.*;


import java.util.*;

/**
 * todo this class should merge any top level attribute!
 * keeps the top scoring instance of the same house on the same street.
 * @author kalle
 * @since 2010-jul-22 11:53:01
 */
public class MergeSameHouseResultsFilter extends ResponseFilter {

  private Locale locale = new Locale("SV_se");
  private double maxKmDistance = 0.3d;

  public MergeSameHouseResultsFilter(Geocoding geocoding) {
    super(geocoding);
  }

  public MergeSameHouseResultsFilter(Geocoding geocoding, double maxKmDistance) {
    super(geocoding);
    this.maxKmDistance = maxKmDistance;
  }

  @Override
  public Geocoding filter() throws Exception {

    Geocoding geocoding =  this.geocoding;

    Set<Result> removed = new HashSet<Result>();
    for (Result result : new ArrayList<Result>(geocoding.getResults())) {

      AddressComponent streetName = result.getAddressComponents().get("route");
      if (streetName == null) {
        streetName = result.getAddressComponents().get("street_address");
      }
      // todo stugbyar är sublocality! kvistagården är en premise!
      if (streetName == null) {
        continue;
      }

      AddressComponent houseNumber = result.getAddressComponents().get("street_number");

      List<Result> results = new ArrayList<Result>();
      results.add(result);

      for (Result result2 : geocoding.getResults()) {
        if (result == result2) {
          break;
        }

        AddressComponent streetName2 = result2.getAddressComponents().get("street_address");
        if (streetName2 == null) {
          streetName2 = result2.getAddressComponents().get("route");
          if (streetName2 == null) {
            continue;
          }
        }

        double kmDistance = result.getLocation().arcDistance(result2.getLocation());
        if (kmDistance <= maxKmDistance) {

          AddressComponent houseNumber2 = result2.getAddressComponents().get("street_number");
          if (houseNumber == null && houseNumber2 == null
              || (houseNumber != null && houseNumber.equals(houseNumber2))) {
            results.add(result2);
          }
        }
      }

      if (results.size() > 1) {
        Result topScoringResult = new ResultImpl();
        topScoringResult.setScore(-1);
        for (int i1 = 0, resultsSize = results.size(); i1 < resultsSize; i1++) {
          Result result2 = results.get(i1);
          if (result2.getScore() > topScoringResult.getScore()) {
            topScoringResult = result2;
          }
        }
        int index = geocoding.getResults().indexOf(topScoringResult);
        geocoding.getResults().removeAll(results);

        Result newResult = new ResultImpl();
        newResult.setBounds(topScoringResult.getBounds());
        newResult.setLocation(topScoringResult.getLocation());
        newResult.setViewPort(topScoringResult.getViewPort());
        newResult.setScore(topScoringResult.getScore());
        
        // order so we hopefully add them in order.
        Collections.sort(results, new Comparator<Result>(){
          @Override
          public int compare(Result result, Result result1) {
            return result1.getAddressComponents().size() - result.getAddressComponents().size();
          }
        });
        for (Result result2 : results) {
          newResult.getAddressComponents().addAll(result2.getAddressComponents());
        }
        // sometimes there are several postings for the same street name with small diffs. this selects the one with the longest name.
        List<AddressComponent> routes = newResult.getAddressComponents().list("route");
        Collections.sort(routes, new Comparator<AddressComponent>(){
          @Override
          public int compare(AddressComponent addressComponent, AddressComponent addressComponent1) {
            return addressComponent1.getLongName().length() - addressComponent.getLongName().length();
          }
        });
        for (int i=1; i<routes.size(); i++) {
          newResult.getAddressComponents().remove(routes.get(i));
        }

        newResult.getAddressComponents().setFormattedAddress(locale.getLanguage(), locale.getCountry());
        newResult.setPrecision(Precision.APPROXIMATE);

        if (index > geocoding.getResults().size() - 1) {
          geocoding.getResults().add(newResult);
        } else {
          geocoding.getResults().add(index, newResult);
        }
        removed.addAll(results);

      }
    }
    Collections.sort(geocoding.getResults(), Result.scoreComparator);
    return geocoding;
  }
}
