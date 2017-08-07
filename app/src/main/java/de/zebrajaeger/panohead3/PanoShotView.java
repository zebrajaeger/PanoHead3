package de.zebrajaeger.panohead3;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import de.zebrajaeger.grblconnector.grbl.move.Pos;
import de.zebrajaeger.panohead3.calc.CalculatorData;
import de.zebrajaeger.panohead3.calc.FovO1D;
import de.zebrajaeger.panohead3.calc.FovO2D;
import de.zebrajaeger.panohead3.shot.ShooterScript;
import de.zebrajaeger.panohead3.shot.ShotPosition;
import de.zebrajaeger.panohead3.util.Size2D;


/**
 * @author lars on 21.10.2016.
 */
public class PanoShotView extends View {
    public static final Paint WORLD_RECT_PAINT = new Paint();
    public static final Paint WORLD_BORDER_PAINT = new Paint();
    public static final Paint CAM_CROSSHAIRS_PAINT = new Paint();
    public static final Paint IMAGE_SHOOTED_FILL_PAINT = new Paint();
    public static final Paint IMAGE_SHOOTED_BORDER_PAINT = new Paint();
    public static final Paint IMAGE_SHOOTED_LABEL_PAINT = new Paint();
    public static final Paint IMAGE_CURRENT_FILL_PAINT = new Paint();
    public static final Paint IMAGE_CURRENT_BORDER_PAINT = new Paint();
    public static final Paint IMAGE_CURRENT_LABEL_PAINT = new Paint();
    public static final Paint IMAGE_WAITING_FILL_PAINT = new Paint();
    public static final Paint IMAGE_WAITING_BORDER_PAINT = new Paint();
    public static final Paint IMAGE_WAITING_LABEL_PAINT = new Paint();
    private int w;
    private int h;
    private int currentImage = 0;
    private Paint imgPaint;
    private ShooterScript script;
    private Pos camPos = Pos.of(180, 90);
    private RectF worldRect;

    public PanoShotView(Context context) {
        super(context);
        init(context);
    }

    public PanoShotView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PanoShotView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        imgPaint = new Paint();
        imgPaint.setColor(Color.BLACK);
        imgPaint.setAntiAlias(true);
        imgPaint.setStrokeWidth(1);
        imgPaint.setStyle(Paint.Style.STROKE);

        // World
        WORLD_RECT_PAINT.setStyle(Paint.Style.FILL);
        //WORLD_RECT_PAINT.setStrokeWidth(1);
        //WORLD_RECT_PAINT.setAntiAlias(true);
        //WORLD_RECT_PAINT.setColor(Color.GRAY);
        WORLD_RECT_PAINT.setColor(Color.GRAY);
        WORLD_RECT_PAINT.setAlpha(150);

        WORLD_BORDER_PAINT.setStyle(Paint.Style.STROKE);
        WORLD_BORDER_PAINT.setStrokeWidth(2);
        //WORLD_BORDER_PAINT.setAntiAlias(true);
        WORLD_BORDER_PAINT.setColor(Color.BLACK);
        //WORLD_BORDER_PAINT.setAlpha(150);

        // Crosshair
        CAM_CROSSHAIRS_PAINT.setStyle(Paint.Style.STROKE);
        CAM_CROSSHAIRS_PAINT.setStrokeWidth(3);
        CAM_CROSSHAIRS_PAINT.setColor(Color.RED);
        CAM_CROSSHAIRS_PAINT.setAlpha(150);

        // Image - Shooted
        IMAGE_SHOOTED_BORDER_PAINT.setStyle(Paint.Style.STROKE);
        IMAGE_SHOOTED_BORDER_PAINT.setColor(Color.BLACK);
        IMAGE_SHOOTED_BORDER_PAINT.setAlpha(150);

        IMAGE_SHOOTED_FILL_PAINT.setStyle(Paint.Style.FILL);
        IMAGE_SHOOTED_FILL_PAINT.setColor(Color.GREEN);
        IMAGE_SHOOTED_FILL_PAINT.setAlpha(50);

        IMAGE_SHOOTED_LABEL_PAINT.setStyle(Paint.Style.FILL);
        IMAGE_SHOOTED_LABEL_PAINT.setColor(Color.BLACK);
        IMAGE_SHOOTED_LABEL_PAINT.setAntiAlias(true);
        IMAGE_SHOOTED_LABEL_PAINT.setTextSize(20f);
        IMAGE_SHOOTED_LABEL_PAINT.setTextAlign(Paint.Align.CENTER);

