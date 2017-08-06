package de.zebrajaeger.grblconnector.grbl.move;

import java.io.Serializable;
import java.util.StringTokenizer;

/**
 * Created by Lars Brandt on 23.07.2017.
 */
public class Pos implements Serializable {
    private float x, y, z;

    private Pos() {
    }

    public Pos(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public static Pos of(float x, float y) {
        return new Pos(x,y,0);
    }

    public static Pos of(String pos) {
        Pos result = new Pos();
        StringTokenizer st = new StringTokenizer(pos, ",");
        if (st.hasMoreTokens()) {
            result.x = Float.parseFloat(st.nextToken());
        }
        if (st.hasMoreTokens()) {
            result.y = Float.parseFloat(st.nextToken());
        }
        if (st.hasMoreTokens()) {
            result.z = Float.parseFloat(st.nextToken());
        }
        return result;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    @Override
    public String toString() {
        return "Pos{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
}
