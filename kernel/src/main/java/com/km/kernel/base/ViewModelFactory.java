package com.km.kernel.base;

import androidx.lifecycle.ViewModelProvider;

/**
 * ViewModel工厂类
 * @param <T>
 */
public abstract interface ViewModelFactory<T> extends  ViewModelProvider.Factory{
    abstract T getRepository();
}