        // Image - Current
        IMAGE_CURRENT_BORDER_PAINT.setStyle(Paint.Style.STROKE);
        IMAGE_CURRENT_BORDER_PAINT.setStrokeWidth(2);
        IMAGE_CURRENT_BORDER_PAINT.setColor(Color.BLACK);
        //IMAGE_CURRENT_BORDER_PAINT.setAlpha(150);

        IMAGE_CURRENT_FILL_PAINT.setStyle(Paint.Style.FILL);
        IMAGE_CURRENT_FILL_PAINT.setColor(Color.YELLOW);
        //IMAGE_CURRENT_FILL_PAINT.setAlpha(50);

        IMAGE_CURRENT_LABEL_PAINT.setStyle(Paint.Style.FILL);
        IMAGE_CURRENT_LABEL_PAINT.setColor(Color.BLUE);
        IMAGE_CURRENT_LABEL_PAINT.setAntiAlias(true);
        IMAGE_CURRENT_LABEL_PAINT.setTextSize(20f);
        IMAGE_CURRENT_LABEL_PAINT.setTypeface(Typeface.DEFAULT_BOLD);
        IMAGE_CURRENT_LABEL_PAINT.setTextAlign(Paint.Align.CENTER);

        // Image - Waiting
        IMAGE_WAITING_BORDER_PAINT.setStyle(Paint.Style.STROKE);
        IMAGE_WAITING_BORDER_PAINT.setColor(Color.BLACK);
        IMAGE_WAITING_BORDER_PAINT.setAlpha(150);

        IMAGE_WAITING_FILL_PAINT.setStyle(Paint.Style.FILL);
        IMAGE_WAITING_FILL_PAINT.setColor(Color.BLUE);
        IMAGE_WAITING_FILL_PAINT.setAlpha(50);

