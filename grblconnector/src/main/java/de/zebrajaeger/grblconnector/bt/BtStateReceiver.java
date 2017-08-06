package de.zebrajaeger.grblconnector.bt;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * Created by Lars Brandt on 06.08.2017.
 */
public abstract class BtStateReceiver {
    private BroadcastReceiver broadcastReceiver;
    private Context ctx;

    public BtStateReceiver(Context ctx) {
        this.ctx = ctx;

        IntentFilter btFilter = new IntentFilter();
        btFilter.addAction(BT.ACTION_CONNECTION_STATE_CHANGED);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                BT.ConnectionState from = (BT.ConnectionState) intent.getExtras().get("from");
                BT.ConnectionState to = (BT.ConnectionState) intent.getExtras().get("to");
                onBtState(from, to);
            }
        };
        ctx.registerReceiver(broadcastReceiver, btFilter);
    }

    public void destroy() {
        if (broadcastReceiver != null) {
            ctx.unregisterReceiver(broadcastReceiver);
            broadcastReceiver = null;
        }
    }

    public abstract void onBtState(BT.ConnectionState from, BT.ConnectionState to);
}
