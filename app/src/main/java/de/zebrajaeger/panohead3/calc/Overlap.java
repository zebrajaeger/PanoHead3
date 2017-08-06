package de.zebrajaeger.panohead3.calc;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;

/**
 * @author lars on 06.11.2016.
 */
public class Overlap implements Serializable {
  private Float x;
  private Float y;

  public Overlap() {
  }

  public Overlap(Float x, Float y) {
    this.x = x;
    this.y = y;
  }

  public Float getX() {
    return x;
  }

  public void setX(Float x) {
    this.x = x;
  }

  public Float getY() {
    return y;
  }

  public void setY(Float y) {
    this.y = y;
  }

  public boolean isInRange(){
    return isXInRange() && isYInRange();
  }
  public boolean isXInRange(){
    return x !=null && x >=0f && x <=100f;
  }
  public boolean isYInRange(){
    return y !=null && y >=0f && y <=100f;
  }
  @Override
  public String toString() {
    return ReflectionToStringBuilder.toString(this);
  }
}
