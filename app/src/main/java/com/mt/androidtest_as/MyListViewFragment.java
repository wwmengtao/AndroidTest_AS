package com.mt.androidtest_as;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.mt.androidtest_as.data.BaseData;
import com.mt.androidtest_as.data.DataBank;
import com.mt.androidtest_as.mylistview.MyLvAdapter;

import java.util.List;

/**
 * Created by Mengtao1 on 2016/12/23.
 */

public class MyListViewFragment extends BaseFragment {
    private Activity mActivity = null;
    private ListView mListView = null;
    private MyLvAdapter mAdapter = null;
    private List<BaseData> mData = null;
    private boolean ifSetAdapter = false;
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
        mAdapter = new MyLvAdapter(this);
        return v;
    }

    @Override
    public void onResume(){
        super.onResume();
        ALog.Log("onResume");
        updateUI();
    }

    @Override
    public void updateUI() {
        mData = DataBank.get(mActivity).getData();
        setBaseData(mData);
        mAdapter.setData(mData);
        if(!ifSetAdapter){
            mListView.setAdapter(mAdapter);
            ifSetAdapter = true;
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateDataConfirm(int dialogDataIndex) {
        int count = (dialogDataIndex+1)*5;
        DataBank.get(mActivity).generateData(count);
        updateUI();
    }

    @Override
    public void onClearAllDataConfirm() {
        DataBank.get(mActivity).clearDataBase();
        updateUI();
    }

    private Dialog mDialog = null;
    private int position = -1;
    @Override
    public void onClick(View v) {
        position = (int)v.getTag(MyLvAdapter.ConvertViewTagID);
        if(null == mDialog) {
            mDialog = new AlertDialog.Builder(mActivity)
                    .setTitle(mActivity.getString(R.string.delete_item))
                    .setNegativeButton(android.R.string.cancel, null)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            DataBank.get(mActivity).delData(mData.get(position));
                            updateUI();
                        }
                    })
                    .create();
        }
        mDialog.show();
    }
}
