package com.example.testmodule.notification.util;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RemoteViews;

import com.example.testmodule.R;
import com.example.testmodule.notification.receiver.ButtonBCReceiver;

/**
 * Created by mengtao1 on 2017/11/25.
 */

public class RemoteViewUtil {

    /**
     * getRemoteViews：获取特定颜色背景的通知布局
     * @return
     */
    public static RemoteViews getRemoteViews(Context mContext){
        CharSequence title = "Moto";
        CharSequence title2 = mContext.getResources().getString(R.string.unplugged);
        CharSequence text = mContext.getResources().getString(R.string.unplugged_content);
        RemoteViews mRemoteViews = new RemoteViews(mContext.getPackageName(), R.layout.notification);
        mRemoteViews.setImageViewResource(R.id.background, R.drawable.rectangle);
        mRemoteViews.setTextViewText(R.id.title, title);
        mRemoteViews.setTextViewText(R.id.title2, title2);
        mRemoteViews.setTextViewText(R.id.text, text);
        return mRemoteViews;
    }

    /**
     * getMusicRemoteView：获取播放器自定义通知视图
     * @param mContext
     * @return
     */
    public static RemoteViews getMusicRemoteView(Context mContext){
        RemoteViews mRemoteViews = new RemoteViews(mContext.getPackageName(), R.layout.view_custom_button);
        mRemoteViews.setImageViewResource(R.id.custom_song_icon, R.drawable.sing_icon);
        //API3.0 以上的时候显示按钮，否则消失
        mRemoteViews.setTextViewText(R.id.tv_custom_song_singer, "周杰伦");
        mRemoteViews.setTextViewText(R.id.tv_custom_song_name, "七里香");
        //如果版本号低于（3.0），那么不显示按钮
        if(BaseTools.getSystemVersion() <= 9){
            mRemoteViews.setViewVisibility(R.id.ll_custom_button, View.GONE);
        }else{
            mRemoteViews.setViewVisibility(R.id.ll_custom_button, View.VISIBLE);
            //
            if(ButtonBCReceiver.isPlay){
                mRemoteViews.setImageViewResource(R.id.btn_custom_play, R.drawable.btn_pause);
            }else{
                mRemoteViews.setImageViewResource(R.id.btn_custom_play, R.drawable.btn_play);
            }
        }
        return mRemoteViews;
    }

    /**
     * 获取特定PendingIntent的类
     */
    public static class PIntentUtil {

        public static void setOnClickPendingIntentMusic(Context mContext, RemoteViews mRemoteViews){
            //点击的事件处理
            Intent buttonIntent = new Intent(ButtonBCReceiver.ACTION_BUTTON_ON_CLICK);
		/* 上一首按钮 */
            buttonIntent.putExtra(ButtonBCReceiver.INTENT_BUTTON_TAG, ButtonBCReceiver.BUTTON_PREV_ID);
            //这里加了广播，所及INTENT的必须用getBroadcast方法
            PendingIntent intent_prev = PendingIntent.getBroadcast(mContext, ButtonBCReceiver.BUTTON_PREV_ID, buttonIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            mRemoteViews.setOnClickPendingIntent(R.id.btn_custom_prev, intent_prev);
		/* 播放/暂停  按钮 */
            buttonIntent.putExtra(ButtonBCReceiver.INTENT_BUTTON_TAG, ButtonBCReceiver.BUTTON_PALY_ID);
            PendingIntent intent_paly = PendingIntent.getBroadcast(mContext, ButtonBCReceiver.BUTTON_PALY_ID, buttonIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            mRemoteViews.setOnClickPendingIntent(R.id.btn_custom_play, intent_paly);
		/* 下一首 按钮  */
            buttonIntent.putExtra(ButtonBCReceiver.INTENT_BUTTON_TAG, ButtonBCReceiver.BUTTON_NEXT_ID);
            PendingIntent intent_next = PendingIntent.getBroadcast(mContext, ButtonBCReceiver.BUTTON_NEXT_ID, buttonIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            mRemoteViews.setOnClickPendingIntent(R.id.btn_custom_next, intent_next);
        }

        /**
         * getDefalutIntent：@获取默认的pendingIntent,为了防止2.3及以下版本报错
         * @flags属性:
         * 在顶部常驻:Notification.FLAG_ONGOING_EVENT
         * 点击去除： Notification.FLAG_AUTO_CANCEL
         */
        public static PendingIntent getDefalutIntent(Context mContext, int flags){
            PendingIntent pendingIntent= PendingIntent.getActivity(mContext, 1, new Intent(), flags);
            return pendingIntent;
        }
    }
}
