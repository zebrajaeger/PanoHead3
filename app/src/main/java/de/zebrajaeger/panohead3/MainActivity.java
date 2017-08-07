package de.zebrajaeger.panohead3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import de.zebrajaeger.grblconnector.PanoHead;
import de.zebrajaeger.grblconnector.ShotConfig;
import de.zebrajaeger.grblconnector.bt.BT;
import de.zebrajaeger.grblconnector.util.Translator;
import de.zebrajaeger.panohead3.calc.Bounds2D;
import de.zebrajaeger.panohead3.calc.CalculatorData;
import de.zebrajaeger.panohead3.calc.Fov2D;
import de.zebrajaeger.panohead3.calc.Overlap;
import de.zebrajaeger.panohead3.calc.Pos2D;
import de.zebrajaeger.panohead3.calc.SimpleCalculator;
import de.zebrajaeger.panohead3.shot.ShooterScript;
import de.zebrajaeger.panohead3.util.AppData;
import de.zebrajaeger.panohead3.util.Storage;

public class MainActivity extends AppCompatActivity {

    public static final int SET_IMAGE_BOUNDS_REQUEST_CODE = 91;
    public static final int SET_PANO_BOUNDS_REQUEST_CODE = 92;
    public static final int SET_SHOT_REQUEST_CODE = 93;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        PanoHead.I.init(this);

        float gearRatio = (float) (19.0d + (38.0d / 187.0d));
        float grblConfig100 = 32; // steps per mm
        float stepsPerRevolution = 200;
        float microSteppingSteps = 32;

        float mmPerDegree = (grblConfig100 / microSteppingSteps);
        float translation = mmPerDegree * stepsPerRevolution * gearRatio;

        float degTranslation = translation / 360;
        PanoHead.I.setTranslator(new Translator(degTranslation));
        btAutoconnect();

        // IMAGE VIEW
        BorderView imageBounds = (BorderView) findViewById(R.id.picture_border);
        imageBounds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, JoggingActivity.class);
                startActivityForResult(intent, SET_IMAGE_BOUNDS_REQUEST_CODE);
            }
        });

        // PANO VIEW
        BorderView panoBounds = (BorderView) findViewById(R.id.pano_border);
        panoBounds.setText("Pano");
        panoBounds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, JoggingActivity.class);
                startActivityForResult(intent, SET_PANO_BOUNDS_REQUEST_CODE);
            }
        });

        Button button_shot = (Button) findViewById(R.id.button_shot);
        button_shot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fov2D camFov = new Fov2D(30f, 50f); //TODO
                Overlap overlap = new Overlap(0.25f, 0.25f); // TODO
                Bounds2D panoBounds = new Bounds2D(50f, 249f, -60f, 60f); // TODO
                Pos2D offset = new Pos2D(panoBounds.getX().getCenter(), 20f);
                CalculatorData data = new CalculatorData(camFov, overlap, panoBounds, offset); // TODO
                ShotConfig cfg = new ShotConfig(1000, 500, 500, 500);

                // calc
                SimpleCalculator simpleCalculator = new SimpleCalculator();
                ShooterScript shooterScript = simpleCalculator.createScript(data);

                Intent intent = new Intent(MainActivity.this, PanoShotActivity.class);
                intent.putExtra(PanoShotActivity.ACTIVITY_PARAM_SCRIPT, shooterScript);
                intent.putExtra(PanoShotActivity.ACTIVITY_PARAM_SHOT_CONFIG, cfg);
                startActivityForResult(intent, SET_SHOT_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SET_IMAGE_BOUNDS_REQUEST_CODE && resultCode == RESULT_OK) {
            Border b = (Border) data.getSerializableExtra(JoggingActivity.RESULT_BORDER);

        } else if (requestCode == SET_PANO_BOUNDS_REQUEST_CODE && resultCode == RESULT_OK) {
            Border b = (Border) data.getSerializableExtra(JoggingActivity.RESULT_BORDER);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            String btAdapter = Storage.I.getAppData().getBtAdapter();
            final BT bt = PanoHead.I.bt;
            bt.showChooseDialog(this, btAdapter, new BT.AdapterSelectionResult() {
                @Override
                public void onBTAdapterSelected(String name) {
                    Storage.I.getAppData(getApplicationContext()).setBtAdapter(name);
                    Storage.I.save(getApplicationContext());
                    bt.connectTo(name);
                }
            });
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean btAutoconnect() {
        final BT bt = PanoHead.I.bt;
        AppData appData = Storage.I.getAppData(getApplicationContext());
        bt.refreshDeviceList();
        String btAdapter = appData.getBtAdapter();
        if (bt.getSortedDeviceNames().contains(btAdapter)) {
            boolean result = bt.connectTo(btAdapter);
            return result;
        }
        return false;
    }
}
