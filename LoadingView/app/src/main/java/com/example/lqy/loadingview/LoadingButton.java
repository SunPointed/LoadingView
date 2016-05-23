package com.example.lqy.loadingview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by lqy on 16/5/23.
 */
public class LoadingButton extends View {

    public static final String TAG = "LoadingButton";

    public static final int UP = 0;
    public static final int DOWN = 1;
    public static final int LEFT = 2;
    public static final int RIGHT = 3;
    public static final int ZOOM_IN = 4;
    public static final int ZOOM_OUT = 5;

    private int mWidth;
    private int mHeight;
    private int mProgressWidth;

    private int mTextColor;
    private int mBackgroundColor;
    private int mProgressColor;

    private int mCorner;

    public LoadingButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public LoadingButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingButton(Context context) {
        this(context, null);
    }
}
