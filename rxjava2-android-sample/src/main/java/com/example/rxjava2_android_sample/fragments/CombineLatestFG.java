package com.example.rxjava2_android_sample.fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.rxjava2_android_sample.BaseFragment2;
import com.example.rxjava2_android_sample.R;
import com.jakewharton.rxbinding2.widget.RxTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function3;

/**
 * Created by mengtao1 on 2018/1/29.
 */

public class CombineLatestFG extends BaseFragment2 {

    @BindView(R.id.name) EditText mETName;
    @BindView(R.id.age) EditText mETAge;
    @BindView(R.id.job) EditText mETJob;
    @BindView(R.id.list) Button mBtn;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context.getApplicationContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_combinelatest, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        doInit();
        return view;
    }

    private void doInit(){

        /*
         * 步骤2：为每个EditText设置被观察者，用于发送监听事件
         * 说明：
         * 1. 此处采用了RxBinding，需要引入依赖：compile 'com.jakewharton.rxbinding2:rxbinding:2.0.0'
         * 2. 传入EditText控件，点击任1个EditText撰写时，都会发送数据事件 = Function3（）的返回值（下面会详细说明）
         * 3. 采用skip(1)原因：跳过 一开始EditText无任何输入时的空值
         **/
        Observable<CharSequence> nameObservable = RxTextView.textChanges(mETName).skip(1);
        Observable<CharSequence> ageObservable = RxTextView.textChanges(mETAge).skip(1);
        Observable<CharSequence> jobObservable = RxTextView.textChanges(mETJob).skip(1);

        /*
         * 步骤3：通过combineLatest（）合并事件 & 联合判断
         **/
        Observable.combineLatest(nameObservable,ageObservable,jobObservable,new Function3<CharSequence, CharSequence, CharSequence,Boolean>() {
            @Override
            public Boolean apply(@NonNull CharSequence charSequence, @NonNull CharSequence charSequence2, @NonNull CharSequence charSequence3) throws Exception {

                /*
                 * 步骤4：规定表单信息输入不能为空
                 **/
                // 1. 姓名信息
                boolean isUserNameValid = !TextUtils.isEmpty(mETName.getText()) ;
                // 除了设置为空，也可设置长度限制
                // boolean isUserNameValid = !TextUtils.isEmpty(name.getText()) && (name.getText().toString().length() > 2 && name.getText().toString().length() < 9);

                // 2. 年龄信息
                boolean isUserAgeValid = !TextUtils.isEmpty(mETAge.getText());
                // 3. 职业信息
                boolean isUserJobValid = !TextUtils.isEmpty(mETJob.getText()) ;
                /*
                 * 步骤5：返回信息 = 联合判断，即3个信息同时已填写，"提交按钮"才可点击
                 **/
                return isUserNameValid && isUserAgeValid && isUserJobValid;
            }

        }).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean s) throws Exception {
                /*
                 * 步骤6：返回结果 & 设置按钮可点击样式
                 **/
                Log.e(TAG, "提交按钮是否可点击： "+s);
                mBtn.setEnabled(s);
            }
        });
    }

    @Override public void onDestroyView() {
        mUnbinder.unbind();
        super.onDestroyView();
    }
}
