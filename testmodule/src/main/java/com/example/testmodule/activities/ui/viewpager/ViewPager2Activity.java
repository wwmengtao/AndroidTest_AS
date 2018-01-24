package com.example.testmodule.activities.ui.viewpager;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;

import com.example.testmodule.ALog;
import com.example.testmodule.BaseAcitivity;
import com.example.testmodule.R;
import com.example.testmodule.viewpager.adapter.VPTAdapter;
import com.example.testmodule.viewpager.data.morning.MockDataEngine;
import com.example.testmodule.viewpager.data.morning.TrendingData;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewPager2Activity extends BaseAcitivity implements ViewPager.OnPageChangeListener{
    private MockDataEngine mMockDataEngine = null;
    @BindView(R.id.view_pager) ViewPager mViewPager;
    private VPTAdapter mVPTAdapter = null;
    private List<TrendingData> mTrendingData = null;
    @BindView(R.id.ll_dot_group) LinearLayout ll_dot_group; //used to add dots
    private int previousPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager2);
        mUnbinder = ButterKnife.bind(this);
        mMockDataEngine = MockDataEngine.getInstance(mContext);
        mTrendingData = mMockDataEngine.getTrendingData();
        init();
    }

    private void init(){
        //1.init dot view
        initDotView();
        //2.init viewpager
        mViewPager.setPageMargin(10);
        mVPTAdapter = new VPTAdapter(mContext);
        mVPTAdapter.setData(mTrendingData);
        mViewPager.setAdapter(mVPTAdapter);
        mViewPager.addOnPageChangeListener(this);
        mViewPager.setCurrentItem(mVPTAdapter.getInitialIndex());
    }

    private void initDotView(){
        //init dot view
        View dotView = null;
        for(int i = 0; i < mTrendingData.size(); i++){
            dotView = new View(mContext);
            dotView.setBackgroundResource(R.drawable.selector_dot);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(10, 10);
            dotView.setEnabled(false);
            if (i != 0){
                params.leftMargin = 20;
            }else{
                dotView.setEnabled(true);
            }
            dotView.setLayoutParams(params);
            ll_dot_group.addView(dotView);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int pos) {
        int position = pos % mTrendingData.size();
        ll_dot_group.getChildAt(previousPosition).setEnabled(false);
        ll_dot_group.getChildAt(position).setEnabled(true);
        previousPosition = position;
        ALog.Log("onPageSelected: "+ position+" "+previousPosition);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
