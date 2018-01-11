package com.mt.myapplication.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;


import com.mt.androidtest_as.R;
import com.example.androidcommon.alog.ALogActivity;
import com.mt.myapplication.criminalintent.crimebasedata.Crime;
import com.mt.myapplication.criminalintent.crimebasedata.CrimeLab;

import java.util.List;
import java.util.UUID;

/**
 * Created by Mengtao1 on 2016/12/9.
 */

public class CrimePagerActivity extends ALogActivity{
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
        mViewPager = (ViewPager)findViewById(R.id.activity_crime_pager_view_pager);
        //设置ViewPager内部页面之间的间距
        mViewPager.setPageMargin(getResources().getDimensionPixelSize(R.dimen.page_margin_width));
        //设置ViewPager内部页面间距的drawable
        mViewPager.setPageMarginDrawable(R.drawable.divider);//或者是R.color.royalblue
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

    private void setResultForPosition(){
        int position = mViewPager.getCurrentItem();
        Intent mIntent1 = getIntent();
        if(null!=mIntent1) {
            mIntent1.putExtra(CURRENT_CRIME_ID_VIEWPAGER, position);
            setResult(RESULT_CODE, mIntent1);//在onPause函数里面setResult是没有效果的
        }
    }

    @Override
    public void finish() {
        /**setResultForPosition必须在super.finish()之前执行，否则无效
         * Activity.finish()部分代码如下：
         * if (ActivityManagerNative.getDefault()
         * .finishActivity(mToken, resultCode, resultData, finishTask))
         * 说明finish函数中已经将resultCode和resultData写入，在finish之后将无效
         */
        setResultForPosition();
        super.finish();
//        setResultForPosition();//setResultForPosition在此处执行无效
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();//会调用finish()
    }
}
