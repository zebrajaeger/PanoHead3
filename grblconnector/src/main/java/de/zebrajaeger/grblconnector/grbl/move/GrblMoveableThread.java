package de.zebrajaeger.grblconnector.grbl.move;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import de.zebrajaeger.grblconnector.grbl.GrblEx;
import de.zebrajaeger.grblconnector.grbl.command.Commands;

/**
 * Created by Lars Brandt on 16.07.2017.
 */
public class GrblMoveableThread extends Thread {

    public static final String DE_ZEBRAJAEGER_GRBL_BROADCAST_STATUS_REPORT = "de.zebrajaeger.grbl.broadcast.STATUS_REPORT";
    private final GrblEx grbl;
    private Moveable moveable;
    private Context ctx;
    //private StateListener stateListener;

    public GrblMoveableThread(Context ctx, GrblEx grbl) {
        super("GrblMoveableThread");
        this.ctx = ctx;
        this.grbl = grbl;
    }

    public void setMoveable(Moveable moveable) {
        this.moveable = moveable;
    }

    //public void setStateListener(StateListener stateListener) {
    //    this.stateListener = stateListener;
    //}

    @Override
    public void run() {
        //long nextPosCall = 0;
        //long readPosPeriod = 500;

        try {
            while (true) {
                // send status request
                /*
                long now = System.currentTimeMillis();
                if (nextPosCall <= now) {
                    nextPosCall = now + readPosPeriod;
                    if(stateListener!=null) {
                        String response = grbl.execute("?");
                        StatusReportResponse statusReportResponse = StatusReportResponse.of(response);
                        if (statusReportResponse != null) {
                            stateListener.onStatus(statusReportResponse);
                            //Intent intent = new Intent();
                            //intent.setAction(DE_ZEBRAJAEGER_GRBL_BROADCAST_STATUS_REPORT);
                            //intent.putExtra("data", statusReportResponse);
                            //ctx.sendBroadcast(intent);
                        }
                    }
                }
                */

                // if available send
                if (moveable != null) {

                    Move move = moveable.pickMove();
                    if (move != null && move.actionRequired()) {

                        if (move.isRequireStopX()) {
                            Log.i("GrblLoop", "STOP");
                            grbl.execute(Commands.getJogCancelCommands());
                        }
                        float diff = move.getDeltaX();
                        if (diff != 0) {
                            diff /= 10.0f;
                            grbl.execute("$J=G91 F10000 G20 X" + diff + "\n");
                        }
                    }
                } else {
                    Thread.sleep(10);
                }
            }
        } catch (InterruptedException e) {
            interrupt();
        }
    }

    //public interface StateListener {
    //    void onStatus(StatusReportResponse status);
    //}
}
