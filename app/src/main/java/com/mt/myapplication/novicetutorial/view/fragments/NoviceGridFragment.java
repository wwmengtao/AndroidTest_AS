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

import com.mt.androidtest_as.R;
import com.mt.myapplication.novicetutorial.com.fernandocejas.android10.sample.presentation.di.components.UserComponent;
import com.mt.myapplication.novicetutorial.com.fernandocejas.android10.sample.presentation.model.UserModelNT;
import com.mt.myapplication.novicetutorial.presenter.NoviceGridPresenter;
import com.mt.myapplication.novicetutorial.view.adapter.UsersAdapterGrid;
import com.mt.myapplication.novicetutorial.view.interfaces.LoadDataView;
import com.mt.myapplication.novicetutorial.view.interfaces.NoviceRecyclerView;

import java.util.Collection;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class NoviceGridFragment extends BaseFragment implements NoviceRecyclerView, LoadDataView {
    public Unbinder unbinder;
    Activity mActivity = null;
    @Inject Context mContext;
    @BindView(R.id.novice_grid_recyclerview) RecyclerView mRecyclerView;
    @Inject UsersAdapterGrid mUsersAdapterGrid;
    @Inject NoviceGridPresenter mNoviceGridPresenter;
    private NoviceListFragment.OnUserClickedListener mOnUserClickedListener;
    private Intent mIntent = null;

    @Override public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity)context;
        if (context instanceof NoviceListFragment.OnUserClickedListener) {
            this.mOnUserClickedListener = (NoviceListFragment.OnUserClickedListener) context;
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
        this.onItemClickListener = null;
        this.mOnUserClickedListener = null;
    }
    private UsersAdapterGrid.OnItemClickListener onItemClickListener =
            new UsersAdapterGrid.OnItemClickListener() {
                @Override public void onUserAdapterItemClicked(UserModelNT userModel) {
                    if (NoviceGridFragment.this.mNoviceGridPresenter != null && userModel != null) {
                        NoviceGridFragment.this.mNoviceGridPresenter.onUserClicked(userModel);
                    }
                }
            };

    private void setupRecyclerView() {
        mUsersAdapterGrid.setOnItemClickListener(onItemClickListener);
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        mRecyclerView.setAdapter(mUsersAdapterGrid);
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
            this.mUsersAdapterGrid.setUsersCollection(userModelCollection);
        }
    }

    @Override
    public void viewUser(UserModelNT userModel) {
        if (this.mOnUserClickedListener != null) {
            this.mOnUserClickedListener.onUserClicked(userModel);
        }
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
}
