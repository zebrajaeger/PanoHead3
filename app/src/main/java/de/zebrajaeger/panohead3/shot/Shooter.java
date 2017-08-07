package de.zebrajaeger.panohead3.shot;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

import de.zebrajaeger.grblconnector.PanoHead;
import de.zebrajaeger.grblconnector.GrblStatusReceiver;
import de.zebrajaeger.grblconnector.ShotConfig;
import de.zebrajaeger.grblconnector.grbl.Grbl;
import de.zebrajaeger.grblconnector.grbl.move.Pos;

/**
 * Created by Lars Brandt on 05.08.2017.
 */
public class Shooter implements GrblStatusReceiver.Listener {
    public static final String BROADCAST_STATE_CHANGED = "de.zebrajaeger.panohead.shooter.STATE";
    public static final String BROADCAST_STATE_CHANGED_FROM = "from";
    public static final String BROADCAST_STATE_CHANGED_TO = "to";
    public static final String BROADCAST_IMAGE_CHANGED = "de.zebrajaeger.panohead.shooter.IMGAGE";
    public static final String BROADCAST_IMAGE_CHANGED_INDEX_FROM = "indexFrom";
    public static final String BROADCAST_IMAGE_CHANGED_INDEX_TO = "indexTo";

    private static final String LOG_SCOPE = "Shooter";

    private final Context ctx;
    private final ShotConfig shotConfig;
    private final ShooterScript script;
    private final GrblStatusReceiver grblStatusReceiver;
    private Timer ticTimer;
    private int currentImageIndex = 0;
    private ShooterState shooterState = ShooterState.IDLE;
    private ShooterState prePauseShooterState;
    private Grbl.GrblStatus grblStatus = Grbl.GrblStatus.UNKNOWN;

    private Pos lastpos;
    private PauseState pause = PauseState.RUNNING;

