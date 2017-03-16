package com.mt.myapplication.photogallery;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.mt.androidtest_as.alog.ALog;
import com.mt.androidtest_as.alog.ALogFragment;

/**
 * VisibleFragment:描述用户看到的界面，这个界面带有setResultCode功能
 */
public abstract class VisibleFragment extends ALogFragment {
    /**
     * 定义用户打开VisibleFragment(此时为PhotoGalleryFragment)时，是否允许用户接收通知，因为PollInfoReceiver.onReceive中有
     * 判断if (getResultCode() != Activity.RESULT_OK)。
     */
    protected boolean mResultCanceled = false;
    /**
     * mCancelNotificationReceiver:为NotificationReceiver设置取消接收广播标记
     */
    private BroadcastReceiver mSetResultCodeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //用户打开可视界面后可以暂时禁止通知的发送
            ALog.Log("mCancelNotificationReceiver_onReceive");
            if(mResultCanceled)setResultCode(Activity.RESULT_CANCELED);
        }
    };

    /**
     * setCancelBroadCast:用于设置是否设置取消接收广播标记
     * @param setCancelBroadCast
     */
    protected abstract void setResultCanceled(boolean setCancelBroadCast);

    protected abstract boolean getResultCanceled();

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(PollService.ACTION_SHOW_NOTIFICATION);
        getActivity().registerReceiver(mSetResultCodeReceiver, filter, PollService.PERMISSION_NOFITY, null);
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(mSetResultCodeReceiver);
    }
}
