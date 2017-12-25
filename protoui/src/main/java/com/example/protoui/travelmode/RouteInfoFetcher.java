package com.example.protoui.travelmode;

import android.content.Context;

import com.example.protoui.ALog;
import com.example.protoui.travelmode.route.RouteInfo;
import com.example.protoui.travelmode.route.RouteInfoFactory;
import com.example.protoui.travelmode.route.lyft.LyftFactory;
import com.example.protoui.travelmode.route.uber.UberFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by mengtao1 on 2017/12/16.
 */

public class RouteInfoFetcher {
    private static final String TAG = "RouteInfoFetcher ";
    private static volatile RouteInfoFetcher mRouteInfoFetcher=null;
    private Context mContext;
    private static final int factoryType = 1;
    private AtomicBoolean shouldBreak = new AtomicBoolean(false);
    private DisposableObserver<List<RouteInfo>> mObserver = null;
    //
    private final Object mPausedLock = new Object();
    private final AtomicBoolean mPaused = new AtomicBoolean(false);

    public static RouteInfoFetcher get(Context context){
        if(null == mRouteInfoFetcher){
            mRouteInfoFetcher = new RouteInfoFetcher(context);
        }
        return mRouteInfoFetcher;
    }

    private RouteInfoFetcher(Context mContext){
        this.mContext = mContext;
    }

    public void setObserver(DisposableObserver<List<RouteInfo>> mObserver){
        this.mObserver = mObserver;
    }

    public synchronized void setShouldBreak(boolean bl){
        shouldBreak.set(bl);
    }

    public synchronized boolean getShouldBreak(){
        return shouldBreak.get();
    }

    public void init(){
        if(null == mObserver)throw new IllegalArgumentException("RouteInfoFetcher.mObserver is null!");
        getIntervalRouteInfo()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(mObserver);
    }

    public void resume(){
        mPaused.set(false);
        synchronized (mPausedLock) {
            mPausedLock.notifyAll();
        }
    }

    public void pause(){
        mPaused.set(true);
    }

    private void waitIfPaused() {
        if (mPaused.get()) {
            synchronized (mPausedLock) {
                if (mPaused.get()) {
                    try {
                        mPausedLock.wait();
                    } catch (InterruptedException e) {
                        ALog.Log4("waitIfPaused InterruptedException");
                    }
                }
            }
        }
    }

    public void destroy(){
        setShouldBreak(true);
        mRouteInfoFetcher = null;
    }

    private Observable<List<RouteInfo>> getIntervalRouteInfo(){
        //下列interval函数运行于线程Schedulers.computation()之上
//        return Observable.interval(0, 2, TimeUnit.SECONDS)//interval会定时2秒查询一次数据，而不管上一次是否返回
        return Observable.create(new ObservableOnSubscribe<Long>() {
                    @Override
                    public void subscribe(ObservableEmitter<Long> emitter) throws Exception {
                        // send events with simulated time wait
                        for (long i = 0; ; i++) {
                            ALog.Log4("RouteInfoFetcher_subscribe");
                            if(getShouldBreak())break;
                            waitIfPaused();
                            emitter.onNext(i);
                            pause();
                        }
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Function<Long, Observable<List<RouteInfo>>>(){
                    @Override
                    public Observable<List<RouteInfo>> apply(Long obs) throws Exception {
                        return getZipRouteInfo();
                    }
                });
    }

    private long preTime = -1,nowTime = -1;
    private Observable<List<RouteInfo>> getZipRouteInfo() {
        if(preTime < 0)preTime = System.currentTimeMillis();
        nowTime = System.currentTimeMillis();
        ALog.Log4("getZipRouteInfo: "+(nowTime - preTime)/1000);
        preTime = nowTime;
        return Observable.zip(getLyftRouteInfo(),
                              getUberRouteInfo(),
                new BiFunction<RouteInfo, RouteInfo, List<RouteInfo>>() {
                    @Override
                    public List<RouteInfo> apply(RouteInfo info1, RouteInfo info2) throws Exception {
                        //此处代码的线程调度受getLyftRouteInfo()和getUberRouteInfo()影响，因为相对于和getxxxRouteInfo()函数
                        //而言，此处的apply函数也相当于一个观察者，因此getxxxRouteInfo()内部的observeOn(某线程)可以影响到此处
                        //代码的执行线程
                        List<RouteInfo> data = new ArrayList<>();
                        data.add(info1);
                        data.add(info2);
                        ALog.Log4("getZipRouteInfo_apply");
                        return data;
                    }
                });
    }

    private Observable<RouteInfo> getLyftRouteInfo() {
        return Observable.create(new ObservableOnSubscribe<RouteInfo>() {
            @Override
            public void subscribe(final ObservableEmitter<RouteInfo> emitter) throws Exception {
                final RouteInfoFactory mFactory = LyftFactory.getInstance(mContext, factoryType);
                ALog.Log3("getLyftRouteInfo_subscribe");//subscribeOn影响的是这类代码的执行线程，无法影响回调中的emitter.onNext
                mFactory.setOnDataLoadListener(new RouteInfoFactory.OnDataLoadListener() {
                    @Override
                    public void onDataLoadSuccess(RouteInfo mRouteInfo) {
                        emitter.onNext(mRouteInfo);//运行于主线程，可见是Retrofit默认回调线程是主线程，只能通过observeOn改变发往下游的线程
                        ALog.Log3("getLyftRouteInfo_onDataLoadSuccess");
                    }

                    @Override
                    public void onDataLoadFailed(Throwable t) {
                        ALog.Log(t.fillInStackTrace().toString());
                        emitter.onError(t);
                        mFactory.unSetDataLoadListener();
                    }
                });
                mFactory.requestEstimateInfo();
            }
        }).observeOn(Schedulers.io());//subscribeOn仅仅能影响到上游的数据产生发起的线程，无法改变回调产生的数据所发往的线程
    }

    private Observable<RouteInfo> getUberRouteInfo() {
        return Observable.create(new ObservableOnSubscribe<RouteInfo>() {
            @Override
            public void subscribe(final ObservableEmitter<RouteInfo> emitter) throws Exception {
                final RouteInfoFactory mFactory = UberFactory.getInstance(mContext, factoryType);
                ALog.Log3("getUberRouteInfo_subscribe");
                mFactory.setOnDataLoadListener(new RouteInfoFactory.OnDataLoadListener() {
                    @Override
                    public void onDataLoadSuccess(RouteInfo mRouteInfo) {
                        emitter.onNext(mRouteInfo);
                        ALog.Log3("getUberRouteInfo_onDataLoadSuccess");
                    }

                    @Override
                    public void onDataLoadFailed(Throwable t) {
                        ALog.Log(t.fillInStackTrace().toString());
                        emitter.onError(t);
                        mFactory.unSetDataLoadListener();
                    }
                });
                mFactory.requestEstimateInfo();
            }
        }).observeOn(Schedulers.io());
    }

}
