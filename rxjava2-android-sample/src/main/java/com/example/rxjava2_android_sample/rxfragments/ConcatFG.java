package com.example.rxjava2_android_sample.rxfragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rxjava2_android_sample.BaseFragment2;
import com.example.rxjava2_android_sample.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;

/**
 * ConcatFG：使用concat结合firstElement实现“检查内存缓存、磁盘缓存 & 发送网络请求”的功能。
 * Created by mengtao1 on 2018/1/29.
 */

public class ConcatFG extends BaseFragment2 {

    @BindView(R.id.tv) TextView mTV;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_concat, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        doInit();
        return view;
    }

    private void doInit(){
        final String memoryCache = null;//"从内存中获取数据";
        final String diskCache = null;//"从磁盘缓存中获取数据";
        final String netWork = "从网络获取数据";
        /*
         * 设置第1个Observable：检查内存缓存是否有该数据的缓存
         **/
        Observable<String> memory = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                //先判断内存缓存有无数据
                if (memoryCache != null) {
                    //若有该数据，则发送
                    emitter.onNext(memoryCache);
                } else {
                    //若无该数据，则直接发送结束事件
                    emitter.onComplete();
                }
            }
        });

        /*
         * 设置第2个Observable：检查磁盘缓存是否有该数据的缓存
         **/
        Observable<String> disk = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {

                // 先判断磁盘缓存有无数据
                if (diskCache != null) {
                    // 若有该数据，则发送
                    emitter.onNext(diskCache);
                } else {
                    // 若无该数据，则直接发送结束事件
                    emitter.onComplete();
                }

            }
        });

        /*
         * 设置第3个Observable：通过网络获取数据
         **/
        Observable<String> network = Observable.just(netWork);

        /*
         * 通过concat（） 和 firstElement（）操作符实现缓存功能
         **/
        // 1. 通过concat（）合并memory、disk、network3个被观察者的事件（即检查内存缓存、磁盘缓存 & 发送网络请求）
        //    并将它们按顺序串联成队列
        Observable.concat(memory, disk, network)
                // 2. 通过firstElement()，从串联队列中取出并发送第1个有效事件（Next事件），即依次判断检查memory、disk、network
                .firstElement()
                // 即本例的逻辑为：
                // a. firstElement()取出第1个事件 = memory，即先判断内存缓存中有无数据缓存；由于memoryCache = null，即内存缓存中无数据，所以发送结束事件（视为无效事件）
                // b. firstElement()继续取出第2个事件 = disk，即判断磁盘缓存中有无数据缓存：由于diskCache ≠ null，即磁盘缓存中有数据，所以发送Next事件（有效事件）
                // c. 即firstElement()已发出第1个有效事件（disk事件），所以停止判断。

                // 3. 观察者订阅
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept( String s) throws Exception {
                        mTV.setText("最终获取的数据来源: \n"+ s);
                    }
                });
    }
}
