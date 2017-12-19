package com.example.rxjava2_android_sample;

import android.os.Bundle;
import android.view.View;

import com.example.rxjava2_android_sample.model.User;
import com.example.rxjava2_android_sample.utils.DpObserverInfo;
import com.example.rxjava2_android_sample.utils.ObsFetcher;
import com.example.rxjava2_android_sample.utils.ObserverInfo;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.BiFunction;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * 操作符解释见网页：https://mcxiaoke.gitbooks.io/rxdocs/content/Operators.html
 */
public class OperatorsActivity extends BaseAcitivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operators);
        mUnbinder = ButterKnife.bind(this);
    }

    @OnClick({R.id.btn1})
    public void IntervalMap(View view){//应用场景：定时从网络上获取数据
        Observable<Long> observable =
            ObsFetcher.getIntervalMapObs()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        DisposableObserver<Long> observer = observable.subscribeWith(DpObserverInfo.ObLong.get());
        mComDisposable.add(observer);
    }

    @OnClick({R.id.btn2})
    public void IntervalFlatMap(){//应用场景：定时从网络上获取数据
        Observable<List<User>> observable =
            ObsFetcher.getIntervalFlatMapObs()//注意与getIntervalMapFlatMapObs的区别
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
        DisposableObserver<List<User>> observer = observable.subscribeWith(DpObserverInfo.ObListUser.get());
        mComDisposable.add(observer);
    }

    @OnClick({R.id.btn3})
    public void Window(){//应用场景：获取一定量的数据，截断这些数据分多次送给观察者，而不是一个一个。
        Observable<Observable<Long>> observable =
            Observable.interval(1, TimeUnit.SECONDS)
                .take(12)//只要前12个数据
                .window(3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        DisposableObserver<Observable<Long>> observer =
                observable.subscribeWith(DpObserverInfo.ObObLong.get());
        mComDisposable.add(observer);
    }

    //如下为采样操作
    @OnClick({R.id.btn4})
    public void Timer(){//应用场景：延时做某种操作
        Observable<Long> observable =
                Observable.timer(2, TimeUnit.SECONDS)//延迟2秒后发送数字0
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
        DisposableObserver<Long> observer =
                observable.subscribeWith(DpObserverInfo.ObLong.get());
        mComDisposable.add(observer);
    }

    @OnClick({R.id.btn5})
    //应用场景：数据采样，以下列例子为例，采样每个ObsFetcher.intervalDuration时间间隔内的最后一个数据。
    //当然也可以使用throttleFirst或者sample
    public void ThrottleLast() {
        Observable<Integer> observable =
                ObsFetcher.getIntegerObs()
                    .sample(ObsFetcher.intervalDuration, TimeUnit.MILLISECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        DisposableObserver<Integer> observer =
                observable.subscribeWith(DpObserverInfo.ObInteger.get());
        mComDisposable.add(observer);
    }

    @OnClick({R.id.btn6})
    public void Skip() {
        Observable<Integer> observable =
            Observable.just(1, 2, 3, 4, 5)//将指定数值包装成Observable发射出去
                .skip(2)//跳过开头的2个数据，最终观测到的内容是3、4、5
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        DisposableObserver<Integer> observer =
                observable.subscribeWith(DpObserverInfo.ObInteger.get());
        mComDisposable.add(observer);
    }

    //如下为变换操作
    @OnClick({R.id.btn7})
    public void Scan() {
        Observable<Integer> observable =
                Observable.just(1, 2, 3, 4, 5)//将指定数值包装成Observable发射出去
                        .scan(new BiFunction<Integer, Integer, Integer>() {
                            @Override
                            public Integer apply(Integer int1, Integer int2) throws Exception {
                                return int1 + int2;
                            }
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
        DisposableObserver<Integer> observer =
                observable.subscribeWith(DpObserverInfo.ObInteger.get());
        mComDisposable.add(observer);
    }

    //Reduce：按顺序对Observable发射的每项数据应用一个函数并发射最终的值，与Scan的不同在于Scan将每个步骤的数值都输出
    @OnClick({R.id.btn8})
    public void Reduce() {
        Observable.just(1, 2, 3, 4, 5)//将指定数值包装成Observable发射出去
            .reduce(new BiFunction<Integer, Integer, Integer>() {
                @Override
                public Integer apply(Integer int1, Integer int2) throws Exception {
                    return int1 + int2;
                }
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(ObserverInfo.getMaybeObserver());
    }

    //以下为结合操作
    @OnClick({R.id.btn9})
    public void merge(){
        final String[] aStrings = {"A1", "A2", "A3", "A4"};
        final String[] bStrings = {"B1", "B2", "B3"};
        final Observable<String> aObservable = Observable.fromArray(aStrings);
        final Observable<String> bObservable = Observable.fromArray(bStrings);
        Observable<String> observable =
            Observable.merge(aObservable, bObservable)//merge：合并多个Observables的发射物，可能会让合并的Observables发射的数据交错
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        DisposableObserver<String> observer =
                observable.subscribeWith(DpObserverInfo.ObString.get());
        mComDisposable.add(observer);
    }

    @OnClick({R.id.btn10})
    public void concat(){
        final String[] aStrings = {"A1", "A2", "A3", "A4"};
        final String[] bStrings = {"B1", "B2", "B3"};
        final Observable<String> aObservable = Observable.fromArray(aStrings);
        final Observable<String> bObservable = Observable.fromArray(bStrings);
        Observable<String> observable =
                Observable.concat(aObservable, bObservable)//concat：合并多个Observables的发射物，不会让合并的Observables发射的数据交错
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
        DisposableObserver<String> observer =
                observable.subscribeWith(DpObserverInfo.ObString.get());
        mComDisposable.add(observer);
    }
}
