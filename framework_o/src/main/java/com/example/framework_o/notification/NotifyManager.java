package com.example.framework_o.notification;

import android.app.INotificationManager;
import android.app.NotificationChannel;
import android.content.Context;
import android.os.ServiceManager;

import com.example.framework_o.ALog;

import static android.app.NotificationManager.IMPORTANCE_NONE;
import static android.app.NotificationManager.IMPORTANCE_UNSPECIFIED;

/**
 * Created by mengtao1 on 2018/1/3.
 */

public class NotifyManager {
    /**
     * blockNotify：阻塞特定包名应用的通知
     * 代码来源：/mt6735_O_drv/packages/apps/Settings/src/com/android/settings/notification/AppNotificationSettings.java
     * protected void setupBlock() {...}
     */
    private void blockNotify(String pkg, int uid, NotificationChannel mChannel){
        boolean isChecked = false;
        final int importance = isChecked ? IMPORTANCE_UNSPECIFIED : IMPORTANCE_NONE;
        if(mChannel != null){
            mChannel.setImportance(importance);
            mChannel.lockFields(NotificationChannel.USER_LOCKED_IMPORTANCE);
            updateChannel(pkg, uid, mChannel);
        }
        setNotificationsEnabledForPackage(pkg, uid, isChecked);
    }

    public void updateChannel(String pkg, int uid, NotificationChannel channel) {
        INotificationManager sINM = INotificationManager.Stub.asInterface(
                ServiceManager.getService(Context.NOTIFICATION_SERVICE));
        try {
            sINM.updateNotificationChannelForPackage(pkg, uid, channel);
        } catch (Exception e) {
            ALog.Log("Exception updateChannel: "+e.getMessage());
        }
    }

    public boolean setNotificationsEnabledForPackage(String pkg, int uid, boolean enabled) {
        INotificationManager sINM = INotificationManager.Stub.asInterface(
                ServiceManager.getService(Context.NOTIFICATION_SERVICE));
        try {
            sINM.setNotificationsEnabledForPackage(pkg, uid, enabled);
            return true;
        } catch (Exception e) {
            ALog.Log("Exception setNotificationsEnabledForPackage: "+e.getMessage());
            return false;
        }
    }
}
