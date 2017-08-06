package de.zebrajaeger.panohead3.util;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * @author lars on 11.12.2016.
 */
public class Size2D {
  private float w;
  private float h;

  public Size2D(float w, float h) {
    this.w = w;
    this.h = h;
  }

  public float getW() {
    return w;
  }

  public void setW(float w) {
    this.w = w;
  }

  public float getH() {
    return h;
  }

  public void setH(float h) {
    this.h = h;
  }

  @Override
  public String toString() {
    return ReflectionToStringBuilder.toString(this);
  }
}
