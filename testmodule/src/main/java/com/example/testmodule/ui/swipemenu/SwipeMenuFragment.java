package com.example.testmodule.ui.swipemenu;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.testmodule.ALog;
import com.example.testmodule.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by mengtao1 on 2018/2/7.
 */

public class SwipeMenuFragment extends Fragment{
    private Context mContext = null;
    private Unbinder mUnbinder = null;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    @BindView(R.id.title) TextView mTVTitle;
    @BindView(R.id.summary) TextView mTVSummary;
    @BindView(R.id.other) TextView mTVOther;

    public static SwipeMenuFragment newInstance(String param1, String param2) {
        SwipeMenuFragment fragment = new SwipeMenuFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context.getApplicationContext();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_swipe_menu, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override public void onDestroyView() {
        mUnbinder.unbind();
        super.onDestroyView();
        ALog.Log("onDestroyView");
    }

}
