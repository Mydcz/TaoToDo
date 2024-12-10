package com.km.db.bean;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * 任务管理实体类
 * 用于任务记录操作处理
 */
@Entity(tableName = "Task")
public class TaskBean {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String title; // 任务标题
    private String description; // 任务描述
    private long dueDate; // 截止日期
    private long categoryId; // 分类ID
    private boolean isCompleted; // 完成状态
    private long createdAt; // 创建时间

    public TaskBean(String title, String description, long dueDate, long categoryId, boolean isCompleted, long createdAt) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.categoryId = categoryId;
        this.isCompleted = isCompleted;
        this.createdAt = createdAt;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getDueDate() {
        return dueDate;
    }

    public void setDueDate(long dueDate) {
        this.dueDate = dueDate;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
}
