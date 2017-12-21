package com.example.rxjava2_android_sample.throttlingbackpressure;

import android.os.Bundle;

import com.example.rxjava2_android_sample.BaseAcitivity;
import com.example.rxjava2_android_sample.R;
import com.example.rxjava2_android_sample.utils.BpObsFetcher;
import com.example.rxjava2_android_sample.utils.BpObserverInfo;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class BackpressureActivity extends BaseAcitivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backpressure);
        mUnbinder = ButterKnife.bind(this);
        BpObserverInfo.setTagTop();
    }

    /* Observable.observeOn注释如下：
     * Modifies an ObservableSource to perform its emissions and notifications on a specified {@link Scheduler},
     * asynchronously with an unbounded buffer with {@link Flowable#bufferSize()} "island size".
     * 上述说明Observable.observeOn异步情况下会创建一个无界buffer，大小为Flowable#bufferSize()，超过
     * 这个数值不会报MissingBackpressureException。说明RX2中，Observable不支持反压。
     */
    @OnClick(R.id.button10)
    void ObservableBackpressureTest() {
        showLog("ObservableBackpressureTest");
        Observable<Integer> observable =
            BpObsFetcher.getDefaultobs()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread());
        mComDisposable.add(observable.subscribeWith(BpObserverInfo.ObDefault.get()));

    }
    
    @OnClick(R.id.button11) void BackpressureStrategyMissing() {
        showLog("BackpressureStrategyMissing");
        Flowable<Integer> flowable =
            BpObsFetcher.getBackpressureObs(BackpressureStrategy.MISSING)//如果流的速度无法保持同步，可能会抛出MissingBackpressureException或IllegalStateException。
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread());
        mComDisposable.add(flowable.subscribe(BpObserverInfo.CsInteger.get()));
    }

    @OnClick(R.id.button12)
    void BackpressureStrategyError() {
        showLog("BackpressureStrategyError");
        Flowable<Integer> flowable =
            BpObsFetcher.getBackpressureObs(BackpressureStrategy.ERROR)//BackpressureStrategy.ERROR：会在下游跟不上速度时抛出MissingBackpressureException。
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread());
        mComDisposable.add(flowable.subscribe(BpObserverInfo.CsInteger.get()));
    }

    @OnClick(R.id.button13)
    void showOnBackpressureBuffer() {
        showLog("showOnBackpressureBuffer");
        Flowable<Integer> flowable =
            BpObsFetcher.getBackpressureObs(BackpressureStrategy.BUFFER)//BackpressureStrategy.BUFFER：反压策略
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread());
        mComDisposable.add(flowable.subscribe(BpObserverInfo.CsInteger.get()));
    }

    @OnClick(R.id.button14)
    void showOnBackpressureDrop() {
        showLog("showOnBackpressureDrop");
        Flowable<Integer> flowable =
            BpObsFetcher.getBackpressureObs(BackpressureStrategy.DROP)//BackpressureStrategy.DROP：反压策略
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread());
        mComDisposable.add(flowable.subscribe(BpObserverInfo.CsInteger.get()));
    }

    @OnClick(R.id.button15)
    void showOnBackpressureLatest() {
        showLog("showOnBackpressureLatest");
        Flowable<Integer> flowable =
            BpObsFetcher.getBackpressureObs(BackpressureStrategy.LATEST)//BackpressureStrategy.LATEST：反压策略
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread());
        mComDisposable.add(flowable.subscribe(BpObserverInfo.CsInteger.get()));
    }

    /**
     * 在下列mSubscription.request(1)的情况以及发送数据量为10*FlowableBufferSize的情况下：
     * 1、BackpressureStrategy.MISSING：会提示"io.reactivex.exceptions.MissingBackpressureException: Queue is full?!"，
     * 流程终止于onError(Throwable t)。
     * 2、BackpressureStrategy.ERROR：会提示"io.reactivex.exceptions.MissingBackpressureException:
     * create: could not emit value due to lack of requests"，流程终止于onError(Throwable t)。
     * 3、BackpressureStrategy.DROP等其他策略正常走完流程，执行到onComplete()。
     */
    @OnClick(R.id.button16)
    void pullViaBackpressureStrategy() {
        showLog("pullViaBackpressureStrategy");
        Flowable<Integer> flowable =
            BpObsFetcher.getPullBackpressureObs(1500, BackpressureStrategy.BUFFER)//可以尝试不同的策略
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread());
        BpObserverInfo.SSInteger observer = BpObserverInfo.SSInteger.get();
        flowable.subscribe(observer);
        mSubscriptions.add(observer.getSubscription());//退出Activity之后停止数据源的数据发射
    }

}
