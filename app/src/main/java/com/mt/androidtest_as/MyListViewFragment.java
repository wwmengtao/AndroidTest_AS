package com.mt.androidtest_as;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.mt.androidtest_as.data.BaseData;
import com.mt.androidtest_as.data.DataBank;
import com.mt.androidtest_as.data.FLBank;
import com.mt.androidtest_as.mylistview.MyListViewAdapter;
import com.mt.androidtest_as.myrecyclerview.FLAdapter;

import java.util.List;

/**
 * Created by Mengtao1 on 2016/12/23.
 */

public class MyListViewFragment extends BaseFragment {
    private Activity mActivity = null;
    private ListView mListView = null;
    private MyListViewAdapter mAdapter = null;
    private List<BaseData> mData = null;
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_mylistview, container, false);
        mListView = (ListView)v.findViewById(R.id.my_listview);
        mAdapter = new MyListViewAdapter(mActivity);
        mData = DataBank.get(mActivity).getData();
        mAdapter.setData(mData);
        mListView.setAdapter(mAdapter);
        return v;
    }

    @Override
    public void onAfterCreateDataConfirm(int initialDataNumber) {
        int count = (0 == initialDataNumber)? 5:initialDataNumber;
        DataBank.get(mActivity).generateData(count);
        updateUI();
    }

    @Override
    public boolean ifCreateDataDialogShow() {
        return (null == mData || 0 == mData.size());
    }

    @Override
    public void onClearAllDataConfirm() {
        DataBank.get(mActivity).clearDataBase();
        updateUI();
    }

    @Override
    public boolean ifClearAllDataDialogShow() {
        return (null != mData && mData.size()>0);
    }

    @Override
    public void updateUI() {
        mData = DataBank.get(mActivity).getData();
        mAdapter.setData(mData);
        mAdapter.notifyDataSetChanged();
    }
}
