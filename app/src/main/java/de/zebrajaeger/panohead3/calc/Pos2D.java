package de.zebrajaeger.panohead3.calc;

import java.io.Serializable;

/**
 * Created by Lars Brandt on 07.08.2017.
 */
public class Pos2D implements Serializable{
    private Float x;
    private Float y;

    public Pos2D(Float x, Float y) {
        this.x = x;
        this.y = y;
    }

    public Float getX() {
        return x;
    }

    public Float getY() {
        return y;
    }

    public boolean hasX(){
        return x!=null;
    }
    public boolean hasY(){
        return x!=null;
    }

    @Override
    public String toString() {
        return "Pos2D{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
