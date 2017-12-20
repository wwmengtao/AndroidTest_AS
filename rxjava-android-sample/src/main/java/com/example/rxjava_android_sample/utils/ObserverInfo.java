package com.example.rxjava_android_sample.utils;


import com.example.rxjava_android_sample.ALog;

import rx.Observer;

/**
 * Created by mengtao1 on 2017/12/14.
 */

public class ObserverInfo {
    private static String TAG_TOP = "ObserverInfo ";

    public static void setTagTop(String tag_top){
        TAG_TOP = tag_top;
    }

    public static abstract class BaseObserver<T> implements Observer<T> {
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
            ALog.Log(TAG+ "onError");
        }

        @Override
        public void onCompleted() {
            nowTime = System.currentTimeMillis();
            ALog.Log2(TAG+ "onComplete, cost time:"+(nowTime - preTime));
        }
    }

    public static class OInteger extends BaseObserver<Integer>{
        public static OInteger get(){
            return new OInteger();
        }

        @Override
        public void onNext(Integer i) {
            ALog.Log2(TAG+ "onNext " + i);
        }
    }

    public static class ObString extends BaseObserver<String>{
        public static ObString get(){
            return new ObString();
        }

        @Override
        public void onNext(String str) {
            ALog.Log2(TAG+ "onNext " + str);
        }
    }
}
