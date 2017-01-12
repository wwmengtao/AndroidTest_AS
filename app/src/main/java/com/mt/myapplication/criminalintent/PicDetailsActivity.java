package com.mt.myapplication.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.mt.androidtest_as.alog.BaseActivity;

import java.io.File;

public class PicDetailsActivity extends BaseActivity {

    public static Intent newIntent(Context mContext, File file){
        Intent mIntent = new Intent(mContext,PicDetailsActivity.class);
        mIntent.putExtra(PicDetailsFragment2.DPF_PIC,file);
        return mIntent;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActionBar();
    }

    @Override
    public Fragment getFragment() {
        Intent it = getIntent();
        File file = (File)it.getSerializableExtra(PicDetailsFragment2.DPF_PIC);
        return PicDetailsFragment2.newInstance(file);
    }
}
