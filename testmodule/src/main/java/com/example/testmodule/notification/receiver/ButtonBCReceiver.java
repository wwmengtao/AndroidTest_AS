package com.example.testmodule.notification.receiver;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.Toast;

/**
 * Created by mengtao1 on 2017/11/23.
 */

public class ButtonBCReceiver extends BroadcastReceiver{
    public static final String ACTION_BUTTON_ON_CLICK = "ACTION_BUTTON_ON_CLICK";
    public final static String INTENT_BUTTON_TAG = "INTENT_BUTTON_TAG";
    /** 上一首 按钮点击 ID */
    public final static int BUTTON_PREV_ID = 1;
    /** 播放/暂停 按钮点击 ID */
    public final static int BUTTON_PALY_ID = 2;
    /** 下一首 按钮点击 ID */
    public final static int BUTTON_NEXT_ID = 3;
    /** 是否在播放*/
    public static boolean isPlay = false;

    private NotificationManager mNotificationManager = null;
    private static ButtonBCReceiver mReceiver = null;
    public static ButtonBCReceiver getSwitchBroadcastReceiver(Context mContext){
        if(null == mReceiver) {
            mReceiver = new ButtonBCReceiver();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(ACTION_BUTTON_ON_CLICK);
            mContext.registerReceiver(mReceiver, intentFilter);
        }
        return mReceiver;
    }

    public void unregisterReceiver(Context mContext){
        if(null != mReceiver){
            isPlay = false;
            mContext.unregisterReceiver(mReceiver);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        String action = intent.getAction();
        if(action.equals(ACTION_BUTTON_ON_CLICK)){
            //通过传递过来的ID判断按钮点击属性或者通过getResultCode()获得相应点击事件
            int buttonId = intent.getIntExtra(INTENT_BUTTON_TAG, 0);
            switch (buttonId) {
                case BUTTON_PREV_ID:
                    Toast.makeText(context, "上一首", Toast.LENGTH_SHORT).show();
                    break;
                case BUTTON_PALY_ID:
                    String play_status = "";
                    isPlay = !isPlay;
                    if(isPlay){
                        play_status = "开始播放";
                    }else{
                        play_status = "已暂停";
                    }
                    Toast.makeText(context, play_status, Toast.LENGTH_SHORT).show();
                    break;
                case BUTTON_NEXT_ID:
                    Toast.makeText(context, "下一首", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    }
}
