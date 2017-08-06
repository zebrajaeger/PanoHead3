package de.zebrajaeger.grblconnector.ui;

/**
 * Created by Lars Brandt on 23.07.2017.
 */
enum Direction {
    FORWARD, BACKWARD, ZERO;

    static Direction ofDelta(float delta) {
        Direction result = ZERO;
        if (delta < 0) {
            result = BACKWARD;
        } else if (delta > 0) {
            result = FORWARD;
        }
        return result;
    }
}
