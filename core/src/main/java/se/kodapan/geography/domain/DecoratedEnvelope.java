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
package se.kodapan.geography.domain;

/**
 * @author kalle
 * @since 2010-jul-20 01:16:15
 */
public class DecoratedEnvelope extends EnvelopeDecorator {

  private static final long serialVersionUID = 1l;

  private Envelope decoratedEnvelope;

  public DecoratedEnvelope(Envelope decoratedEnvelope) {
    this.decoratedEnvelope = decoratedEnvelope;
  }

  public Envelope getDecoratedEnvelope() {
    return decoratedEnvelope;
  }

  public void setDecoratedEnvelope(Envelope decoratedEnvelope) {
    this.decoratedEnvelope = decoratedEnvelope;
  }

  @Override
  public double archDistanceDiagonal() {
    return this.decoratedEnvelope.archDistanceDiagonal();
  }
}