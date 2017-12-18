package com.example.rxjava2_android_sample.utils;

import com.example.rxjava2_android_sample.ALog;
import com.example.rxjava2_android_sample.model.User;

import java.util.List;

import io.reactivex.observers.DisposableObserver;

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

    public static ObLong getObLong(){
        return new ObLong();
    }

    private static class ObLong extends BaseObserver<Long> {

        @Override
        public void onNext(Long l) {
            ALog.Log2(TAG+ "onNext " + l);
        }
    }

    public static ObListUser getObListUser(){
        return new ObListUser();
    }

    private static class ObListUser extends BaseObserver<List<User>> {
        @Override
        public void onNext(List<User> l) {
            ALog.Log2(TAG+ "onNext " + l.size());
        }
    }
}