        IMAGE_WAITING_LABEL_PAINT.setStyle(Paint.Style.FILL);
        IMAGE_WAITING_LABEL_PAINT.setColor(Color.BLUE);
        IMAGE_WAITING_LABEL_PAINT.setAntiAlias(true);
        IMAGE_WAITING_LABEL_PAINT.setTextSize(20f);
        IMAGE_WAITING_LABEL_PAINT.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
/*        int minw = getPaddingLeft() + getPaddingRight() + getSuggestedMinimumWidth();
        int w = resolveSizeAndState(minw, widthMeasureSpec, 1);

        // Whatever the width ends up being, ask for a height that would let the pie
        // get as big as it can
        int minh = MeasureSpec.getSize(w) - (int)100 + getPaddingBottom() + getPaddingTop();
        int h = resolveSizeAndState(MeasureSpec.getSize(w) - (int)100, heightMeasureSpec, 0);
*/

        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec - 65);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.w = w;
        this.h = h;

        worldRect = createWorldRect();
    }

    private RectF createWorldRect() {
        float borderXRel = 0.1f;
        float borderYRel = 0.1f;

        float myW = w * (1f - borderXRel);
        float myH = h * (1f - borderYRel);
        /*if ((myW / myH) > 0.5f) {
            myH = myW / 2f;
        } else {
            myW = myH * 2f;
        }*/

        float offW = (w - myW) / 2f;
        float offH = (h - myH) / 2f;

        return new RectF(offW, offH, w - offW, h - offH);
    }

    private void drawImage(Canvas canvas, String label, RectF imageRect, ImageType imageType) {
        Paint borderPaint;
        Paint fillPaint;
        Paint labelPaint;
        switch (imageType) {
            case SHOOTED:
                borderPaint = IMAGE_SHOOTED_BORDER_PAINT;
                fillPaint = IMAGE_SHOOTED_FILL_PAINT;
                labelPaint = IMAGE_SHOOTED_LABEL_PAINT;
                break;
            case CURRENT:
                borderPaint = IMAGE_CURRENT_BORDER_PAINT;
                fillPaint = IMAGE_CURRENT_FILL_PAINT;
                labelPaint = IMAGE_CURRENT_LABEL_PAINT;
                break;
            case WAITING:
                borderPaint = IMAGE_WAITING_BORDER_PAINT;
                fillPaint = IMAGE_WAITING_FILL_PAINT;
                labelPaint = IMAGE_WAITING_LABEL_PAINT;
                break;
            default: {
                throw new IllegalStateException("Unknown Image Type for drawing");
            }
        }

        canvas.drawRect(imageRect, fillPaint);
        canvas.drawRect(imageRect, borderPaint);

        // draw text
        Rect textBounds = new Rect();
        IMAGE_SHOOTED_LABEL_PAINT.getTextBounds(label, 0, label.length(), textBounds);
        canvas.drawText(label, imageRect.centerX(), imageRect.centerY() - textBounds.exactCenterY(), labelPaint);
    }

    private void drawWorld(Canvas canvas, RectF worldRect) {
        // outside of border
        canvas.drawRect(worldRect, WORLD_RECT_PAINT);
        canvas.drawRect(0, 0, worldRect.left, h, WORLD_RECT_PAINT);
        canvas.drawRect(worldRect.right, 0, w, h, WORLD_RECT_PAINT);
        canvas.drawRect(worldRect.left, 0, worldRect.right, worldRect.top, WORLD_RECT_PAINT);
        canvas.drawRect(worldRect.left, worldRect.bottom, worldRect.right, h, WORLD_RECT_PAINT);

        // border
        canvas.drawRect(worldRect, WORLD_BORDER_PAINT);
    }

    private Size2D createImageSize(RectF worldRect, CalculatorData calculationData) {
        FovO2D camFov = calculationData.getCamFov();
        FovO1D relX = camFov.getX();
        FovO1D relY = camFov.getY();

        float iX;
        float iY;
        if (relX == null && relY == null) {
            // we should prevent this
            iX = worldRect.width() / 10;
            iY = worldRect.width() / 10;
        } else if (relX != null && relY != null) {
            iX = ((relX.getSweepAngle() / 360f) * worldRect.width());
            iY = ((relY.getSweepAngle() / 180f) * worldRect.height());
        } else {
            if (relX != null) {
                iX = ((relX.getSweepAngle() / 360f) * worldRect.width());
                iY = ((relX.getSweepAngle() / 180f) * worldRect.height());
            } else {
                iX = ((relY.getSweepAngle() / 360f) * worldRect.width());
                iY = ((relY.getSweepAngle() / 180f) * worldRect.height());
            }
        }

        return new Size2D(iX, iY);
    }

    private RectF createImageRect(float x, float y, Size2D imageSize) {
        return new RectF(x - (imageSize.getW() / 2f), y - (imageSize.getH() / 2f), x + (imageSize.getW() / 2f), y + (imageSize.getH() / 2f));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawWorld(canvas, worldRect);

        if (script != null) {
            Size2D imageSize = createImageSize(worldRect, script.getCalculationData());

            int index = 0;
            RectF currentRect = null;
            for (ShotPosition s : script.getShots()) {
                float relX = (s.getX() + 180 - script.getCalculationData().getXOffsetOr(0f)) / 360f;
                // add 90deg for center pos
                float relY = (s.getY() + 90 - script.getCalculationData().getYOffsetOr(0f)) / 180f;
                float iX = (relX * worldRect.width()) + worldRect.left;
                float iY = (relY * worldRect.height()) + worldRect.top;
                RectF imageRect = createImageRect(iX, iY, imageSize);

                if (index < currentImage) {
                    drawImage(canvas, Integer.toString(index + 1), imageRect, ImageType.SHOOTED);
                }
                if (index > currentImage) {
                    drawImage(canvas, Integer.toString(index + 1), imageRect, ImageType.WAITING);
                } else {
                    currentRect = imageRect;
                }
                ++index;
            }

            if (currentRect != null) {
                drawImage(canvas, Integer.toString(currentImage + 1), currentRect, ImageType.CURRENT);
            }
        }

        if (camPos != null) {
            float x = camPos.getX();
            float y = camPos.getY() +90f;

            float relX = x / 360f;
            float relY = y / 180f;
            float iX = (relX * worldRect.width()) + worldRect.left;
            float iY = (relY * worldRect.height()) + worldRect.top;

            canvas.drawLine(0, iY, w, iY, CAM_CROSSHAIRS_PAINT);
            canvas.drawLine(iX, 0, iX, h, CAM_CROSSHAIRS_PAINT);
        }
    }

    public void setScript(ShooterScript script) {
        this.script = script;
    }

    public void setCurrentImage(int currentImage) {
        this.currentImage = currentImage;
        postInvalidate();
    }

    public void setCamPosition(Pos camPos) {
        this.camPos = camPos;
        postInvalidate();
    }

    enum ImageType {
        SHOOTED, CURRENT, WAITING
    }
}
