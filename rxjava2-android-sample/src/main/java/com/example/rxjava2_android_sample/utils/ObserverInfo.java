package com.example.rxjava2_android_sample.utils;

import android.util.Pair;

import com.example.rxjava2_android_sample.ALog;
import com.example.rxjava2_android_sample.model.User;
import com.example.rxjava2_android_sample.model.UserDetail;

import java.util.List;

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

    public static ObUser getObUser(){
        return new ObUser();
    }

    private static class ObUser extends BaseObserver<User>{

        @Override
        public void onNext(User user) {
            ALog.Log2(TAG+ "onNext " + user.toString());
        }
    }

    public static ObUserDetail getObUserDetail(){
        return new ObUserDetail();
    }

    private static class ObUserDetail extends BaseObserver<UserDetail>{
        @Override
        public void onNext(UserDetail userDetail) {
            // do anything with userDetail
            ALog.Log2(TAG+ "onNext " + userDetail.toString());
        }
    }

    public static ObListUser getObListUser(){
        return new ObListUser();
    }

    private static class ObListUser extends BaseObserver<List<User>> {
        @Override
        public void onNext(List<User> users) {
            // do anything with user who loves both
            ALog.Log2(TAG+ "onNext " + "userList size : " + users.size());
            for (User user : users) {
                ALog.Log2(TAG+ "onNext " + user.toString());
            }
        }
    }

    public static ObPair getObPair(){
        return new ObPair();
    }

    private static class ObPair extends BaseObserver<Pair<UserDetail, User>> {
        @Override
        public void onNext(Pair<UserDetail, User> pair) {
            // here we are getting the userDetail for the corresponding user one by one
            UserDetail userDetail = pair.first;
            User user = pair.second;
            ALog.Log2(TAG+ "onNext " + user.toString());
            ALog.Log2(TAG+ "onNext " + userDetail.toString());
        }
    }
}
