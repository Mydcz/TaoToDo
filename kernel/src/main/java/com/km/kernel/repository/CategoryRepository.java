package com.km.kernel.repository;

import androidx.lifecycle.MutableLiveData;

import com.km.db.CustomDisposable;
import com.km.db.DataBaseManage;
import com.km.db.bean.CategoryBean;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;

/**
 * 分类管理数据操作方法
 */
public class CategoryRepository {

    private static CategoryRepository mInstance;
    private CategoryRepository() {
    }

    public static CategoryRepository getInstance() {
        if (mInstance == null) {
            synchronized (CategoryRepository.class) {
                mInstance = new CategoryRepository();
            }
        }
        return mInstance;
    }

    public void insert(CategoryBean categoryBean) {
        DataBaseManage.getInstance().getCategoryDao().insert(categoryBean);
    }

    public void findAll(MutableLiveData<List<CategoryBean>> findAllCategoryBeanLiveData) {
        Flowable<List<CategoryBean>> all = DataBaseManage.getInstance().getCategoryDao().findAllFlowable();
        CustomDisposable.addDisposable(all, findAllCategoryBeanLiveData::postValue);
    }

    public List<CategoryBean> findById(long categoryId){
        return DataBaseManage.getInstance().getCategoryDao().findById(categoryId);
    }

    public void likeFind(String name, MutableLiveData<List<CategoryBean>> findAllCategoryBeanLiveData) {
        Flowable<List<CategoryBean>> byDataListFlowable = DataBaseManage.getInstance().getCategoryDao().likeFindFlowable(name);
        CustomDisposable.addDisposable(byDataListFlowable, findAllCategoryBeanLiveData::postValue);
    }

    public void updateById(String name, String color, long id) {
        DataBaseManage.getInstance().getCategoryDao().updateById(name,color,id);
    }

    public void deleteById(long id) {
        DataBaseManage.getInstance().getCategoryDao().deleteById(id);
    }

    public void deleteAll() {
        DataBaseManage.getInstance().getCategoryDao().deleteAll();
    }

}
