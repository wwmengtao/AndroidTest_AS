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
import io.reactivex.functions.Predicate;
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
    //1、创建操作
    @OnClick({R.id.btn10})
    public void timer(){//应用场景：延时做某种操作
        showLog("timer");
        Observable<Long> observable =
                Observable.timer(2, TimeUnit.SECONDS)//延迟2秒后发送数字0
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
        DisposableObserver<Long> observer =
                observable.subscribeWith(DpObserverInfo.ObLong.get());
        mComDisposable.add(observer);
    }

    Observable<Integer> DeferObservable =
            ObsFetcher.getDeferObservable();//defer：只发送当前监听时刻的数值
    @OnClick({R.id.btn11})
    public void defer(){
        showLog("defer");
        ObsFetcher.setDeferValue();//改变defer中的初始值
        DisposableObserver<Integer> observer =
                DeferObservable.subscribeWith(DpObserverInfo.ObInteger.get());
        mComDisposable.add(observer);
    }

    //2、变换操作
    @OnClick({R.id.btn20})
    public void IntervalMap(View view){//应用场景：定时从网络上获取数据
        showLog("IntervalMap");
        Observable<Long> observable =
                ObsFetcher.getIntervalMapObs()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
        DisposableObserver<Long> observer = observable.subscribeWith(DpObserverInfo.ObLong.get());
        mComDisposable.add(observer);
    }

    @OnClick({R.id.btn21})
    public void IntervalFlatMap(){//应用场景：定时从网络上获取数据
        showLog("IntervalFlatMap");
        Observable<List<User>> observable =
                ObsFetcher.getIntervalFlatMapObs()//注意与getIntervalMapFlatMapObs的区别
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread());
        DisposableObserver<List<User>> observer = observable.subscribeWith(DpObserverInfo.ObListUser.get());
        mComDisposable.add(observer);
    }

    @OnClick({R.id.btn22})
    public void Window(){//应用场景：获取一定量的数据，截断这些数据分多次送给观察者，而不是一个一个。
        showLog("Window");
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

    @OnClick({R.id.btn23})
    public void scan() {
        showLog("scan");
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

    //3、过滤操作
    @OnClick({R.id.btn30})
    //应用场景：数据采样，以下列例子为例，采样每个ObsFetcher.TimeDuration时间间隔内的最后一个数据。
    public void throttleLast() {
        showLog("throttleLast");
        Observable<Integer> observable =
                ObsFetcher.getFilterObs()
                        .throttleLast(ObsFetcher.TimeDuration, TimeUnit.MILLISECONDS)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
        DisposableObserver<Integer> observer =
                observable.subscribeWith(DpObserverInfo.ObInteger.get());
        mComDisposable.add(observer);
    }

    @OnClick({R.id.btn31})
    //应用场景：数据采样，以下列例子为例，采样每个ObsFetcher.TimeDuration时间间隔内的第一个数据。
    public void throttleFirst() {
        showLog("throttleFirst");
        Observable<Integer> observable =
                ObsFetcher.getFilterObs()
                        .throttleFirst(ObsFetcher.TimeDuration, TimeUnit.MILLISECONDS)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
        DisposableObserver<Integer> observer =
                observable.subscribeWith(DpObserverInfo.ObInteger.get());
        mComDisposable.add(observer);
    }

    @OnClick({R.id.btn32})
    //应用场景：数据采样，以下列例子为例，采样每个ObsFetcher.TimeDuration时间间隔内的最后一个数据。
    public void sample() {
        showLog("sample");
        Observable<Integer> observable =
                ObsFetcher.getFilterObs()
                        .sample(ObsFetcher.TimeDuration, TimeUnit.MILLISECONDS)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
        DisposableObserver<Integer> observer =
                observable.subscribeWith(DpObserverInfo.ObInteger.get());
        mComDisposable.add(observer);
    }

    @OnClick({R.id.btn33})
    public void debounce(){//应用场景：防止用户误触碰某控件，类似于防抖动作
        showLog("debounce");
        Observable<Integer> observable =
                ObsFetcher.getFilterObs()
                        .debounce(ObsFetcher.TimeDuration, TimeUnit.MILLISECONDS);//debounce操作符会过滤掉发射速率过快(发射间隔时间小于ObsFetcher.TimeDuration)的数据项
        DisposableObserver<Integer> observer =
                observable.subscribeWith(DpObserverInfo.ObInteger.get());
        mComDisposable.add(observer);
    }

    @OnClick({R.id.btn34})
    public void filter(){
        showLog("filter");
        Observable<Integer> observable =
                Observable.just(1, 2, 3, 4, 5, 6)
                        .filter(new Predicate<Integer>() {//filter:过滤操作
                            @Override
                            public boolean test(Integer integer) throws Exception {
                                return integer % 2 == 0;
                            }
                        });
        DisposableObserver<Integer> observer =
                observable.subscribeWith(DpObserverInfo.ObInteger.get());
        mComDisposable.add(observer);
    }

    @OnClick({R.id.btn35})
    public void distinct(){
        showLog("distinct");
        Observable<Integer> observable =
                Observable.just(1, 2, 2,3, 3, 3, 4)
                        .distinct();//过滤掉重复数据项
        DisposableObserver<Integer> observer =
                observable.subscribeWith(DpObserverInfo.ObInteger.get());
        mComDisposable.add(observer);
    }

    @OnClick({R.id.btn36})
    public void skip() {
        showLog("skip");
        Observable<Integer> observable =
                Observable.just(1, 2, 3, 4, 5)//将指定数值包装成Observable发射出去
                        .skip(2)//跳过开头的2个数据，最终观测到的内容是3、4、5
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
        DisposableObserver<Integer> observer =
                observable.subscribeWith(DpObserverInfo.ObInteger.get());
        mComDisposable.add(observer);
    }

    //4、辅助操作
    @OnClick({R.id.btn40})
    public void delay(){//应用场景：延时做某种操作，每个数据项都延迟发射，效果是数据项整体延迟
        showLog("delay");
        Observable<Integer> observable =
                Observable.just(1, 2, 3).
                        delay(2, TimeUnit.SECONDS)//延迟2秒后发送
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
        DisposableObserver<Integer> observer =
                observable.subscribeWith(DpObserverInfo.ObInteger.get());
        mComDisposable.add(observer);
    }

    //5、算术及聚合操作
    @OnClick({R.id.btn50})
    public void concat(){
        showLog("concat");
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

    //Reduce：按顺序对Observable发射的每项数据应用一个函数并发射最终的值，与Scan的不同在于Scan将每个步骤的数值都输出
    @OnClick({R.id.btn51})
    public void reduce() {
        showLog("reduce");
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

    //6、结合操作
    @OnClick({R.id.btn60})
    public void merge(){
        showLog("merge");
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




}
