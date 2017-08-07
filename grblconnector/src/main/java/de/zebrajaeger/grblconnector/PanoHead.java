package de.zebrajaeger.grblconnector;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

import de.zebrajaeger.grblconnector.bt.BT;
import de.zebrajaeger.grblconnector.bt.BtStateReceiver;
import de.zebrajaeger.grblconnector.grbl.Grbl;
import de.zebrajaeger.grblconnector.grbl.GrblEx;
import de.zebrajaeger.grblconnector.grbl.command.Commands;
import de.zebrajaeger.grblconnector.grbl.move.GrblMoveableThread;
import de.zebrajaeger.grblconnector.grbl.move.Moveable;
import de.zebrajaeger.grblconnector.grbl.move.Pos;
import de.zebrajaeger.grblconnector.grbl.move.StatusReportResponse;
import de.zebrajaeger.grblconnector.util.Translator;

/**
 * Created by Lars Brandt on 23.07.2017.
 */
public class PanoHead {
    public static final String BROADCAST_POS = "de.zebrajaeger.panohead.broadcast.POS";
    public static final String BROADCAST_POS_DATA_POS = "pos";
    public static final String BROADCAST_POS_DATA_STATUS = "status";
    public static final int POS_TIMER_PERIOD = 250;
    public static final PanoHead I = new PanoHead();
    private static final String LOG_SCOPE = "PanoHead";
    public final BT bt = new BT();
    private float feedrate = 30000; // TODO make configrable
    private Context context;
    private GrblEx grbl;
    private GrblMoveableThread grblMoveableThread;
    private BroadcastReceiver btBroadcastReceiver;
    private Translator translator = Translator.DIRECT_DRIVE;
    private Timer posTimer;
    private BtStateReceiver btStateReceiver;

    public void init(Context ctx) {
        context = ctx;

        // grbl
        grbl = new GrblEx(10000);

        // BT connection change listener
        btStateReceiver = new BtStateReceiver(ctx) {
            @Override
            public void onBtState(BT.ConnectionState from, BT.ConnectionState to) {
                if (to == BT.ConnectionState.CONNECTED) {
                    grbl.start(bt);
                } else if (to == BT.ConnectionState.DISCONNECTED) {
                    try {
                        grbl.stop();
                    } catch (InterruptedException e) {
                        Log.e(LOG_SCOPE, "Failed to stop grbl", e);
                    }
                }
            }
        };

        // bluetooth
        bt.init(ctx);

        // thread that polls for pos change
        posTimer = new Timer("Pos Timer");
        posTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    String response = grbl.execute("?");
                    StatusReportResponse status = StatusReportResponse.of(response);
                    if (status != null) {
                        Pos motor = status.getMpos();
                        Pos drive = translator.translateMotor2Drive(motor);
                        Intent i = new Intent();
                        i.setAction(BROADCAST_POS);

                        i.putExtra("status", Grbl.GrblStatus.of(status.getState()));
                        i.putExtra("pos", drive);
                        context.sendBroadcast(i);
                    }
                } catch (InterruptedException e) {
                    Log.e(LOG_SCOPE, "could not request pos", e);
                }
            }
        }, 0, POS_TIMER_PERIOD);
    }

    public void setTranslator(Translator translator) {
        this.translator = translator;
    }

    public void uninit() {
        // TODO implement me
        // destroy pos timer
        // stop moveable thread
        // unregister broadcastReceiver
        // grbl.stop()
        // stop BT
        throw new UnsupportedOperationException();
    }

    private void createMovableThread() {
        if (grblMoveableThread == null || !grblMoveableThread.isAlive()) {
            grblMoveableThread = new GrblMoveableThread(context, grbl);
            /*grblMoveableThread.setStateListener(new GrblMoveableThread.StateListener() {
                @Override
                public void onStatus(StatusReportResponse status) {
                    Pos motor = status.getMpos();
                    Pos drive = translator.translateMotor2Drive(motor);
                    Intent i = new Intent();
                    i.setAction(BROADCAST_POS);
                    i.putExtra("status", status.getState());
                    i.putExtra("pos", drive);
                    context.sendBroadcast(i);
                }
            });/*/
            grblMoveableThread.start();
        }
    }

    private void destroyMovableThread() throws InterruptedException {
        if (grblMoveableThread != null && grblMoveableThread.isAlive()) {
            grblMoveableThread.interrupt();
            grblMoveableThread.join();
        }
        grblMoveableThread = null;
    }


    public void setMoveable(Moveable moveable) {
        if (moveable == null) {
            try {
                destroyMovableThread();
            } catch (InterruptedException e) {
                Log.e(LOG_SCOPE, "could not stop grblMoveableThread", e);
            }
        } else {
            createMovableThread();
            grblMoveableThread.setMoveable(moveable);
        }
    }

    public void moveTo(Pos pos) throws InterruptedException {
        grbl.execute(Commands.createMoveToCommand(translator.translateDrive2Motor(pos), feedrate));
    }

    public void takeShot(ShotConfig shotConfig) throws InterruptedException {
        Thread.sleep(shotConfig.getPauseBeforeShot());
        grbl.execute(Commands.createCamTriggerCommand(true, false));
        Thread.sleep(shotConfig.getFocusTime());
        grbl.execute(Commands.createCamTriggerCommand(true, true));
        Thread.sleep(shotConfig.getShotTime());
        grbl.execute(Commands.createCamTriggerCommand(false, false));
        Thread.sleep(shotConfig.getPauseAfterShot());
    }
}
