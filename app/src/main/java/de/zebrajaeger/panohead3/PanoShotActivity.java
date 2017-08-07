package de.zebrajaeger.panohead3;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import de.zebrajaeger.grblconnector.GrblStatusReceiver;
import de.zebrajaeger.grblconnector.ShotConfig;
import de.zebrajaeger.grblconnector.grbl.Grbl;
import de.zebrajaeger.grblconnector.grbl.move.Pos;
import de.zebrajaeger.panohead3.shot.Shooter;
import de.zebrajaeger.panohead3.shot.ShooterImageReceiver;
import de.zebrajaeger.panohead3.shot.ShooterScript;
import de.zebrajaeger.panohead3.shot.ShooterStateReceiver;

public class PanoShotActivity extends AppCompatActivity implements ShooterStateReceiver.Listener, ShooterImageReceiver.Listener, GrblStatusReceiver.Listener {


    public static final String ACTIVITY_PARAM_SCRIPT = "script";
    public static final String ACTIVITY_PARAM_SHOT_CONFIG = "shotConfig";
    private Shooter shooter;
    private ShooterStateReceiver shooterStateReceiver;
    private ShooterImageReceiver shooterImageReceiver;
    private GrblStatusReceiver grblStatusReceiver;
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
        grblStatusReceiver = new GrblStatusReceiver(this,this);
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
    public void onPos(Pos pos, Grbl.GrblStatus status) {
        float x;
        if(script.getCalculationData().hasXOffset()){
            x = pos.getX() +180 - script.getCalculationData().getOffset().getX();
        }else{
            x = pos.getX() % 360;
        }

        float y;
        if(script.getCalculationData().hasYOffset()){
            y = pos.getY() - script.getCalculationData().getOffset().getY();
        }else{
            y = (pos.getY()+90) % 180;
        }

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
            btnPause.setText("Pause");
        }else{
            btnPause.setText("Resume");
        }
    }

}
