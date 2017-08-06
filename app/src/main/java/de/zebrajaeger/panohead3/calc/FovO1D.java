package de.zebrajaeger.panohead3.calc;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;

/**
 * Created by lars on 27.10.2016.
 */

public class FovO1D implements Serializable {
  private float sweepAngle;
  private float overlap;

  public FovO1D() {
    this.sweepAngle = 0f;
    this.overlap = 0f;
  }

  public FovO1D(float sweepAngle) {
    this.sweepAngle = sweepAngle;
    this.overlap = 0f;
  }

  public FovO1D(float sweepAngle, float overlap) {
    this.sweepAngle = sweepAngle;
    this.overlap = overlap;
  }

  public FovO1D(Fov1D fov1D) {
    this.sweepAngle = fov1D.getSweepAngle();
    this.overlap = 0f;
  }

  public FovO1D(Fov1D fov1D, float overlap) {
    this.sweepAngle = fov1D.getSweepAngle();
    this.overlap = overlap;
  }

  public float getSweepAngle() {
    return sweepAngle;
  }

  public float getOverlap() {
    return overlap;
  }

  public float getOverlapingAngle() {
    return sweepAngle * overlap;
  }

  public float getNonOverlapingAngle() {
    return sweepAngle * (1f - overlap);
  }

  public void setSweepAngle(float sweepAngle) {
    this.sweepAngle = sweepAngle;
  }

  public void setOverlap(float overlap) {
    this.overlap = overlap;
  }

  @Override
  public String toString() {
    return ReflectionToStringBuilder.toString(this);
  }
}
