package com.mt.androidtest_as;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mt.androidtest_as.data.FLBank;
import com.mt.androidtest_as.myrecyclerview.FLAdapter;

import java.util.List;

/**
 * Created by Mengtao1 on 2016/12/22.
 */

public class FunctionListFragment extends Fragment {
    private Activity mActivity = null;
    private RecyclerView mRecyclerView = null;
    private FLAdapter mAdapter = null;
    private List<String> mData = null;
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
        View v = inflater.inflate(R.layout.fragment_function_list, container, false);
        mRecyclerView = (RecyclerView)v.findViewById(R.id.function_list_rv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL));
        mAdapter = new FLAdapter(mActivity);
        mData = FLBank.get(mActivity).getData();
        mAdapter.setData(mData);
        mRecyclerView.setAdapter(mAdapter);
        return v;
    }

}
