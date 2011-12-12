package se.kodapan.geography.tools;

/**
 * @author kalle
 * @since 2011-11-18 23:51
 */
public class TooFewCoordinatesException extends Exception {

  public TooFewCoordinatesException() {
  }

  public TooFewCoordinatesException(String message) {
    super(message);
  }

  public TooFewCoordinatesException(int asserted, int found) {
    super("Expected " + asserted + " but found " + found);
  }
}
