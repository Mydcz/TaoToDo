package com.km.db.bean;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * 分类管理实体类
 */
@Entity(tableName = "Category")
public class CategoryBean {
    @PrimaryKey(autoGenerate = true)
    private long id; // 分类ID
    private String name; // 分类名称
    private String color; // 分类颜色

    public CategoryBean(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public java.lang.String getName() {
        return name;
    }

    public void setName(java.lang.String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
