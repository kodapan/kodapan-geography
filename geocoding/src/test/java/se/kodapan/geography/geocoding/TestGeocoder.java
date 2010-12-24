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

import junit.framework.TestCase;
import org.junit.Test;
import se.kodapan.geography.polygon.CoordinateImpl;
import se.kodapan.geography.polygon.PolygonTools;
import se.kodapan.geography.domain.AddressComponent;
import se.kodapan.geography.domain.AddressComponentType;

import java.io.File;

/**
 * @author kalle
 * @since 2010-jun-23 02:01:03
 */
public class TestGeocoder extends TestCase {

  private Geocoder geocoder;

  @Override
  protected void setUp() throws Exception {
    com.google.maps.geocoding.GoogleGeocoder googleGeocoder = new com.google.maps.geocoding.GoogleGeocoder();
    googleGeocoder.setCachePath(new File("target/cache/google/geocoder"));
    googleGeocoder.open();
    geocoder = new GoogleGeocoder(googleGeocoder);
  }

  @Test
  public void testReverseGeocoding() throws Exception {
    Geocoding geocoding = geocoder.geocode(new CoordinateImpl(59.3350469d, 18.0569641d));
    assertTrue(geocoding.isSuccess());
  }

  @Test
  public void test() throws Exception {


    // todo its in tranås, but is not found!
    Request request = new Request();
    AddressComponentsRequestAugmenter augmenter = new AddressComponentsRequestAugmenter();
    augmenter.getComponents().add(new AddressComponent("Jönköpings Län", AddressComponentType.administrative_area_level_1, AddressComponentType.political));
    augmenter.getComponents().add(new AddressComponent("Sverige", AddressComponentType.country));
    request.setAugmenter(augmenter);
    request.setTextQuery("Prästgatan 10");
    Geocoding tmp = geocoder.geocode(request);

    // todo the first does not match.
    // todo so keep track of all data ever received and search in the local data!
    Geocoding blekinges = geocoder.geocode("Blekinges län, Sverige");
    Geocoding blekinge = geocoder.geocode("Blekinge län, Sverige");

    double km;

    Geocoding halmstad = geocoder.geocode("Halmstad, Hallands län, Sverige");
    Geocoding halland = geocoder.geocode("Hallands län, Sverige");
    Geocoding sverige = geocoder.geocode("Sverige");

    assertTrue(halmstad.isSuccess());
    assertTrue(halland.isSuccess());
    assertTrue(sverige.isSuccess());

    assertTrue(sverige.contains(halland));
    assertTrue(halland.contains(halmstad));

    assertFalse(halmstad.contains(halland));
    assertFalse(halland.contains(sverige));

    assertTrue(halmstad.contains(halmstad));
    assertTrue(halland.contains(halland));
    assertTrue(sverige.contains(sverige));


    assertEquals(0d, halmstad.archDistance(halland));
    assertEquals(0d, halmstad.archDistance(sverige));

    assertEquals(0d, halland.archDistance(halmstad));
    assertEquals(0d, halland.archDistance(sverige));

    assertEquals(0d, sverige.archDistance(halmstad));
    assertEquals(0d, sverige.archDistance(halland));


    Geocoding laholm = assertArchDistance(halmstad, "Laholm, Hallands län, Sverige", 17, 20);
    assertFalse(halmstad.contains(laholm));
    assertFalse(laholm.contains(halmstad));
    assertCentroidDistance(halmstad, laholm, 30, 40);

    Geocoding stockholm = assertArchDistance(halmstad, "Stockholm, Stockholms län, Sverige", 350, 370);
    assertFalse(halmstad.contains(stockholm));
    assertFalse(stockholm.contains(halmstad));
    assertFalse(stockholm.contains(laholm));
    assertFalse(laholm.contains(stockholm));

    assertCentroidDistance(halmstad, stockholm, 415, 430);


    // test bounds

    Geocoding opgs = geocoder.geocode("Olof palmes gatan 23", stockholm);
    assertFalse(opgs.isSuccess());
    opgs = new MergeSameHouseResultsFilter(opgs).filter();    
    new ProximityScorer(stockholm).score(opgs);
    new ThreadsholdScorer().score(opgs);
    assertTrue(opgs.isSuccess());
    assertTrue(stockholm.contains(opgs));

    Geocoding umeå = geocoder.geocode("Umeå, Sverige");
    assertTrue(umeå.isSuccess());
    Geocoding opgu = geocoder.geocode("Olof palmes gatan 23, umeå");
    assertTrue(opgu.isSuccess());
    assertTrue(umeå.contains(opgu));


    // test proximity scorer

    Geocoding opg = geocoder.geocode("Olof palmes gatan 23");
    new ProximityScorer(stockholm, umeå).score(opg);
    assertFalse(opg.isSuccess());


    opg = geocoder.geocode("Olof palmes gatan 23");
    opg = new MergeSameHouseResultsFilter(opg).filter();        
    new ProximityScorer(stockholm).score(opg);
    new ThreadsholdScorer().score(opg);
    assertTrue(opg.isSuccess());
    assertTrue(stockholm.contains(opg));

    // this will fail
    // its an address, but it has floor number, says there is an elevator and that it is sold.
    assertFalse(geocoder.geocode("Svalgränd 4, 5 tr, hiss sålt").isSuccess());
    System.currentTimeMillis();


    assertEquals(halmstad, PolygonTools.findSmallestEnclosingBounds(halland, halmstad));    
    assertEquals(halmstad, PolygonTools.findSmallestEnclosingBounds(halland, halmstad, sverige));
    assertEquals(halland, PolygonTools.findSmallestEnclosingBounds(halland, halmstad, sverige, laholm));
    assertEquals(laholm, PolygonTools.findSmallestEnclosingBounds(laholm, sverige, halland));
    assertEquals(sverige, PolygonTools.findSmallestEnclosingBounds(halland, stockholm, umeå, halmstad, sverige));
    System.currentTimeMillis();


//    Geocoding gardet = geocoder.geocode("Gärdet, Stockholm, Sverige");
//    assertEquals(gardet, PolygonTools.findSmallestEnclosingBounds(gardet, stockholm));

    Geocoding osteraker = geocoder.geocode("Österåker, Stockholms län, Sverige");
    assertEquals(null, PolygonTools.findSmallestEnclosingBounds(osteraker, stockholm));    


    // test request filters
    Geocoding mstou = geocoder.geocode("Main street, Toledo, Ohio, USA");
    assertTrue(mstou.isSuccess());

    request = new Request();
    request.setTextQuery("Main street");
    AddressComponentsRequestAugmenter acbf = new AddressComponentsRequestAugmenter();
    acbf.getComponents().add(new AddressComponent("Toledo", AddressComponentType.locality, AddressComponentType.political));
    acbf.getComponents().add(new AddressComponent("Ohio", AddressComponentType.political, AddressComponentType.administrative_area_level_1));
    acbf.getComponents().add(new AddressComponent("USA", AddressComponentType.country));
    request.setAugmenter(acbf);
    Geocoding geocoding = geocoder.geocode(request);
    assertTrue(geocoding.isSuccess());
    assertEquals(mstou, geocoding);

    new AddressComponentsScorer(geocoder.geocode("Toledo, Ohio, USA").getAddressComponents()).score(geocoding);

    assertTrue(geocoding.isSuccess());
    assertEquals(mstou, geocoding);

    new ProximityScorer(geocoding).score(geocoding);
    assertTrue(geocoding.isSuccess());
    assertEquals(mstou, geocoding);

    System.currentTimeMillis();

  }

  private void assertCentroidDistance(Geocoding from, Geocoding to, double minimumDistance, double maximumDistance) {
    double km;
    km = to.getCentroid().archDistance(from.getCentroid());
    assertTrue(km >= minimumDistance);
    assertTrue(km <= maximumDistance);
  }

  private Geocoding assertArchDistance(Geocoding from, String textQuery, double minimumKilometers, double maximumKilometers) throws Exception {
    double km;
    Geocoding to = geocoder.geocode(textQuery);


    km = to.archDistance(from);
    assertEquals(km, from.archDistance(to));

    assertTrue(km >= minimumKilometers);
    assertTrue(km <= maximumKilometers);

    return to;
  }

}
