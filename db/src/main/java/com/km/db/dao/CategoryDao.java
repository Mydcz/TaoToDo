package com.km.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.km.db.bean.CategoryBean;
import java.util.List;
import io.reactivex.rxjava3.core.Flowable;

/**
 * 分类管理数据操作接口
 */
@Dao
public interface CategoryDao {
    //新增分类
    @Insert
    void insert(CategoryBean Category);

    //查询任务
    @Query("select * from Category")
    List<CategoryBean> findAll();

    @Query("select * from Category")
    Flowable<List<CategoryBean>> findAllFlowable();

    @Query("select * from Category where id=:categoryId")
    List<CategoryBean> findById(long categoryId);

    @Query("select * from Category where name like '%' || :name || '%'")
    Flowable<List<CategoryBean>> likeFindFlowable(String name);

    //修改操作
    @Query("update  Category set name =:name ,color =:color  where id =:id ")
    void updateById(String name, String color, long id);

    //删除操作
    @Query("delete from Category where id=:id")
    void deleteById(long id);

    @Query("delete from Category")
    void deleteAll();

}
