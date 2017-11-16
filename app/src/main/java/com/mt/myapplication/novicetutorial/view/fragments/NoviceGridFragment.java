package com.mt.myapplication.novicetutorial.view.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.mt.androidtest_as.R;
import com.mt.myapplication.novicetutorial.com.fernandocejas.android10.sample.presentation.di.components.UserComponent;
import com.mt.myapplication.novicetutorial.com.fernandocejas.android10.sample.presentation.model.UserModelNT;
import com.mt.myapplication.novicetutorial.presenter.NoviceGridPresenter;
import com.mt.myapplication.novicetutorial.view.adapter.UserAdapterGrid;
import com.mt.myapplication.novicetutorial.view.interfaces.NoviceRecyclerView;

import java.util.Collection;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class NoviceGridFragment extends BaseFragment implements NoviceRecyclerView{
    public Unbinder unbinder;
    Activity mActivity = null;
    @Inject Context mContext;
    @BindView(R.id.novice_grid_recyclerview) RecyclerView mRecyclerView;
    @BindView(R.id.rl_progress) RelativeLayout rl_progress;
    @BindView(R.id.rl_retry) RelativeLayout rl_retry;
    @BindView(R.id.bt_retry) Button bt_retry;
    @Inject UserAdapterGrid mUserAdapterGrid;
    @Inject NoviceGridPresenter mNoviceGridPresenter;
    private BaseFragment.OnFragmentClickListener mOnFragmentClickListener;
    private Intent mIntent = null;

    @Override public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity)context;
        if (context instanceof BaseFragment.OnFragmentClickListener) {
            this.mOnFragmentClickListener = (BaseFragment.OnFragmentClickListener) context;
        }
        mIntent = mActivity.getIntent();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getComponent(UserComponent.class).inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_novice_recview, container, false);
        unbinder = ButterKnife.bind(this, v);
        setupRecyclerView();
        return v;
    }

    @Override public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.mNoviceGridPresenter.setView(this);
        if (savedInstanceState == null) {
            this.loadUserList();
        }
    }

    /**
     * Loads all users.
     */
    private void loadUserList() {
        this.mNoviceGridPresenter.initialize();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        this.mRecyclerView.setAdapter(null);
        unbinder.unbind();
    }

    @Override public void onDestroy() {
        super.onDestroy();
        this.mNoviceGridPresenter.destroy();
    }

    @Override public void onDetach() {
        super.onDetach();
        this.mOnFragmentClickListener = null;
    }

    /**
     * onUserAdapterItemClicked：内部的Adapter数据有点击事件时产生的在fragment中的回调
     * @param userModel
     */
    @Override public void onUserAdapterItemClicked(UserModelNT userModel) {
        if (NoviceGridFragment.this.mNoviceGridPresenter != null && userModel != null) {
            NoviceGridFragment.this.mNoviceGridPresenter.onUserClicked(userModel);
        }
    }

    @Override
    public void onActivityFinish(){//NoviceGridActivity在finish时的回调

    }

    private void setupRecyclerView() {
        mUserAdapterGrid.setOnItemClickListener(this);
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        mRecyclerView.setAdapter(mUserAdapterGrid);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mActivity, DividerItemDecoration.HORIZONTAL |
                DividerItemDecoration.VERTICAL));
    }

    @Override
    public Intent getViewIntent() {
        return mIntent;
    }

    @Override
    public void setUserList(Collection<UserModelNT> userModelCollection) {
        if (userModelCollection != null) {
            this.mUserAdapterGrid.setUsersCollection(userModelCollection);
        }
    }

    @Override
    public void setUserList(UserModelNT mUserModelNT, Collection<UserModelNT> userModelCollection) {

    }

    @Override
    public void viewUser(UserModelNT userModel) {
        if (this.mOnFragmentClickListener != null) {
            this.mOnFragmentClickListener.onFragmentClicked(userModel);
        }
    }

    @Override
    public void showLoading() {
        this.mRecyclerView.setVisibility(View.INVISIBLE);
        this.rl_progress.setVisibility(View.VISIBLE);
        this.getActivity().setProgressBarIndeterminateVisibility(true);
    }

    @Override
    public void hideLoading() {
        this.rl_progress.setVisibility(View.GONE);
        this.mRecyclerView.setVisibility(View.VISIBLE);
        this.getActivity().setProgressBarIndeterminateVisibility(false);
    }

    @Override public void showRetry() {
        this.rl_retry.setVisibility(View.VISIBLE);
    }

    @Override public void hideRetry() {
        this.rl_retry.setVisibility(View.GONE);
    }

    @Override
    public void showError(String message) {
        this.showToastMessage(message);
    }

    @Override
    public Context context() {
        return mContext;
    }

    @Override
    /**
     * setCurrentItemBackGround：当用户由主界面进入具体条目并且退出后，此时展示具体条目的序号变换效果
     */
    public void setCurrentItemBackGround(int currentSubLevelIndex){
        this.mUserAdapterGrid.setCurrentSubLevelIndex(currentSubLevelIndex);//
        this.mUserAdapterGrid.notifyCertainSubLevelDataChanged();
    }
}
