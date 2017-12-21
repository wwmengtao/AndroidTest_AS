package com.example.rxjava2_android_sample.utils;

import com.example.rxjava2_android_sample.ALog;
import com.example.rxjava2_android_sample.model.User;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.schedulers.Timed;

import static com.example.rxjava2_android_sample.model.SchoolInfo.Student;

/**
 * Created by mengtao1 on 2017/12/15.
 */

public class DpObserverInfo {
    private static String TAG_TOP = "DpObserverInfo ";

    public static void setTagTop(String tag_top){
        TAG_TOP = tag_top;
    }

    public static abstract class BaseObserver<T> extends DisposableObserver<T> {
        protected String TAG = null;
        protected long preTime, nowTime;
        public BaseObserver(){
            String tag = this.toString();
            tag = tag.substring(tag.indexOf('$')+1, tag.lastIndexOf('@'));
            TAG = TAG_TOP + tag + " ";
            preTime = System.currentTimeMillis();
        }

        @Override
        public void onNext(T t) {

        }

        @Override
        public void onError(Throwable e) {
            ALog.Log2(TAG+ "onError");
        }

        @Override
        public void onComplete() {
            nowTime = System.currentTimeMillis();
            ALog.Log2(TAG+ "onComplete, cost time:"+(nowTime - preTime));
        }
    }

    public static class ObLong extends BaseObserver<Long> {
        public static ObLong get(){
            return new ObLong();
        }

        @Override
        public void onNext(Long l) {
            ALog.Log2(TAG+ "onNext " + l);
        }
    }

    public static class ObObLong extends BaseObserver<Observable<Long>> {
        public static ObObLong get(){
            return new ObObLong();
        }

        @Override
        public void onNext(Observable<Long> observable) {
            ALog.Log2(TAG+ "onNext ");
            observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long value) {
                        ALog.Log2(TAG+ "accept:" + value);
                    }
                });
        }
    }

    public static class ObListLong extends BaseObserver<List<Long>> {
        public static ObListLong get(){
            return new ObListLong();
        }

        @Override
        public void onNext(List<Long> observable) {
            ALog.Log2(TAG+ "onNext "+observable.size());
            for(Long l : observable){
                ALog.Log2(TAG+ "value: "+l);
            }
        }
    }

    public static class ObStudent extends BaseObserver<Student> {
        public static ObStudent get(){
            return new ObStudent();
        }

        @Override
        public void onNext(Student l) {
            ALog.Log2(TAG+ "onNext " + l.toString());
        }
    }

    public static class ObListUser extends BaseObserver<List<User>> {
        public static ObListUser get(){
            return new ObListUser();
        }

        @Override
        public void onNext(List<User> l) {
            ALog.Log2(TAG+ "onNext " + l.size());
        }
    }

    public static class ObInteger extends BaseObserver<Integer> {
        public static ObInteger get(){
            return new ObInteger();
        }

        @Override
        public void onNext(Integer l) {
            ALog.Log2(TAG+ "onNext " + l);
        }
    }

    public static class ObTimedInteger extends BaseObserver<Timed<Integer>> {
        SimpleDateFormat sdf = null;
        public static ObTimedInteger get(){
            return new ObTimedInteger();
        }

        private ObTimedInteger(){
            sdf = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss", Locale.CHINA);
        }

        @Override
        public void onNext(Timed<Integer> l) {
            String timeValue = sdf.format(new Date(l.time()));//例如2017-12-19-07:50:47
            ALog.Log2(TAG+ "onNext value: " + l.value()+" time: "+timeValue);
        }
    }

    public static class ObString extends BaseObserver<String> {
        public static ObString get(){
            return new ObString();
        }

        @Override
        public void onNext(String l) {
            ALog.Log2(TAG+ "onNext " + l);
        }
    }
}
