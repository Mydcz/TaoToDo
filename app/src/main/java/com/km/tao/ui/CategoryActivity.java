package com.km.tao.ui;

import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.km.db.bean.CategoryBean;
import com.km.db.bean.TaskBean;
import com.km.kernel.base.BaseActivity;
import com.km.kernel.viewmodel.CategoryViewModel;
import com.km.kernel.viewmodel.TaskViewModel;
import com.km.kernel.viewmodelfactory.CategoryViewModelByFactory;
import com.km.kernel.viewmodelfactory.TaskViewModelByFactory;
import com.km.tao.R;
import com.km.tao.adapter.CategoryAdapter;
import com.km.tao.databinding.ActivityCategoryBinding;
import com.km.tao.utils.LinearLayoutManagerScrollTop;
import com.km.tao.view.TipsDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * 分类列表
 */
public class CategoryActivity extends BaseActivity<ActivityCategoryBinding> {
    private TaskViewModel mTaskViewModel;
    private CategoryViewModel mCategoryViewModel;
    private CategoryAdapter adapter;
    private List<CategoryBean> mCategory = new ArrayList<>();
    private View tipsView;

    @Override
    protected ActivityCategoryBinding getViewBinding() {
        return ActivityCategoryBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initView() {
        mTaskViewModel = new ViewModelProvider(this, new TaskViewModelByFactory()).get(TaskViewModel.class);
        mCategoryViewModel = new ViewModelProvider(this, new CategoryViewModelByFactory()).get(CategoryViewModel.class);
        mBinding.titleView.setTitle(getString(R.string.category_list));
        mBinding.titleView.setBack(View.VISIBLE);
        mBinding.titleView.setSearch(View.GONE);
        mBinding.titleView.setAdd(View.VISIBLE);

        adapter = new CategoryAdapter(this, mCategory);

        LinearLayoutManagerScrollTop linearLayoutManager = new LinearLayoutManagerScrollTop(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mBinding.recyclerView.setLayoutManager(linearLayoutManager);
        mBinding.recyclerView.setAdapter(adapter);

        mCategoryViewModel.findAll();

        //这里显示搜索框，并进行搜索
        mBinding.titleView.setOnSearchClickListener(view -> {
            mBinding.titleView.setBack(View.VISIBLE);
            mBinding.titleView.setSearchBox(View.VISIBLE);
        });

        //模糊查询
        mBinding.titleView.setOnSearchTitleClickListener(name -> {
            mCategoryViewModel.likeFind(name);
        });

        //这里设置更多操作
        mBinding.titleView.setOnAddClickListener(view -> {
            showActivity(AddCategoryActivity.class);
        });

        adapter.setDeleteListener((categoryBean, position) -> delete(categoryBean));
        adapter.setDeleteLongListener((categoryBean, position) -> deleteCategoryAndTask(categoryBean));

    }

    @Override
    protected void initData() {
        //分类列表LiveData
        mCategoryViewModel.dayAllCategoryBeanLiveData.observe(this, categoryBeans -> {
            adapter.setData(categoryBeans);
        });
    }

    /**
     * 删除提示确认
     *
     * @param categoryBean
     */
    private void deleteCategoryAndTask(CategoryBean categoryBean, boolean isLong) {
        tipsView = getLayoutInflater().inflate(R.layout.view_tips, null);
        AppCompatTextView tipsTv = tipsView.findViewById(R.id.tips_tv);
        if (isLong) {
            tipsTv.setText(R.string.delete_category_task);
        } else {
            tipsTv.setText(R.string.delete_category);
        }
        TipsDialog alertDialog = new TipsDialog.Builder(this).setView(tipsView).build();
        alertDialog.setTitle(getString(R.string.tips));
        alertDialog.setCancelClickListener(dialog -> {

        });
        alertDialog.setConfirmClickListener(dialog -> {
            if (isLong) {
                //删除任务
                mTaskViewModel.deleteByCategoryId(categoryBean.getId());
            }
            //删除分类
            mCategoryViewModel.deleteById(categoryBean.getId());
            //重新查询数据，刷新界面
            mCategoryViewModel.findAll();
        });
        alertDialog.show();
    }
}