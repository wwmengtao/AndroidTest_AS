package com.example.rxjava_android_sample.throttlingbackpressure;

import android.os.Bundle;

import com.example.rxjava_android_sample.BaseAcitivity;
import com.example.rxjava_android_sample.R;
import com.example.rxjava_android_sample.utils.BpObsFetcher;
import com.example.rxjava_android_sample.utils.BpObserverInfo;

import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
/**
 * 一、流量控制方法：反压(BackPressure)、打包、节流(Throttling)、阻塞。反压从生产者入手，通过ReactivePull方式以及反压操作符使得生产者
 * 看起来“降低”了生产率；其余方法都是从消费者入手，对生产者发送的数据采用某种形式的处理以匹配消费者消费数据的速度。
 * 二、RxJava1.X中处理Hot Observables和cold Observables存在的问题
 * Cold Observables：指的是那些在订阅之后才开始发送事件的Observable（每个Subscriber都能接收到完整的事件）。
 * Hot Observables:指的是那些在创建了Observable之后，（不管是否订阅）就开始发送事件的Observable
 * Hot Observable这一类是不支持反压的，而是Cold Observable这一类中也有一部分并不支持反压（比如interval，timer等操作符创建的Observable）。
 * 都是Observable，结果有的支持反压，有的不支持，这就是RxJava1.X的一个问题。RxJava2.X已经将被观察者分为支持/不支持反压两种方式。
 * 三、反压实现的两种方式：ReactivePull方式以及反压操作符，消费者执行request(n)。对不支持ReactivePull操作的Observable通过类似onBackpressurebuffer、onBackpressureDrop
 * 操作使得其看上去支持ReactivePull方式，总体上看，生产者“降低”了生产速率。
 * 四、对不支持反压的Observable执行onBackpressurebuffer，onBackpressureDrop等操作使其支持反压。
 * @author mengtao1 关于背压
 */
public class BackpressureActivity extends BaseAcitivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backpressure);
        mUnbinder = ButterKnife.bind(this);
        BpObserverInfo.setTagTop();
    }

    @OnClick(R.id.button10)
    void DefaultObservableBuffer() {
        showLog("DefaultObservableBuffer");
        Observable<Integer> observable =
            BpObsFetcher.getDefaultObs()
                .subscribeOn(Schedulers.io())//subscribeOn:指定Observable发射数据的线程
                .observeOn(AndroidSchedulers.mainThread());//observeOn:指定观察者运行的线程
        BpObserverInfo.ObDefault observer = BpObserverInfo.ObDefault.get();
        Subscription ss = observable.subscribe(observer);
        mSubscriptions.add(ss);
    }

    @OnClick(R.id.button11)
    void MissingBackpressureException() {
        showLog("MissingBackpressureException");
        Observable<Long> observable = Observable.interval(1, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())//subscribeOn:指定Observable发射数据的线程
                .observeOn(Schedulers.newThread());//observeOn:指定观察者运行的线程
        BpObserverInfo.ObLong observer = BpObserverInfo.ObLong.get();
        Subscription ss = observable.subscribe(observer);
        mSubscriptions.add(ss);
    }

    @OnClick(R.id.button12)
    void showOnBackpressureBuffer() {//对MissingBackpressureException中的内容进行反压
        showLog("showOnBackpressureBuffer");
        Observable<Long> observable = Observable.interval(1, TimeUnit.MILLISECONDS)
                .onBackpressureBuffer()//onBackpressureBuffer：对于不支持反压的Observable使用onBackpressureBuffer操作使其支持反压
                .subscribeOn(Schedulers.io())//subscribeOn:指定Observable发射数据的线程
                .observeOn(Schedulers.newThread());//observeOn:指定观察者运行的线程
        BpObserverInfo.ObLong observer = BpObserverInfo.ObLong.get();
        Subscription ss = observable.subscribe(observer);
        mSubscriptions.add(ss);
    }

    @OnClick(R.id.button13)
    void showOnBackpressureDrop() {
        showLog("showOnBackpressureDrop");
        Observable<Long> observable = Observable.interval(1, TimeUnit.MILLISECONDS)
                .onBackpressureDrop()//onBackpressureDrop：对于不支持反压的Observable使用onBackpressureDrop操作使其支持反压
                .subscribeOn(Schedulers.io())//subscribeOn:指定Observable发射数据的线程
                .observeOn(Schedulers.newThread());//observeOn:指定观察者运行的线程
        BpObserverInfo.ObLong observer = BpObserverInfo.ObLong.get();
        Subscription ss = observable.subscribe(observer);
        mSubscriptions.add(ss);
    }
    @OnClick(R.id.button14)
    void showOnBackpressureLatest() {
        showLog("showOnBackpressureLatest");
        Observable<Long> observable = Observable.interval(1, TimeUnit.MILLISECONDS)
                .onBackpressureLatest()//onBackpressureLatest：对于不支持反压的Observable使用onBackpressureLatest操作使其支持反压
                .subscribeOn(Schedulers.io())//subscribeOn:指定Observable发射数据的线程
                .observeOn(Schedulers.newThread());//observeOn:指定观察者运行的线程
        BpObserverInfo.ObLong observer = BpObserverInfo.ObLong.get();
        Subscription ss = observable.subscribe(observer);
        mSubscriptions.add(ss);
    }
    @OnClick(R.id.button15)
    void showReactivePull() {//ReactivePull：响应式拉取，此方法对于支持反压的Observable适用。
        showLog("showReactivePull");
        Observable<Integer> observable = Observable.range(0, 10000)
                .subscribeOn(Schedulers.io())//subscribeOn:指定Observable发射数据的线程
                .observeOn(Schedulers.newThread());//observeOn:指定观察者运行的线程
        BpObserverInfo.SSInteger observer = BpObserverInfo.SSInteger.get();
        Subscription ss = observable.subscribe(observer);
        mSubscriptions.add(ss);//此时无法取消订阅，Observable.range产生的数据会一直发送直到发送完毕
    }

}
