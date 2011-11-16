package se.kodapan.geography.domain;

import junit.framework.TestCase;
import org.junit.Test;

/**
 * @author kalle
 * @since 2011-11-15 22:24
 */
public class TestDomain extends TestCase {

  public static final Envelope sweden = new EnvelopeImpl(new CoordinateImpl(54.67383096593114, 9.31640625), new CoordinateImpl(69.71810669906763, 26.3671875));
  public static final Envelope stockholm = new EnvelopeImpl(new CoordinateImpl(58.79667063373541, 17.4023437), new CoordinateImpl(59.88342484295896, 19.764404296875));
  public static final Envelope stockholmCity = new EnvelopeImpl(new CoordinateImpl(59.29955167361263, 17.992172241210938), new CoordinateImpl(59.36189532631362, 18.143234252929688));


  @Test
  public void test() {

    Envelope envelope = new EnvelopeImpl();
    envelope.addBounds(sweden);
    assertEquals(envelope, sweden);

    assertTrue(sweden.contains(stockholm));
    assertTrue(stockholm.contains(stockholmCity));
    assertTrue(sweden.contains(stockholmCity));

    assertTrue(sweden.contains(stockholm.getCentroid()));



    // todo test span international date line

    // todo implement and test span polars

  }

}
