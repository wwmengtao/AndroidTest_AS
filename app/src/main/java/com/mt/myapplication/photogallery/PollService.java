package com.mt.myapplication.photogallery;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
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
        if (!isNetworkAvailableAndConnected()) {
            ALog.Log("====onHandleIntent_NetworkNotAvailable");
            showMyToast();
            return;
        }
        initBroadcast();
    }

    private void initBroadcast(){
        Resources resources = getResources();
        Intent i = PhotoGalleryActivity.newIntent(this);
        i.putExtra(INTENT_SERVICE_TAG, INTENT_SERVICE_TAG);//用于标识此intent的发起者
        PendingIntent pi = PendingIntent
                .getActivity(this, 0, i, 0);

        Notification notification = new NotificationCompat.Builder(this)
                .setTicker(resources.getString(R.string.new_pictures_title))
                .setSmallIcon(android.R.drawable.ic_menu_report_image)
                .setContentTitle(resources.getString(R.string.new_pictures_title))
                .setContentText(resources.getString(R.string.new_pictures_text))
                .setContentIntent(pi)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .build();

        showBackgroundNotification(0, notification);
    }

    private void showBackgroundNotification(int requestCode, Notification notification) {
        Intent i = new Intent(ACTION_SHOW_NOTIFICATION);
        i.putExtra(REQUEST_CODE, requestCode);
        i.putExtra(NOTIFICATION, notification);
        /**
         * sendOrderedBroadcast：有序广播可以停止、修改
         */
        sendOrderedBroadcast(i, PERMISSION_NOFITY, null, null,
                Activity.RESULT_OK, null, null);
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
                String str = "Network not available!\nTry to open it.";
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
