package com.example.framework_o.notification;

import android.annotation.TargetApi;
import android.app.INotificationManager;
import android.app.NotificationChannel;
import android.content.Context;
import android.os.Build;
import android.os.ServiceManager;

import com.example.framework_o.ALog;

import static android.app.NotificationManager.IMPORTANCE_NONE;
import static android.app.NotificationManager.IMPORTANCE_UNSPECIFIED;

/**
 * Created by mengtao1 on 2018/1/3.
 */

public class NotifyBlockManager {
    static INotificationManager sINM = INotificationManager.Stub.asInterface(
            ServiceManager.getService(Context.NOTIFICATION_SERVICE));
    /**
     * blockNotify：阻塞特定包名应用的通知
     * 代码来源：/mt6735_O_drv/packages/apps/Settings/src/com/android/settings/notification/AppNotificationSettings.java
     * protected void setupBlock() {...}
     */
    @TargetApi(Build.VERSION_CODES.O)
    private void blockNotify(String pkg, int uid, NotificationChannel mChannel){
        boolean isChecked = false;
        final int importance = isChecked ? IMPORTANCE_UNSPECIFIED : IMPORTANCE_NONE;
        if(mChannel != null){
            mChannel.setImportance(importance);
//            mChannel.lockFields(NotificationChannel.USER_LOCKED_IMPORTANCE);
            updateChannel(pkg, uid, mChannel);
        }
        setNotificationsEnabledForPackage(pkg, uid, isChecked);
    }

    public void updateChannel(String pkg, int uid, NotificationChannel channel) {
        try {
            sINM.updateNotificationChannelForPackage(pkg, uid, channel);
        } catch (Exception e) {
            ALog.Log("Exception updateChannel: "+e.getMessage());
        }
    }

    public static boolean setNotificationsEnabledForPackage(String pkg, int uid, boolean enabled) {
        try {
            sINM.setNotificationsEnabledForPackage(pkg, uid, enabled);
            return true;
        } catch (Exception e) {
            //mt6735_O_drv/frameworks/base/services/core/java/com/android/server/notification/NotificationManagerService.java
            //文件中对于该操作的限制如下：即当前应用必须为系统应用或者Phone应用或者uid为0的应用
            /**
             * protected boolean isUidSystemOrPhone(int uid) {
             *      final int appid = UserHandle.getAppId(uid);
             *      return (appid == Process.SYSTEM_UID || appid == Process.PHONE_UID || uid == 0);
             * }
             */
            ALog.Log("Exception setNotificationsEnabledForPackage: "+e.getMessage());
            return false;
        }
    }

    public static boolean areNotificationsEnabledForPackage(String pkg, int uid){
        boolean enabled = false;
        INotificationManager sINM = INotificationManager.Stub.asInterface(
                ServiceManager.getService(Context.NOTIFICATION_SERVICE));
        try {
            enabled = sINM.areNotificationsEnabledForPackage(pkg, uid);
        }catch (Exception e) {
            ALog.Log("Exception setNotificationsEnabledForPackage: "+e.getMessage());
        }
        return enabled;
    }

    public boolean areNotificationsEnabled(String pkg){
        boolean enabled = false;
        INotificationManager sINM = INotificationManager.Stub.asInterface(
                ServiceManager.getService(Context.NOTIFICATION_SERVICE));
        try {
            enabled = sINM.areNotificationsEnabled(pkg);
        }catch (Exception e) {
            ALog.Log("Exception setNotificationsEnabledForPackage: "+e.getMessage());
        }
        return enabled;
    }

}
