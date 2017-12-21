package com.example.rxjava_android_sample.throttlingbackpressure;

import android.os.Bundle;

import com.example.rxjava_android_sample.BaseAcitivity;
import com.example.rxjava_android_sample.R;
import com.example.rxjava_android_sample.utils.ThrObsFetcher;
import com.example.rxjava_android_sample.utils.ThrObserverInfo;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscription;
import rx.schedulers.Schedulers;

public class ThrottlingActivity extends BaseAcitivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_throttling);
        mUnbinder = ButterKnife.bind(this);
        ThrObserverInfo.setTagTop();
    }

    @OnClick(R.id.button21)
    void showSample() {
        showLog("showSample");
        Observable<Long> observable =
                ThrObsFetcher.getSampleObs()
                .observeOn(Schedulers.newThread());
        ThrObserverInfo.ObSample observer = ThrObserverInfo.ObSample.get();
        Subscription ss = observable.subscribe(observer);
        mSubscriptions.add(ss);
    }


    @OnClick(R.id.button22)
    void showtTrottleFirst() {
        showLog("showtTrottleFirst");
        Observable<Long> observable =
                ThrObsFetcher.getThrFirstObs()
                        .observeOn(Schedulers.newThread());
        ThrObserverInfo.ObThrFirst observer = ThrObserverInfo.ObThrFirst.get();
        Subscription ss = observable.subscribe(observer);
        mSubscriptions.add(ss);
    }

    @OnClick(R.id.button23)
    void showDebounce() {
        showLog("showDebounce");
        Observable<Long> observable =
                ThrObsFetcher.getDebounceObs()
                        .observeOn(Schedulers.newThread());
        ThrObserverInfo.ObDebounce observer = ThrObserverInfo.ObDebounce.get();
        Subscription ss = observable.subscribe(observer);
        mSubscriptions.add(ss);
    }

    @OnClick(R.id.button24)
    void showtBuffer() {
        showLog("showtBuffer");
        Observable<List<Long>> observable =
                ThrObsFetcher.getBufferObs()
                        .observeOn(Schedulers.newThread());
        ThrObserverInfo.ObBuffer observer = ThrObserverInfo.ObBuffer.get();
        Subscription ss = observable.subscribe(observer);
        mSubscriptions.add(ss);
    }

    @OnClick(R.id.button25)
    void showtshowtWindow() {
        Observable<Observable<String>> observable =
                ThrObsFetcher.getWindowObs()
                        .observeOn(Schedulers.newThread());
        ThrObserverInfo.ObWindow observer = ThrObserverInfo.ObWindow.get();
        Subscription ss = observable.subscribe(observer);
        mSubscriptions.add(ss);
    }
}
