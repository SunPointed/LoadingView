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

    public static final int UP = 0;
    public static final int DOWN = 1;
    public static final int LEFT = 2;
    public static final int RIGHT = 3;
    public static final int ZOOM_IN = 4;
    public static final int ZOOM_OUT = 5;

    private String mText = "submit";
    private int mTextSize;

    private int mWidth;
    private int mHeight;
    private int mProgressWidth = 0;
    private int mWaitWidth;
    private RectF mBackRect;

    private int mTextColor;
    private int mButtonColor;
    private int mBackColor;
    private int mProgressColor;

    private int mCorner;

    private int mDisappearType;
    private int mDisappearTime;
    private Timer mDisappearTimer;

    private Timer mLoadingTimer;

    private Paint mPaint;
    private int mAlpha;

    private boolean mIsLoading;
    private boolean mIsProgress;

    private int mTotalChangeTime;
    private int mTimeSchedule;

    public LoadingButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LoadingButton);
        mTextColor = typedArray.getColor(R.styleable.LoadingButton_text_color, 0xFFFFFFFF);
        mBackColor = typedArray.getColor(R.styleable.LoadingButton_backgound_color, 0xFF333333);
        mProgressColor = typedArray.getColor(R.styleable.LoadingButton_progress_color, 0xFFCCCCCC);
        mCorner = typedArray.getDimensionPixelSize(R.styleable.LoadingButton_corner, 30);
        mDisappearType = typedArray.getDimensionPixelOffset(R.styleable.LoadingButton_disappear_type, ZOOM_IN);
        mButtonColor = typedArray.getColor(R.styleable.LoadingButton_button_color, 0xFFFFFF00);
        mTotalChangeTime = typedArray.getDimensionPixelSize(R.styleable.LoadingButton_total_change_time, 500);
        mTimeSchedule = typedArray.getDimensionPixelSize(R.styleable.LoadingButton_time_schedule, 20);
        mText = typedArray.getString(R.styleable.LoadingButton_text);
        mTextSize = typedArray.getDimensionPixelSize(R.styleable.LoadingButton_text_size, 60);
        typedArray.recycle();

        Log.i(TAG, "mCorner:" + mCorner);
        Log.i(TAG, "mTextSize:" + mTextSize);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mAlpha = 255;
        mIsLoading = false;
        mIsProgress = true;
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

        mPaint.setColor(mBackColor);
        canvas.drawRoundRect(mBackRect, mCorner, mCorner, mPaint);

        if(mDisappearType == ZOOM_IN){

        } else if(mDisappearType == ZOOM_OUT){

        } else if(mDisappearType == UP){

        } else if(mDisappearType == DOWN){

        } else if(mDisappearType == LEFT){

        } else if(mDisappearType == RIGHT){

        }

        if(mIsLoading){
            drawLoading(canvas);
        } else {
            drawText(canvas);
        }
    }

    private void drawText(Canvas canvas){
        mPaint.setColor(mTextColor);
        mPaint.setAlpha(255);
        mPaint.setTextSize(mTextSize);
        mPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(mText, mWidth/2, mHeight/7*4, mPaint);
    }

    private void drawLoading(Canvas canvas){
        if(mIsProgress){

        }
        int alpha = mAlpha;
        float angle = 360f / 12;
        mPaint.setColor(mProgressColor);
        for (int i = 0; i < 12; i++) {
            mPaint.setAlpha(alpha);
            canvas.rotate(angle, mWidth/2, mHeight/2);
            if(mWaitWidth == mWidth) {
                canvas.drawRect(mHeight / 2 - mWaitWidth / 18, mWaitWidth / 15, mHeight / 2 + mWaitWidth / 18, mWaitWidth / 4, mPaint);
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
        mBackRect = new RectF(0, 0, mWidth, mHeight);
    }

    @Override
    public void onClick(View v) {
        mDisappearTimer = new Timer();
        mDisappearTime = 0;

        mLoadingTimer = new Timer();
        mAlpha = 255;

        mLoadingTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                mAlpha -= 255 / 12;
                if(mAlpha < 0){
                    mAlpha = 255;
                }
                postInvalidate();
            }
        }, 0, 150);

        mDisappearTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(mDisappearTime >= mTotalChangeTime){
                    mDisappearTimer.cancel();
                    mDisappearTimer = null;
                    mIsLoading = true;
                } else {
                    mDisappearTime += mTimeSchedule;
                }
                postInvalidate();
            }
        }, 0, mTimeSchedule);
    }

    public void setProgressWidth(int progressWidth){
        this.mProgressWidth = progressWidth;
    }

    public void setBackColor(int backgroundColor) {
        this.mBackColor = backgroundColor;
    }

    public void setCorner(int corner) {
        this.mCorner = corner;
    }

    public void setDisappearType(int disappearType) {
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
        this.mTextSize = textSize;
    }
}
