package com.example.testmodule.notification.views;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.WindowManager;

import com.example.testmodule.ALog;
import com.example.testmodule.BaseAcitivity;
import com.example.testmodule.R;
import com.example.testmodule.notification.notifiutils.MockNotifyBlockManager;

public class NotiAppActivity extends BaseAcitivity implements NotiAppUnblockedFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noti_app);
        initAppInfoData();
        initFragment();
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        WindowManager.LayoutParams mLayoutParams = this.getWindow().getAttributes();
        mLayoutParams.dimAmount = 0.8f;//设置灰度,0.0f完全不暗，1.0f全暗
//        mLayoutParams.alpha = 0.5f;//1.0完全不透明，0.0f完全透明
        this.getWindow().setAttributes(mLayoutParams);
//        this.getWindow().getDecorView().setBackgroundColor(getResources().getColor(android.R.color.transparent));
//        this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);//提示AndroidRuntime: java.lang.IllegalArgumentException: Window type can not be changed after the window is added.
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {//在Android8.0以上只能识别返回键，无法识别home、菜单键
        ALog.Log("onKeyDown");
        switch (keyCode) {
            case KeyEvent.KEYCODE_HOME://普通应用无法捕获home按键信息，因为权限不够
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {//在Android8.0以上只能识别触摸屏时间，无法识别返回、home、菜单键
        ALog.Log(TAG+"_dispatchTouchEvent");
        //下列event.getKeyCode() == KeyEvent.KEYCODE_HOME在高版本上不识别
//        if (event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_HOME) {
//        }
        return super.dispatchTouchEvent(event);
    }

    private void initAppInfoData(){
        mAppExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                MockNotifyBlockManager.get(NotiAppActivity.this.getApplicationContext());
            }
        });
    }

    private void initFragment(){
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if(fragment == null){
            fragment = getFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }

    private Fragment getFragment(){
        return NotiAppUnblockedFragment.newInstance("param1", "param2");
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void showNotiAppBlockedFragment(){
        final NotiAppBlockedFragment fragment = NotiAppBlockedFragment.newInstance("param11", "param12");
        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(NotiAppUnblockedFragment.PARENT_FRAGMENT)
                .commit();
    }
}
