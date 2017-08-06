package de.zebrajaeger.panohead3;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import static android.R.attr.paddingLeft;

public class BorderView extends View {
    private ChoosenBorder choosenBorder = new ChoosenBorder();
    private String text = "Image";
    private int paddingLeft;
    private int paddingTop;
    private int paddingRight;
    private int paddingBottom;
    private int contentWidth;
    private int contentHeight;
    private int lineW;
    private int lineH;
    private Drawable drawable;
    private Paint textPaint = new Paint();
    private float textX;
    private float textY;


    public BorderView(Context context) {
        super(context);
        init(null, 0);
    }

    public BorderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public BorderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        drawable = ContextCompat.getDrawable(getContext(), R.drawable.ziege);
        textPaint.setColor(Color.MAGENTA);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        recalculateViewSetup();
        recalculateImageSetup();
        recalculateTextSetup();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int w = resolveSizeAndState(200, widthMeasureSpec, MEASURED_STATE_TOO_SMALL);
        int h = resolveSizeAndState(500, heightMeasureSpec, MEASURED_STATE_TOO_SMALL);

        setMeasuredDimension(200, 200);
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void recalculateViewSetup() {
        paddingLeft = getPaddingLeft();
        paddingTop = getPaddingTop();
        paddingRight = getPaddingRight();
        paddingBottom = getPaddingBottom();

        contentWidth = getWidth() - paddingLeft - paddingRight;
        contentHeight = getHeight() - paddingTop - paddingBottom;

        lineW = contentWidth / 10;
        lineH = contentHeight / 10;
    }

    private void recalculateImageSetup() {
        drawable.setBounds(paddingLeft, paddingTop, paddingLeft + contentWidth, paddingTop + contentHeight);
    }

    private void recalculateTextSetup(){
        float expectedWidth = contentWidth;
        expectedWidth *= 0.7f;

        // calculate font size
        float measuredWidth = textPaint.measureText(text);
        float scale = expectedWidth / measuredWidth;
        float textSize = textPaint.getTextSize();
        textPaint.setTextSize(textSize * scale);

        // calculate x pos
        Rect textBounds = new Rect();
        textPaint.getTextBounds(text, 0, text.length(), textBounds);

        float realTextWidth = textPaint.measureText(text);
        textX = paddingLeft + (contentWidth/2) - (realTextWidth / 2f);

        // calculate y
        float realTextHeigth = textBounds.height();
        textY = paddingTop + (contentHeight/2) + (realTextHeigth / 2f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawable.draw(canvas);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setColor(Color.BLUE);
        if (choosenBorder.isLeft())
            canvas.drawRect(
                    paddingLeft,
                    paddingTop,
                    paddingLeft + lineW,
                    paddingTop + contentHeight,
                    paint);
        if (choosenBorder.isRight())
            canvas.drawRect(
                    paddingLeft + contentWidth - lineW,
                    paddingTop,
                    paddingLeft + contentWidth,
                    paddingTop + contentHeight,
                    paint);
        if (choosenBorder.isTop())
            canvas.drawRect(
                    paddingLeft,
                    paddingTop,
                    paddingLeft + contentWidth,
                    paddingTop + lineH,
                    paint);
        if (choosenBorder.isBot())
            canvas.drawRect(
                    paddingLeft,
                    paddingTop + contentHeight - lineH,
                    paddingLeft + contentWidth,
                    paddingTop + contentHeight,
                    paint);

        canvas.drawText(text, textX, textY, textPaint);
    }

    public ChoosenBorder getChoosenBorder() {
        return choosenBorder;
    }

    public void setChoosenBorder(ChoosenBorder choosenBorder) {
        this.choosenBorder = choosenBorder;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        //recalculateTextSetup();
    }
}
