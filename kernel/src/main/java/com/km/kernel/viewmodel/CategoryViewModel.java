package com.km.kernel.viewmodel;


import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.km.db.bean.CategoryBean;
import com.km.kernel.base.BaseViewModel;
import com.km.kernel.repository.CategoryRepository;

import java.util.List;

/**
 * 分类管理数据操作方法 ViewModel
 */
public class CategoryViewModel extends BaseViewModel {
    private static final String TAG = CategoryViewModel.class.getName();
    public MutableLiveData<List<CategoryBean>> dayAllCategoryBeanLiveData = new MutableLiveData();
    private CategoryRepository mCategoryRepository;


    public CategoryViewModel(CategoryRepository mCategoryRepository) {
        this.mCategoryRepository = mCategoryRepository;
    }

    public void insert(String name, String color) {
        if (initCheck()) {
            mCategoryRepository.insert(new CategoryBean(name, color));
        }
    }

    public void findAll() {
        if (initCheck()) {
            mCategoryRepository.findAll(dayAllCategoryBeanLiveData);
        }
    }

    public void likeFind(String name) {
        if (initCheck()) {
            mCategoryRepository.likeFind(name,dayAllCategoryBeanLiveData);
        }
    }

    public List<CategoryBean> findById(long categoryId) {
        if (initCheck()) {
            return mCategoryRepository.findById(categoryId);
        }
        return null;
    }

    public void findName(String name) {
        if (initCheck()) {
            mCategoryRepository.likeFind(name, dayAllCategoryBeanLiveData);
        }
    }


    public void updateById(String name, String color, long id) {
        if (initCheck()) {
            mCategoryRepository.updateById(name, color, id);
        }
    }

    public void deleteById(long id) {
        if (initCheck()) {
            mCategoryRepository.deleteById(id);
        }
    }

    public void deleteAll() {
        if (initCheck()) {
            mCategoryRepository.deleteAll();
        }
    }

    private boolean initCheck(){
        if (mCategoryRepository == null) {
            Log.e(TAG, "initCheck: 请确保实例化操作");
            return false;
        }
        return true;
    }

}
