package com.km.kernel.viewmodelfactory;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.km.kernel.base.ViewModelFactory;
import com.km.kernel.repository.CategoryRepository;
import com.km.kernel.viewmodel.CategoryViewModel;

/**
 * 分类管理数据操作方法 ViewModel 工厂
 */
public class CategoryViewModelByFactory implements ViewModelFactory<CategoryRepository> {

    @Override
    public CategoryRepository getRepository() {
        return CategoryRepository.getInstance();
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new CategoryViewModel(getRepository());
    }
}
