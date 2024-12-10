package com.km.tao.ui;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.enums.AppType;
import com.bigkoo.pickerview.view.TimePickerView;
import com.bigkoo.pickerview.view.WheelView;
import com.km.db.bean.CategoryBean;
import com.km.kernel.base.BaseActivity;
import com.km.kernel.viewmodel.CategoryViewModel;
import com.km.kernel.viewmodel.TaskViewModel;
import com.km.kernel.viewmodelfactory.CategoryViewModelByFactory;
import com.km.kernel.viewmodelfactory.TaskViewModelByFactory;
import com.km.tao.R;
import com.km.tao.databinding.ActivityAddTaskBinding;
import com.km.tao.utils.StringUtils;
import com.km.tao.view.LoadingDialog;
import com.km.tao.view.SelectorDialog;
import com.km.tao.view.looview.LoopView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

/**
 * 新增任务
 */
public class AddTaskActivity extends BaseActivity<ActivityAddTaskBinding> {
    private static final String TAG = AddTaskActivity.class.getName();
    private LoadingDialog loadingDialog;
    private TaskViewModel mTaskViewModel;
    private CategoryViewModel mCategoryViewModel;
    private View singleSelectView;
    private SelectorDialog bottomDialog;
    private long categoryId = -1;
    private long dueDate = 0l;
    private List<String> categoryNameList = new ArrayList<>();
    private List<Long> categoryIdList = new ArrayList<>();

    @Override
    protected ActivityAddTaskBinding getViewBinding() {
        return ActivityAddTaskBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initView() {
        mTaskViewModel = new ViewModelProvider(this, new TaskViewModelByFactory()).get(TaskViewModel.class);
        loadingDialog = new LoadingDialog(this,2);
        mCategoryViewModel = new ViewModelProvider(this, new CategoryViewModelByFactory()).get(CategoryViewModel.class);
        //获取一下当前分类，用于新增选择分类做准备
        mCategoryViewModel.findAll();
        mBinding.titleView.setTitle(getString(R.string.add_task));
        mBinding.titleView.setSearch(View.GONE);
        mBinding.titleView.setMore(View.GONE);


    }

    @Override
    protected void initData() {
        mBinding.categoryCl.setOnClickListener(v -> {
            if (!categoryNameList.isEmpty()) {
                categoryDialog();
            } else {
                showToast(getString(R.string.add_category));
            }
        });

        mBinding.dueDateCl.setOnClickListener(v -> dueDate());

        mBinding.add.setOnClickListener(v -> {
            String title = Objects.requireNonNull(mBinding.titleTv.getText()).toString();
            if (TextUtils.isEmpty(title)) {
                showToast(getString(R.string.input_title));
                return;
            }
            String description = Objects.requireNonNull(mBinding.descriptionTv.getText()).toString();
            mTaskViewModel.insert(title, description, dueDate, categoryId);

            loadingDialog.setILoadingCallback(() -> {
                showToast(getString(R.string.add_succeed));
                finish();
            });
            loadingDialog.show();
        });
        mCategoryViewModel.dayAllCategoryBeanLiveData.observe(this, categoryBeans -> {
            if (categoryBeans != null) {
                for (int i = 0; i < categoryBeans.size(); i++) {
                    CategoryBean categoryBean = categoryBeans.get(i);
                    categoryNameList.add(categoryBean.getName());
                    categoryIdList.add(categoryBean.getId());
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 需要释放一下资源
        if (loadingDialog!=null){
            loadingDialog.dismiss();
        }
    }

    /**
     * 分类选择器
     */
    private void categoryDialog() {
        singleSelectView = getLayoutInflater().inflate(R.layout.view_single_select_loop, null);
        LoopView singleSelectLoop = singleSelectView.findViewById(R.id.single_select_loop);
        singleSelectLoop.setDividerColor(Color.TRANSPARENT);
        singleSelectLoop.setNotLoop();
        singleSelectLoop.setItems(categoryNameList);
        singleSelectLoop.setInitPosition(0);
        singleSelectLoop.setCurrentPosition(0);

        if (bottomDialog == null || !bottomDialog.isShowing()) {
            bottomDialog = new SelectorDialog.Builder(this).setView(singleSelectView).build();
            bottomDialog.setTitle(getString(R.string.category1));
            bottomDialog.setConfirmClickListener(dialog -> {
                categoryId = categoryIdList.get(singleSelectLoop.getSelectedItem());
                mBinding.categoryTv.setText(categoryNameList.get(singleSelectLoop.getSelectedItem()));
            });
            bottomDialog.show();
        }
    }

    /**
     * 时间选择器
     */
    private void dueDate() {
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        Calendar selectedDate = Calendar.getInstance();
        int endYear = selectedDate.get(Calendar.YEAR);
        int endMonth = selectedDate.get(Calendar.MONTH);
        int endDay = selectedDate.get(Calendar.DAY_OF_MONTH);
        startDate.set(2000, 0, 1); //设置选择器开始时间
        endDate.set(endYear + 3, endMonth, endDay);//设置选择器结束时间
        selectedDate.set(endYear, endMonth, endDay);//设置选择器选中时间
        TimePickerView pickerView = new TimePickerBuilder(this, AppType.WatchFitPro, (date, v) -> {//选中事件回调
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            dueDate = calendar.getTimeInMillis();
            mBinding.dueDateTv.setText(StringUtils.format("%04d/%02d/%02d", calendar.get(Calendar.YEAR), (calendar.get(Calendar.MONTH) + 1), calendar.get(Calendar.DAY_OF_MONTH)));
        }).setLineSpacingMultiplier(1.8f)
                .setTitleText(getString(R.string.data))
                .setTitleSize(20)
                .setType(new boolean[]{true, true, true, false, false, false})// 默认全部显示
                .setCancelText(getString(R.string.cancel))//取消按钮文字
                .setSubmitText(getString(R.string.submit))//确认按钮文字
                .setContentTextSize(30)
                .setTitleBgColor(getColor(R.color.white)) //底部按钮背景
                .setOutSideCancelable(false)//点击屏幕，点在控件外部范围时，是否取消显示
                .isCyclic(false)//是否循环滚动
                .isFutureData(true)//是否可以选择未来时间
                .setSubmitColor(getColor(R.color.white))//确定按钮文字颜色
                .setCancelColor(getColor(R.color.black))//取消按钮文字颜色
                .setDividerColor(ContextCompat.getColor(this, R.color.black))
                .setTextColorCenter(ContextCompat.getColor(this, R.color.black))
                .setRangDate(startDate, endDate)//起始终止年月日设定
                .setDividerType(WheelView.DividerType.NULL)
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setLabel("", "", "", "", "", "")
                .setDecorView(getWindow().getDecorView().findViewById(android.R.id.content))
                .build();
        pickerView.show();
    }
}