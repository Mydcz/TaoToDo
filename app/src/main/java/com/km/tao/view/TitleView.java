package com.km.tao.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.km.tao.R;
import com.km.tao.databinding.LayoutTitleBinding;

/**
 * 标题栏
 */
public class TitleView extends ConstraintLayout {
    private static final String TAG = TitleView.class.getName();
    private Activity activity;
    private LayoutTitleBinding binding;
    private OnSearchTitleClickListener onSearchTitleClickListener;
    private OnSearchClickListener onSearchClickListener;
    private OnMoreClickListener onMoreClickListener;
    private OnAddClickListener onAddClickListener;
    private View.OnClickListener onClickListener;

    public TitleView(@NonNull Context context) {
        this(context, null);
    }

    public TitleView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        activity = (Activity) context;
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.TitleView);
        int backResId = typedArray.getResourceId(R.styleable.TitleView_drawableLeft, R.drawable.arrow_left);
        String title = typedArray.getString(R.styleable.TitleView_title);
        int titleColor = typedArray.getColor(R.styleable.TitleView_titleTextColor, Color.BLACK);

        View view = View.inflate(activity, R.layout.layout_title, this);
        binding = LayoutTitleBinding.bind(view);

        binding.titleNameTv.setText(title);
        binding.titleNameTv.setTextColor(titleColor);
        binding.titleBackImg.setImageResource(backResId);

        binding.titleBackImg.setOnClickListener(v -> {
            if (onClickListener != null) {
                onClickListener.onClick(v);
                return;
            }
            activity.finish();
        });

        binding.titleSearchImg.setOnClickListener(v -> {
            if (onSearchClickListener != null) {
                onSearchClickListener.onSearchClick(v);
            }
        });

        binding.titleMoreImg.setOnClickListener(v -> {
            if (onMoreClickListener != null) {
                onMoreClickListener.onMoreClick(v);
            }
        });

        binding.titleAddImg.setOnClickListener(v -> {
            if (onAddClickListener != null) {
                onAddClickListener.onAddClick(v);
            }
        });


//        binding.searchEt.setFilters(new InputFilter[]{new EmojiExcludeFilter()});
        binding.searchEt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(12)});

        binding.searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 文本改变之前执行的逻辑
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String name = s.toString();
                Log.d(TAG, "afterTextChanged: name = " + name);
                if (onSearchTitleClickListener != null) {
                    onSearchTitleClickListener.onSearchTitleClick(name);
                }
            }
        });

        // 设置EditText的焦点变化监听器
        binding.searchEt.setOnFocusChangeListener((v, hasFocus) -> {
            // 当EditText失去焦点时
            if (!hasFocus) {
                // 隐藏软键盘
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });
    }

    public void setTitle(String title) {
        binding.titleNameTv.setText(title);
    }

    public void setTitle(String title, int color) {
        binding.titleNameTv.setText(title);
        binding.titleNameTv.setTextColor(color);
    }

    public void setTextColor(int textColor) {
        binding.titleNameTv.setTextColor(textColor);
    }

    public void setSearch(int visibility) {
        binding.titleSearchImg.setVisibility(visibility);
    }

    public void setMore(int visibility) {
        binding.titleMoreImg.setVisibility(visibility);
        if (visibility == VISIBLE) {
            binding.titleAddImg.setVisibility(GONE);
        }
    }

    public void setAdd(int visibility) {
        binding.titleAddImg.setVisibility(visibility);
        if (visibility == VISIBLE) {
            binding.titleMoreImg.setVisibility(GONE);
        }
    }

    public void setBack(int visibility) {
        binding.titleBackImg.setVisibility(visibility);
    }

    public void setSearchBox(int visibility) {
        if (visibility == VISIBLE) {
            binding.titleSearchImg.setVisibility(GONE);
            binding.titleNameTv.setVisibility(GONE);
            binding.search.setVisibility(VISIBLE);
        } else {
            binding.titleSearchImg.setVisibility(VISIBLE);
            binding.titleNameTv.setVisibility(VISIBLE);
            binding.search.setVisibility(GONE);
        }
    }

    public void setBackImg(@NonNull Integer resId) {
        if (resId != null) {
            binding.titleBackImg.setImageResource(resId);
        } else {
            binding.titleBackImg.setImageDrawable(null);
        }
    }

    public void setOnSearchTitleClickListener(OnSearchTitleClickListener onSearchTitleClickListener) {
        this.onSearchTitleClickListener = onSearchTitleClickListener;
    }

    public interface OnSearchTitleClickListener {
        void onSearchTitleClick(String name);
    }

    public void setOnSearchClickListener(OnSearchClickListener onSearchClickListener) {
        this.onSearchClickListener = onSearchClickListener;
    }

    public interface OnSearchClickListener {
        void onSearchClick(View view);
    }

    public void setOnMoreClickListener(OnMoreClickListener onMoreClickListener) {
        this.onMoreClickListener = onMoreClickListener;
    }

    public interface OnMoreClickListener {
        void onMoreClick(View view);
    }

    public void setOnAddClickListener(OnAddClickListener onAddClickListener) {
        this.onAddClickListener = onAddClickListener;
    }

    public interface OnAddClickListener {
        void onAddClick(View view);
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.onClickListener = listener;
    }


}
