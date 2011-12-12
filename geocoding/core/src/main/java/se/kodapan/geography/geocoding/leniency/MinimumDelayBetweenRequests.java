package se.kodapan.geography.geocoding.leniency;

import java.util.concurrent.TimeUnit;

/**
 * @author kalle
 * @since 2011-11-16 17:18
 */
public class MinimumDelayBetweenRequests extends Leniency {

  private long delay;
  private TimeUnit delayUnit;

  public MinimumDelayBetweenRequests(long delay, TimeUnit delayUnit) {
    this.delay = delay;
    this.delayUnit = delayUnit;
  }

  private long previousRequest = System.currentTimeMillis();

  @Override
  public synchronized void sleep() throws InterruptedException {
    long now = System.currentTimeMillis();
    long sleep = now - previousRequest + delayUnit.toMillis(delay);
    if (sleep > 0) {
      Thread.sleep(sleep);
    }
    previousRequest = now;
  }

  public long getDelay() {
    return delay;
  }

  public void setDelay(long delay) {
    this.delay = delay;
  }

  public TimeUnit getDelayUnit() {
    return delayUnit;
  }

  public void setDelayUnit(TimeUnit delayUnit) {
    this.delayUnit = delayUnit;
  }

  public long getPreviousRequest() {
    return previousRequest;
  }

  public void setPreviousRequest(long previousRequest) {
    this.previousRequest = previousRequest;
  }
}
