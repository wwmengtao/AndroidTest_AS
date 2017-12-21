package com.example.rxjava_android_sample.utils;

import com.example.rxjava_android_sample.ALog;

import rx.Subscriber;

import static com.example.rxjava_android_sample.utils.ObserverInfo.BaseObserver;

/**
 * Created by mengtao1 on 2017/12/20.
 */

public class BpObserverInfo {
    private static final String TAG_TOP = "BpObserverInfo ";

    public static void setTagTop(){
        ObserverInfo.setTagTop(TAG_TOP);
    }

    public static class ObDefault extends BaseObserver<Integer> {
        public static ObDefault get() {
            return new ObDefault();
        }

        @Override
        public void onNext(Integer i) {
            ALog.sleep(1000);
            ALog.Log(TAG + "onNext " + "default#" + "<-" + i + "->");
        }
    }

    public static class ObLong extends BaseObserver<Long>{
        public static ObLong get(){
            return new ObLong();
        }

        @Override
        public void onNext(Long l) {
            ALog.sleep(1000);
            ALog.Log2(TAG+ "onNext " + l);
        }
    }

    public static class SSInteger extends Subscriber<Integer>{
        public static SSInteger get(){
            return new SSInteger();
        }
        @Override
        public void onStart() {
            request(2);
        }
        @Override
        public void onCompleted() {
        }
        @Override
        public void onError(Throwable throwable) {
        }
        @Override
        public void onNext(Integer integer) {
            ALog.Log("ReactivePull#"+"<--------" + integer + "--------->");
            request(1);
        }
    }
}
