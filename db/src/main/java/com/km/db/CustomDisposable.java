package com.km.db;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * 数据库数据线程安全
 */
public class CustomDisposable {

    /**
     * Flowable
     * @param flowable
     * @param consumer
     * @param <T>
     */
    public static <T> void addDisposable(Flowable<T> flowable, Consumer<T> consumer) {
        CompositeDisposable compositeDisposable = new CompositeDisposable();

        Disposable disposable = flowable
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(consumer);

        compositeDisposable.add(disposable);
    }

}
