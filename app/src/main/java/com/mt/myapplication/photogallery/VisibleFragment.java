package com.mt.myapplication.photogallery;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.mt.androidtest_as.alog.ALog;
import com.mt.androidtest_as.alog.ALogFragment;

/**
 * VisibleFragment:描述用户看到的界面，这个界面带有设置取消接收广播标记的功能
 */
public abstract class VisibleFragment extends ALogFragment {

    /**
     * mCancelNotificationReceiver:为NotificationReceiver设置取消接收广播标记
     */
    private BroadcastReceiver mCancelNotificationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //用户打开可视界面后可以暂时禁止通知的发送
            ALog.Log("mCancelNotificationReceiver_onReceive");
            setResultCode(Activity.RESULT_CANCELED);
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(PollService.ACTION_SHOW_NOTIFICATION);
        getActivity().registerReceiver(mCancelNotificationReceiver, filter, PollService.PERMISSION_NOFITY, null);
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(mCancelNotificationReceiver);
    }
}
