package com.example.rxjava_android_sample;

import android.os.Bundle;

import com.example.rxjava_android_sample.utils.ObserverInfo;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.functions.Func1;

public class OperatorsActivity extends BaseAcitivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operators);
        mUnbinder = ButterKnife.bind(this);
    }

    /*---------------------------------1、转换操作---------------------------------*/
    @OnClick({R.id.btn10})
    public void toSortedList(){//应用场景：延时做某种操作
        showLog("toSortedList");
        Integer [] words={5,1,9,2,7,3,4,6,8};
        Observable.from(words)
                .toSortedList()//将Comparable类型数据按照升序排列
                .flatMap(new Func1<List<Integer>, Observable<Integer>>() {
                    @Override
                    public Observable<Integer> call(List<Integer> strings) {
                        return Observable.from(strings);
                    }
                })
                .subscribe(ObserverInfo.OInteger.get());
    }
}
