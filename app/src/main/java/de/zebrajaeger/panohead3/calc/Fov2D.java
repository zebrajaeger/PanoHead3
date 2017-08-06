package de.zebrajaeger.panohead3.calc;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * @author lars on 06.11.2016.
 */
public class Fov2D {
    private Fov1D x;
    private Fov1D y;

    public Fov2D() {
    }

    public Fov2D(Float x, Float y) {
        if (x != null)
            this.x = new Fov1D(x);
        if (y != null)
            this.y = new Fov1D(y);
    }

    public Fov1D getX() {
        return x;
    }

    public void setX(Fov1D x) {
        this.x = x;
    }

    public Fov1D getY() {
        return y;
    }

    public void setY(Fov1D y) {
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
