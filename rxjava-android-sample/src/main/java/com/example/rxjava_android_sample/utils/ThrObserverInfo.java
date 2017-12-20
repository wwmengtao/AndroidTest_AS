package com.example.rxjava_android_sample.utils;

import android.os.SystemClock;

import com.example.rxjava_android_sample.ALog;

import java.util.List;

import rx.Observable;

import static com.example.rxjava_android_sample.utils.ObserverInfo.BaseObserver;
/**
 * Created by mengtao1 on 2017/12/20.
 */

public class ThrObserverInfo {
    private static final String TAG_TOP = "ThrObserverInfo ";

    public static void setTagTop(){
        ObserverInfo.setTagTop(TAG_TOP);
    }

    public static class ObSample extends BaseObserver<Long> {
        public static ObSample get() {
            return new ObSample();
        }

        @Override
        public void onNext(Long i) {
            SystemClock.sleep(1000);
            ALog.Log(TAG + "onNext " + "sample#" + "<--------" + i + "--------->");
        }
    }

    public static class ObThrFirst extends BaseObserver<Long> {
        public static ObThrFirst get() {
            return new ObThrFirst();
        }

        @Override
        public void onNext(Long i) {
            SystemClock.sleep(1000);
            ALog.Log(TAG + "onNext " + "trottleFirst#" + "<--------" + i + "--------->");
        }
    }

    public static class ObDebounce extends BaseObserver<Long> {
        public static ObDebounce get() {
            return new ObDebounce();
        }

        @Override
        public void onNext(Long i) {
            SystemClock.sleep(1000);
            ALog.Log(TAG + "onNext " + "debounce#" + "<--------" + i + "--------->");
        }
    }

    public static class ObBuffer extends BaseObserver<List<Long>> {
        public static ObBuffer get() {
            return new ObBuffer();
        }

        @Override
        public void onNext(List<Long> i) {
            SystemClock.sleep(100);//如果睡眠时间大于ThrObsFetcher.getBufferObs中的buffer的timespan，那么将报错
            ALog.Log(TAG + "onNext " + "buffer#\n" + "<--" + i.toString() + "-->");
        }
    }

    public static class ObWindow extends BaseObserver<Observable<String>> {
        public static ObWindow get() {
            return new ObWindow();
        }

        @Override
        public void onNext(Observable<String> i) {
            ALog.sleep(1000);
            ALog.Log(TAG + "onNext " + "window#\n");
            i.subscribe(ObserverInfo.ObString.get());
        }
    }
}