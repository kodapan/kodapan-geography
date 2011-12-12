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

package geocoding;

import junit.framework.TestCase;
import org.junit.Test;
import se.kodapan.geography.domain.CoordinateImpl;
import se.kodapan.geography.domain.PolygonTools;
import se.kodapan.geography.geocoding.*;
import se.kodapan.geography.geocoding.postprocessing.filter.MergeSameHouseResultsFilter;
import se.kodapan.geography.geocoding.geocoding.AddressComponentsScorer;
import se.kodapan.geography.geocoding.geocoding.Geocoding;
import se.kodapan.geography.geocoding.postprocessing.scorer.ProximityScorer;
import se.kodapan.geography.geocoding.postprocessing.scorer.ThreadsholdScorer;

/**
 * @author kalle
 * @since 2010-jun-23 02:01:03
 */
public abstract class GeocoderTest extends TestCase {


  private Geocoder geocoder;

  protected abstract Geocoder getGeocoder();

  @Override
  protected void setUp() throws Exception {
    geocoder = getGeocoder();
  }

  @Test
  public void testReverseGeocoding() throws Exception {
    Geocoding geocoding = geocoder.reverse(new CoordinateQuery().setCoordinate(new CoordinateImpl(59.3350469d, 18.0569641d)));
    assertTrue(geocoding.isSuccess());
  }

  @Test
  public void test() throws Exception {

    Geocoding blekinge = geocoder.geocode(new TextQuery().setText("Blekinge län, Sverige"));

    // this is a common typo
    Geocoding blekinges = geocoder.geocode(new TextQuery().setText("Blekinges län, Sverige"));
    if (!blekinge.getResults().isEmpty()) {
      System.currentTimeMillis(); // todo make sure its a good response then.
    }

    double km;

    Geocoding halmstad = geocoder.geocode(new TextQuery().setText("Halmstad, Hallands län, Sverige"));
    Geocoding halland = geocoder.geocode(new TextQuery().setText("Hallands län, Sverige"));
    Geocoding sverige = geocoder.geocode(new TextQuery().setText("Sverige"));

    makeFirstResultSucces(halmstad);
    makeFirstResultSucces(halland);
    makeFirstResultSucces(sverige);

    assertTrue(sverige.contains(halland.getLocation()));
    assertTrue(halland.contains(halmstad.getLocation()));

    assertFalse(halmstad.contains(halland));
    assertFalse(halland.contains(sverige));

    assertTrue(halmstad.contains(halmstad));
    assertTrue(halland.contains(halland));
    assertTrue(sverige.contains(sverige));


    assertEquals(0d, halmstad.arcDistance(halland));
    assertEquals(0d, halmstad.arcDistance(sverige));

    assertEquals(0d, halland.arcDistance(halmstad));
    assertEquals(0d, halland.arcDistance(sverige));

    assertEquals(0d, sverige.arcDistance(halmstad));
    assertEquals(0d, sverige.arcDistance(halland));

    // very close to each other
    Geocoding laholm = geocoder.geocode(new TextQuery().setText("Laholm, Hallands län, Sverige"));
    makeFirstResultSucces(laholm);
    assertLocationDistance(halmstad, laholm, 15, 40);

    Geocoding stockholm = assertArcDistance(halmstad, "Stockholm, Stockholms län, Sverige", 350, 450);
    assertFalse(halmstad.contains(stockholm));
    assertFalse(stockholm.contains(halmstad));
    assertFalse(stockholm.contains(laholm));
    assertFalse(laholm.contains(stockholm));

    assertLocationDistance(halmstad, stockholm, 415, 430);


    if (false) {


      // test bounds

      Geocoding opgs = geocoder.geocode(new TextQuery().setText("Olof palmes gata 23").setBounds(stockholm));
      makeFirstResultSucces(opgs);
      assertFalse(opgs.isSuccess());
      opgs = new MergeSameHouseResultsFilter(opgs).filter();
      new ProximityScorer(stockholm).score(opgs);
      new ThreadsholdScorer().score(opgs);
      assertTrue(opgs.isSuccess());
      assertTrue(stockholm.contains(opgs));
    }

    Geocoding umeå = geocoder.geocode(new TextQuery().setText("Umeå, Sverige"));
    makeFirstResultSucces(umeå);
    assertTrue(umeå.isSuccess());

    Geocoding opgu = geocoder.geocode(new TextQuery().setText("Olof palmes gatan 23, umeå"));
    makeFirstResultSucces(opgu);
    assertTrue(opgu.isSuccess());
    assertTrue(umeå.contains(opgu));


    if (false) {

      // test proximity scorer

      Geocoding opg = geocoder.geocode(new TextQuery().setText("Olof palmes gatan 23"));
      makeFirstResultSucces(opg);
      new ProximityScorer(stockholm, umeå).score(opg);
      assertFalse(opg.isSuccess());


      opg = geocoder.geocode(new TextQuery().setText("Olof palmes gata, sverige"));
      opg = new MergeSameHouseResultsFilter(opg).filter();
      new ProximityScorer(stockholm).score(opg);
      new ThreadsholdScorer().score(opg);
      assertTrue(opg.isSuccess());
      assertTrue(stockholm.contains(opg));

      // this will fail
      // its an address, but it has floor number, says there is an elevator and that it is sold.
      assertFalse(geocoder.geocode(new TextQuery().setText("Svalgränd 4, 5 tr, hiss sålt")).isSuccess());
      System.currentTimeMillis();

    }

    assertEquals(halmstad, PolygonTools.findSmallestEnclosingBounds(halland, halmstad));
    assertEquals(halmstad, PolygonTools.findSmallestEnclosingBounds(halland, halmstad, sverige));
    if (false) {
      // todo osm has problems with this
      assertEquals(halland, PolygonTools.findSmallestEnclosingBounds(halland, halmstad, sverige, laholm));
    }
    assertEquals(laholm, PolygonTools.findSmallestEnclosingBounds(laholm, sverige, halland));
    assertEquals(sverige, PolygonTools.findSmallestEnclosingBounds(halland, stockholm, umeå, halmstad, sverige));
    System.currentTimeMillis();


    if (false) {


//    Geocoding gardet = geocoder.geocode("Gärdet, Stockholm, Sverige");
//    assertEquals(gardet, PolygonTools.findSmallestEnclosingBounds(gardet, stockholm));

      Geocoding osteraker = geocoder.geocode(new TextQuery().setText("Österåker, Stockholms län, Sverige"));
      assertEquals(null, PolygonTools.findSmallestEnclosingBounds(osteraker, stockholm));


      // test request filters
      Geocoding mstou = geocoder.geocode(new TextQuery().setText("141 Main street, Toledo, Ohio, USA"));
      assertTrue(mstou.isSuccess());

      TextQuery request = new TextQuery();
      request.setText("141 Main street");
      Geocoding geocoding = geocoder.geocode(request);
      assertTrue(geocoding.isSuccess());
      assertEquals(mstou, geocoding);


      // todo this is buggy!!
      Geocoding toledo = geocoder.geocode(new TextQuery().setText("Toledo, Ohio, USA"));
      new AddressComponentsScorer(toledo.getResults().get(0).getAddressComponents()).score(geocoding);

      assertTrue(geocoding.isSuccess());
      assertEquals(mstou, geocoding);

      new ProximityScorer(geocoding).score(geocoding);
      assertTrue(geocoding.isSuccess());
      assertEquals(mstou, geocoding);

    }

    System.currentTimeMillis();
  }

