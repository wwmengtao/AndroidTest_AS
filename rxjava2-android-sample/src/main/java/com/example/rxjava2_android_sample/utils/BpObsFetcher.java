package com.example.rxjava2_android_sample.utils;

import com.example.rxjava2_android_sample.ALog;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;

/**
 * Created by mengtao1 on 2017/12/21.
 */

public class BpObsFetcher {
    private static final String TAG = "BpObsFetcher ";
    /*------------------------------------------不支持反压的被观察者---------------------------------------------*/
    public static Observable<Integer> getDefaultobs() {
        return Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                for (int i = 0; ; i++) {   //无限循环发事件，不会报MissingBackpressureException。
                    emitter.onNext(i);
                }
            }
        });
    }

    /*------------------------------------------支持反压的被观察者---------------------------------------------*/
    public static Flowable<Integer> getBackpressureObs(BackpressureStrategy mBackpressureStrategy) {
        Flowable<Integer> mFlowable = Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull FlowableEmitter<Integer> emitter) throws Exception {
                for (int i = 0; ; i++) {
                    emitter.onNext(i);
                }
            }
        }, mBackpressureStrategy);
        return mFlowable;
    }

    /**
     * 在下列mSubscription.request(1)的情况以及发送数据量为count*FlowableBufferSize的情况下：
     * 1、BackpressureStrategy.MISSING：会提示"io.reactivex.exceptions.MissingBackpressureException: Queue is full?!"，
     * 流程终止于onError(Throwable t)。
     * 2、BackpressureStrategy.ERROR：会提示"io.reactivex.exceptions.MissingBackpressureException:
     * create: could not emit value due to lack of requests"，流程终止于onError(Throwable t)。
     * 3、BackpressureStrategy.DROP等其他策略正常走完流程，执行到onComplete()。
     */
    public static Flowable<Integer> getPullBackpressureObs(final int count, BackpressureStrategy mBackpressureStrategy) {
        final int FlowableBufferSize = Flowable.bufferSize();//默认的Followable缓存个数
        Flowable<Integer> mFlowable = Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                ALog.Log(TAG+"getPullBackpressureObs#subscribe："+FlowableBufferSize);
                for(int i = 0; i< count * FlowableBufferSize; i++){//发送的数据量为count * FlowableBufferSize
                    ALog.sleep(5);
                    emitter.onNext(i);
                }
                emitter.onComplete();
            }
        }, mBackpressureStrategy);//尝试不同的策略来处理此种情况
        return mFlowable;
    }
}
