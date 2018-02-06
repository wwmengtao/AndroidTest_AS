package com.example.protoui.swipemenu;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.protoui.ALog;
import com.example.protoui.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SwipeMenuActivity extends AppCompatActivity{
    private Unbinder mUnbinder = null;
    private Context mContext = null;

    //
    String googleAccountName = null;
    Uri googleAccountPicUri = null;

    @BindView(R.id.main_swipemenu) SwipeMenu mMainSwipemenu;
    @BindView(R.id.user_info_iv) ImageView mImageView;
    @BindView(R.id.user_info_name) TextView mTextView;
    @BindView(R.id.user_info_iv2) ImageView mImageView2;
    @BindView(R.id.user_info_name2) TextView mTextView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_menu);
        mContext = getApplicationContext();
        mUnbinder = ButterKnife.bind(this);
        initFragment();
    }

    @OnClick({R.id.user_info_iv, R.id.user_info_iv2})
    public void onUserInfoIVClicked(){
        ALog.Log("onUserInfoIVClicked");
    }

    @OnClick(R.id.main_btn_menu)
    public void onMainBtnClicked(){
        if (mMainSwipemenu.isMenuShowing()) {
            mMainSwipemenu.hideMenu();
        } else {
            mMainSwipemenu.showMenu();
        }
    }

    private void initFragment(){
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if(fragment == null){
            fragment = getFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }

    private Fragment getFragment(){
        return SwipeMenuFragment.newInstance("param1", "param2");
    }



    @Override
    public void onDestroy(){
        super.onDestroy();
        mUnbinder.unbind();
    }
}
