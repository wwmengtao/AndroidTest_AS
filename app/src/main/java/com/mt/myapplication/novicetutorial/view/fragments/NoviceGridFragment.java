package com.mt.myapplication.novicetutorial.view.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mt.androidtest_as.alog.ALogFragment;

import com.mt.androidtest_as.R;
import com.mt.androidtest_as.alog.AndroidTest_AS_Application;
import com.mt.myapplication.novicetutorial.model.UserModel;
import com.mt.myapplication.novicetutorial.view.adapter.UsersAdapter;
import com.mt.myapplication.novicetutorial.view.interfaces.NoviceGridView;

import java.util.Collection;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NoviceGridFragment extends ALogFragment implements NoviceGridView{
    Activity mActivity = null;
    @Bind(R.id.novice_grid_recyclerview) RecyclerView mRecyclerView;
    @Inject UsersAdapter usersAdapter;
    @Inject Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        if(null == mActivity)return;
        ((AndroidTest_AS_Application)mActivity.getApplication()).getAppComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_novice_grid, container, false);
        ButterKnife.bind(this, v);
        setupRecyclerView();
        return v;
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        mRecyclerView.setAdapter(null);
        ButterKnife.unbind(this);
    }

    private void setupRecyclerView() {
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        mRecyclerView.setAdapter(usersAdapter);
    }

    @Override
    public void getUserList(Collection<UserModel> userModelCollection) {

    }

    @Override
    public void viewUser(UserModel userModel) {

    }
}
