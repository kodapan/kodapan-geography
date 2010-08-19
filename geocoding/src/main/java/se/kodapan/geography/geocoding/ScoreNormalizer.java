package se.kodapan.geography.geocoding;

/**
 * @author kalle
 * @since 2010-aug-10 19:44:38
 */
public class ScoreNormalizer extends ResultsScorer {

  private double topScore = 1d;

  public ScoreNormalizer() {
  }

  public ScoreNormalizer(double topScore) {
    this.topScore = topScore;
  }

  @Override
  public void score(Geocoding geocoding) throws Exception {
    if (geocoding.getResults().size() > 0) {
      double factor = topScore / geocoding.getResults().get(0).getScore();
      for (Result result : geocoding.getResults()) {
        result.setScore(result.getScore() * factor);
      }
    }
  }
}
