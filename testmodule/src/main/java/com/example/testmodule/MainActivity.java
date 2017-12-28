package com.example.testmodule;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

import com.example.testmodule.location.LocationActivity;
import com.example.testmodule.notification.NotifiListActivity;
import com.example.testmodule.viewpager.ViewPagerGatherActivity;
import com.example.testmodule.windowmanager.WindowManagerActivity;
import com.example.testmodule.windowmanager.WindowTools;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseAcitivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mUnbinder = ButterKnife.bind(this);
        initActivities(buttonIDs, classEs);
    }

    int []buttonIDs={R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8};
    Class<?>[] classEs ={SQLiteActivity.class, WebViewActivity.class, EventBusActivity.class, XmlParserActivity.class,
            NotifiListActivity.class, ViewPagerGatherActivity.class, WindowManagerActivity.class, LocationActivity.class};

    @OnClick({R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8})
    public void onClickActivity(View view){
        Class<?> activity = mActivitySA.get(view.getId());
        if(activity.getSimpleName().equals("WindowManagerActivity") && !WindowTools.canDrawOverlays(this)){
            showDialog();
        }else {
            startActivity(getCallingIntent(this, activity));
        }
    }

    private DialogInterface mShowDialog;
    private void showDialog() {
        mShowDialog = new AlertDialog.Builder(this)
            .setTitle(getResources().getString(R.string.open_can_draw_overlays))
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setPositiveButton(android.R.string.yes, mDialogListener)
            .setNegativeButton(android.R.string.no, null)
            .show();
    }

    private DialogInterface.OnClickListener mDialogListener = new DialogInterface.OnClickListener(){
        @Override
        public void onClick(DialogInterface dialog, int i) {
            if (dialog == mShowDialog) {
                if(i == DialogInterface.BUTTON_POSITIVE){
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" +getPackageName()));
                    startActivity(intent);
                }
            }
        }
    };
}
