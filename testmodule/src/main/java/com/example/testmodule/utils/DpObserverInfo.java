package com.example.testmodule.utils;

import com.example.testmodule.ALog;

import io.reactivex.observers.DisposableObserver;

/**
 * Created by mengtao1 on 2017/12/15.
 */

public class DpObserverInfo {
    private static String TAG_TOP = "DpObserverInfo ";

    public static abstract class BaseObserver<T> extends DisposableObserver<T> {
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

}
