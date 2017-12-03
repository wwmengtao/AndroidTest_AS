package com.example.testmodule.notification.receiver;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.testmodule.R;
import com.fernandocejas.android10.sample.data.ALog;

/**
 * MusicNotifyReceiver：自定义播放器通知监听广播
 * Created by mengtao1 on 2017/11/23.
 */

public class MusicNotifyReceiver extends BroadcastReceiver{
    public static final String ACTION_VIEW_ON_CLICK = "ACTION_VIEW_ON_CLICK";
    public final static String INTENT_VIEW_TAG = "INTENT_VIEW_TAG";
    public final static String INTENT_NOTIFY_CLICKED_TEXT = "INTENT_NOTIFY_CLICKED_TEXT";

    /** 通知整体 **/
    public final static int NOTIFY_CLICKED_ALL = 0x001;
    /** 上一首 按钮点击 ID */
    public final static int BUTTON_PREV_ID = 0x002;
    /** 播放/暂停 按钮点击 ID */
    public final static int BUTTON_PALY_ID = 0x003;
    /** 下一首 按钮点击 ID */
    public final static int BUTTON_NEXT_ID = 0x004;
    /** Action Yes 按钮点击 ID */
    public final static int ACTION_YES_ID = 0x005;
    /** Action No 按钮点击 ID */
    public final static int ACTION_NO_ID = 0x006;
    /** 是否在播放*/
    public boolean isPlay = false;

    private static MusicNotifyReceiver mReceiver = null;
    private OnPlayViewClickedListener mOnPlayViewClickedListener = null;
    public static MusicNotifyReceiver getInstance(){
        if(null == mReceiver) {
            mReceiver = new MusicNotifyReceiver();
        }
        return mReceiver;
    }

