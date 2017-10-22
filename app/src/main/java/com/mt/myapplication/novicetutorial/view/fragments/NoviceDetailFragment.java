package com.mt.myapplication.novicetutorial.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mt.androidtest_as.R;
import com.mt.androidtest_as.alog.ALogFragment;
import com.mt.myapplication.novicetutorial.model.UserModel;
import com.mt.myapplication.novicetutorial.view.interfaces.NoviceDetailView;

import butterknife.ButterKnife;

public class NoviceDetailFragment extends ALogFragment implements NoviceDetailView{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_novice_detail, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
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
        return null;
    }

    @Override
    public void renderUser(UserModel user) {

    }
}
