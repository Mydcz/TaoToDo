package com.km.tao.view;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.km.tao.R;
import com.km.tao.databinding.SelectorDialogBinding;


/**
 * 选择器弹窗
 */
public class SelectorDialog extends Dialog implements View.OnClickListener {

    private final Activity activity;
    private SelectorDialogBinding binding;
    private View mView;
    private OnButtonClickListener cancelClickListener;
    private OnButtonClickListener confirmClickListener;
    private String cancelBtnStr = "";
    private String confirmBtnStr = "";
    private String title = "";

    public SelectorDialog(@NonNull Activity activity) {
        super(activity, R.style.BottomDialogTheme);
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 处理底部导航栏覆盖问题
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        View decorView = window.getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        getWindow().getDecorView().setPadding(0, 0, 0, 0);
        int screenWidth = activity.getWindowManager().getDefaultDisplay().getWidth(); // 屏幕宽（像素，如：px）
        int screenHeight = activity.getWindowManager().getDefaultDisplay().getHeight(); // 屏幕高（像素，如：p）
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.CENTER;
        params.width = Math.min(screenWidth, screenHeight);
        getWindow().setAttributes(params);


        binding = SelectorDialogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (mView != null) {
            binding.bottomDialogContainer.addView(mView);
        }
        binding.tvTitle.setText(title);
        binding.bottomDialogCancelBtn.setText(cancelBtnStr);
        binding.bottomDialogConfirmBtn.setText(confirmBtnStr);
        binding.bottomDialogCancelBtn.setOnClickListener(this);
        binding.bottomDialogConfirmBtn.setOnClickListener(this);

        setCanceledOnTouchOutside(false);

    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.dismiss();
            if (cancelClickListener != null) {
                cancelClickListener.onClick(this);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        if (binding.bottomDialogCancelBtn.equals(v)) {

            this.dismiss();
            if (cancelClickListener != null) {
                cancelClickListener.onClick(this);
            }
        } else if (binding.bottomDialogConfirmBtn.equals(v)) {

            this.dismiss();
            if (confirmClickListener != null) {
                confirmClickListener.onClick(this);
            }
        }
        binding.bottomDialogContainer.removeView(mView);
    }

    public void setView(View view) {
        if(mView != null){
            binding.bottomDialogContainer.removeView(mView);
        }
        this.mView = view;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setCancelClickListener(OnButtonClickListener cancelClickListener) {
        this.cancelClickListener = cancelClickListener;
    }

    public void setConfirmClickListener(OnButtonClickListener confirmClickListener) {
        this.confirmClickListener = confirmClickListener;
    }

    public interface OnButtonClickListener {
        void onClick(Dialog dialog);
    }

    public void setCancelBtnStr(String cancelBtnStr) {
        this.cancelBtnStr = cancelBtnStr;
        if (TextUtils.isEmpty(cancelBtnStr)) {
            this.cancelBtnStr = activity.getString(R.string.cancel);
        }
    }

    public void setConfirmBtnStr(String confirmBtnStr) {
        this.confirmBtnStr = confirmBtnStr;
        if (TextUtils.isEmpty(confirmBtnStr)) {
            this.confirmBtnStr = activity.getString(R.string.submit);
        }
    }

    public static class Builder {

        private Activity activity;
        private View view;
        private String cancel;
        private String confirm;
        private OnButtonClickListener confirmClickListener;
        private OnButtonClickListener cancelClickListener;

        public Builder(Activity activity) {
            this.activity = activity;
        }

        public Builder setView(View view) {
            this.view = view;
            return this;
        }

        public Builder setOnConfirmClickListener(OnButtonClickListener listener) {
            confirmClickListener = listener;
            return this;
        }

        public Builder setOnConfirmClickListener(String confirm, OnButtonClickListener listener) {
            this.confirm = confirm;
            confirmClickListener = listener;
            return this;
        }

        public Builder setOnCancelClickListener(OnButtonClickListener listener) {
            cancelClickListener = listener;
            return this;
        }

        public Builder setOnCancelClickListener(String cancel, OnButtonClickListener listener) {
            this.cancel = cancel;
            cancelClickListener = listener;
            return this;
        }

        public SelectorDialog build() {
            SelectorDialog dialog = new SelectorDialog(activity);
            dialog.setView(view);
            dialog.setCancelBtnStr(cancel);
            dialog.setConfirmBtnStr(confirm);
            dialog.setConfirmClickListener(confirmClickListener);
            dialog.setCancelClickListener(cancelClickListener);
            return dialog;
        }
    }
}
