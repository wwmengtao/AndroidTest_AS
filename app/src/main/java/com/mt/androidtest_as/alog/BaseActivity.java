package com.mt.androidtest_as.alog;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.androidcommon.alog.ALogActivity;
import com.mt.androidtest_as.R;

/**
 * Created by Mengtao1 on 2016/12/8.
 */

public abstract class BaseActivity extends ALogActivity {
    private int AndroidVersion=-1;
    private static final int REQUEST_PERMISSION_CODE = 0x001;
    private Context mContext;
    public static final String[] REQUIRED_PERMISSIONS = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            //permission.SYSTEM_ALERT_WINDOW：如果增加此权限判断，那么普通应用(没有签名，不在system区)将无法获取该权限
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getResourceID());
        mContext = getApplicationContext();
        AndroidVersion = Build.VERSION.SDK_INT;
        if(!allPermissionGranted(REQUIRED_PERMISSIONS)){
            requestPermissions(REQUIRED_PERMISSIONS);
        }
        initActionBar();
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragment = getFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }
    public abstract Fragment getFragment();

    @LayoutRes
    protected Integer getResourceID(){//用于规定BaseActivity的布局文件
        return R.layout.activity_base;
    }

    /**
     * initActionBar：如果Activity需要支持ActionBar箭头点击销毁当前Activity功能，那么在Activity中调用此方法
     */
    protected void initActionBar(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    //以下申请权限，下列方法仅在AndroidM及以上版本适用
    @TargetApi(Build.VERSION_CODES.M)
    public void requestPermissions(String [] permissionsRequired){
        if(null!=permissionsRequired && permissionsRequired.length>0){
            requestPermissions(permissionsRequired,REQUEST_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        switch (requestCode){
            case REQUEST_PERMISSION_CODE:
                if (permissions.length != 0 && isAllGranted(grantResults)){
                    Toast.makeText(this, "Get all Permissions!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "Not get all Permissions!", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }

    public boolean isAllGranted(int[] grantResults){
        if(null==grantResults)return false;
        for(int i=0;i<grantResults.length;i++){
            if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }

    private boolean allPermissionGranted(String[] requiredPermissions){
        PackageManager pm = mContext.getPackageManager();
        for(String permission : requiredPermissions){
            boolean getPermission = (PackageManager.PERMISSION_GRANTED ==
                    pm.checkPermission(permission, mContext.getPackageName()));
            if(!getPermission){
                return false;
            }
        }
        return true;
    }
}