    public void registerReceiver(Context mContext){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_VIEW_ON_CLICK);
        mContext.registerReceiver(mReceiver, intentFilter);
    }

    public void unRegisterReceiver(Context mContext){
        if(null != mReceiver){
            mContext.unregisterReceiver(mReceiver);
        }
        this.mOnPlayViewClickedListener = null;
    }

    public interface OnPlayViewClickedListener{
        void onPlayClicked(boolean isPlay);
    }

    public void setOnPlayViewClickedListener(OnPlayViewClickedListener mOnPlayViewClickedListener){
        this.mOnPlayViewClickedListener = mOnPlayViewClickedListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        String notifyOnClickedString3 = intent.getStringExtra(INTENT_NOTIFY_CLICKED_TEXT);
        ALog.Log("notifyOnClickedString3: "+notifyOnClickedString3);
        String action = intent.getAction();
        ALog.Log("intent.toString: "+intent.toString());
        if(action.equals(ACTION_VIEW_ON_CLICK)){
            //通过传递过来的ID判断按钮点击属性或者通过getResultCode()获得相应点击事件
            int buttonId = intent.getIntExtra(INTENT_VIEW_TAG, 0);
            switch (buttonId) {
                case NOTIFY_CLICKED_ALL://通知整体被点击
                    String notifyOnClickedString = intent.getStringExtra(INTENT_NOTIFY_CLICKED_TEXT);
                    ALog.Log("notifyOnClickedString2: "+notifyOnClickedString);
                    Toast.makeText(context, notifyOnClickedString, Toast.LENGTH_SHORT).show();
                    break;
                case BUTTON_PREV_ID:
                    Toast.makeText(context, "上一首", Toast.LENGTH_SHORT).show();
                    break;
                case BUTTON_PALY_ID:
                    isPlay = !isPlay;
                    String play_status = "";
                    if(isPlay){
                        play_status = "开始播放";
                    }else{
                        play_status = "已暂停";
                    }
                    if(null != this.mOnPlayViewClickedListener){
                        this.mOnPlayViewClickedListener.onPlayClicked(isPlay);
                    }
                    Toast.makeText(context, play_status, Toast.LENGTH_SHORT).show();
                    break;
                case BUTTON_NEXT_ID:
                    Toast.makeText(context, "下一首", Toast.LENGTH_SHORT).show();
                    break;
                case ACTION_YES_ID:
                    Toast.makeText(context, "Action Yes按钮", Toast.LENGTH_SHORT).show();
                    break;
                case ACTION_NO_ID:
                    Toast.makeText(context, "Action NO按钮", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    }

    //以下定义通知整体点击响应PendingIntent，PendingIntent.getBroadcast注意requestCode的数值要区分开。
    private static int requestCode1 = 0x1a2b3c;
    public static PendingIntent getContentIntentNotification(Context mContext, int flags, String notifyOnClickedString){
        Intent intent = new Intent(ACTION_VIEW_ON_CLICK);
        intent.putExtra(INTENT_VIEW_TAG, NOTIFY_CLICKED_ALL);
        intent.putExtra(INTENT_NOTIFY_CLICKED_TEXT, notifyOnClickedString);//用户点击通知整体后显示的内容
        ALog.Log("notifyOnClickedString: "+notifyOnClickedString);
        /**
         * requestCode：标识当前Broadcast，如果多次使用同一个requestCode的话，那么最终得到的只是第一个pendingIntent，因此如果想
         * 获取不同的PendingIntent，需要区分所有PendingIntent.getBroadcast函数的requestCode参数
         */
        PendingIntent pendingIntent= PendingIntent.getBroadcast(mContext, requestCode1++, intent, flags);
        return pendingIntent;
    }

    //以下定义自定义播放器的按钮点击响应PendingIntent
    public static void setOnClickPendingIntentMusic(Context mContext, RemoteViews mRemoteViews){
        //点击的事件处理
        Intent buttonIntent = new Intent(ACTION_VIEW_ON_CLICK);
        /* 上一首按钮 */
        buttonIntent.putExtra(INTENT_VIEW_TAG, BUTTON_PREV_ID);
        //这里加了广播，所及INTENT的必须用getBroadcast方法
        PendingIntent intent_prev = PendingIntent.getBroadcast(mContext, BUTTON_PREV_ID, buttonIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mRemoteViews.setOnClickPendingIntent(R.id.btn_custom_prev, intent_prev);
        /* 播放/暂停  按钮 */
        buttonIntent.putExtra(INTENT_VIEW_TAG, BUTTON_PALY_ID);
        PendingIntent intent_paly = PendingIntent.getBroadcast(mContext, BUTTON_PALY_ID, buttonIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mRemoteViews.setOnClickPendingIntent(R.id.btn_custom_play, intent_paly);
        /* 下一首 按钮  */
        buttonIntent.putExtra(INTENT_VIEW_TAG, BUTTON_NEXT_ID);
        PendingIntent intent_next = PendingIntent.getBroadcast(mContext, BUTTON_NEXT_ID, buttonIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mRemoteViews.setOnClickPendingIntent(R.id.btn_custom_next, intent_next);
    }

    /**
     * 以下定义通知Action按钮的点击响应PendingIntent
     */
    public static PendingIntent getActionIntentYes(Context mContext, int flags){
        Intent intent = new Intent(ACTION_VIEW_ON_CLICK);
        intent.putExtra(INTENT_VIEW_TAG, ACTION_YES_ID);
        PendingIntent pendingIntent= PendingIntent.getBroadcast(mContext, ACTION_YES_ID, intent, flags);
        return pendingIntent;
    }

    public static PendingIntent getActionIntentNo(Context mContext, int flags){
        Intent intent = new Intent(ACTION_VIEW_ON_CLICK);
        intent.putExtra(INTENT_VIEW_TAG, ACTION_NO_ID);
        PendingIntent pendingIntent= PendingIntent.getBroadcast(mContext, ACTION_NO_ID, intent, flags);
        return pendingIntent;
    }
}
