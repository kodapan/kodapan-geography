package se.kodapan.geography.geocoding;

/**
 * Could just be a decorated/filtered geocoder in many cases,
 * but this allows for passing different augmenters in the request.
 *
 * @author kalle
 * @since 2010-jul-21 17:55:06
 */
public interface RequestAugmenter {

  /**
   *
   * @param geocoder 
   * @param request implementations are not allowed to touch this instance!
   * @return
   * @throws Exception
   */
  public abstract Geocoding filter(Geocoder geocoder, Request request) throws Exception;

}
