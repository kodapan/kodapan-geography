package se.kodapan.geography.geocoding.google;

import java.io.IOException;

/**
 * @author kalle
 * @since 2010-aug-16 11:40:47
 */
public class OverQueryLimitException extends IOException {

  public OverQueryLimitException() {
  }

  public OverQueryLimitException(String s) {
    super(s);
  }
}
