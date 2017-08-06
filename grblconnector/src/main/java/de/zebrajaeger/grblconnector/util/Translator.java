package de.zebrajaeger.grblconnector.util;

import de.zebrajaeger.grblconnector.grbl.move.Pos;

/**
 * Created by Lars Brandt on 23.07.2017.
 */

public class Translator {

    public static final Translator DIRECT_DRIVE = new Translator(1f);

    private float translationX;
    private float translationY;
    private float translationZ;

    public Translator(float translation) {
        this.translationX = translation;
        this.translationY = translation;
        this.translationZ = translation;
    }
    public Translator(float translationX, float translationY, float translationZ) {
        this.translationX = translationX;
        this.translationY = translationY;
        this.translationZ = translationZ;
    }

    public Pos translateMotor2Drive(Pos pos){
        return new Pos(
                pos.getX() / translationX,
                pos.getY() / translationY,
                pos.getZ() / translationZ);
    }
    public Pos translateDrive2Motor(Pos pos){
        return new Pos(
                pos.getX() * translationX,
                pos.getY() * translationY,
                pos.getZ() * translationZ);
    }

    @Override
    public String toString() {
        return "Translator{" +
                "translationX=" + translationX +
                ", translationY=" + translationY +
                ", translationZ=" + translationZ +
                '}';
    }
}
