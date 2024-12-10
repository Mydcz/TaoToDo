package com.km.kernel.repository;

import androidx.lifecycle.MutableLiveData;

import com.km.db.CustomDisposable;
import com.km.db.DataBaseManage;
import com.km.db.bean.TaskBean;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;

/**
 * 任务管理数据操作方法
 */
public class TaskRepository {

    private static TaskRepository mInstance;

    private TaskRepository() {
    }

    public static TaskRepository getInstance() {
        if (mInstance == null) {
            synchronized (TaskRepository.class) {
                mInstance = new TaskRepository();
            }
        }
        return mInstance;
    }


    public void insert(TaskBean taskBean) {
        DataBaseManage.getInstance().getTaskDao().insert(taskBean);
    }

    public void findAll(MutableLiveData<List<TaskBean>> findAllTaskBeanLiveData) {
        Flowable<List<TaskBean>> all = DataBaseManage.getInstance().getTaskDao().findAllFlowable();
        CustomDisposable.addDisposable(all, findAllTaskBeanLiveData::postValue);
    }

    public void findByCategoryId(long categoryId,MutableLiveData<List<TaskBean>> findAllTaskBeanLiveData) {
        Flowable<List<TaskBean>> all = DataBaseManage.getInstance().getTaskDao().findByCategoryIdFlowable(categoryId);
        CustomDisposable.addDisposable(all, findAllTaskBeanLiveData::postValue);
    }

    public void findByIsCompleted(boolean isCompleted, MutableLiveData<List<TaskBean>> findAllTaskBeanLiveData) {
        Flowable<List<TaskBean>> byDataListFlowable = DataBaseManage.getInstance().getTaskDao().findByIsCompletedFlowable(isCompleted);
        CustomDisposable.addDisposable(byDataListFlowable, findAllTaskBeanLiveData::postValue);
    }

    public void likeFind(String content, MutableLiveData<List<TaskBean>> findAllTaskBeanLiveData) {
        Flowable<List<TaskBean>> byDataListFlowable = DataBaseManage.getInstance().getTaskDao().likeFindFlowable(content);
        CustomDisposable.addDisposable(byDataListFlowable, findAllTaskBeanLiveData::postValue);
    }

    public void updateTaskType(boolean isCompleted, long id) {
        DataBaseManage.getInstance().getTaskDao().updateTaskType(isCompleted, id);
    }

    public void updateByTask(String title, String description, long dueDate, long categoryId, boolean isCompleted, long id) {
        DataBaseManage.getInstance().getTaskDao().updateByTask(title, description, dueDate, categoryId, isCompleted, id);
    }

    public void deleteById(long id) {
        DataBaseManage.getInstance().getTaskDao().deleteById(id);
    }
    public void deleteByCategoryId(long categoryId) {
        DataBaseManage.getInstance().getTaskDao().deleteByCategoryId(categoryId);
    }

    public void deleteAll() {
        DataBaseManage.getInstance().getTaskDao().deleteAll();
    }

}
