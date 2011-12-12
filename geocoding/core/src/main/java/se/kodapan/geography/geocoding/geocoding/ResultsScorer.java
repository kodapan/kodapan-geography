package se.kodapan.geography.geocoding.geocoding;

/**
 * @author kalle
 * @since 2010-jul-21 20:25:25
 */
public abstract class ResultsScorer {
  
  public abstract void score(Geocoding geocoding) throws Exception;

}
