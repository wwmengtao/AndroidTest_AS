package com.example.testmodule.notification.notifiutils;

import android.content.Context;
import android.widget.RemoteViews;

import com.example.testmodule.R;

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
    private static RemoteViews mMusicRemoteView = null;
    public static RemoteViews getMusicRemoteView(Context mContext, boolean isPlay){
        if(android.os.Build.VERSION.SDK_INT <= 9)return null;
        if(null == mMusicRemoteView) {
            mMusicRemoteView = new RemoteViews(mContext.getPackageName(), R.layout.view_custom_button);
            mMusicRemoteView.setImageViewResource(R.id.custom_song_icon, R.drawable.sing_icon);
            mMusicRemoteView.setTextViewText(R.id.tv_custom_song_singer, "周杰伦");
            mMusicRemoteView.setTextViewText(R.id.tv_custom_song_name, "七里香");
        }
        if(isPlay){
            mMusicRemoteView.setImageViewResource(R.id.btn_custom_play, R.drawable.btn_play);
        }else{
            mMusicRemoteView.setImageViewResource(R.id.btn_custom_play, R.drawable.btn_pause);
        }
        return mMusicRemoteView;
    }
}
