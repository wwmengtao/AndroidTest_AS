package com.mt.myapplication.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;


import com.mt.androidtest_as.R;
import com.mt.androidtest_as.alog.ALogActivity;
import com.mt.myapplication.criminalintent.crimebasedata.Crime;
import com.mt.myapplication.criminalintent.crimebasedata.CrimeLab;

import java.util.List;
import java.util.UUID;

/**
 * Created by Mengtao1 on 2016/12/9.
 */

public class CrimePagerActivity extends ALogActivity implements CrimeFragment.Callbacks{
    public static final String CRIME_ID = "CrimePagerActivity_CrimeID";
    public static final String CURRENT_CRIME_ID_VIEWPAGER = "CrimePagerActivity_Current_CrimeID";
    public static final int RESULT_CODE = 0x11;
    private ViewPager mViewPager=null;
    private List<Crime> mData = null;
    public static Intent newIntent(Context mContext, UUID crimeID){
        Intent mIntent = new Intent(mContext,CrimePagerActivity.class);
        mIntent.putExtra(CRIME_ID,crimeID);
        return mIntent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);
        setTitle("CrimePagerActivity");
        mViewPager = (ViewPager)findViewById(R.id.activity_crime_pager_view_pager);
        mData = CrimeLab.get(this).getCrimes();
        UUID crimeID = (UUID)getIntent().getSerializableExtra(CRIME_ID);
        mViewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()){

            @Override
            public int getCount() {
                return mData.size();
            }

            @Override
            public Fragment getItem(int position) {
                return CrimeFragment.newFragment(mData.get(position).getId());
            }
        });

        for (int i = 0; i < mData.size(); i++) {
            if (mData.get(i).getId().equals(crimeID)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }

    @Override
    public void onBackPressed(){
        int position = mViewPager.getCurrentItem();
        Intent mIntent1 = getIntent();
        if(null!=mIntent1) {
            mIntent1.putExtra(CURRENT_CRIME_ID_VIEWPAGER, position);
            setResult(RESULT_CODE, mIntent1);//在onPause函数里面setResult是没有效果的
        }
        super.onBackPressed();
    }

    @Override
    public void onCrimeUpdated() {

    }
}
