package com.scholarlyapps.pathlingo.ui.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.scholarlyapps.pathlingo.R;

import java.util.ArrayList;
import java.util.List;

public class KanaDrawingView extends View {

    private static class Stroke {
        final Path path;
        final boolean eraser;

        Stroke(Path path, boolean eraser) {
            this.path = path;
            this.eraser = eraser;
        }
    }

    private final List<Stroke> strokes = new ArrayList<>();
    private final List<Stroke> undone = new ArrayList<>();
    private final Paint penPaint = new Paint();
    private final Paint eraserPaint = new Paint();
    private final Paint hintPaint = new Paint();

    private Stroke currentStroke;
    private boolean eraserMode = false;
    private String hint = "";
    private float lastX;
    private float lastY;

    public KanaDrawingView(Context context) {
        super(context);
        init();
    }

    public KanaDrawingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setLayerType(LAYER_TYPE_HARDWARE, null);

        penPaint.setAntiAlias(true);
        penPaint.setColor(Color.BLACK);
        penPaint.setStyle(Paint.Style.STROKE);
        penPaint.setStrokeJoin(Paint.Join.ROUND);
        penPaint.setStrokeCap(Paint.Cap.ROUND);
        penPaint.setStrokeWidth(dp(6));

        eraserPaint.set(penPaint);
        eraserPaint.setStrokeWidth(dp(24));
        eraserPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        hintPaint.setAntiAlias(true);
        hintPaint.setColor(ContextCompat.getColor(getContext(), R.color.color_theme_extra_light));
        hintPaint.setTextAlign(Paint.Align.CENTER);
    }

    public void setHint(String hint) {
        this.hint = hint != null ? hint : "";
        invalidate();
    }

    public void setEraserMode(boolean enabled) {
        eraserMode = enabled;
    }

    public boolean isEraserMode() {
        return eraserMode;
    }

    public void undo() {
        if (strokes.isEmpty()) return;
        undone.add(strokes.remove(strokes.size() - 1));
        invalidate();
    }

    public void redo() {
        if (undone.isEmpty()) return;
        strokes.add(undone.remove(undone.size() - 1));
        invalidate();
    }

    public void clear() {
        strokes.clear();
        undone.clear();
        invalidate();
    }

    public boolean hasDrawing() {
        return !strokes.isEmpty();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (!hint.isEmpty()) {
            hintPaint.setTextSize(getHeight() * (hint.length() > 1 ? 0.45f : 0.7f));
            float centerY = getHeight() / 2f - (hintPaint.descent() + hintPaint.ascent()) / 2f;
            canvas.drawText(hint, getWidth() / 2f, centerY, hintPaint);
        }

        int layer = canvas.saveLayer(0, 0, getWidth(), getHeight(), null);
        for (Stroke stroke : strokes) {
            canvas.drawPath(stroke.path, stroke.eraser ? eraserPaint : penPaint);
        }
        canvas.restoreToCount(layer);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);
                currentStroke = new Stroke(new Path(), eraserMode);
                currentStroke.path.moveTo(x, y);
                currentStroke.path.lineTo(x + 0.1f, y + 0.1f);
                strokes.add(currentStroke);
                undone.clear();
                lastX = x;
                lastY = y;
                invalidate();
                return true;

            case MotionEvent.ACTION_MOVE:
                if (currentStroke == null) return false;
                currentStroke.path.quadTo(lastX, lastY, (x + lastX) / 2f, (y + lastY) / 2f);
                lastX = x;
                lastY = y;
                invalidate();
                return true;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                getParent().requestDisallowInterceptTouchEvent(false);
                currentStroke = null;
                return true;
        }

        return super.onTouchEvent(event);
    }

    private float dp(int value) {
        return value * getResources().getDisplayMetrics().density;
    }
}
