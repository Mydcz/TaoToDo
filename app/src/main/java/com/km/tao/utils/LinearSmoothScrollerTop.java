package com.km.tao.utils;

import android.content.Context;
import android.util.DisplayMetrics;

import androidx.recyclerview.widget.LinearSmoothScroller;

public class LinearSmoothScrollerTop extends LinearSmoothScroller {
    
    private int scrollType= LinearSmoothScroller.SNAP_TO_START;
   
    public static final float DEFAULT_MILLISECONDS_PER_INCH = 25f;

    public LinearSmoothScrollerTop(Context context) {
        super(context);
    }


    @Override
    protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
        return DEFAULT_MILLISECONDS_PER_INCH / displayMetrics.densityDpi;
    }
 
    
    /**
     * LinearSmoothScroller.SNAP_TO_ANY   默认模式
     * LinearSmoothScroller.SNAP_TO_START 顶部
     * LinearSmoothScroller.SNAP_TO_END   底部
     *  竖向
     */
    @Override
    protected int getVerticalSnapPreference() {
        return scrollType;
    }
 
    /**
     * LinearSmoothScroller.SNAP_TO_ANY   默认模式
     * LinearSmoothScroller.SNAP_TO_START 顶部
     * LinearSmoothScroller.SNAP_TO_END   底部
     *  横向
     */
    @Override  
    protected int getHorizontalSnapPreference() {
        return super.getHorizontalSnapPreference();
    }
}