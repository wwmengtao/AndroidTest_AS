package com.example.testmodule.notification.views;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RemoteViews;

import com.example.testmodule.BaseAcitivity;
import com.example.testmodule.R;
import com.example.testmodule.notification.notifiutils.NotificationImpl;
import com.example.testmodule.notification.notifiutils.NotifyImplFactoryManager;
import com.example.testmodule.notification.notifiutils.RemoteViewUtil;
import com.example.testmodule.notification.receiver.NotifyReceiver;
import com.fernandocejas.android10.sample.data.ALog;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class NotificationActivity extends BaseAcitivity implements NotifyReceiver.OnPlayViewClickedListener,
        NotifyReceiver.OnNotifyClickedListener, NotifyReceiver.OnNotifyDeletedListener{
    private Unbinder mUnbinder = null;
    private String NOTIFICATION_TAG = "TestModule.Notification";//用于标识发送/取消广播时候的tag
    private NotificationManager mNotificationManager = null;
    private NotifyImplFactoryManager mNotifyFactoryManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        mUnbinder = ButterKnife.bind(this);
        mNotificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        doInit();
    }

    @Override
    public void onResume() {
        super.onResume();
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, android.R.color.holo_purple));
    }

    @Override
    public void onDestroy(){
        mUnbinder.unbind();
        NotifyReceiver.getInstance().unRegisterReceiver(this);
        NotifyReceiver.getInstance().setOnPlayViewClickedListener(null);
        NotifyReceiver.getInstance().setOnNotifyClickedListener(null);
        NotifyReceiver.getInstance().setOnNotifyDeletedListener(null);
        mNotificationManager.cancelAll();
        super.onDestroy();
    }

    private void doInit(){
        ArrayList<String> mViewTextList = new ArrayList<String>();
        ArrayList<String> mViewTextList2 = new ArrayList<String>();
        mNotifyFactoryManager = NotifyImplFactoryManager.getInstance(this);
        Button mButton;
        //1、初始化NotifiImplFactory
        int []buttonIDs={R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5};
        //将Button ID和NotificationImpl一一对应
        for(int i = 0; i<buttonIDs.length; i++){
            mButton = findViewById(buttonIDs[i]);
            mButton.setTag(false);
            mViewTextList.add(mButton.getText().toString());
        }
        visitAL(mViewTextList);
        mNotifyFactoryManager.addNotificationImpls(NotifyImplFactoryManager.FACTORY_TYPE.TYPE_COMMON, buttonIDs, mViewTextList);
        //2、初始化NotifiImplCompactFactory
        int []buttonCompactIDs={R.id.btn100, R.id.btn101, R.id.btn1011, R.id.btn102, R.id.btn103, R.id.btn104};
        //将Button Compact ID和NotificationImpl一一对应
        for(int i = 0; i<buttonCompactIDs.length; i++){
            mButton = findViewById(buttonCompactIDs[i]);
            mButton.setTag(false);
            mViewTextList2.add(mButton.getText().toString());
        }
        visitAL(mViewTextList2);
        mNotifyFactoryManager.addNotificationImpls(NotifyImplFactoryManager.FACTORY_TYPE.TYPE_COMPACT, buttonCompactIDs, mViewTextList2);
        //3、设置监听广播
        NotifyReceiver.getInstance().registerReceiver(this);
        NotifyReceiver.getInstance().setOnPlayViewClickedListener(this);
        NotifyReceiver.getInstance().setOnNotifyClickedListener(this);
        NotifyReceiver.getInstance().setOnNotifyDeletedListener(this);

    }

    private void visitAL(ArrayList<String> al){
        ALog.Log("visitAL");
        for(String str:al){
            ALog.Log(str);
        }
    }

    int index = 0;
    @OnClick({R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,R.id.btn5,
              R.id.btn100, R.id.btn101, R.id.btn1011, R.id.btn102, R.id.btn103, R.id.btn104})
    public void onClick(View view){
        index++;
        NotificationImpl mNotificationImpl = mNotifyFactoryManager.getNotificationImpl(view.getId());
        if (null == mNotificationImpl)return;
        Button mButton = findViewById(view.getId());
        if(!(Boolean) mButton.getTag()){
            if(0 == index%2){//采用两种不同的发送通知的方式
                mNotificationImpl.sendNotify(NOTIFICATION_TAG, view.getId());
            }else{
                mNotificationImpl.sendNotify(view.getId());
            }
            mButton.setTag(true);
        }else{
            mNotificationImpl.cancelNotify();
            mButton.setTag(false);
        }
    }

    @Override
    public void onPlayClicked(boolean isPlay) {//如果自定义播放器的播放按钮被点击
        RemoteViews mRemoteViews = RemoteViewUtil.getMusicRemoteView(this, isPlay);
        NotificationImpl mNotificationImpl = mNotifyFactoryManager.getNotificationImpl(R.id.btn1011);
        mNotificationImpl.getNotificationCompatBuilder().setContent(mRemoteViews);
        mNotificationImpl.sendNotify(R.id.btn1011);
    }

    @Override
    public void onNotifyClicked(int viewID) {//如下viewID对应的通知是可以通过点击取消的
        Button mButton = null;
        switch (viewID){
            case R.id.btn0:
            case R.id.btn1:
            case R.id.btn2:
            case R.id.btn3:
            case R.id.btn4:
            case R.id.btn5:
                mButton = (Button) findViewById(viewID);
                mButton.setTag(false);
                break;
        }
    }

    @Override
    public void onNotifyDeleted(int viewID) {//如下viewID对应的通知是可以通过滑动取消的
        Button mButton = null;
        switch (viewID){
            case R.id.btn100:
            case R.id.btn101:
            case R.id.btn1011:
            case R.id.btn102:
            case R.id.btn103:
            case R.id.btn104:
                mButton = (Button) findViewById(viewID);
                mButton.setTag(false);
                break;
        }
    }
}
