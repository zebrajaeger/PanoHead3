package de.zebrajaeger.grblconnector;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * Created by Lars Brandt on 05.08.2017.
 */
import de.zebrajaeger.grblconnector.grbl.Grbl;
import de.zebrajaeger.grblconnector.grbl.move.Pos;

public class GrblStatusReceiver {
    private BroadcastReceiver broadcastReceiver;
    private Context ctx;
    private Listener listener;

    public GrblStatusReceiver(Context ctx, Listener listener) {
        this.ctx = ctx;
        this.listener = listener;
        IntentFilter filter = new IntentFilter();
        filter.addAction(PanoHead.BROADCAST_POS);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Pos pos = (Pos) intent.getExtras().get(PanoHead.BROADCAST_POS_DATA_POS);
                Grbl.GrblStatus status = (Grbl.GrblStatus) intent.getExtras().get(PanoHead.BROADCAST_POS_DATA_STATUS);
                GrblStatusReceiver.this.listener.onPos(pos,status);
            }
        };
        ctx.registerReceiver(broadcastReceiver, filter);
    }

    public void destroy() {
        if (broadcastReceiver != null) {
            ctx.unregisterReceiver(broadcastReceiver);
            broadcastReceiver = null;
        }
    }

    public interface Listener {
        void onPos(Pos pos, Grbl.GrblStatus status);
    }

}
