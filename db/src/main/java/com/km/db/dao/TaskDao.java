package com.km.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.km.db.bean.TaskBean;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;

/**
 * 任务管理数据操作接口
 */
@Dao
public interface TaskDao {
    //新增操作
    @Insert
    void insert(TaskBean task);

    //查询操作
    @Query("select * from Task")
    List<TaskBean> findAll();

    @Query("select * from Task")
    Flowable<List<TaskBean>> findAllFlowable();

    @Query("select * from Task where categoryId =:categoryId")
    List<TaskBean> findByCategoryId(long categoryId);

    @Query("select * from Task where categoryId =:categoryId")
    Flowable<List<TaskBean>> findByCategoryIdFlowable(long categoryId);

    @Query("select * from Task where isCompleted =:isCompleted")
    List<TaskBean> findByIsCompleted(boolean isCompleted);

    @Query("select * from Task where isCompleted =:isCompleted")
    Flowable<List<TaskBean>> findByIsCompletedFlowable(boolean isCompleted);


    //模糊查询
    //@Query("select * from Task where title like '%' || :content || '%' or description like '%' || :content || '%'")
    @Query("select * from Task where title like '%' || :content || '%'")
    Flowable<List<TaskBean>> likeFindFlowable(String content);

    //修改操作

    @Query("update  Task set isCompleted =:isCompleted where id =:id")
    void updateTaskType(boolean isCompleted, long id);

    @Query("update  Task set title =:title, description =:description , dueDate =:dueDate,categoryId=:categoryId,isCompleted=:isCompleted where   id =:id")
    void updateByTask(String title,String description,  long dueDate,long categoryId,boolean isCompleted, long id);

    //删除操作
    @Query("delete from Task where id=:id")
    void deleteById(long id);

    @Query("delete from Task where categoryId =:categoryId")
    void deleteByCategoryId(long categoryId);

    @Query("delete from Task")
    void deleteAll();
}
