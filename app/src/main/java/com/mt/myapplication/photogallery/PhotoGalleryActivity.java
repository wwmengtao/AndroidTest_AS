package com.mt.myapplication.photogallery;

import android.support.v4.app.Fragment;

import com.mt.androidtest_as.alog.BaseActivity;


public class PhotoGalleryActivity extends BaseActivity {
    
    @Override
    public Fragment getFragment() {
        return PhotoGalleryFragment.newInstance();
    }

}
