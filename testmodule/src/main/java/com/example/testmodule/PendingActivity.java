package com.example.testmodule;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.testmodule.services.PendingService;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.testmodule.services.PendingService.EXTRA_PENDING_INTENT;

/**
 * PendingActivity：用于开启/关闭执行延迟操作的Service
 * Created by mengtao1 on 2018/1/19.
 */
public class PendingActivity extends BaseAcitivity {
    private Intent mIntent = null;
    @BindView(R.id.serviceState) TextView mTVServiceState;
    @BindView(R.id.startService) Button mBtnStartService;
    @BindView(R.id.stopService) Button mBtnStopService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending);
        mUnbinder = ButterKnife.bind(this);
        mBtnStartService.setEnabled(true);
        mBtnStopService.setEnabled(false);
    }

    @OnClick(R.id.startService)
    public void startService(){
        sendTo();
    }

    private void sendTo(){
        if(null == mIntent){
            mIntent = PendingService.getLaunchIntent(this);
            mIntent.putExtra(EXTRA_PENDING_INTENT,PendingService.getSendToPendingIntent(this));
        }
        startService(mIntent);
        mTVServiceState.setText("Service is now running...");
        mBtnStartService.setEnabled(false);
        mBtnStopService.setEnabled(true);
    }//end sendTo

    @OnClick(R.id.stopService)
    public void stopService(){
        if(null != mIntent){
            stopService(mIntent);
            mTVServiceState.setText("Service is now stopped!");
            mBtnStartService.setEnabled(true);
            mBtnStopService.setEnabled(false);
        }
    }//end stopService
}
