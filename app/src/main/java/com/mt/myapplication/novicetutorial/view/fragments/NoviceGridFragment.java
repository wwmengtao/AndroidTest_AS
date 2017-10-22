package com.mt.myapplication.novicetutorial.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mt.androidtest_as.alog.ALogFragment;

import com.mt.androidtest_as.R;
import com.mt.myapplication.novicetutorial.model.UserModel;
import com.mt.myapplication.novicetutorial.view.interfaces.NoviceGridView;

import java.util.Collection;

import butterknife.ButterKnife;

public class NoviceGridFragment extends ALogFragment implements NoviceGridView{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_novice_grid, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void getUserList(Collection<UserModel> userModelCollection) {

    }

    @Override
    public void viewUser(UserModel userModel) {

    }
}
