package de.zebrajaeger.grblconnector;

import java.io.Serializable;

/**
 * Created by Lars Brandt on 06.08.2017.
 */
public class ShotConfig implements Serializable {
    private int pauseBeforeShot;
    private int focusTime;
    private int shotTime;
    private int pauseAfterShot;

    public ShotConfig(int pauseBeforeShot, int focusTime, int shotTime, int pauseAfterShot) {
        this.pauseBeforeShot = pauseBeforeShot;
        this.focusTime = focusTime;
        this.shotTime = shotTime;
        this.pauseAfterShot = pauseAfterShot;
    }

    public int getPauseBeforeShot() {
        return pauseBeforeShot;
    }

    public int getFocusTime() {
        return focusTime;
    }

    public int getShotTime() {
        return shotTime;
    }

    public int getPauseAfterShot() {
        return pauseAfterShot;
    }
}
