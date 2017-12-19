package com.example.rxjava2_android_sample.utils;

import com.example.rxjava2_android_sample.ALog;
import com.example.rxjava2_android_sample.model.User;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by mengtao1 on 2017/12/15.
 */

public class DpObserverInfo {
    private static final String TAG_TOP = "DpObserverInfo ";

    private static abstract class BaseObserver<T> extends DisposableObserver<T> {
        protected String TAG = null;
        protected long preTime, nowTime;
        public BaseObserver(){
            String tag = this.toString();
            tag = tag.substring(tag.indexOf('$')+1, tag.lastIndexOf('@'));
            TAG = TAG_TOP + tag + " ";
            preTime = System.currentTimeMillis();
        }

        @Override
        public void onNext(T t) {

        }

        @Override
        public void onError(Throwable e) {
            ALog.Log2(TAG+ "onError");
        }

        @Override
        public void onComplete() {
            nowTime = System.currentTimeMillis();
            ALog.Log2(TAG+ "onComplete, cost time:"+(nowTime - preTime));
        }
    }

    public static class ObLong extends BaseObserver<Long> {
        public static ObLong get(){
            return new ObLong();
        }

        @Override
        public void onNext(Long l) {
            ALog.Log2(TAG+ "onNext " + l);
        }
    }

    public static class ObObLong extends BaseObserver<Observable<Long>> {
        public static ObObLong get(){
            return new ObObLong();
        }

        @Override
        public void onNext(Observable<Long> observable) {
            ALog.Log2(TAG+ "onNext ");
            observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long value) {
                        ALog.Log2(TAG+ "accept:" + value);
                    }
                });
        }
    }

    public static class ObListUser extends BaseObserver<List<User>> {
        public static ObListUser get(){
            return new ObListUser();
        }

        @Override
        public void onNext(List<User> l) {
            ALog.Log2(TAG+ "onNext " + l.size());
        }
    }

    public static class ObInteger extends BaseObserver<Integer> {
        public static ObInteger get(){
            return new ObInteger();
        }

        @Override
        public void onNext(Integer l) {
            ALog.Log2(TAG+ "onNext " + l);
        }
    }

    public static class ObString extends BaseObserver<String> {
        public static ObString get(){
            return new ObString();
        }

        @Override
        public void onNext(String l) {
            ALog.Log2(TAG+ "onNext " + l);
        }
    }
}
