package de.zebrajaeger.panohead3;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import de.zebrajaeger.grblconnector.PosReceiver;
import de.zebrajaeger.grblconnector.ShotConfig;
import de.zebrajaeger.grblconnector.grbl.move.Pos;
import de.zebrajaeger.panohead3.shot.Shooter;
import de.zebrajaeger.panohead3.shot.ShooterImageReceiver;
import de.zebrajaeger.panohead3.shot.ShooterScript;
import de.zebrajaeger.panohead3.shot.ShooterStateReceiver;

public class PanoShotActivity extends AppCompatActivity implements ShooterStateReceiver.Listener, ShooterImageReceiver.Listener, PosReceiver.Listener {


    public static final String ACTIVITY_PARAM_SCRIPT = "script";
    public static final String ACTIVITY_PARAM_SHOT_CONFIG = "shotConfig";
    private Shooter shooter;
    private ShooterStateReceiver shooterStateReceiver;
    private ShooterImageReceiver shooterImageReceiver;
    private PosReceiver posReceiver;
    private ShooterScript script;
    private ShotConfig config;
    private Button btnPause;
    private PanoShotView panoShotView;
    private Button btnStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();
        setContentView(R.layout.activity_pano_shot);

        script = (ShooterScript) getIntent().getExtras().getSerializable(ACTIVITY_PARAM_SCRIPT);
        config = (ShotConfig) getIntent().getExtras().getSerializable(ACTIVITY_PARAM_SHOT_CONFIG);
        shooter = new Shooter(this, config, script);

        btnPause = (Button) findViewById(R.id.button_pause);
        btnStop = (Button) findViewById(R.id.button_stop);
        panoShotView = (PanoShotView) findViewById(R.id.pano_shot_view);

        panoShotView.setScript(script);
    }

    @Override
    protected void onStart() {
        super.onStart();
        shooterStateReceiver = new ShooterStateReceiver(this, this);
        shooterImageReceiver = new ShooterImageReceiver(this, this);
        posReceiver = new PosReceiver(this,this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        shooterStateReceiver.destroy();
        shooterImageReceiver.destroy();
        shooter.destroy();
    }

    @Override
    public void onShooterState(Shooter.ShooterState from, Shooter.ShooterState to) {
        btnStop.setText(to.toString());
    }

    @Override
    public void onImageChanged(int fromIndex, int toIndex) {
        panoShotView.setCurrentImage(toIndex);
    }

    @Override
    public void onPos(Pos pos, String status) {
        Log.e("XXX", "POS " + pos);

        float x = pos.getX() % 360;
        float y = (pos.getY()+90) % 180;
        // TODO really +90?
        // TODO y must [-90..+90]

        panoShotView.setCamPosition(Pos.of(x,y));
    }


    public void onBtnStart(View view){
        shooter.start();
    }
    public void onBtnStop(View view){
        shooter.stop();
    }
    public void onBtnPause(View view){
        shooter.pauseOrResume();
        if(shooter.isPaused()){
            btnPause.setText("Resume");
        }else{
            btnPause.setText("Pause");
        }
    }

}
