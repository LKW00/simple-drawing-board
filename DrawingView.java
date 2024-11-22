package com.cookandroid.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class DrawingView extends View {

    private Paint paint;
    private Path path;
    private Bitmap bitmap;
    private Canvas canvas;
    private Paint bitmapPaint;
    private Paint eraserPaint;
    private boolean isEraser = false;

    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(12);

        path = new Path();
        bitmapPaint = new Paint(Paint.DITHER_FLAG);

        eraserPaint = new Paint();
        eraserPaint.setAntiAlias(true);
        eraserPaint.setDither(true);
        eraserPaint.setColor(Color.WHITE);
        eraserPaint.setStyle(Paint.Style.STROKE);
        eraserPaint.setStrokeJoin(Paint.Join.ROUND);
        eraserPaint.setStrokeCap(Paint.Cap.ROUND);
        eraserPaint.setStrokeWidth(12);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(bitmap, 0, 0, bitmapPaint);
        canvas.drawPath(path, isEraser ? eraserPaint : paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path.moveTo(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                path.lineTo(x, y);
                break;
            case MotionEvent.ACTION_UP:
                canvas.drawPath(path, isEraser ? eraserPaint : paint);
                path.reset();
                break;
        }
        invalidate();
        return true;
    }

    public void clear() {
        bitmap.eraseColor(Color.WHITE);
        invalidate();
    }

    public void setPen() {
        isEraser = false;
    }

    public void setEraser() {
        isEraser = true;
    }

    public void setColor(String color) {
        paint.setColor(Color.parseColor(color));
    }

    public void setThickness(float thickness) {
        paint.setStrokeWidth(thickness);
        eraserPaint.setStrokeWidth(thickness);
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void loadBitmap(Bitmap bitmap) {
        this.bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        canvas = new Canvas(this.bitmap);
        invalidate();
    }
}
