package com.example.lqy.loadingview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by lqy on 16/1/26.
 */
public class LoadingView extends View {

    public static final int POINT = 0;
    public static final int CIRCLE = 1;

    public static final int MIN_NUM = 6;
    public static final int MAX_NUM = 16;
    public static final int DEFAULT_NUM = 8;

    public static final int MIN_CONNER = 0;
    public static final int MAX_CONNER = 100;

    int mStyle = POINT;

    int mLength;

    int mBubleRadius;

    int mCenterX;
    int mCenterY;

    int mConnerX = 100;
    int mConnerY = 100;

    int mBubleNum = DEFAULT_NUM;

    int mColor = 0xFF888888;

    int mBackColor = 0xFFFF00FF;

    int mTextColor = 0xFF333333;

    int mAlpha = 255;

    int mBackAlpha = 0;

    float mCirleWidth = 5.0f;

    float mOriginWidth;

    Paint mPaint;

    RectF mRectF;

    RectF mCircleRectF;

    double mAngle = 0.0;

    int mInterval = 150;
    Timer mUpdateTimer;
    TimerTask mUpdateTask;

    Path mTextPath;
    String mText = "";

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
//        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//        mLength = (wm.getDefaultDisplay().getWidth() > wm.getDefaultDisplay().getHeight() ? wm.getDefaultDisplay().getHeight() : wm.getDefaultDisplay().getWidth()) / 3;
        mRectF = new RectF();
        mCircleRectF = new RectF();
//        mCenterX = mLength / 2;
//        mCenterY = mLength / 2;
//        mBubleRadius = mLength / mBubleNum / 2;

        mTextPath = new Path();
//        mTextPath.moveTo(mCenterX - mCenterX / 4, mCenterY);
//        mTextPath.lineTo(mCenterX + mCenterX / 4, mCenterY);
//        mTextPath.close();

        //用Timer更改alpha值实现动态效果
        mUpdateTimer = new Timer();
        mUpdateTask = new TimerTask() {
            @Override
            public void run() {
                mAlpha -= 255 / mBubleNum;
                if (mAlpha < 0) {
                    mAlpha = 255;
                }
                LoadingView.this.postInvalidate();
            }
        };
        mUpdateTimer.schedule(mUpdateTask, 100, mInterval);
    }

