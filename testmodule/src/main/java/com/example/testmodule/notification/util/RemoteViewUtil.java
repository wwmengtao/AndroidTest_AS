package com.example.testmodule.notification.util;

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

}
