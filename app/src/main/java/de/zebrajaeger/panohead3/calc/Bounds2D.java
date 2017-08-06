package de.zebrajaeger.panohead3.calc;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;

/**
 * @author lars on 23.10.2016.
 */
public class Bounds2D implements Serializable {
    private Bounds1D x;
    private Bounds1D y;

    public Bounds2D() {
    }

    public Bounds2D(Float x1, Float x2, Float y1, Float y2) {
        if (x1 != null && x2 != null)
            this.x = new Bounds1D(x1, x2);
        if (y1 != null && y2 != null)
            this.y = new Bounds1D(y1, y2);
    }

    public Bounds1D getX() {
        return x;
    }

    public void setX(Bounds1D x) {
        this.x = x;
    }

    public Bounds1D getY() {
        return y;
    }

    public void setY(Bounds1D y) {
        this.y = y;
    }

    public boolean isFull() {
        return x != null && x.isFull() && y != null && y.isFull();
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
