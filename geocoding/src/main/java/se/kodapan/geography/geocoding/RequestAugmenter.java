package se.kodapan.geography.geocoding;

/**
 * @author kalle
 * @since 2010-jul-21 17:55:06
 */
public interface RequestAugmenter {

  /**
   *
   * @param geocoder 
   * @param request implementations are not allowed to touch this instance!
   * @param input implementations are not allowed to touch this instance!
   * @return
   * @throws Exception
   */
  public abstract Geocoding filter(Geocoder geocoder, Request request, Geocoding input) throws Exception;

}
