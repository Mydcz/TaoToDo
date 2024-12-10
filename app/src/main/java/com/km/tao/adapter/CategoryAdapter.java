package com.km.tao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.km.db.bean.CategoryBean;
import com.km.tao.R;

import java.util.List;

/**
 * 分类列表适配器
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private static final String TAG = CategoryAdapter.class.getName();
    private Context mContext;
    private List<CategoryBean> mCategory;

    private OnClickListener deleteListener,longDeleteListener;

    public CategoryAdapter(Context mContext, List<CategoryBean> mCategory) {
        this.mContext = mContext;
        this.mCategory = mCategory;
    }

    public void setData(List<CategoryBean> mCategory) {
        this.mCategory = mCategory;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.item_category, parent, false);
        return new CategoryAdapter.ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position) {
        CategoryBean categoryBean = mCategory.get(position);
        holder.nameTv.setText(categoryBean.getName());
        holder.colorTv.setText(categoryBean.getColor());

        holder.categoryItemCl.setOnClickListener(v -> {
            if (deleteListener != null) {
                deleteListener.onClickListener(categoryBean, position);
            }
        });
        holder.categoryItemCl.setOnLongClickListener(v -> {
            if (longDeleteListener != null) {
                longDeleteListener.onClickListener(categoryBean, position);
            }
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return mCategory.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private AppCompatTextView nameTv, colorTv;
        private ConstraintLayout categoryItemCl;

        public ViewHolder(View item) {
            super(item);
            categoryItemCl = item.findViewById(R.id.category_item_cl);
            nameTv = item.findViewById(R.id.name_tv);
            colorTv = item.findViewById(R.id.color_tv);
        }
    }


    //设置删除点击事件
    public void setDeleteListener(OnClickListener deleteListener) {
        this.deleteListener = deleteListener;
    }

    //设置删除点击事件
    public void setDeleteLongListener(OnClickListener longDeleteListener) {
        this.longDeleteListener = longDeleteListener;
    }

    public interface OnClickListener {
        void onClickListener(CategoryBean categoryBean, int position);
    }
}
