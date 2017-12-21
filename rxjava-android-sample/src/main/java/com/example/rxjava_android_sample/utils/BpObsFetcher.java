package com.example.rxjava_android_sample.utils;

import rx.Observable;
import rx.Subscriber;
import rx.internal.util.RxRingBuffer;

/**
 * Created by mengtao1 on 2017/12/20.
 */

public class BpObsFetcher {

    public static Observable<Integer> getDefaultObs() {
        /*Observable.observeOn注释如下：
         * Modifies an Observable to perform its emissions and notifications on a specified {@link Scheduler},
         * asynchronously with a bounded buffer of {@link rx.internal.util.RxRingBuffer#SIZE} slots.
         * 上述说明Observable.observeOn异步情况下会创建一个有界buffer，大小为rx.internal.util.RxRingBuffer#SIZE，超过
         * 这个数值就会报MissingBackpressureException。
         */
        final int BUFFERSIZE = RxRingBuffer.SIZE;//此时数值为16
        return Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                for (int i = 0;i< BUFFERSIZE; i++) {//超过这个数值就会报MissingBackpressureException
                    subscriber.onNext(i);
                }
            }
        });
    }
}
