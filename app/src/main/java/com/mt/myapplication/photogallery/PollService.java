package com.mt.myapplication.photogallery;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mt.androidtest_as.R;
import com.mt.androidtest_as.alog.ALog;
import com.mt.myapplication.photogallery.data.QueryPreferences;

public class PollService extends IntentService {
    public static final String INTENT_SERVICE_TAG = "PollService";
    private static final long POLL_INTERVAL = 5 * 1000;//定义定时器的唤醒间隔，此时为5秒
    public static final String PERMISSION_NOFITY = "com.mt.myapplication.photogallery.notifybroadcast.permission";
    public static final String ACTION_SHOW_NOTIFICATION = "com.bignerdranch.android.photogallery.SHOW_NOTIFICATION";
    public static final String REQUEST_CODE = "REQUEST_CODE";
    public static final String NOTIFICATION = "NOTIFICATION";

    public static Intent newIntent(Context context) {
        return new Intent(context, PollService.class);
    }

    public static void setServiceAlarm(Context context, boolean isOn) {
        Intent i = PollService.newIntent(context);
        PendingIntent pi = PendingIntent.getService(
                context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager)
                context.getSystemService(Context.ALARM_SERVICE);

        if (isOn) {
            ALog.Log("isOn: true");
            alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME,
                    SystemClock.elapsedRealtime(), POLL_INTERVAL, pi);
        } else {
            alarmManager.cancel(pi);
            pi.cancel();
        }
        QueryPreferences.setAlarmOn(context, isOn);
    }

    public static boolean isServiceAlarmOn(Context context) {
        Intent i = PollService.newIntent(context);
        PendingIntent pi = PendingIntent
                .getService(context, 0, i, PendingIntent.FLAG_NO_CREATE);
        return pi != null;
    }

    public PollService() {
        super(INTENT_SERVICE_TAG);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        ALog.Log("====onHandleIntent");
//        if (!isNetworkAvailableAndConnected()) {
//            ALog.Log("====onHandleIntent_NetworkNotAvailable");
//            showMyToast();
//            return;
//        }
        sendBroadcast();
    }

    private void sendBroadcast(){
        int requestCode = 0;
        Intent i = new Intent(ACTION_SHOW_NOTIFICATION);
        i.putExtra(REQUEST_CODE, requestCode);
        /**
         * sendOrderedBroadcast：有序广播可以停止、修改
         */
        sendOrderedBroadcast(i, PERMISSION_NOFITY, null, null, Activity.RESULT_OK, null, null);
    }

    private boolean isNetworkAvailableAndConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        boolean isNetworkAvailable = cm.getActiveNetworkInfo() != null;
        boolean isNetworkConnected = isNetworkAvailable &&
                cm.getActiveNetworkInfo().isConnected();

        return isNetworkConnected;
    }

    private void showMyToast(){
        Handler handler = new Handler(Looper.getMainLooper());//建立一个和UI线程关联的handler
        LayoutInflater inflater = LayoutInflater.from(this);
        final View view = inflater.inflate(R.layout.toast_view,null);
        /**
         * 下面改用view.post不成功是因为view.post起作用的前提是view被attached到一个窗口上，显然此时非主线程中的view仅仅被
         * inflate而没有添加到某个窗口上，这也是onCreate中调用view.post不成功的原因，因为onCreate中view还没有attathed到
         * 窗口上。
         */
        boolean isSuccess = handler.post(new Runnable() {
            @Override
            public void run() {
                String str = "PollService.showMyToast():\nNetwork not available!\nTry to open it.";
                view.setBackgroundColor(Color.RED);
                TextView tv=(TextView) view.findViewById(R.id.toast_tv);
                tv.setText(str);
                tv.setTextColor(Color.WHITE);
                tv.setTextSize(25);

                Toast toast = new Toast(getApplicationContext());
                toast.setGravity(Gravity.CENTER_VERTICAL,0,0);
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(view);
                toast.show();
            }
        });
        ALog.Log("isSuccess: "+isSuccess);
        handler.removeCallbacksAndMessages(null);
    }
}
