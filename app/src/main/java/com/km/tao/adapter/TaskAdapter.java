package com.km.tao.adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.km.db.bean.TaskBean;
import com.km.tao.R;
import com.km.tao.utils.DateUtils;

import java.util.List;

/**
 * 任务列表适配器
 */
public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private static final String TAG = TaskAdapter.class.getName();
    private Context mContext;
    private List<TaskBean> mTask;

    private OnClickListener finishTaskListener, editListener, deleteListener;

    public TaskAdapter(Context mContext, List<TaskBean> mTask) {
        this.mContext = mContext;
        this.mTask = mTask;
    }

    public void setData(List<TaskBean> mTask) {
        this.mTask = mTask;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TaskAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.item_task, parent, false);
        return new TaskAdapter.ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskAdapter.ViewHolder holder, int position) {
        TaskBean taskBean = mTask.get(position);
        holder.titleTv.setText(taskBean.getTitle());
        holder.descriptionTv.setText(taskBean.getDescription());
        if (taskBean.getDueDate() > 0) { // 有截止日期
            holder.dueDateTv.setText(DateUtils.formatTime(taskBean.getDueDate(), mContext.getString(R.string.yyyy_mm_dd)));
        }
        if (taskBean.isCompleted()) {
            holder.completedTypeTv.setText(mContext.getString(R.string.done));
            holder.completedTypeTv.setTextColor(mContext.getResources().getColor(R.color.color_main));
            holder.finishTaskCl.setVisibility(View.GONE);
        } else {
            holder.completedTypeTv.setText(mContext.getString(R.string.ongoing));
            holder.completedTypeTv.setTextColor(mContext.getResources().getColor(R.color.color_F8B5B5));
            holder.finishTaskCl.setVisibility(View.VISIBLE);
        }
        holder.finishTaskCl.setOnClickListener(v -> {
            if (finishTaskListener != null) {
                finishTaskListener.onClickListener(taskBean, position);
            }
        });
        holder.editCl.setOnClickListener(v -> {
            if (editListener != null) {
                editListener.onClickListener(taskBean, position);
            }
        });
        holder.deleteCl.setOnClickListener(v -> {
            if (deleteListener != null) {
                deleteListener.onClickListener(taskBean, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTask.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private AppCompatTextView titleTv, descriptionTv, dueDateTv, completedTypeTv;
        private ConstraintLayout finishTaskCl, editCl, deleteCl;

        public ViewHolder(View item) {
            super(item);
            titleTv = item.findViewById(R.id.title_tv);
            descriptionTv = item.findViewById(R.id.description_tv);
            dueDateTv = item.findViewById(R.id.due_date_tv);
            completedTypeTv = item.findViewById(R.id.completed_type_tv);
            finishTaskCl = item.findViewById(R.id.finish_task_cl);
            editCl = item.findViewById(R.id.edit_cl);
            deleteCl = item.findViewById(R.id.delete_cl);
        }
    }

    //设置完成点击事件
    public void setFinishTaskListener(OnClickListener finishTaskListener) {
        this.finishTaskListener = finishTaskListener;
    }

    //设置编辑点击事件
    public void setEditListener(OnClickListener editListener) {
        this.editListener = editListener;
    }

    //设置删除点击事件
    public void setDeleteListener(OnClickListener deleteListener) {
        this.deleteListener = deleteListener;
    }

    public interface OnClickListener {
        void onClickListener(TaskBean taskBean, int position);
    }
}
