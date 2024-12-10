package com.km.tao.ui;

import android.text.TextUtils;
import android.view.View;

import androidx.lifecycle.ViewModelProvider;

import com.km.kernel.base.BaseActivity;
import com.km.kernel.viewmodel.CategoryViewModel;
import com.km.kernel.viewmodelfactory.CategoryViewModelByFactory;
import com.km.tao.R;
import com.km.tao.databinding.ActivityAddCategoryBinding;
import com.km.tao.view.LoadingDialog;

import java.util.Objects;

/**
 * 新增分类
 */
public class AddCategoryActivity extends BaseActivity<ActivityAddCategoryBinding> {
    private CategoryViewModel mCategoryViewModel;
    private LoadingDialog loadingDialog;
    @Override
    protected ActivityAddCategoryBinding getViewBinding() {
        return ActivityAddCategoryBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initView() {
        mCategoryViewModel = new ViewModelProvider(this, new CategoryViewModelByFactory()).get(CategoryViewModel.class);
        loadingDialog = new LoadingDialog(this,1);
        mBinding.titleView.setTitle(getString(R.string.category_list));
        mBinding.titleView.setBack(View.VISIBLE);
        mBinding.titleView.setSearch(View.GONE);
        mBinding.titleView.setMore(View.GONE);
        mBinding.titleView.setAdd(View.GONE);
    }

    @Override
    protected void initData() {
        mBinding.add.setOnClickListener(v -> {
            String name = Objects.requireNonNull(mBinding.nameTv.getText()).toString();
            if (TextUtils.isEmpty(name)) {
                showToast(getString(R.string.input_name));
                return;
            }
            String color = Objects.requireNonNull(mBinding.colorTv.getText()).toString();
            if (TextUtils.isEmpty(color)) {
                showToast(getString(R.string.input_color));
                return;
            }
            mCategoryViewModel.insert(name, color);
            loadingDialog.setILoadingCallback(() -> {
                showToast(getString(R.string.add_succeed));
                finish();
            });
            loadingDialog.show();
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (loadingDialog!=null){
            loadingDialog.dismiss();
        }
    }
}