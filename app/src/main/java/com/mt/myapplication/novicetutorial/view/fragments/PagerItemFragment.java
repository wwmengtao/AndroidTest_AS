package com.mt.myapplication.novicetutorial.view.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mt.androidtest_as.R;
import com.mt.androidtest_as.alog.ALog;
import com.mt.myapplication.novicetutorial.com.fernandocejas.android10.sample.presentation.di.components.UserComponent;
import com.mt.myapplication.novicetutorial.com.fernandocejas.android10.sample.presentation.model.UserModelNT;
import com.mt.myapplication.novicetutorial.view.interfaces.NoviceDetailView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class PagerItemFragment extends BaseFragment implements NoviceDetailView{
    public static final String NOVICE_PAGER_ITEM_FRAGMENT_KEY = "PagerItemFragment_UserModel_KEY";
    private UserModelNT mUserModelNT = null;
    private Unbinder mUnbinder;
    private Activity mActivity = null;
    @Inject Context mContext;
    @BindView(R.id.novice_detail_tv) TextView mTextView;
    @BindView(R.id.novice_detail_img) ImageView mImageView;

    public static Fragment newFragment(UserModelNT user){
        ALog.Log1("PagerItemFragment_newFragment: "+user.getKey());
        PagerItemFragment mPagerItemFragment = new PagerItemFragment();
        Bundle mBundle = new Bundle();
        mBundle.putParcelable(NOVICE_PAGER_ITEM_FRAGMENT_KEY, user);
        mPagerItemFragment.setArguments(mBundle);
        return mPagerItemFragment;
    }

    @Override public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
        mUserModelNT = getArguments().getParcelable(NOVICE_PAGER_ITEM_FRAGMENT_KEY);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getComponent(UserComponent.class).inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_novice_detail, container, false);
        mUnbinder = ButterKnife.bind(this, v);
        return v;
    }

    @Override public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState == null) {
            if(null != mUserModelNT)this.loadUserList(mUserModelNT);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    /**
     * Loads all users.
     */
    private void loadUserList(UserModelNT userModel) {
        showUser(userModel);
    }

    @Override
    public void showUser(UserModelNT user) {
        ALog.Log1("PagerItemFragment_showUser: "+user.getKey());
        String content = user.getAdjunction();
        String picName = user.getPic();
        if(null != content){
            mTextView.setText(getString(content));
            mTextView.setVisibility(View.VISIBLE);
        }
        if(null != picName){
            Glide.with(mContext)
                    .load(getPicID(picName))//加载res/drawblexxx文件夹中的图片资源
                    .into(mImageView);
            mImageView.setVisibility(View.VISIBLE);
        }
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showRetry() {

    }

    @Override
    public void hideRetry() {

    }

    @Override
    public void showError(String message) {

    }

    @Override
    public Context context() {
        return mContext;
    }

    @Override
    public Intent getViewIntent() {
        return null;
    }

    @Override
    public void onActivityFinish() {//NoviceViewPagerActivity在finish时的回调

    }
}
