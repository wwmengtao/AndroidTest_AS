package com.mt.myapplication.novicetutorial.view.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.mt.myapplication.novicetutorial.presenter.NoviceDetailPresenter;
import com.mt.myapplication.novicetutorial.view.interfaces.NoviceDetailView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.mt.myapplication.novicetutorial.view.activities.NoviceDetailActivity.NOVICE_DETAIL_ACTIVITY_KEY;


public class NoviceDetailFragment extends BaseFragment implements NoviceDetailView{
    private Unbinder mUnbinder;
    private Activity mActivity = null;
    private Intent mIntent = null;
    @Inject Context mContext;
    @BindView(R.id.novice_detail_tv) TextView mTextView;
    @BindView(R.id.novice_detail_img) ImageView mImageView;

    @Inject NoviceDetailPresenter mNoviceDetailPresenter;

    @Override public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
        mIntent = mActivity.getIntent();
        UserModelNT userModel = (UserModelNT)mIntent.getParcelableExtra(NOVICE_DETAIL_ACTIVITY_KEY);
//        ALog.Log("NoviceDetailFragment_onAttach: "+userModel.toString());
        mActivity.setTitle(getString(userModel.getKey()));
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
        this.mNoviceDetailPresenter.setView(this);
        if (savedInstanceState == null) {
            UserModelNT userModel = (UserModelNT)mActivity.getIntent().getParcelableExtra(NOVICE_DETAIL_ACTIVITY_KEY);
            if(null != userModel)this.loadUserList(userModel);
        }
    }

    /**
     * Loads all users.
     */
    private void loadUserList(UserModelNT userModel) {
        this.mNoviceDetailPresenter.initialize(userModel);
    }

    @Override
    public void showUser(UserModelNT user) {
        ALog.Log("NoviceDetailFragment_showUser: "+user.toString());
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
        return mIntent;
    }
}
