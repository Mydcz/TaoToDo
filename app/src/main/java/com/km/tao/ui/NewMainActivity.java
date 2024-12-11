package com.km.tao.ui;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.enums.AppType;
import com.bigkoo.pickerview.view.TimePickerView;
import com.bigkoo.pickerview.view.WheelView;
import com.km.db.bean.TaskBean;
import com.km.kernel.base.BaseActivity;
import com.km.kernel.viewmodel.CategoryViewModel;
import com.km.kernel.viewmodel.TaskViewModel;
import com.km.kernel.viewmodelfactory.CategoryViewModelByFactory;
import com.km.kernel.viewmodelfactory.TaskViewModelByFactory;
import com.km.tao.R;
import com.km.tao.adapter.NewTaskAdapter;
import com.km.tao.databinding.ActivityMainBinding;
import com.km.tao.utils.DateUtils;
import com.km.tao.utils.LinearLayoutManagerScrollTop;
import com.km.tao.utils.StringUtils;
import com.km.tao.view.LoadingDialog;
import com.km.tao.view.PopupWindowView;
import com.km.tao.view.SelectorDialog;
import com.km.tao.view.TipsDialog;
import com.km.tao.view.looview.LoopView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class NewMainActivity extends BaseActivity<ActivityMainBinding> {
    private static final String TAG = NewMainActivity.class.getName();
    private TaskViewModel mTaskViewModel;
    private CategoryViewModel mCategoryViewModel;
    private NewTaskAdapter adapter;
    private List<TaskBean> mTask = new ArrayList<>();

    private LoadingDialog loadingDialog;
    private View singleSelectView, tipsView, completedSiftView;
    private SelectorDialog bottomDialog;
    private long taskId = -1;
    private long categoryId = -1;
    private long dueDate = 0l;
    private List<String> categoryNameList = new ArrayList<>();
    private List<Long> categoryIdList = new ArrayList<>();


    @Override
    protected ActivityMainBinding getViewBinding() {
        return ActivityMainBinding.inflate(getLayoutInflater());
    }


    @Override
    protected void initView() {
        loadingDialog = new LoadingDialog(this, 2);
        mTaskViewModel = new ViewModelProvider(this, new TaskViewModelByFactory()).get(TaskViewModel.class);
        mCategoryViewModel = new ViewModelProvider(this, new CategoryViewModelByFactory()).get(CategoryViewModel.class);
        mBinding.titleView.setTitle(getString(R.string.task_list));
        mBinding.titleView.setBack(View.GONE);

        adapter = new NewTaskAdapter(this, mTask);

        LinearLayoutManagerScrollTop linearLayoutManager = new LinearLayoutManagerScrollTop(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mBinding.recyclerView.setLayoutManager(linearLayoutManager);
        mBinding.recyclerView.setAdapter(adapter);

        mTaskViewModel.findAll();
        mCategoryViewModel.findAll();

        //返回按钮特殊处理，用于重置UI操作
        mBinding.titleView.setOnClickListener(v -> {
            resetUI();
        });

        //这里显示搜索框，并进行搜索
        mBinding.titleView.setOnSearchClickListener(view -> {
            mBinding.titleView.setBack(View.VISIBLE);
            mBinding.titleView.setSearchBox(View.VISIBLE);
        });

        //模糊查询
        mBinding.titleView.setOnSearchTitleClickListener(name -> {
            Log.d(TAG, "onSearchTitleClick: " + name);
            mTaskViewModel.likeFind(name);
        });

        //这里设置更多操作
        mBinding.titleView.setOnMoreClickListener(view -> {
            showPopupWindow(mBinding.titleView);
        });

        //编辑任务
        adapter.setEditListener((taskBean, position) -> {
            taskId = taskBean.getId();
            mBinding.titleView.setTitle(getString(R.string.edit));
            mBinding.titleView.setBack(View.VISIBLE);
            mBinding.recyclerView.setVisibility(View.GONE);
            mBinding.editLl.setVisibility(View.VISIBLE);
            mBinding.editTitleTv.setText(taskBean.getTitle());
            mBinding.editDescriptionTv.setText(taskBean.getDescription());
            categoryId = taskBean.getCategoryId();
            mBinding.editCategoryTv.setText(getString(R.string.please_select_category));
            for (int i = 0; i < categoryIdList.size(); i++) {
                //通过分类ID获取到分类名称
                if (categoryIdList.get(i) == categoryId) {
                    mBinding.editCategoryTv.setText(categoryNameList.get(i));
                    break;
                }
            }
            dueDate = taskBean.getDueDate();
            if (dueDate > 0) {//有截止日期
                mBinding.editDueDateTv.setText(DateUtils.formatTime(dueDate, getString(R.string.yyyy_mm_dd)));
            }else{
                mBinding.editDueDateTv.setText(getString(R.string.please_select_due_date));
            }
            mBinding.editDone.setChecked(taskBean.isCompleted());
            mBinding.editToBeContinued.setChecked(!taskBean.isCompleted());
        });

        //删除操作
        adapter.setDeleteListener((taskBean, position) -> {
            delete(taskBean);
        });

        //分类列表
        mBinding.editCategoryCl.setOnClickListener(v -> {
            int index = 0;
            for (int i = 0; i < categoryIdList.size(); i++) {
                if (categoryIdList.get(i) == categoryId) {
                    //获取当前选中的下标
                    index = i;
                    break;
                }
            }
            categoryDialog(index);
        });

        //日期选择
        mBinding.editDueDateCl.setOnClickListener(v -> {
            dueDate();
        });

        //修改提交
        mBinding.edit.setOnClickListener(v -> {
            String title = Objects.requireNonNull(mBinding.editTitleTv.getText()).toString();
            if (TextUtils.isEmpty(title)) {
                showToast(getString(R.string.input_title));
                return;
            }
            String description = Objects.requireNonNull(mBinding.editDescriptionTv.getText()).toString();
            mTaskViewModel.updateByTask(title, description, dueDate, categoryId, mBinding.editDone.isChecked(), taskId);
            loadingDialog.setILoadingCallback(() -> {
                showToast(getString(R.string.edit_succeed));
                resetUI();
            });
            loadingDialog.show();
        });
    }

    @Override
    protected void initData() {
        //任务列表查询LiveData
        mTaskViewModel.dayAllTaskBeanLiveData.observe(this, taskBeans -> {
            adapter.setData(taskBeans);
        });

        //分类列表LiveData
        mCategoryViewModel.dayAllCategoryBeanLiveData.observe(this, categoryBeans -> {
            categoryNameList.clear();
            categoryIdList.clear();
            for (int i = 0; i < categoryBeans.size(); i++) {
                categoryNameList.add(categoryBeans.get(i).getName());
                categoryIdList.add(categoryBeans.get(i).getId());
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    /**
     * 重置UI
     */
    private void resetUI() {
        taskId = -1;
        categoryId = -1;
        dueDate = 0l;
        mBinding.titleView.setTitle(getString(R.string.task_list));
        mBinding.titleView.setBack(View.GONE);
        mBinding.recyclerView.setVisibility(View.VISIBLE);
        mBinding.editLl.setVisibility(View.GONE);
        mBinding.titleView.setSearchBox(View.GONE);
        mTaskViewModel.findAll();
    }

    /**
     * menu方法
     * @param v
     */
    private void showPopupWindow(View v) {
        View contentView = LayoutInflater.from(this).inflate(R.layout.popup_window, null);
        PopupWindowView popWnd = new PopupWindowView(this);
        popWnd.setAnimationStyle(R.style.PopupWindowAnimation);
        popWnd.setContentView(contentView);
        popWnd.setOutsideTouchable(false); //是否允许弹出窗外事件
        popWnd.setFocusable(true);
        ColorDrawable dw = new ColorDrawable(Color.TRANSPARENT);
        popWnd.setBackgroundDrawable(dw);
        popWnd.showAsDropDown(v, (v.getWidth() - 30), 0);
        contentView.findViewById(R.id.add).setOnClickListener(view -> {
            showActivity(AddTaskActivity.class);
            popWnd.dismiss();
        });

        contentView.findViewById(R.id.completed_sift).setOnClickListener(view -> {
            completedSift();
            popWnd.dismiss();
        });
        contentView.findViewById(R.id.category_sift).setOnClickListener(view -> {
            mCategoryViewModel.findAll();
            categorySift();
            popWnd.dismiss();
        });

        contentView.findViewById(R.id.category).setOnClickListener(view -> {
            showActivity(CategoryActivity.class);
            popWnd.dismiss();
        });
    }


    /**
     * 分类选择器
     */
    private void categoryDialog(int index) {
        singleSelectView = getLayoutInflater().inflate(R.layout.view_single_select_loop, null);
        LoopView singleSelectLoop = singleSelectView.findViewById(R.id.single_select_loop);
        singleSelectLoop.setDividerColor(Color.TRANSPARENT);
        singleSelectLoop.setNotLoop();
        singleSelectLoop.setItems(categoryNameList);//设置显示的list
        singleSelectLoop.setInitPosition(0); //初始化下标
        singleSelectLoop.setCurrentPosition(index); //选中下标

        if (bottomDialog == null || !bottomDialog.isShowing()) {
            bottomDialog = new SelectorDialog.Builder(this).setView(singleSelectView).build();
            bottomDialog.setTitle(getString(R.string.category1));
            bottomDialog.setConfirmClickListener(dialog -> {
                categoryId = categoryIdList.get(singleSelectLoop.getSelectedItem());
                mBinding.editCategoryTv.setText(categoryNameList.get(singleSelectLoop.getSelectedItem()));
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
        if (dueDate > 0) {
            selectedDate.setTimeInMillis(dueDate);
        }
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
            mBinding.editDueDateTv.setText(StringUtils.format("%04d/%02d/%02d", calendar.get(Calendar.YEAR), (calendar.get(Calendar.MONTH) + 1), calendar.get(Calendar.DAY_OF_MONTH)));
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
                .setDate(selectedDate)// 如果不设置的话，默认是系统时间
                .setDividerType(WheelView.DividerType.NULL)
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setLabel("", "", "", "", "", "")
                .setDecorView(getWindow().getDecorView().findViewById(android.R.id.content))
                .build();
        pickerView.show();
    }

    /**
     * 删除提示确认
     *
     * @param taskBean
     */
    private void delete(TaskBean taskBean) {
        tipsView = getLayoutInflater().inflate(R.layout.view_tips, null);
        AppCompatTextView tipsTv = tipsView.findViewById(R.id.tips_tv);
        tipsTv.setText(R.string.delete_task);
        TipsDialog alertDialog = new TipsDialog.Builder(this).setView(tipsView).build();
        alertDialog.setTitle(getString(R.string.tips));
        alertDialog.setCancelClickListener(dialog -> {

        });
        alertDialog.setConfirmClickListener(dialog -> {
            mTaskViewModel.deleteById(taskBean.getId());
            mTaskViewModel.findAll();
        });
        alertDialog.show();
    }

    /**
     * 状态筛选
     */
    private void completedSift() {
        completedSiftView = getLayoutInflater().inflate(R.layout.view_completed_sift, null);
        RadioButton done = completedSiftView.findViewById(R.id.done);

        TipsDialog alertDialog = new TipsDialog.Builder(this).setView(completedSiftView).build();
        alertDialog.setTitle(getString(R.string.completed_sift));
        alertDialog.setCancelClickListener(dialog -> {

        });
        alertDialog.setConfirmClickListener(dialog -> {
            mTaskViewModel.findByIsCompleted(done.isChecked());
        });
        alertDialog.show();
    }

    /**
     * 分类选择器筛选确认
     */
    private void categorySift() {
        singleSelectView = getLayoutInflater().inflate(R.layout.view_single_select_loop, null);
        LoopView singleSelectLoop = singleSelectView.findViewById(R.id.single_select_loop);
        singleSelectLoop.setDividerColor(Color.TRANSPARENT);
        singleSelectLoop.setNotLoop();
        singleSelectLoop.setItems(categoryNameList);//设置显示的list
        singleSelectLoop.setInitPosition(0); //初始化下标
        singleSelectLoop.setCurrentPosition(0); //选中下标
        TipsDialog alertDialog = new TipsDialog.Builder(this).setView(singleSelectView).build();
        alertDialog.setTitle(getString(R.string.category_sift));
        alertDialog.setCancelClickListener(dialog -> {

        });
        alertDialog.setConfirmClickListener(dialog -> {
            categoryId = categoryIdList.get(singleSelectLoop.getSelectedItem());
            Log.d(TAG, "categorySift: categoryId = " + categoryId);
            mTaskViewModel.findByCategoryId(categoryId);
        });
        alertDialog.show();
    }
}