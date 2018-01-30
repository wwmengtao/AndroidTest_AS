package com.example.rxjava2_android_sample;

import android.os.Bundle;
import android.view.View;

import com.example.rxjava2_android_sample.model.SchoolInfo;
import com.example.rxjava2_android_sample.model.User;
import com.example.rxjava2_android_sample.utils.DpObserverInfo;
import com.example.rxjava2_android_sample.utils.ObsFetcher;
import com.example.rxjava2_android_sample.utils.ObserverInfo;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.observables.ConnectableObservable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.schedulers.Timed;

import static com.example.rxjava2_android_sample.model.SchoolInfo.SchoolClass;
import static com.example.rxjava2_android_sample.model.SchoolInfo.Student;

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

    /*---------------------------------1、创建操作---------------------------------*/
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

    /*---------------------------------2、变换操作---------------------------------*/
    @OnClick({R.id.btn20})
    public void IntervalMap(View view){//应用场景：定时从网络上获取数据
        showLog("IntervalMap");
        Observable<Long> observable =
                ObsFetcher.getIntervalMapObs()//注意与getIntervalMapFlatMapObs的区别
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

    @OnClick({R.id.btn211})
    public void flatMap(){//应用场景：定时从网络上获取数据
        showLog("flatMap");
        Observable<Student> observable=
            Observable.fromArray(SchoolInfo.getData())
                .flatMap(new Function<SchoolClass, Observable<Student>>() {
                    @Override
                    public Observable<Student> apply(SchoolClass schoolClass) {
                        //将Student列表使用from方法一个一个发出去
                        return Observable.fromArray(schoolClass.getStudents());
                    }
                });
        DisposableObserver<Student> observer = observable.subscribeWith(DpObserverInfo.ObStudent.get());
        mComDisposable.add(observer);
    }

    @OnClick({R.id.btn22})
    public void window(){//应用场景：获取一定量的数据，截断这些数据分多次送给观察者，而不是一个一个。
        showLog("window");
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

    @OnClick({R.id.btn221})
    public void buffer(){//应用场景：滑动窗口模式获取一定量的数据，这些数据集合的构成受buffer函数中的skip数值影响，也可以一次性收集齐所有数据
        showLog("buffer");
        Observable<List<Long>> observable =
                Observable.interval(1, TimeUnit.SECONDS)
                        .take(5)//只要前12个数据
//                        .buffer(5)//以列表(List)的形式发射非重叠的缓存，每一个缓存至多包含来自原始Observable的count项数据（最后发射的列表数据可能少于count项）
                        .buffer(3, 1)//skip数值等于count的话，那么就等效于分段截取数值了
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
        DisposableObserver<List<Long>> observer =
                observable.subscribeWith(DpObserverInfo.ObListLong.get());
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

    /*---------------------------------3、过滤操作---------------------------------*/
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

    private int filterIndex = -1;
    @OnClick({R.id.btn34})
    public void filter(){
        showLog("filter");
        Observable<Long> observable =
                Observable.interval(0, 2, TimeUnit.SECONDS)
                .observeOn(Schedulers.io())
                .filter(new Predicate<Long>() {//filter:过滤操作
                    @Override
                    public boolean test(Long integer) throws Exception {
                        return ++filterIndex % 2 == 0;
                    }
                });
        DisposableObserver<Long> observer =
                observable.subscribeWith(DpObserverInfo.ObLong.get());
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

    /*---------------------------------4、辅助操作---------------------------------*/
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

    @OnClick({R.id.btn41})
    public void timestamp(){
        showLog("timestamp");
        Observable<Timed<Integer>> observable =
            Observable.just(1, 2, 3)
                      .timestamp();//为数据增加时间戳，例如 2017-12-19-07:50:47
        DisposableObserver<Timed<Integer>> observer =
                observable.subscribeWith(DpObserverInfo.ObTimedInteger.get());
        mComDisposable.add(observer);
    }

    @OnClick({R.id.btn42})
    public void timeout(){
        showLog("timeout");
        //如果1：period>timeout，说明发射数据的时间间隔大于超时间隔，此时将调用观察者的onError；
        //如果2：period<=timeout，此时数据可以正常发射
        int period = 2;//定时器发射数据的时间间隔
        int timeout = 1;//超时时间
        Observable<Long> observable =
                Observable.interval(0, period, TimeUnit.SECONDS)
                        .timeout(timeout, TimeUnit.SECONDS);//
        DisposableObserver<Long> observer =
                observable.subscribeWith(DpObserverInfo.ObLong.get());
        mComDisposable.add(observer);
    }

    /*---------------------------------5、算术及聚合操作---------------------------------*/
    @OnClick({R.id.btn50})
    public void concat(){
        showLog("concat");
        final String[] aStrings = {"A1", "A2", "A3", "A4"};
        final String[] bStrings = {"B1", "B2", "B3"};
        final String diskCache = null;
        final Observable<String> aObservable = Observable.fromArray(aStrings);
        final Observable<String> bObservable = Observable.fromArray(bStrings);
        final Observable<String> diskObs = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {

                // 先判断磁盘缓存有无数据
                if (diskCache != null) {
                    // 若有该数据，则发送
                    emitter.onNext(diskCache);
                } else {
                    emitter.onNext("\"diskCache is null!\"");
                    // 若无该数据，则直接发送结束事件
                    emitter.onComplete();
                }
            }
        });
        Observable<String> observable =
                Observable.concat(aObservable, diskObs, bObservable)//concat：合并多个Observables的发射物，不会让合并的Observables发射的数据交错
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
        DisposableObserver<String> observer =
                observable.subscribeWith(DpObserverInfo.ObString.get());
        mComDisposable.add(observer);
    }

    @OnLongClick({R.id.btn50})
    public boolean concatFG() {
        showLog("concatFG");
        startActivity(PracticalActivity.newIntent(this, "Concat"));
        return true;
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
            .subscribeWith(ObserverInfo.MbObInteger.get());
    }

    /*---------------------------------6、结合操作---------------------------------*/
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

    @OnClick({R.id.btn61})
    public void CombineLatest(){//应用场合
        startActivity(PracticalActivity.newIntent(this, "CombineLatest"));
    }

    /*---------------------------------7、连接操作---------------------------------*/
    @OnClick({R.id.btn70})
    public void connect() {//应用场景：两个观察者同时开始观察同一个数据源，做同一类事情，比如测试两个线程同时处理某项任务的耗时
        showLog("connect");
        Integer [] integers={1,2,3,4,5,6};
        ConnectableObservable<Integer> observable= Observable.fromArray(integers)
                .publish();//将一个Observable转换为一个可连接的Observable

        Consumer c1=new Consumer<Integer>(){
            @Override
            public void accept(Integer i) {
                ALog.Log("观察者1收到:  "+i);
            }
        };
        Consumer c2=new Consumer<Integer>(){
            @Override
            public void accept(Integer i) {
                ALog.Log("观察者2收到:  "+i);
            }
        };
        observable.subscribe(c1);
        observable.subscribe(c2);
        observable.connect();//只有执行connect操作后，上述两个观察者才同时开始观察数据
    }
}
