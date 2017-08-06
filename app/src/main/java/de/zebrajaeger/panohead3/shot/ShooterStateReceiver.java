package de.zebrajaeger.panohead3.shot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * Created by Lars Brandt on 06.08.2017.
 */
public class ShooterStateReceiver {
    private BroadcastReceiver broadcastReceiver;
    private Context ctx;
    private Listener receiver;

    public ShooterStateReceiver(Context ctx, Listener receiver) {
        this.ctx = ctx;
        this.receiver = receiver;

        IntentFilter btFilter = new IntentFilter();
        btFilter.addAction(Shooter.BROADCAST_STATE_CHANGED);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Shooter.ShooterState from = (Shooter.ShooterState) intent.getExtras().get(Shooter.BROADCAST_STATE_CHANGED_FROM);
                Shooter.ShooterState to = (Shooter.ShooterState) intent.getExtras().get(Shooter.BROADCAST_STATE_CHANGED_TO);
                ShooterStateReceiver.this.receiver.onShooterState(from, to);
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
        void onShooterState(Shooter.ShooterState from, Shooter.ShooterState to);
    }
}
