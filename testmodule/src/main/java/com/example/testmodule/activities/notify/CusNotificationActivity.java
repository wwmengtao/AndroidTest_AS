package com.example.testmodule.activities.notify;

import android.os.Bundle;
import android.widget.Button;

import com.example.testmodule.ALog;
import com.example.testmodule.BaseAcitivity;
import com.example.testmodule.R;
import com.example.testmodule.notification.notifiutils.NotifiImplCPFactory;
import com.example.testmodule.notification.notifiutils.NotificationImpl;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CusNotificationActivity extends BaseAcitivity {
    private NotificationImpl mNotificationImpl = null;
    @BindView(R.id.btn1) Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cus_notification);
        mUnbinder = ButterKnife.bind(this);
        init();
    }

    private void init(){
        mButton.setTag(false);
        mNotificationImpl = NotifiImplCPFactory.build(mContext).get(20,
                -1, "Custom notification");
    }

    @OnClick({R.id.btn1})
    public void showCustomeNotify(){
        ALog.Log("showCustomeNotify1");
        if(!(Boolean) mButton.getTag()){
            ALog.Log("showCustomeNotify");
            mNotificationImpl.sendNotify(mButton.getId());
            mButton.setTag(true);
        }else{
            mNotificationImpl.cancelNotify();
            mButton.setTag(false);
        }
    }
}
