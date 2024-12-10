package com.km.kernel.viewmodel;


import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.km.db.bean.TaskBean;
import com.km.kernel.base.BaseViewModel;
import com.km.kernel.repository.TaskRepository;

import java.util.List;

/**
 * 任务管理数据操作方法 ViewModel
 */
public class TaskViewModel extends BaseViewModel {
    private static final String TAG = TaskViewModel.class.getName();
    public MutableLiveData<List<TaskBean>> dayAllTaskBeanLiveData = new MutableLiveData();
    private TaskRepository mTaskRepository;


    public TaskViewModel(TaskRepository mTaskRepository) {
        this.mTaskRepository = mTaskRepository;
    }

    public void insert(String title, String description, long dueDate, long categoryId) {
        if (initCheck()) {
            mTaskRepository.insert(new TaskBean(title, description, dueDate, categoryId, false, System.currentTimeMillis()));
        }
    }

    public void findAll() {
        if (initCheck()) {
            mTaskRepository.findAll(dayAllTaskBeanLiveData);
        }
    }

    public void findByCategoryId(long categoryId) {
        if (initCheck()) {
            mTaskRepository.findByCategoryId(categoryId, dayAllTaskBeanLiveData);
        }
    }

    public void findByIsCompleted(boolean isCompleted) {
        if (initCheck()) {
            mTaskRepository.findByIsCompleted(isCompleted, dayAllTaskBeanLiveData);
        }
    }

    public void likeFind(String name) {
        if (initCheck()) {
            mTaskRepository.likeFind(name, dayAllTaskBeanLiveData);
        }
    }


    public void updateTaskType(boolean isCompleted, long id) {
        if (initCheck()) {
            mTaskRepository.updateTaskType(isCompleted, id);
        }
    }

    public void updateByTask(String title, String description, long dueDate, long categoryId, boolean isCompleted, long id) {
        if (initCheck()) {
            mTaskRepository.updateByTask(title, description, dueDate, categoryId, isCompleted, id);
        }
    }

    public void deleteById(long id) {
        if (initCheck()) {
            mTaskRepository.deleteById(id);
        }
    }

    public void deleteByCategoryId(long categoryId) {
        if (initCheck()) {
            mTaskRepository.deleteByCategoryId(categoryId);
        }
    }

    public void deleteAll() {
        if (initCheck()) {
            mTaskRepository.deleteAll();
        }
    }

    //校验实例化
    private boolean initCheck() {
        if (mTaskRepository == null) {
            Log.e(TAG, "initCheck: 请确保实例化操作");
            return false;
        }
        return true;
    }

}