    public Shooter(Context ctx, ShotConfig shotConfig, ShooterScript script) {
        this.ctx = ctx;
        this.shotConfig = shotConfig;
        this.script = script;

        grblStatusReceiver = new GrblStatusReceiver(ctx, this);
        setShooterState(ShooterState.IDLE);
        ticTimer = new Timer("Shooter");
        ticTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                onTic();
            }
        }, 0, 100);
    }

    public void destroy() {
        ticTimer.cancel();
        ticTimer = null;
        grblStatusReceiver.destroy();
    }

    public void start() {
        if (isStartable()) {
            setCurrentImageIndex(0);
            setShooterState(ShooterState.GO_TO_POS);
        }
    }

    public void stop() {
        setShooterState(ShooterState.STOPPING);
    }

    public void pauseOrResume() {
        switch(pause) {
            case WAIT_FOR_PAUSE:
            case PAUSED:
                pause = PauseState.RUNNING;
                break;
            case RUNNING:
                pause = PauseState.WAIT_FOR_PAUSE;
                break;
        }
    }

    private synchronized void onTic() {
        if(pause==PauseState.WAIT_FOR_PAUSE){
            prePauseShooterState = getShooterState();
            setShooterState(ShooterState.PAUSED);
            pause = PauseState.PAUSED;
        }

        switch (getShooterState()) {
            case PAUSED: {
                if(pause!=PauseState.PAUSED ){
                    setShooterState(prePauseShooterState);
                }
            }
            break;

            case GO_TO_POS: {
                try {
                    moveToShot(getCurrentImageIndex());
                    setShooterState(ShooterState.WAIT_FOR_MOTION_STOP);
                } catch (InterruptedException e) {
                    Log.e(LOG_SCOPE, "Could not move to image index " + getCurrentImageIndex(), e);
                    setShooterState(ShooterState.FAILED);
                }
            }
            break;

            case WAIT_FOR_MOTION_STOP: {
                if(grblStatus== Grbl.GrblStatus.IDLE){
                    setShooterState(ShooterState.SHOOTING);
                }
            }
            break;

            case SHOOTING: {
                try {
                    takeShot();
                    if (incrementCurrentImageIndex()) {
                        setShooterState(ShooterState.GO_TO_POS);
                    } else {
                        setShooterState(ShooterState.FINISHED);
                    }
                } catch (InterruptedException e) {
                    Log.e(LOG_SCOPE, "Could not take a shot", e);
                    setShooterState(ShooterState.FAILED);
                }
            }
            break;

            case GO_TO_START_POS: {
                ShotPosition shot = script.getShots().get(0);
                Pos pos2 = Pos.of(shot.getX(), shot.getY());
                try {
                    PanoHead.I.moveTo(pos2);
                    setShooterState(ShooterState.SHOOTING);
                } catch (InterruptedException e) {
                    Log.e(LOG_SCOPE, "Could not move to pos " + pos2, e);
                    setShooterState(ShooterState.FAILED);
                }
            }
            break;

            case STOPPING: {
                setShooterState(ShooterState.STOPPED);
            }
            break;
        }
    }

    private void takeShot() throws InterruptedException {
        PanoHead.I.takeShot(shotConfig);
    }

    private void moveToPos(Pos pos) throws InterruptedException {
        if (lastpos == null || lastpos.getX() > pos.getX()) {
            Pos posBacklash = Pos.of(pos.getX() - 5f, pos.getY());
            // at first we go 5deg more left to come always from same site.
            // reason is backlash of gear
            PanoHead.I.moveTo(posBacklash);
        }
        PanoHead.I.moveTo(pos);
        lastpos = pos;
    }

    private void moveToShot(int index) throws InterruptedException {
        ShotPosition shot = script.getShots().get(index);
        moveToPos(Pos.of(shot.getX(), shot.getY()));
    }

    public int getCurrentImageIndex() {
        return currentImageIndex;
    }

    private void setCurrentImageIndex(int index) {
        if ((index >= 0) && (index < script.getShots().size())) {
            Intent i = new Intent();
            i.setAction(BROADCAST_IMAGE_CHANGED);
            i.putExtra(BROADCAST_IMAGE_CHANGED_INDEX_FROM, this.currentImageIndex);
            i.putExtra(BROADCAST_IMAGE_CHANGED_INDEX_TO, index);
            this.currentImageIndex = index;
            ctx.sendBroadcast(i);
        }
    }

    private boolean incrementCurrentImageIndex() {
        if (getCurrentImageIndex() < script.getShots().size() - 1) {
            setCurrentImageIndex(getCurrentImageIndex() + 1);
            return true;
        }
        return false;
    }

    private synchronized ShooterState getShooterState() {
        return shooterState;
    }

    private synchronized void setShooterState(ShooterState shooterState) {
        Intent i = new Intent();
        i.setAction(BROADCAST_STATE_CHANGED);
        i.putExtra(BROADCAST_STATE_CHANGED_FROM, this.shooterState);
        i.putExtra(BROADCAST_STATE_CHANGED_TO, shooterState);
        this.shooterState = shooterState;
        ctx.sendBroadcast(i);
    }

    public boolean isRunning() {
        return shooterState == ShooterState.GO_TO_POS
                || shooterState == ShooterState.GO_TO_START_POS
                || shooterState == ShooterState.WAIT_FOR_MOTION_STOP
                || shooterState == ShooterState.SHOOTING;
    }

    public boolean isPaused() {
        return shooterState == ShooterState.PAUSED;
    }

    public boolean isStartable() {
        return shooterState == ShooterState.IDLE
                || shooterState == ShooterState.STOPPED
                || shooterState == ShooterState.FINISHED
                || shooterState == ShooterState.FAILED;
    }

    @Override
    public void onPos(Pos pos, Grbl.GrblStatus status) {
        grblStatus = status;
    }

    public enum ShooterState {
        IDLE, FAILED, STOPPED, FINISHED,
        GO_TO_POS, WAIT_FOR_MOTION_STOP, SHOOTING, GO_TO_START_POS,
        STOPPING,
        PAUSED
    }

    public enum PauseState {
        WAIT_FOR_PAUSE,
        PAUSED,
        RUNNING
    }
}
