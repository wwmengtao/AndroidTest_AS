package com.example.androidcommon.alog;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

/**
 * CrashBaseActivity：用于处理应用异常崩溃时所需的Activity配置
 * Created by mengtao1 on 2018/1/11.
 */

public abstract class CrashBaseActivity extends ALogActivity {
    private static final int REQUEST_PERMISSION_CODE = 0x001;
    protected Context mContext = null;
    int AndroidVersion = -1;

    public static final String[] REQUIRED_PERMISSIONS = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            //permission.SYSTEM_ALERT_WINDOW：如果增加此权限判断，那么普通应用(没有签名，不在system区)将无法获取该权限
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();
        AndroidVersion = Build.VERSION.SDK_INT;
        if(!allPermissionGranted(REQUIRED_PERMISSIONS)){
            requestPermissions(REQUIRED_PERMISSIONS);
        }
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
