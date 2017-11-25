package com.example.testmodule.notification.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.Toast;

/**
 * SwitchBroadcastReceiver：通知栏内的Switch按钮点击广播
 * Created by mengtao1 on 2017/11/23.
 */

public class SwitchBCReceiver extends BroadcastReceiver {
    public static final String ACTION_SWITCH_ON_CLICK = "ACTION_SWITCH_ON_CLICK";
    public final static String INTENT_SWITCH_TAG = "INTENT_SWITCH_TAG";
    public final static int SWITCH_ID_DEFAULT = 0x001;
    public final static int SWITCH_ID_UNPLUGGED = 0x002;
    public final static int SWITCH_ID_KIDS = 0x003;
    public final static int SWITCH_ID_ADULT = 0x004;

    public static SwitchBCReceiver getSwitchBroadcastReceiver(Context mContext){
        SwitchBCReceiver mSwitchBroadcastReceiver = new SwitchBCReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_SWITCH_ON_CLICK);
        mContext.registerReceiver(mSwitchBroadcastReceiver, intentFilter);
        return mSwitchBroadcastReceiver;
    }

    public static void unregisterReceiver(Context mContext, BroadcastReceiver mReceiver){
        if(null != mReceiver){
            mContext.unregisterReceiver(mReceiver);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        String action = intent.getAction();
        if(action.equals(ACTION_SWITCH_ON_CLICK)){
            //通过传递过来的ID判断按钮点击属性或者通过getResultCode()获得相应点击事件
            int switchID = intent.getIntExtra(INTENT_SWITCH_TAG, 0);
            switch (switchID) {
                case SWITCH_ID_UNPLUGGED:
                    Toast.makeText(context, "Switch on click: "+SWITCH_ID_UNPLUGGED, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    }
}
