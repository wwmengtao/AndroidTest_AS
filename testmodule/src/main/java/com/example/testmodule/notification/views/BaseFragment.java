package com.example.testmodule.notification.views;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.example.testmodule.application.AppExecutors;
import com.example.testmodule.application.BasicApp;

/**
 * Created by mengtao1 on 2018/1/11.
 */

public class BaseFragment extends Fragment {
    protected AppExecutors mAppExecutors = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAppExecutors = ((BasicApp)getActivity().getApplication()).getAppExecutors();
    }
}
