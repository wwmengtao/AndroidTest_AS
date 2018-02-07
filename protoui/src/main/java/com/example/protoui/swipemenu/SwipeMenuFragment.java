package com.example.protoui.swipemenu;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.protoui.ALog;
import com.example.protoui.R;
import com.example.protoui.swipemenu.data.RVData;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by mengtao1 on 2018/2/5.
 */

public class SwipeMenuFragment extends Fragment implements SwipeMenuActivity.OnActivityViewClickListener{
    private Context mContext = null;
    private Unbinder mUnbinder = null;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private List<RVData> mData = null;

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
        this.mData = RVData.getData();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        ((SwipeMenuActivity)getActivity()).setOnActivityViewClickListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_swipe_menu, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityViewClick(String str) {
        ALog.Log("onActivityViewClick: "+str);
        if(str.equals("Moto Sleep")){
            mTVTitle.setText(mContext.getString(R.string.sleep_mode_swipe_menu));
            mTVSummary.setText(mContext.getString(R.string.sleep_mode_des_swipe_menu));
        }else{
            mTVTitle.setText(str);
            mTVSummary.setText(str+" description");
        }
    }

    @Override public void onDestroyView() {
        mUnbinder.unbind();
        super.onDestroyView();
        ALog.Log("onDestroyView");
    }

}
