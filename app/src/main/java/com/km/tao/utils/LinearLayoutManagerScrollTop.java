package com.km.tao.utils;

import android.content.Context;
import android.util.AttributeSet;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class LinearLayoutManagerScrollTop extends LinearLayoutManager {
    public LinearLayoutManagerScrollTop(Context context) {
        super(context);
    }
 
    public LinearLayoutManagerScrollTop(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }
 
    public LinearLayoutManagerScrollTop(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
 
    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
        LinearSmoothScrollerTop topScroller = new LinearSmoothScrollerTop(recyclerView.getContext());
        topScroller.setTargetPosition(position);
        startSmoothScroll(topScroller );
    }
}