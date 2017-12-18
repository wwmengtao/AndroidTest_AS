package com.example.protoui.travelmode;

import android.content.Context;

import com.example.protoui.ALog;
import com.example.protoui.travelmode.route.RouteInfo;
import com.example.protoui.travelmode.route.RouteInfoFactory;
import com.example.protoui.travelmode.route.lyft.LyftFactory;
import com.example.protoui.travelmode.route.uber.UberFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
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
    private final CompositeDisposable disposables = new CompositeDisposable();
    private DisposableObserver<List<RouteInfo>> mObserver = null;

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

    public void start(){
        if(null == mObserver)return;
        disposables.add(getIntervalRouteInfo()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(mObserver)
        );
    }

    public void stop(){
        disposables.clear();
        mObserver = null;
    }

    private Observable<List<RouteInfo>> getIntervalRouteInfo(){
        return Observable.interval(0, 10, TimeUnit.SECONDS)
                .flatMap(new Function<Long, Observable<List<RouteInfo>>>(){
                    @Override
                    public Observable<List<RouteInfo>> apply(Long obs) throws Exception {
                        return getZipRouteInfo();
                    }
                });
    }

    private Observable<List<RouteInfo>> getZipRouteInfo() {
        ALog.Log3("/**********************************************************/\n");
        return Observable.zip(getLyftRouteInfo(),
                       getUberRouteInfo().observeOn(Schedulers.newThread()),
                new BiFunction<RouteInfo, RouteInfo, List<RouteInfo>>() {
                    @Override
                    public List<RouteInfo> apply(RouteInfo info1, RouteInfo info2) throws Exception {
                        List<RouteInfo> data = new ArrayList<>();
                        data.add(info1);
                        data.add(info2);
                        ALog.Log3("getZipRouteInfo: "+Thread.currentThread().toString());
                        return data;
                    }
                });
    }

    private Observable<RouteInfo> getLyftRouteInfo() {
        return Observable.create(new ObservableOnSubscribe<RouteInfo>() {
            @Override
            public void subscribe(final ObservableEmitter<RouteInfo> emitter) throws Exception {
                final RouteInfoFactory mFactory = new LyftFactory(mContext, factoryType);
                mFactory.setOnDataLoadListener(new RouteInfoFactory.OnDataLoadListener() {
                    @Override
                    public void onDataLoadSuccess(RouteInfo mRouteInfo) {
                        emitter.onNext(mRouteInfo);
                        ALog.Log3("getLyftRouteInfo_onDataLoadSuccess: "+Thread.currentThread().toString());
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
        });
    }

    private Observable<RouteInfo> getUberRouteInfo() {
        return Observable.create(new ObservableOnSubscribe<RouteInfo>() {
            @Override
            public void subscribe(final ObservableEmitter<RouteInfo> emitter) throws Exception {
                final RouteInfoFactory mFactory = new UberFactory(mContext, factoryType);
                mFactory.setOnDataLoadListener(new RouteInfoFactory.OnDataLoadListener() {
                    @Override
                    public void onDataLoadSuccess(RouteInfo mRouteInfo) {
                        emitter.onNext(mRouteInfo);
                        ALog.Log3("getUberRouteInfo_onDataLoadSuccess: "+Thread.currentThread().toString());
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
        });
    }

}
