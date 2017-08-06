package de.zebrajaeger.panohead3.shot;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;

/**
 * @author lars on 08.10.2016.
 */
public class ShotPosition implements Serializable {
  private float x;
  private float y;

  public ShotPosition(float x, float y) {
    this.x = x;
    this.y = y;
  }

  public float getX() {
    return x;
  }

  public float getY() {
    return y;
  }

  @Override
  public String toString() {
    return ReflectionToStringBuilder.toString(this);
  }
}
