package com.km.tao.view;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.km.tao.R;
import com.km.tao.databinding.DialogLoadingBinding;

/**
 * Loading
 */
public class LoadingDialog extends Dialog {

    private DialogLoadingBinding binding;
    private ILoadingCallback callback;
    private String prompt = "";
    /**
     * 秒钟
     */
    private int timeout = 15;

    public LoadingDialog(Activity activity, int timeout) {
        super(activity, R.style.loadingDialogTheme);
        this.timeout = timeout;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public void setILoadingCallback(ILoadingCallback callback) {
        this.callback = callback;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(params);

        binding = DialogLoadingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (!TextUtils.isEmpty(prompt)) {
            binding.promptTv.setText(prompt);
        }
        setCanceledOnTouchOutside(false);
    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void show() {
        super.show();
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable, timeout * 1000);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        handler.removeCallbacks(runnable);
    }

    Handler handler = new Handler(Looper.myLooper());
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            dismiss();
            if (callback != null) {
                callback.onTmieoutCallback();
            }
        }
    };
}
