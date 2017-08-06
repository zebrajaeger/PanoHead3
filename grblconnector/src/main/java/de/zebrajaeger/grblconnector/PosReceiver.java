package de.zebrajaeger.grblconnector;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * Created by Lars Brandt on 05.08.2017.
 */
import de.zebrajaeger.grblconnector.grbl.move.Pos;

public class PosReceiver {
    private BroadcastReceiver broadcastReceiver;
    private Context ctx;
    private Listener listener;

    public PosReceiver(Context ctx, Listener listener) {
        this.ctx = ctx;
        this.listener = listener;
        IntentFilter filter = new IntentFilter();
        filter.addAction(PanoHead.BROADCAST_POS);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Pos pos = (Pos) intent.getExtras().get(PanoHead.BROADCAST_POS_DATA_POS);
                String status = (String) intent.getExtras().get(PanoHead.BROADCAST_POS_DATA_STATUS);
                PosReceiver.this.listener.onPos(pos,status);
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
        void onPos(Pos pos, String status);
    }

}
