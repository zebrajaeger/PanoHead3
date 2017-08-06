package de.zebrajaeger.panohead3.calc;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;

/**
 * Created by lars on 23.10.2016.
 */
public class FovO2D implements Serializable {
  private FovO1D x = null;
  private FovO1D y = null;

  public FovO2D(Fov2D fov, Overlap overlap) {
    if (fov.hasX()) {
      this.x = new FovO1D(fov.getX(), overlap.getX());
    }
    if (fov.hasY()) {
      this.y = new FovO1D(fov.getY(), overlap.getY());
    }
  }

  public FovO1D getX() {
    return x;
  }

  public FovO1D getY() {
    return y;
  }

  public void setX(FovO1D x) {
    this.x = x;
  }

  public void setY(FovO1D y) {
    this.y = y;
  }

  public boolean hasX() {
    return x != null;
  }

  public boolean hasY() {
    return y != null;
  }

  @Override
  public String toString() {
    return ReflectionToStringBuilder.toString(this);
  }
}
