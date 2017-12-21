package com.example.rxjava2_android_sample.utils;

import com.example.rxjava2_android_sample.ALog;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by mengtao1 on 2017/12/21.
 */

public class BpObserverInfo {
    private static final String TAG_TOP = "BpObserverInfo ";

    public static void setTagTop(){
        DpObserverInfo.setTagTop(TAG_TOP);
    }

    public static class ObDefault extends DpObserverInfo.BaseObserver<Integer> {
        public static ObDefault get() {
            return new ObDefault();
        }

        @Override
        public void onNext(Integer i) {
            ALog.sleep(10);
            ALog.Log(TAG + "onNext " + "default#" + "<-" + i + "->");
        }
    }

    /*------------------------------------------BaseConsumer---------------------------------------------*/
    public static abstract class BaseConsumer<T> implements Consumer<T> {
        protected String TAG = null;

        public BaseConsumer() {
            String tag = this.toString();
            tag = tag.substring(tag.indexOf('$') + 1, tag.lastIndexOf('@'));
            TAG = TAG_TOP + tag + " ";
        }
    }

    public static class CsInteger extends BaseConsumer<Integer>{
        public static CsInteger get() {
            return new CsInteger();
        }

        @Override
        public void accept(@NonNull Integer integer) throws Exception {
            ALog.sleep(10);
            ALog.Log(TAG + "accept " + "<-" + integer + "->");
        }
    }

    /*------------------------------------------Subscriber---------------------------------------------*/
    public static class SSInteger implements Subscriber<Integer> {
        private static final String TAG = TAG_TOP + "SSInteger";
        private Subscription mSubscription = null;

        public static SSInteger get() {
            return new SSInteger();
        }

        public Subscription getSubscription(){
            ALog.Log(TAG+"#getSubscription "+(mSubscription != null));
            return mSubscription;
        }

        @Override
        public void onSubscribe(Subscription s) {
            ALog.Log(TAG+"#onSubscribe");
            mSubscription = s;
//        mSubscription.request(Long.MAX_VALUE);  //下游处理事件能力值，不写request的话，下游收不到上游发送的数值
            mSubscription.request(1);
        }
        @Override
        public void onNext(Integer s) {
            ALog.Log2(TAG+"#onNext: "+s.intValue());
            mSubscription.request(1);//接收到数据后马上再请求发送一个数据
        }

        @Override
        public void onError(Throwable t) {
            ALog.Log(TAG+"#onError:\n"+t.fillInStackTrace().toString());
        }

        @Override
        public void onComplete() {
            ALog.Log(TAG+"#onComplete");
        }
    }
}
