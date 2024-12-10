package com.km.kernel.viewmodelfactory;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.km.kernel.base.ViewModelFactory;
import com.km.kernel.repository.TaskRepository;
import com.km.kernel.viewmodel.TaskViewModel;

/**
 * 任务管理数据操作方法 ViewModel 工厂
 */
public class TaskViewModelByFactory implements ViewModelFactory<TaskRepository> {

    @Override
    public TaskRepository getRepository() {
        return TaskRepository.getInstance();
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new TaskViewModel(getRepository());
    }
}
