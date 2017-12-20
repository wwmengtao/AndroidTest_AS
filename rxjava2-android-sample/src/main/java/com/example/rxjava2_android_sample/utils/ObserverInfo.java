package com.example.rxjava2_android_sample.utils;

import android.util.Pair;

import com.example.rxjava2_android_sample.ALog;
import com.example.rxjava2_android_sample.model.User;
import com.example.rxjava2_android_sample.model.UserDetail;

import java.util.List;

import io.reactivex.MaybeObserver;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by mengtao1 on 2017/12/14.
 */

public class ObserverInfo {
    private static final String TAG_TOP = "ObserverInfo ";

    private static abstract class BaseObserver<T> implements Observer<T>{
        protected String TAG = null;
        protected long preTime, nowTime;
        public BaseObserver(){
            String tag = this.toString();
            tag = tag.substring(tag.indexOf('$')+1, tag.lastIndexOf('@'));
            TAG = TAG_TOP + tag + " ";
        }

        @Override
        public void onSubscribe(Disposable d) {
            preTime = System.currentTimeMillis();
        }

        @Override
        public void onNext(T t) {
        }

        @Override
        public void onError(Throwable e) {
            ALog.Log(TAG+ "onError");
        }

        @Override
        public void onComplete() {
            nowTime = System.currentTimeMillis();
            ALog.Log2(TAG+ "onComplete, cost time:"+(nowTime - preTime));
        }
    }

    private static class ObUser extends BaseObserver<User>{
        public static ObUser get(){
            return new ObUser();
        }

        @Override
        public void onNext(User user) {
            ALog.Log2(TAG+ "onNext " + user.toString());
        }
    }


    private static class ObUserDetail extends BaseObserver<UserDetail>{
        public static ObUserDetail get(){
            return new ObUserDetail();
        }

        @Override
        public void onNext(UserDetail userDetail) {
            // do anything with userDetail
            ALog.Log2(TAG+ "onNext " + userDetail.toString());
        }
    }


    public static class ObListUser extends BaseObserver<List<User>> {
        public static ObListUser get(){
            return new ObListUser();
        }

        @Override
        public void onNext(List<User> users) {
            // do anything with user who loves both
            ALog.Log2(TAG+ "onNext " + "userList size : " + users.size());
            for (User user : users) {
                ALog.Log2(TAG+ "onNext " + user.toString());
            }
        }
    }



    public static class ObPair extends BaseObserver<Pair<UserDetail, User>> {
        public static ObPair get(){
            return new ObPair();
        }

        @Override
        public void onNext(Pair<UserDetail, User> pair) {
            // here we are getting the userDetail for the corresponding user one by one
            UserDetail userDetail = pair.first;
            User user = pair.second;
            ALog.Log2(TAG+ "onNext " + user.toString());
            ALog.Log2(TAG+ "onNext " + userDetail.toString());
        }
    }

    private static abstract class BaseMaybeObserver<T> implements MaybeObserver<T> {
        protected String TAG = null;
        protected long preTime, nowTime;

        public BaseMaybeObserver() {
            String tag = this.toString();
            tag = tag.substring(tag.indexOf('$') + 1, tag.lastIndexOf('@'));
            TAG = TAG_TOP + tag + " ";
        }


        @Override
        public void onSubscribe(Disposable d) {
            preTime = System.currentTimeMillis();
        }

        @Override
        public void onSuccess(T value) {
        }

        @Override
        public void onError(Throwable e) {
            ALog.Log(TAG+ "onError");
        }

        @Override
        public void onComplete() {
            nowTime = System.currentTimeMillis();
            ALog.Log2(TAG+ "onComplete, cost time:"+(nowTime - preTime));
        }
    }

    public static class ObListInteger extends BaseObserver<List<Integer>> {
        public static ObListInteger get(){
            return new ObListInteger();
        }

        @Override
        public void onNext(List<Integer> value) {
            ALog.Log2(TAG+ "onNext "+value.size());
            for(Integer l : value){
                ALog.Log2(TAG+ "value: "+l);
            }
        }
    }

    public static class MbObInteger extends BaseMaybeObserver<Integer> {
        public static MbObInteger get(){
            return new MbObInteger();
        }

        @Override
        public void onSuccess(Integer value) {
            ALog.Log(TAG+ " onSuccess : value : " + value);
        }

    }

}
