package com.dev.bins.remotecontrolview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by bin on 07/12/2016.
 */

public class RemoteControlView extends View {

    private final int center = 0;
    private final int up = 1;
    private final int right = 2;
    private final int down = 3;
    private final int left = 4;
    private int pos = -1;
    private Paint mNormalPaint;
    private Paint mSelectPaint;
    private int width;
    private Path centerPath;
    private Path upPath;
    private Path rightPath;
    private Path downPath;
    private Path leftPath;
    private Region centerRegion;
    private Region upRegion;
    private Region rightRegion;
    private Region downRegion;
    private Region leftRegion;
    private Region global;
    private RectF smallCircle;
    private RectF bigCircle;
    private int bigSweepAngle = 84;
    private int smallSweepAngle = -80;

    public RemoteControlView(Context context) {
        this(context, null);
    }

    public RemoteControlView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RemoteControlView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.RemoteControlView, defStyleAttr, 0);
        int normalColor = typedArray.getColor(R.styleable.RemoteControlView_normal_color, getResources().getColor(R.color.normal_color));
        int selectColor = typedArray.getColor(R.styleable.RemoteControlView_select_color, getResources().getColor(R.color.select_color));
        mNormalPaint = new Paint();
        mNormalPaint.setAntiAlias(true);
        mNormalPaint.setDither(true);
        mNormalPaint.setColor(normalColor);

        mSelectPaint = new Paint();
        mSelectPaint.setAntiAlias(true);
        mSelectPaint.setDither(true);
        mSelectPaint.setColor(selectColor);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = Math.min(w, h);
        global = new Region(0, 0, w, h);

        centerPath = new Path();
        centerRegion = new Region();
        centerPath.addCircle(width / 2, width / 2, width * 0.5f / 2, Path.Direction.CW);
        centerRegion.setPath(centerPath, global);

        upPath = new Path();
        rightPath = new Path();
        downPath = new Path();
        leftPath = new Path();

        upRegion = new Region();
        rightRegion = new Region();
        downRegion = new Region();
        leftRegion = new Region();

        bigCircle = new RectF(0, 0, width, width);
        smallCircle = new RectF(width / 2 - width * 0.5f / 2, width / 2 - width * 0.5f / 2, width / 2 + width * 0.5f / 2, width / 2 + width * 0.5f / 2);

        upPath.addArc(bigCircle, 230, bigSweepAngle);
        upPath.arcTo(smallCircle, 310, smallSweepAngle);
        upPath.close();
        upRegion.setPath(upPath, global);

        rightPath.addArc(bigCircle, 320, bigSweepAngle);
        rightPath.arcTo(smallCircle, 40, smallSweepAngle);
        rightPath.close();
        rightRegion.setPath(rightPath, global);

        downPath.addArc(bigCircle, 50, bigSweepAngle);
        downPath.arcTo(smallCircle, 130, smallSweepAngle);
        downPath.close();
        downRegion.setPath(downPath, global);

        leftPath.addArc(bigCircle, 140, bigSweepAngle);
        leftPath.arcTo(smallCircle, 220, smallSweepAngle);
        leftPath.close();
        leftRegion.setPath(leftPath, global);


    }

    @Override
    protected void onDraw(Canvas canvas) {

        canvas.drawPath(upPath, mNormalPaint);
        canvas.drawPath(rightPath, mNormalPaint);
        canvas.drawPath(downPath, mNormalPaint);
        canvas.drawPath(leftPath, mNormalPaint);
        canvas.drawCircle(width / 2, width / 2, width * 0.2f, mNormalPaint);
        switch (pos) {
            case center:
                canvas.drawCircle(width / 2, width / 2, width * 0.2f, mSelectPaint);
                break;
            case up:
                canvas.drawPath(upPath, mSelectPaint);
                break;
            case right:
                canvas.drawPath(rightPath, mSelectPaint);
                break;
            case down:
                canvas.drawPath(downPath, mSelectPaint);
                break;
            case left:
                canvas.drawPath(leftPath, mSelectPaint);
                break;
            default:
                break;
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                pos = getTouchPos(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                pos = getTouchPos(x, y);
                break;
            case MotionEvent.ACTION_UP:
                pos = getTouchPos(x, y);
                break;
        }
        invalidate();
        return true;
    }

    private int getTouchPos(int x, int y) {
        if (centerRegion.contains(x, y)) {
            return center;
        } else if (upRegion.contains(x, y)) {
            return up;
        } else if (rightRegion.contains(x, y)) {
            return right;
        } else if (downRegion.contains(x, y)) {
            return down;
        } else if (leftRegion.contains(x, y)) {
            return left;
        }
        return -1;
    }

    interface OnClickListener {
        void onCenterClick();

        void onUpClick();

        void onRightClick();

        void onDownClick();

        void onLeftClick();
    }
}
