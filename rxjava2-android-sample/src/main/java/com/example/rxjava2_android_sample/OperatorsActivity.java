package com.example.rxjava2_android_sample;

import android.os.Bundle;
import android.view.View;

import com.example.rxjava2_android_sample.model.User;
import com.example.rxjava2_android_sample.utils.DpObserverInfo;
import com.example.rxjava2_android_sample.utils.ObsFetcher;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class OperatorsActivity extends BaseAcitivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operators);
        mUnbinder = ButterKnife.bind(this);
    }

    @OnClick({R.id.btn1})
    public void IntervalMap(View view){
        Observable<Long> observable =
            ObsFetcher.getIntervalMapObs()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        DisposableObserver<Long> observer = observable.subscribeWith(DpObserverInfo.getObLong());
        mComDisposable.add(observer);
    }

    @OnClick({R.id.btn2})
    public void IntervalFlatMap(){
        Observable<List<User>> observable =
            ObsFetcher.getIntervalFlatMapObs()//注意与getIntervalMapFlatMapObs的区别
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
        DisposableObserver<List<User>> observer = observable.subscribeWith(DpObserverInfo.getObListUser());
        mComDisposable.add(observer);
    }
}
