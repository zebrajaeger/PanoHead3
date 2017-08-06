package de.zebrajaeger.panohead3.shot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * Created by Lars Brandt on 06.08.2017.
 */
public class ShooterImageReceiver {
    private BroadcastReceiver broadcastReceiver;
    private Context ctx;
    private Listener receiver;

    public ShooterImageReceiver(Context ctx, Listener receiver) {
        this.ctx = ctx;
        this.receiver = receiver;

        IntentFilter btFilter = new IntentFilter();
        btFilter.addAction(Shooter.BROADCAST_IMAGE_CHANGED);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int from = (int) intent.getExtras().get(Shooter.BROADCAST_IMAGE_CHANGED_INDEX_FROM);
                int to = (int) intent.getExtras().get(Shooter.BROADCAST_IMAGE_CHANGED_INDEX_TO);
                ShooterImageReceiver.this.receiver.onImageChanged(from, to);
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

    public interface Listener {
        void onImageChanged(int fromIndex, int toIndex);
    }
}
