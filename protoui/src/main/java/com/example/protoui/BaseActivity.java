package com.example.protoui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by mengtao1 on 2017/12/25.
 */

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        ALog.Log("onCreate");
    }

    @Override
    protected void onResume(){
        super.onResume();
        ALog.Log("onResume");
    }

    @Override
    protected void onPause(){
        super.onPause();
        ALog.Log("onPause");
    }

    @Override
    protected void onStop(){
        super.onStop();
        ALog.Log("onStop");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        ALog.Log("onDestroy");
    }
}
