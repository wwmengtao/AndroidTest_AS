package com.mt.myapplication.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.mt.androidtest_as.alog.BaseActivity;

import java.util.Date;
/**
 * 竖屏状态下日期选择使用Activity方式
 * Created by Mengtao1 on 2017/10/18.
 */
public class DatePickerActivity extends BaseActivity {

    public static Intent newIntent(Context mContext, Date date){
        Intent mIntent = new Intent(mContext,DatePickerActivity.class);
        mIntent.putExtra(CrimeFragment.CRIME_DATE,date);
        return mIntent;
    }

    @Override
    public Fragment getFragment() {
        Intent it = getIntent();
        Date date = (Date)it.getSerializableExtra(CrimeFragment.CRIME_DATE);
        return DatePickerFragment2.newInstance(date);
    }
}
