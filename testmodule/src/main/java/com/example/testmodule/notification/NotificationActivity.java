package com.example.testmodule.notification;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;

import com.example.testmodule.BaseAcitivity;
import com.example.testmodule.R;
import com.example.testmodule.notification.notifiutils.NotifiImplCompactFactory;
import com.example.testmodule.notification.notifiutils.NotifiImplFactory;
import com.example.testmodule.notification.notifiutils.NotificationImpl;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class NotificationActivity extends BaseAcitivity {
    private Unbinder mUnbinder;
    private NotificationManager mNotificationManager = null;
    private String NOTIFICATION_TAG = "TestModule.Notification";//用于标识发送/取消广播时候的tag
    private SparseArray<NotificationImpl> mNotificationImplArray = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        mNotificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        mUnbinder = ButterKnife.bind(this);
        doInit();
    }

    @Override
    public void onResume() {
        super.onResume();
//        Window window = getWindow();
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//        window.setStatusBarColor(ContextCompat.getColor(this, android.R.color.holo_purple));
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        mUnbinder.unbind();
    }

    private void doInit(){
        Button mButton;
        mNotificationImplArray = new SparseArray<>();
        //1、初始化NotifiImplFactory
        int []buttonIDs={R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4};
        //将Button ID和NotificationImpl一一对应
        for(int i = 0; i<buttonIDs.length; i++){
            mButton = findViewById(buttonIDs[i]);
            mButton.setTag(false);
            mNotificationImplArray.put(buttonIDs[i], NotifiImplFactory.build(this).get(i));
        }
        //2、初始化NotifiImplCompactFactory
        int []buttonCompactIDs={R.id.btn100, R.id.btn101, R.id.btn102, R.id.btn103, R.id.btn104};
        //将Button Compact ID和NotificationImpl一一对应
        for(int i = 0; i<buttonCompactIDs.length; i++){
            mButton = findViewById(buttonCompactIDs[i]);
            mButton.setTag(false);
            mNotificationImplArray.put(buttonCompactIDs[i], NotifiImplCompactFactory.build(this).get(i));
        }
    }

    @OnClick({R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
              R.id.btn100, R.id.btn101, R.id.btn102, R.id.btn103, R.id.btn104})
    public void onClick(View view){
        NotificationImpl mNotificationImpl = mNotificationImplArray.get(view.getId());
        Button mButton = findViewById(view.getId());
        if(!(Boolean) mButton.getTag()){
            mNotificationImpl.sendNotify(view.getId());
            mButton.setTag(true);
        }else{
            mNotificationImpl.cancelNotify();
            mButton.setTag(false);
        }
    }

}
