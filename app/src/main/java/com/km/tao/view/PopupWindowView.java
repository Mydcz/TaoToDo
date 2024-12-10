package com.km.tao.view;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.widget.PopupWindow;

public class PopupWindowView extends PopupWindow {
    public PopupWindowView(Context context) {
        super(context);
    }

    @Override
    public void showAsDropDown(View anchor) {
        if (Build.VERSION.SDK_INT >= 24) {
            Rect rect = new Rect();
            anchor.getGlobalVisibleRect(rect);
        }
        super.showAsDropDown(anchor);
    }
}
