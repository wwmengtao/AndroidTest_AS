package com.mt.myapplication.novicetutorial.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mt.androidtest_as.alog.ALogFragment;

import com.mt.androidtest_as.R;

public class NoviceGridFragment extends ALogFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_novice, container, false);
        return v;
    }
}
