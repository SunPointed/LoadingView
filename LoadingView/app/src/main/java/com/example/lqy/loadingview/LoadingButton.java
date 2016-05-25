package com.example.lqy.loadingview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by lqy on 16/5/23.
 */
public class LoadingButton extends View implements View.OnClickListener{

    public static final String TAG = "LoadingButton";

    public static final String UP = "UP";
    public static final String DOWN = "DOWN";
    public static final String LEFT = "LEFT";
    public static final String RIGHT = "RIGHT";
    public static final String ZOOM_IN = "ZOOM_IN";
    public static final String ZOOM_OUT = "ZOOM_OUT";

    private String mText;
    private int mTextSize;
    private int mTextSizeTemp;
    private int mTextBaseLine;

    private int mWidth;
    private int mHeight;
    private int mProgressWidth = 0;
    private int mWaitWidth;

    private int mTop = 0;
    private int mLeft = 0;

    private RectF mBackRect;
    private RectF mProgressRect;

    private int mTextColor;
    private int mButtonColor;
    private int mBackColor;
    private int mProgressColor;

    private int mCorner;

    private String mDisappearType;
    private int mDisappearTime;
    private int mDisappearCount;
    private Timer mDisappearTimer;

    private Timer mLoadingTimer;

    private Paint mPaint;
    private int mAlpha;

    private boolean mIsLoading;
    private boolean mIsProgress;
    private boolean mIsDisappear;

    private int mTotalChangeTime;
    private int mTimeSchedule;

    private ClickListener mListener;

