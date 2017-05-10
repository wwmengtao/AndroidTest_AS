package com.mt.myapplication.oschina.view;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mt.androidtest_as.R;
import com.mt.androidtest_as.alog.ALogFragment;

import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Mengtao1 on 2017/5/3.
 */

public class SolarSystemFragment extends ALogFragment {
    protected View mRoot;
    protected Context mContext;
    //
    @Bind(R.id.user_view_solar_system)
    SolarSystemView mSolarSystem;
    @Bind(R.id.rl_show_my_info)
    LinearLayout mRlShowInfo;
    @Bind(R.id.user_info_icon_container)
    ImageView mFlUserInfoIconContainer;
    //
    private float mPx;
    private float mPy;
    private int mMaxRadius;
    private int mR;
    //
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }
    //
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRoot = inflater.inflate(R.layout.fragment_solarsystem, container, false);
        ButterKnife.bind(this, mRoot);
        initSolar();
        return mRoot;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    /**
     * init solar view
     */
    private void initSolar() {
        View root = this.mRoot;
        if (root != null) {
            root.post(new Runnable() {
                @Override
                public void run() {

                    if (mRlShowInfo == null) return;
                    int width = mRlShowInfo.getWidth();
                    float rlShowInfoX = mRlShowInfo.getX();

                    int height = mFlUserInfoIconContainer.getHeight();
                    float y1 = mFlUserInfoIconContainer.getY();

                    float x = mFlUserInfoIconContainer.getX();
                    float y = mFlUserInfoIconContainer.getY();
                    int portraitWidth = mFlUserInfoIconContainer.getWidth();

                    mPx = x + +rlShowInfoX + (width >> 1);
                    mPy = y1 + y + (height - y) / 2;
                    mMaxRadius = (int) (mSolarSystem.getHeight() - mPy + 250);
                    mR = (portraitWidth >> 1);
                    mPx = mFlUserInfoIconContainer.getLeft()+mFlUserInfoIconContainer.getRight();
                    mPy = mFlUserInfoIconContainer.getTop()+mFlUserInfoIconContainer.getBottom();
                    updateSolar(mPx/2, mPy/2);

                }
            });
        }
    }

    private void updateSolar(float px, float py) {

        Random random = new Random(System.currentTimeMillis());
        int maxRadius = mMaxRadius;
        int r = mR;
        mSolarSystem.clear();
        for (int i = 40, radius = r + i; radius <= maxRadius; i = (int) (i * 1.4), radius += i) {
            SolarSystemView.Planet planet = new SolarSystemView.Planet();
            planet.setClockwise(random.nextInt(10) % 2 == 0);
            planet.setAngleRate((random.nextInt(35) + 1) / 1000.f);
            planet.setRadius(radius);
            mSolarSystem.addPlanets(planet);

        }
        mSolarSystem.setPivotPoint(px, py);
    }

    private boolean fast = true;

    @OnClick(R.id.user_info_icon_container)
    public void fastOrSlow(){
        if(fast){
            mSolarSystem.accelerate();
            Toast.makeText(mContext, "Move fast!", Toast.LENGTH_SHORT).show();
        }else{
            mSolarSystem.decelerate();
            Toast.makeText(mContext, "Move slow!", Toast.LENGTH_SHORT).show();
        }
        fast = !fast;
    }
}
