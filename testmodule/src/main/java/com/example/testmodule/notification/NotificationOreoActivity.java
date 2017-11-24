package com.example.testmodule.notification;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;

import com.example.testmodule.BaseAcitivity;
import com.example.testmodule.MainActivity;
import com.example.testmodule.R;

public class NotificationOreoActivity extends BaseAcitivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int notification_one = 101;
    private static final int notification_two = 102;
    private MainUi mainUI;
    private NotificationHelper notificationHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_oreo);
        notificationHelper = new NotificationHelper(this);
        mainUI = new MainUi(findViewById(R.id.activity_main));
    }

//Post the notifications//

    public void postNotification(int id, String title) {
        Notification.Builder notificationBuilder = null;
        switch (id) {
            case notification_one:
                notificationBuilder = notificationHelper.getNotification1(title,
                        getString(R.string.channel_one_body));
                break;

            case notification_two:
                notificationBuilder = notificationHelper.getNotification2(title,
                        getString(R.string.channel_two_body));
                break;
        }

        if (notificationBuilder != null) {
            notificationHelper.notify(id, notificationBuilder);
        }
    }

//Load the settings screen for the selected notification channel//

    public void goToNotificationSettings(String channel) {
        Intent i = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
        i.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
        i.putExtra(Settings.EXTRA_CHANNEL_ID, channel);
        startActivity(i);
    }

//Implement our onClickListeners//

    class MainUi implements View.OnClickListener {

        private MainUi(View root) {
            ((Button) root.findViewById(R.id.post_to_channel_one)).setOnClickListener(this);
            ((Button) root.findViewById(R.id.channel_one_settings)).setOnClickListener(this);
            ((Button) root.findViewById(R.id.post_to_channel_two)).setOnClickListener(this);
            ((Button) root.findViewById(R.id.channel_two_settings)).setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.post_to_channel_one:
                    postNotification(notification_one, "notification1");
                    break;

                case R.id.channel_one_settings:
                    goToNotificationSettings(NotificationHelper.CHANNEL_ONE_ID);
                    break;

                case R.id.post_to_channel_two:
                    postNotification(notification_two, "notification2");
                    break;

                case R.id.channel_two_settings:
                    goToNotificationSettings(NotificationHelper.CHANNEL_TWO_ID);
                    break;
            }//end switch
        }//end onClick
    }
}