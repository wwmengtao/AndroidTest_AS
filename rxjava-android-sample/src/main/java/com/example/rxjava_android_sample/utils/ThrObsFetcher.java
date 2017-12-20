package com.example.rxjava_android_sample.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by mengtao1 on 2017/12/20.
 */

public class ThrObsFetcher {
    public static Observable<Long> getSampleObs() {
        return Observable.interval(1, TimeUnit.MILLISECONDS)
                .sample(1000, TimeUnit.MILLISECONDS);

    }

    public static Observable<List<Long>> getBufferObs() {
        return  Observable.interval(10, TimeUnit.MILLISECONDS)
            .buffer(100, TimeUnit.MILLISECONDS);
    }

    public static Observable<Long> getThrFirstObs() {
        return Observable.interval(1, TimeUnit.MILLISECONDS)
                .throttleFirst(1000, TimeUnit.MILLISECONDS);
    }

    public static Observable<Long> getDebounceObs() {
        return
        //每600ms发送一个事件
        Observable.interval(600, TimeUnit.MILLISECONDS)
                .filter(new Func1<Long, Boolean>() {
                    @Override
                    public Boolean call(Long aLong) {
                        return aLong % 2 == 0;   //为偶数时继续
                    }
                })
                .debounce(1000, TimeUnit.MILLISECONDS);//如果超过1000ms内没有收到事件，则发送出超时前的最后一个事件
    }

    public static Observable<Observable<String>> getWindowObs() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            list.add("\"hello : " + i+"\"");
        }
        return Observable.from(list).window(4);
    }
}
