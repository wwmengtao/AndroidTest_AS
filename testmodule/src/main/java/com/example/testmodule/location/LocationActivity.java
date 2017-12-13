package com.example.testmodule.location;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.example.testmodule.ALog;
import com.example.testmodule.BaseAcitivity;
import com.example.testmodule.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class LocationActivity extends BaseAcitivity {
    private Unbinder mUnbinder;
    private LocationUtils mLocationUtils = null;
    @BindView(R.id.address)TextView mAddressTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        mUnbinder = ButterKnife.bind(this);
        mLocationUtils = LocationUtils.build(this);
        if(!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        checkPermission();
    }

    @Override
    protected void onResume(){
        super.onResume();

    }

    @Override
    protected void onPause(){
        super.onPause();
    }

    private void showDialog() {
        AlertDialog.Builder mAlertDialog;
        mAlertDialog = new AlertDialog.Builder(this).setTitle("开启定位设置")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int which) {
                        mLocationUtils.gotoLocServiceSettings();
                        ALog.Log("getAddresslocation");
                        mLocationUtils.getAddresslocation();
                    }
                })
                .setNegativeButton("No", null);
        mAlertDialog.show();
    }

    private void startLocating(){
        if(!mLocationUtils.isOpenLocService()){//如果没有开启定位服务
            showDialog();
        }
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onMessage(MessageEvent event) {//显示用户地理信息
        ALog.Log("onMessage");
        Address address = event.getAddress();
        if(null == address){
            mAddressTV.setText("No location!");
        }else{
            mAddressTV.setText(address.toString());
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        mLocationUtils.removeLocationListener();
        mLocationUtils.removeLocationListener();

        mLocationUtils.removeLocationListener();

        mUnbinder.unbind();
        EventBus.getDefault().unregister(this);
    }

    private void checkPermission(){
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 12345);
        }else{
            startLocating();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        ALog.Log("onRequestPermissionsResult: "+(grantResults[0] == PackageManager.PERMISSION_GRANTED));
        if((grantResults[0] == PackageManager.PERMISSION_GRANTED)){//获取了定位权限后才可以获取地理信息
            startLocating();
        }else{
            mAddressTV.setText("No permission ACCESS_FINE_LOCATION!");
        }
    }
}