  private void makeFirstResultSucces(Geocoding opgs) {
    if (!opgs.getResults().isEmpty()) {
      opgs.setSuccess(true);
    } else {
      fail();
    }
  }

  private void assertCentroidDistance(Geocoding from, Geocoding to, double minimumKilometers, double maximumKilometers) {
    double km;
    km = to.getCentroid().arcDistance(from.getCentroid());
    assertMinMax(minimumKilometers, maximumKilometers, km);
  }

  private void assertLocationDistance(Geocoding from, Geocoding to, double minimumKilometers, double maximumKilometers) {
    double km;
    km = to.getLocation().arcDistance(from.getLocation());
    assertMinMax(minimumKilometers, maximumKilometers, km);
  }

  private void assertMinMax(double minimumKilometers, double maximumKilometers, double km) {
    assertTrue("Expected to be between " + minimumKilometers + " and " + maximumKilometers + " but was " + km, km >= minimumKilometers);
    assertTrue("Expected to be between " + minimumKilometers + " and " + maximumKilometers + " but was " + km, km <= maximumKilometers);
  }

  private Geocoding assertArcDistance(Geocoding from, String textQuery, double minimumKilometers, double maximumKilometers) throws Exception {
    double km;
    Geocoding to = geocoder.geocode(new TextQuery().setText(textQuery));
    makeFirstResultSucces(to);

    km = to.arcDistance(from);
    assertEquals(km, from.arcDistance(to));

    assertMinMax(minimumKilometers, maximumKilometers, km);

    return to;
  }

}
