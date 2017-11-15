package com.mt.myapplication.novicetutorial.model;

import com.mt.myapplication.novicetutorial.com.fernandocejas.android10.sample.presentation.model.UserModelNT;

import java.util.Collection;

/**
 * MessageEvent：EventBus所使用的事件
 */
public class MessageEvent {
    public static enum EVENT_TYPE{
        TO_VIEWPAGER,//此时的EventBus事件是发送给ViewPager类型视图对象的
        FROM_VIEWPAGE,
        FROM_LISTVIEW,//此时的EventBus事件是接收自LIST类型视图对象的
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
     * 一、下列setXXX以及getXXX等设置/获取数据的操作是接收ViewPager/ListView对象返回数据的，此时的事件类型为EVENT_TYPE.FROM_VIEWPAGE
     */
    private int currentIndex = -1;
    public void setCurrentIndex(int currentIndex){
        if(null != mEventType && (mEventType == EVENT_TYPE.FROM_VIEWPAGE || mEventType == EVENT_TYPE.FROM_LISTVIEW)) {
            this.currentIndex = currentIndex;
        }
    }

    public int getCurrentIndex(){
        if(null != mEventType && (mEventType == EVENT_TYPE.FROM_VIEWPAGE || mEventType == EVENT_TYPE.FROM_LISTVIEW)) {
            return currentIndex;
        }
        return -99;
    }

    /**
     * 二、下列setXXX以及getXXX等设置/获取数据的操作是发送给ViewPager对象的，此时的事件类型为EVENT_TYPE.TO_VIEWPAGER
     */
    private UserModelNT mUserModelNT = null;
    private Collection<UserModelNT> userModelsCollection = null;
    public void setData(UserModelNT mUserModelNT){
        if(null != mEventType && mEventType == EVENT_TYPE.TO_VIEWPAGER){
            this.mUserModelNT = mUserModelNT;
        }
    }

    public UserModelNT getData(){
        if(null != mEventType && mEventType == EVENT_TYPE.TO_VIEWPAGER) {
            return mUserModelNT;
        }
        return null;
    }

    public void setDataCollection(Collection<UserModelNT> userModelsCollection){
        if(null != mEventType && mEventType == EVENT_TYPE.TO_VIEWPAGER) {
            this.userModelsCollection = userModelsCollection;
        }
    }

    public Collection<UserModelNT> getDataCollection(){
        if(null != mEventType && mEventType == EVENT_TYPE.TO_VIEWPAGER) {
            return userModelsCollection;
        }
        return null;
    }
}
