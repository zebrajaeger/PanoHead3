package de.zebrajaeger.panohead3.calc;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * @author lars on 06.11.2016.
 */
public class Fov1D {
  private float sweepAngle;

  public Fov1D() {
  }

  public Fov1D(float sweepAngle) {
    this.sweepAngle = sweepAngle;
  }

  public float getSweepAngle() {
    return sweepAngle;
  }

  public void setSweepAngle(float sweepAngle) {
    this.sweepAngle = sweepAngle;
  }

  @Override
  public String toString() {
    return ReflectionToStringBuilder.toString(this);
  }
}
