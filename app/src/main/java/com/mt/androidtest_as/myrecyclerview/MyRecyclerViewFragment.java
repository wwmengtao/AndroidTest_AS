package com.mt.androidtest_as.myrecyclerview;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mt.androidtest_as.ALog;
import com.mt.androidtest_as.R;
import com.mt.androidtest_as.data.DataBank;

/**
 * Created by Mengtao1 on 2016/12/22.
 */

public class MyRecyclerViewFragment extends Fragment {
    private Activity mActivity = null;
    private RecyclerView mRecyclerView = null;
    private BaseAdapter mBaseAdapter = null;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        ALog.Log("CrimeListFragment:onCreate");
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_myrecyclerview, container, false);
        mRecyclerView = (RecyclerView)v.findViewById(R.id.my_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mBaseAdapter = new BaseAdapter(mActivity);
        mBaseAdapter.setData(DataBank.getDataBank(mActivity).getData(10));
        mRecyclerView.setAdapter(mBaseAdapter);
        return v;
    }


}
