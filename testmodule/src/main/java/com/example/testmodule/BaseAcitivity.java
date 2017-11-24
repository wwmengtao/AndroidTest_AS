package com.example.testmodule;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.fernandocejas.android10.sample.data.ALog;

/**
 * Created by mengtao1 on 2017/11/11.
 */

public abstract class BaseAcitivity extends AppCompatActivity {
    public static final String ACTIVITY_NAME_TAG = "ACTIVITY_NAME";
    public String TAG = null;
    public String activity_name = null;

    /**
     * getCallingIntent：获取Activity的开启intent同时获取Activity的名称。
     * @param context
     * @param activity
     * @return
     */
    public static Intent getCallingIntent(Context context, Class<?> activity){
        String activityName1 = activity.getName();//getName获取的是com.example.testmodule.SQLiteActivity之类的名称
//        ALog.Log1("getName: "+activityName1);
        String activityName = activity.getSimpleName();
//        ALog.Log1("getSimpleName: "+activityName);//getName获取的是SQLiteActivity之类的名称
        //
        Intent intent = new Intent(context, activity);
        intent.putExtra(ACTIVITY_NAME_TAG, activityName);
//        ALog.Log1("activityName: "+activityName);
        return intent;
    }

    public void setActivityTitle(){
        activity_name = getIntent().getStringExtra(ACTIVITY_NAME_TAG);
        ALog.Log1("activity_name: "+activity_name);
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
}