    public LoadingButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LoadingButton);
        mTextColor = typedArray.getColor(R.styleable.LoadingButton_text_color, 0xFFFFFFFF);
        mBackColor = typedArray.getColor(R.styleable.LoadingButton_backgound_color, 0xFFF5F5F5);
        mProgressColor = typedArray.getColor(R.styleable.LoadingButton_progress_color, 0xFF333333);
        mCorner = typedArray.getDimensionPixelSize(R.styleable.LoadingButton_corner, 30);
        mDisappearType = typedArray.getString(R.styleable.LoadingButton_disappear_type);
        mButtonColor = typedArray.getColor(R.styleable.LoadingButton_button_color, 0xFFB6B6B6);
        mTotalChangeTime = typedArray.getDimensionPixelSize(R.styleable.LoadingButton_total_change_time, 200);
        mTimeSchedule = typedArray.getDimensionPixelSize(R.styleable.LoadingButton_time_schedule, 20);
        mDisappearCount = mTotalChangeTime/mTimeSchedule;
        mText = typedArray.getString(R.styleable.LoadingButton_text);
        mTextSize = typedArray.getDimensionPixelSize(R.styleable.LoadingButton_text_size, 60);
        mTextSizeTemp = mTextSize;
        mIsProgress = typedArray.getBoolean(R.styleable.LoadingButton_is_progress, false);
        typedArray.recycle();

        if(mText == null){
            mText = "submit";
        }

        if(mDisappearType == null){
            mDisappearType = UP;
        }

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBackRect = new RectF();
        mProgressRect = new RectF();
        mAlpha = 255;
        mIsLoading = false;
        mIsDisappear = false;
        this.setOnClickListener(this);
    }

    public LoadingButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingButton(Context context) {
        this(context, null);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if(mIsDisappear){
            drawDisappear(canvas);
        }

        mPaint.setColor(mButtonColor);
        mBackRect.set(mLeft, mTop, mLeft + mWidth, mTop + mHeight);
        canvas.drawRoundRect(mBackRect, mCorner, mCorner, mPaint);

        if(mIsLoading){
            drawLoading(canvas);
        } else {
            drawText(canvas);
        }
    }

    private void drawDisappear(Canvas canvas){
        if(ZOOM_IN.equals(mDisappearType)){
            mTextSize -= 5;
        } else if(ZOOM_OUT.equals(mDisappearType)){
            mTextSize += 20;
            mTextBaseLine += 5;
        } else if(UP.equals(mDisappearType)){
            mTop -= mHeight/mDisappearCount;
        } else if(DOWN.equals(mDisappearType)){
            mTop += mHeight/mDisappearCount;
        } else if(LEFT.equals(mDisappearType)){
            mLeft -= mWidth/mDisappearCount;
        } else if(RIGHT.equals(mDisappearType)){
            mLeft += mWidth/mDisappearCount;
        }
    }

    private void drawText(Canvas canvas){
        mPaint.setColor(mTextColor);
        mPaint.setAlpha(255);
        mPaint.setTextSize(mTextSize);
        mPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(mText, mLeft + mWidth / 2, mTop + mTextBaseLine, mPaint);
    }

    private void drawLoading(Canvas canvas){
        if(mIsProgress){
            mPaint.setColor(mBackColor);
            canvas.drawRoundRect(mBackRect, mCorner, mCorner, mPaint);
            mPaint.setColor(mButtonColor);
            mProgressRect.set(0, 0, mProgressWidth, mHeight);
            canvas.drawRoundRect(mProgressRect, mCorner, mCorner, mPaint);
        }

        int alpha = mAlpha;
        float angle = 360f / 12;
        mPaint.setColor(mProgressColor);
        for (int i = 0; i < 12; i++) {
            mPaint.setAlpha(alpha);
            canvas.rotate(angle, mWidth/2, mHeight/2);
            if(mWaitWidth == mWidth) {
                canvas.drawRect(mWidth / 2 - mWaitWidth / 18, mHeight/2 - mWidth/2, mWidth / 2 + mWaitWidth / 18, mHeight/2 - mWidth/4, mPaint);
            } else {
                canvas.drawRect(mWidth / 2 - mWaitWidth / 18, mWaitWidth / 15, mWidth / 2 + mWaitWidth / 18, mWaitWidth / 4, mPaint);
            }
            alpha -= 255 / 12;
            if(alpha < 0){
                alpha = 255;
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        mWaitWidth = mWidth < mHeight ? mWidth : mHeight;
        mTextBaseLine = mHeight / 7 * 4;
    }

    @Override
    public void onClick(View v) {
        if(mLoadingTimer != null){
            return;
        }

        if(mListener != null){
            mListener.click();
        }

        mIsDisappear = true;
        mDisappearTimer = new Timer();
        mDisappearTime = 0;

        mLoadingTimer = new Timer();
        mAlpha = 255;

        mDisappearTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (mDisappearTime >= mTotalChangeTime) {
                    mDisappearTimer.cancel();
                    mDisappearTimer = null;

                    mIsLoading = true;
                    mIsDisappear = false;

                    mTextSize = mTextSizeTemp;
                    mTextBaseLine = mHeight / 7 * 4;
                    mTop = 0;
                    mLeft = 0;

                    mLoadingTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            mAlpha -= 255 / 12;
                            if(mAlpha < 0){
                                mAlpha = 255;
                            }
                            postInvalidate();
                        }
                    }, mTotalChangeTime, 150);

                    System.gc();

                } else {
                    mDisappearTime += mTimeSchedule;
                }
                postInvalidate();
            }
        }, 0, mTimeSchedule);
    }

    public void finishLoading(){
        if(mLoadingTimer != null) {
            mLoadingTimer.cancel();
            mLoadingTimer = null;
            mIsLoading = false;
            System.gc();
            postInvalidate();
        }
    }

    public void setPercent(float percent){
        this.mProgressWidth = (int) (mWidth * percent / 100f);
    }

    public void setBackColor(int backgroundColor) {
        this.mBackColor = backgroundColor;
    }

    public void setCorner(int corner) {
        this.mCorner = corner;
    }

    public void setDisappearType(String disappearType) {
        this.mDisappearType = disappearType;
    }

    public void setProgressColor(int progressColor) {
        this.mProgressColor = progressColor;
    }

    public void setTextColor(int textColor) {
        this.mTextColor = textColor;
    }

    public void setIsProgress(boolean isProgress){
        this.mIsProgress = isProgress;
    }

    public void setText(String text){
        this.mText = text;
    }

    public void setTextSize(int textSize){
        this.mTextSizeTemp = textSize;
        this.mTextSize = textSize;
    }

    public void setClickListener(ClickListener listener){
        this.mListener = listener;
    }

    public interface ClickListener{
        void click();
    }
}
