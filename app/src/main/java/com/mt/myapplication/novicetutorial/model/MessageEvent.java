package com.mt.myapplication.novicetutorial.model;

import com.mt.myapplication.novicetutorial.com.fernandocejas.android10.sample.presentation.model.UserModelNT;

import java.util.Collection;

/**
 * MessageEvent：EventBus所使用的事件
 */
public class MessageEvent {
    public static enum EVENT_TYPE{
        VIEWPAGER//此时的EventBus事件是针对ViewPager类型
    };

    private String message = null;
    private EVENT_TYPE mEventType = null;

    public MessageEvent(String message) {
        this.message = message;
    }

    public MessageEvent(){

    }

    public void setEventType(EVENT_TYPE type){
        this.mEventType = type;
    }

    public EVENT_TYPE getEventType(){
        return this.mEventType;
    }

    /**
     * 下列setXXX以及getXXX等设置/获取数据的操作是针对ViewPager浏览方式的
     * @param mUserModelNT
     */
    private UserModelNT mUserModelNT = null;
    private Collection<UserModelNT> userModelsCollection = null;
    public void setData(UserModelNT mUserModelNT){
        if(null != mEventType && mEventType == EVENT_TYPE.VIEWPAGER){
            this.mUserModelNT = mUserModelNT;
        }
    }

    public UserModelNT getData(){
        if(null != mEventType && mEventType == EVENT_TYPE.VIEWPAGER) {
            return mUserModelNT;
        }
        return null;
    }

    public void setDataCollection(Collection<UserModelNT> userModelsCollection){
        if(null != mEventType && mEventType == EVENT_TYPE.VIEWPAGER) {
            this.userModelsCollection = userModelsCollection;
        }
    }

    public Collection<UserModelNT> getDataCollection(){
        if(null != mEventType && mEventType == EVENT_TYPE.VIEWPAGER) {
            return userModelsCollection;
        }
        return null;
    }
}
