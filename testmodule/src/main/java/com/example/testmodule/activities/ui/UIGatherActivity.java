package com.example.testmodule.activities.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

import com.example.testmodule.BaseAcitivity;
import com.example.testmodule.R;
import com.example.testmodule.activities.ui.viewpager.ViewPagerGatherActivity;
import com.example.testmodule.windowmanager.WindowTools;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class UIGatherActivity extends BaseAcitivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uigather);
        mUnbinder = ButterKnife.bind(this);
        initActivities(buttonIDs, classEs);
    }

    int []buttonIDs={R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5};
    Class<?>[] classEs ={ViewPagerGatherActivity.class, TransparentActivity.class, WebViewActivity.class, WindowManagerActivity.class,
            DrawerActivity.class};

    @OnClick({R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5})
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
