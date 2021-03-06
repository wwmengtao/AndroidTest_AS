package com.example.androidcommon.alog;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * ALogActivity：用于展示Activity的生命周期
 * Created by Mengtao1 on 2016/12/8.
 */

public abstract class ALogActivity extends AppCompatActivity {
    private boolean isLogRun = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(isLogRun) ALog.Log("onCreate",this);
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        if(isLogRun) ALog.Log("onRestart",this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(isLogRun)ALog.Log("onStart",this);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(isLogRun)ALog.Log("onRestoreInstanceState",this);
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(isLogRun)ALog.Log("onResume",this);
    }

    @Override
    protected void onPostResume(){
        super.onPostResume();
        if(isLogRun)ALog.Log("onPostResume",this);
    }

    @Override
    protected void onResumeFragments(){
        super.onResumeFragments();
        if(isLogRun)ALog.Log("onResumeFragments",this);
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        if(isLogRun)ALog.Log("onBackPressed",this);
    }

    @Override
    protected void onPause(){
        super.onPause();
        if(isLogRun)ALog.Log("onPause",this);
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        if(isLogRun)ALog.Log("onSaveInstanceState",this);
    }

    @Override
    public void finish() {
        super.finish();
        if(isLogRun)ALog.Log("finish",this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(isLogRun)ALog.Log("onStop",this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(isLogRun)ALog.Log("onDestroy",this);
    }

    @Override
    protected void onNewIntent(Intent intent){
        super.onNewIntent(intent);
        if(isLogRun)ALog.Log("onNewIntent",this);
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        if(isLogRun)ALog.Log("onContentChanged",this);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(isLogRun)ALog.Log("onWindowFocusChanged",this);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if(isLogRun)ALog.Log("onAttachedToWindow",this);
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if(isLogRun)ALog.Log("onDetachedFromWindow",this);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
        if(isLogRun)ALog.Log("onConfigurationChanged",this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if(isLogRun)ALog.Log("onActivityResult",this);
    }
}
