package de.zebrajaeger.panohead3;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import de.zebrajaeger.grblconnector.PanoHead;
import de.zebrajaeger.grblconnector.grbl.move.Pos;
import de.zebrajaeger.grblconnector.ui.JoggingGestureListener;
import de.zebrajaeger.grblconnector.PosReceiver;

public class JoggingActivity extends AppCompatActivity implements GestureDetector.OnDoubleTapListener, PosReceiver.Listener {

    public static final String RESULT_BORDER = "border";
    private GestureDetectorCompat detector;
    private PosReceiver posReceiver;
    private TextView grblStatusText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();

        setContentView(R.layout.activity_jogging);

        grblStatusText = (TextView) findViewById(R.id.text_status);

        JoggingGestureListener joggingGestureListener = new JoggingGestureListener();
        detector = new GestureDetectorCompat(this, joggingGestureListener);
        detector.setIsLongpressEnabled(false);
        detector.setOnDoubleTapListener(this);

        PanoHead.I.setMoveable(joggingGestureListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        posReceiver = new PosReceiver(this, this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        posReceiver.destroy();
        PanoHead.I.setMoveable(null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (detector != null) {
            this.detector.onTouchEvent(event);
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent motionEvent) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_edge);
        dialog.setTitle("Title...");
        dialog.show();
        Button cancel = dialog.findViewById(R.id.button_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        View.OnClickListener buttonClicklistener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button b = (Button) view;
                Intent data = new Intent();
                CharSequence text = b.getText().toString().toLowerCase();
                if ("top-left".equals(text)) {
                    data.putExtra(RESULT_BORDER, Border.TOP_LEFT);
                } else if ("top".equals(text)) {
                    data.putExtra(RESULT_BORDER, Border.TOP);
                } else if ("top-right".equals(text)) {
                    data.putExtra(RESULT_BORDER, Border.TOP_RIGHT);
                } else if ("left".equals(text)) {
                    data.putExtra(RESULT_BORDER, Border.LEFT);
                } else if ("right".equals(text)) {
                    data.putExtra(RESULT_BORDER, Border.RIGHT);
                } else if ("bot-left".equals(text)) {
                    data.putExtra(RESULT_BORDER, Border.BOT_LEFT);
                } else if ("bot".equals(text)) {
                    data.putExtra(RESULT_BORDER, Border.BOT);
                } else if ("bot-right".equals(text)) {
                    data.putExtra(RESULT_BORDER, Border.BOT_RIGHT);
                }
                setResult(RESULT_OK, data);
                finish();
            }
        };

        dialog.findViewById(R.id.button_tl).setOnClickListener(buttonClicklistener);
        dialog.findViewById(R.id.button_t).setOnClickListener(buttonClicklistener);
        dialog.findViewById(R.id.button_tr).setOnClickListener(buttonClicklistener);
        dialog.findViewById(R.id.button_l).setOnClickListener(buttonClicklistener);
        dialog.findViewById(R.id.button_r).setOnClickListener(buttonClicklistener);
        dialog.findViewById(R.id.button_bl).setOnClickListener(buttonClicklistener);
        dialog.findViewById(R.id.button_b).setOnClickListener(buttonClicklistener);
        dialog.findViewById(R.id.button_br).setOnClickListener(buttonClicklistener);
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onPos(Pos pos, String status) {
        grblStatusText.setText(pos + "/" + status);
    }
}
