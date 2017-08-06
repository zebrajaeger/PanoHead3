package de.zebrajaeger.panohead3.util;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * Created by lars on 02.10.2016.
 */
public class Position {
  private double x;
  private double y;

  public Position() {
    this.x = 0;
    this.y = 0;
  }

  public Position(double x, double y) {
    this.x = x;
    this.y = y;
  }

  public boolean isZero() {
    return x == 0d && y == 0d;
  }

  public Position addX(double x) {
    return new Position(this.x + x, this.y);
  }

  public Position addY(double y) {
    return new Position(this.x, this.y + y);
  }

  public Position addXY(double x, double y) {
    return new Position(this.x + x, this.y + y);
  }

  public Position subX(double x) {
    return new Position(this.x - x, this.y);
  }

  public Position subY(double y) {
    return new Position(this.x, this.y - y);
  }

  public Position subXY(double x, double y) {
    return new Position(this.x - x, this.y - y);
  }

  public Position add(Position p) {
    return new Position(this.x + p.x, this.y + p.y);
  }

  public Position sub(Position p) {
    return new Position(this.x - p.x, this.y - p.y);
  }

  public Position div(double d) {
    return new Position(x / d, y / d);
  }
  public Position div(double dx,double dy) {
    return new Position(x / dx, y / dy);
  }

  public Position mul(double d) {
    return new Position(x * d, y * d);
  }
  public Position mul(double dx,double dy) {
    return new Position(x * dx, y * dy);
  }

  public double getX() {
    return x;
  }

  public double getY() {
    return y;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Position position = (Position) o;

    if (Double.compare(position.x, x) != 0) return false;
    return Double.compare(position.y, y) == 0;
  }

  @Override
  public int hashCode() {
    int result;
    long temp;
    temp = Double.doubleToLongBits(x);
    result = (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(y);
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    return result;
  }

  @Override
  public String toString() {
    return ReflectionToStringBuilder.toString(this);
  }

}
