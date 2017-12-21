package com.example.rxjava2_android_sample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.View;

import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;

import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;


/**
 * Created by mengtao1 on 2017/11/11.
 */

public abstract class BaseAcitivity extends AppCompatActivity {
    protected String TAG = null;
    private static final String ACTIVITY_NAME_TAG = "ACTIVITY_NAME";
    private SparseArray<Class<?>> mActivitySA = null;
    //
    protected Unbinder mUnbinder;
    protected final CompositeDisposable mComDisposable = new CompositeDisposable();
    protected final List<Subscription> mSubscriptions = new ArrayList<>();
    /**
     * getCallingIntent：获取Activity的开启intent同时获取Activity的名称。
     * @param context
     * @param activity
     * @return
     */
    private Intent getCallingIntent(Context context, Class<?> activity){
        String activityName1 = activity.getName();//getName获取的是com.example.rxjava2_android_sample.OperatorsActivity
        String activityName = activity.getSimpleName();//getSimpleName获取的是OperatorsActivity之类的名称
        //
        Intent intent = new Intent(context, activity);
        intent.putExtra(ACTIVITY_NAME_TAG, activityName);
        return intent;
    }

    private void setActivityTitle(){
        String activity_name = getIntent().getStringExtra(ACTIVITY_NAME_TAG);
        if(activity_name != null){
            setTitle(activity_name);
            TAG = activity_name;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityTitle();
    }

    protected void initActivities(int []viewIDs, Class<?>[] classEs){
        if(null == viewIDs || viewIDs.length == 0 || null == classEs || classEs.length == 0)return;
        if(viewIDs.length != classEs.length){
            throw new IllegalArgumentException("BaseAcitivity.initActivities: buttonIDs and classEs size not equal!");
        }
        mActivitySA = new SparseArray<>();
        for(int i = 0; i< viewIDs.length; i++){
            mActivitySA.put(viewIDs[i], classEs[i]);
        }
    }

    protected void startActivity(View view){
        Class<?> activity = mActivitySA.get(view.getId());
        startActivity(getCallingIntent(this, activity));
    }

    @Override
    protected void onDestroy(){
        mUnbinder.unbind();
        cancelSubscription();
        mComDisposable.clear(); // clearing it : do not emit after destroy
        super.onDestroy();
    }

    private void cancelSubscription(){
        for(Subscription ss : mSubscriptions){
            ss.cancel();
            ALog.Log("cancelSubscription");
        }
    }

    protected void showLog(String info){
        ALog.Log("/----------------------------"+TAG+" "+info+"----------------------------/");
    }
}
