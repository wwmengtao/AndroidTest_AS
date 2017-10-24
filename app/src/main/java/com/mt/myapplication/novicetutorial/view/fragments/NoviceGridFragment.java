package com.mt.myapplication.novicetutorial.view.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mt.androidtest_as.R;
import com.mt.myapplication.novicetutorial.com.fernandocejas.android10.sample.presentation.di.components.UserComponent;
import com.mt.myapplication.novicetutorial.com.fernandocejas.android10.sample.presentation.model.UserModel;
import com.mt.myapplication.novicetutorial.presenter.NoviceGridPresenter;
import com.mt.myapplication.novicetutorial.view.adapter.UsersAdapter;
import com.mt.myapplication.novicetutorial.view.interfaces.LoadDataView;
import com.mt.myapplication.novicetutorial.view.interfaces.NoviceRecyclerView;

import java.util.Collection;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NoviceGridFragment extends BaseFragment implements NoviceRecyclerView, LoadDataView {
    Activity mActivity = null;
    @Inject Context mContext;
    @Bind(R.id.novice_grid_recyclerview) RecyclerView mRecyclerView;
    @Inject UsersAdapter usersAdapter;
    @Inject NoviceGridPresenter mNoviceGridPresenter;
    private NoviceListFragment.UserListListener userListListener;

    @Override public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
        if (activity instanceof NoviceListFragment.UserListListener) {
            this.userListListener = (NoviceListFragment.UserListListener) activity;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getComponent(UserComponent.class).inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_novice_grid, container, false);
        ButterKnife.bind(this, v);
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
        ButterKnife.unbind(this);
    }

    @Override public void onDestroy() {
        super.onDestroy();
        this.mNoviceGridPresenter.destroy();
    }

    @Override public void onDetach() {
        super.onDetach();
        this.onItemClickListener = null;
        this.userListListener = null;
    }
    private UsersAdapter.OnItemClickListener onItemClickListener =
            new UsersAdapter.OnItemClickListener() {
                @Override public void onUserAdapterItemClicked(UserModel userModel) {
                    if (NoviceGridFragment.this.mNoviceGridPresenter != null && userModel != null) {
                        NoviceGridFragment.this.mNoviceGridPresenter.onUserClicked(userModel);
                    }
                }
            };

    private void setupRecyclerView() {
        usersAdapter.setOnItemClickListener(onItemClickListener);
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        mRecyclerView.setAdapter(usersAdapter);
    }

    @Override
    public void setUserList(Collection<UserModel> userModelCollection) {

    }

    @Override
    public void showUser(UserModel userModel) {
        if (this.userListListener != null) {
            this.userListListener.onUserClicked(userModel);
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
