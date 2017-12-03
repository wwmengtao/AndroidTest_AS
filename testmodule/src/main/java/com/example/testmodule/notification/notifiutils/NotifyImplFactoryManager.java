package com.example.testmodule.notification.notifiutils;

import android.content.Context;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by mengtao1 on 2017/11/28.
 */

public class NotifyImplFactoryManager {
    private volatile static NotifyImplFactoryManager mNotifyManager = null;
    private Context mContext = null;
    private ConcurrentHashMap<Integer, NotificationImpl> mNotificationImplMap = null;//存储NotificationImpl对象

    /**
     * FACTORY_TYPE：标识NotificationImpl生产工厂的类型。
     * TYPE_COMMON：NotifiImplFactory；
     * TYPE_COMPACT：NotifiImplCompactFactory
     */
    public static enum FACTORY_TYPE{
        TYPE_COMMON,
        TYPE_COMPACT
    }

    public static synchronized NotifyImplFactoryManager getInstance(Context mContext) {
        if (null == mNotifyManager) {
            mNotifyManager = new NotifyImplFactoryManager(mContext);
        }
        return mNotifyManager;
    }

    private NotifyImplFactoryManager(Context mContext){
        this.mContext = mContext;
//        this.mNotificationImplArray = new SparseArray<>();
        this.mNotificationImplMap = new ConcurrentHashMap<>();
    }


    public void addNotificationImpls(final FACTORY_TYPE factoryType, final int []viewIDs, final ArrayList<String> mViewTextList){
        if(null == viewIDs || 0 == viewIDs.length)return;
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i < viewIDs.length; i++){
                    NotificationImpl mNotificationImpl = null;
                    if (FACTORY_TYPE.TYPE_COMMON == factoryType){
                        mNotificationImpl = NotifiImplFactory.build(mContext).get(i,
                                viewIDs[i], (mViewTextList.get(i)+"_Notification.Builder"));
                    }else if(FACTORY_TYPE.TYPE_COMPACT == factoryType){
                        mNotificationImpl = NotifiImplCPFactory.build(mContext).get(i,
                                viewIDs[i], (mViewTextList.get(i)+"_NotificationCompat.Builder"));
                    }
                    mNotificationImplMap.put(viewIDs[i], mNotificationImpl);
                }
            }
        }).start();
    }

    public NotificationImpl getNotificationImpl(int viewID){
        return mNotificationImplMap.get(viewID);
    }
}
