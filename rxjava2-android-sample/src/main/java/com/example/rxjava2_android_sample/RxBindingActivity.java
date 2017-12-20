package com.example.rxjava2_android_sample;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * rxbinding是基于RxJava提供了一套在Android平台上的基于RxJava的Binding API，类似于ClickListener这样的监听绑定。
 */
public class RxBindingActivity extends BaseAcitivity {
    private DisplayHandler mDisplayHandler = null;
    @BindView(R.id.edit) EditText mEdit;
    @BindView(R.id.button) Button mBtn;
    @BindView(R.id.text_edit) TextView mText;
    @BindView(R.id.text_edit2) TextView mText2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_binding);
        mUnbinder = ButterKnife.bind(this);
        init();
    }

    private void init() {
        mDisplayHandler = new DisplayHandler();
        //
        initEdit();
        antiMisTouch();
    }

    protected void onDestroy(){
        super.onDestroy();
        if(null != mDisplayHandler)mDisplayHandler.removeCallbacksAndMessages(null);
    }

    /**
     * antiMisTouch：防误触设置
     */
    private int index = -1;
    private void antiMisTouch(){
        RxView.clicks(mBtn)
//            .throttleFirst(500, TimeUnit.MILLISECONDS)//会采样500ms内第一个操作
            .debounce(500, TimeUnit.MILLISECONDS)//会抛弃前面点击的太快的操作
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<Object>() {
                //这就相当于OnClickListener中的OnClick方法回调
                @Override
                public void accept(Object o) throws Exception {
                    mBtn.setText("index: "+index++);
                }
            });
    }

    private void initEdit(){
        //用来监听edittext输入，同时匹配输入数数据来提示
        Observable<String> observable =
        RxTextView.textChanges(mEdit)
            //在一次事件发生后的一段时间内没有新操作，则发出这次事件
            .debounce(500, TimeUnit.MILLISECONDS)//debounce会自动过滤掉过快输入产生的内容
            //转换线程
            .observeOn(Schedulers.newThread())
            //通过输入的数据，来匹配"数据库"中的数据从而提示。。
            .map(new Function<CharSequence, List<String>>() {
                @Override
                public List<String> apply(CharSequence charSequence) {
                    String str = charSequence.toString();
                    Message msg = Message.obtain();
                    msg.obj = str;
                    mDisplayHandler.sendMessage(msg);
                    List<String> list=new ArrayList<>();
                    if (str.contains("1")){
                        for (int i=1;i<3;i++){
                            list.add("10"+i);
                        }
                    }
                    return list;
                }
            })
            //由于我不想要listl列表，所以使用了flatMap来分解成一个一个的数据发送
            .flatMap(new Function<List<String>, Observable<String>>() {
                @Override
                public Observable<String> apply(List<String> strings) {
                    return Observable.fromIterable(strings);
                }
            })
            //这里做一些过滤动作
            .filter(new Predicate<String>() {//filter:过滤操作
                @Override
                public boolean test(String str) throws Exception {
                    return !mText2.getText().toString().contains(str);
                }
            });
            observable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                //这就相当于OnClickListener中的OnClick方法回调
                @Override
                public void accept(String s) throws Exception {
                    mText2.append(s + "\n");
                }
            });
    }

    private class DisplayHandler extends Handler{
        private String str = null;
        @Override
        public void handleMessage(Message msg) {
            str = (String)msg.obj;
            if(null != str){
                mText.append(str + "\n");
            }
        }
    }
}