//    public LoadingView(Context context) {
//        super(context);
//        mPaint = new Paint();
//        mPaint.setAntiAlias(true);
//        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//        mLength = (wm.getDefaultDisplay().getWidth() > wm.getDefaultDisplay().getHeight() ? wm.getDefaultDisplay().getHeight() : wm.getDefaultDisplay().getWidth()) / 3;
//        mRectF = new RectF(0, 0, mLength, mLength);
//        mCenterX = mLength / 2;
//        mCenterY = mLength / 2;
//        mBubleRadius = mLength / mBubleNum / 2;
//
//        mTextPath = new Path();
//        mTextPath.moveTo(mCenterX - mCenterX / 4, mCenterY);
//        mTextPath.lineTo(mCenterX + mCenterX / 4, mCenterY);
//        mTextPath.close();
//
//        //用Timer更改alpha值实现动态效果
//        mUpdateTimer = new Timer();
//        mUpdateTask = new TimerTask() {
//            @Override
//            public void run() {
//                mAlpha -= 255 / mBubleNum;
//                if (mAlpha < 0) {
//                    mAlpha = 255;
//                }
//                LoadingView.this.postInvalidate();
//            }
//        };
//        mUpdateTimer.schedule(mUpdateTask, 100, mInterval);
//    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //draw background
        mOriginWidth = mPaint.getStrokeWidth();
        mPaint.setColor(mBackColor);
        mPaint.setAlpha(mBackAlpha);
        canvas.drawRoundRect(mRectF, mConnerX, mConnerY, mPaint);
        //draw content
        mPaint.setColor(mColor);
        int alpha = mAlpha;
        if (mStyle == 1) {
            //circle
            mPaint.setStrokeWidth(mCirleWidth);
            float startAngle = 0.0f;
            float sweepAngle = 360.0f/255.0f;
            mPaint.setStyle(Paint.Style.STROKE);
            for (int i = 0; i < 360; ++i) {
                mPaint.setAlpha(alpha);
                canvas.drawArc(mCircleRectF, startAngle, sweepAngle, false, mPaint);
                startAngle += sweepAngle;
                alpha -= 1;
            }
        } else {
            //point
            for (int i = 0; i < mBubleNum; ++i) {
                mPaint.setAlpha(alpha);
                canvas.drawCircle((float) (mCenterX + mCenterX / 2 * Math.sin(mAngle * Math.PI / 180.0)), (float) (mCenterY + mCenterY / 2 * Math.cos(mAngle * Math.PI / 180.0)), mBubleRadius, mPaint);
                mAngle += 360.0 / mBubleNum;
                alpha -= 255 / mBubleNum;
            }
        }
        mPaint.setStrokeWidth(mOriginWidth);
        mPaint.setStyle(Paint.Style.FILL);
        //draw text
        mPaint.setAlpha(255);
        mPaint.setColor(mTextColor);
        mPaint.setTextSize(15f);
        canvas.drawTextOnPath(mText, mTextPath, 0, 0, mPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        mLength = width > height ? height : width;

        mRectF.bottom = mLength;
        mRectF.right = mLength;

        mCircleRectF.top = mLength / 4;
        mCircleRectF.left = mLength / 4;
        mCircleRectF.bottom = mLength / 4 * 3;
        mCircleRectF.right = mLength / 4 * 3;

        mCenterX = mLength / 2;
        mCenterY = mLength / 2;
        mBubleRadius = mLength / mBubleNum / 2;
        mTextPath.moveTo(mCenterX - mCenterX / 4, mCenterY);
        mTextPath.lineTo(mCenterX + mCenterX / 4, mCenterY);
        mTextPath.close();

        setMeasuredDimension(mLength, mLength);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    public int getmBubleNum() {
        return mBubleNum;
    }

    public void setmBubleNum(int mBubleNum) {
        if (mBubleNum < MIN_NUM) {
            this.mBubleNum = MIN_NUM;
        } else if (mBubleNum > MAX_NUM) {
            this.mBubleNum = MAX_NUM;
        } else {
            this.mBubleNum = mBubleNum;
        }
    }

    public int getmBackColor() {
        return mBackColor;
    }

    public void setmBackColor(int mBackColor) {
        this.mBackColor = mBackColor;
    }

    public int getmColor() {
        return mColor;
    }

    public void setmColor(int mColor) {
        this.mColor = mColor;
    }

    public int getmBackAlpha() {
        return mBackAlpha;
    }

    public void setmBackAlpha(int mBackAlpha) {
        this.mBackAlpha = mBackAlpha;
    }

    public int getConner() {
        return mConnerX;
    }

    public void setConner(int mConner) {
        if (mConner < MIN_CONNER) {
            mConner = MIN_CONNER;
        } else if (mConner > MAX_CONNER) {
            mConner = MAX_CONNER;
        }
        this.mConnerX = mConner;
        this.mCenterY = mConner;
    }

    public int getmTextColor() {
        return mTextColor;
    }

    public void setmTextColor(int mTextColor) {
        this.mTextColor = mTextColor;
    }

    public String getmText() {
        return mText;
    }

    public void setmText(String mText) {
        this.mText = mText;
    }

    public int getmStyle() {
        return mStyle;
    }

    public void setmStyle(int mStyle) {
        this.mStyle = mStyle;
    }

    public void setCircleWidth(float width){
        mCirleWidth = width;
    }

    public float getCircleWidth(){
        return  mCirleWidth;
    }
}
