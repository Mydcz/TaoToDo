package com.bigkoo.pickerview.listener;

import android.view.KeyEvent;

public interface OnKyeClickListener {
    /**
     * 将父类的按键事件，抛到子类处理
     * @param keyCode
     * @param event
     */
    void onClick(int keyCode, KeyEvent event);
}
