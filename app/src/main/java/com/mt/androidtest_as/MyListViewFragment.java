package com.mt.androidtest_as;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.mt.androidtest_as.data.BaseData;
import com.mt.androidtest_as.data.DataBank;
import com.mt.androidtest_as.mylistview.MyLvAdapter;

import java.util.List;

/**
 * Created by Mengtao1 on 2016/12/23.
 */

public class MyListViewFragment extends BaseFragment implements AdapterView.OnItemClickListener{
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
        mListView.setOnItemClickListener(this);
        mAdapter = new MyLvAdapter(this);
        setListViewEmptyText(mListView);
        return v;
    }

    @Override
    public void onResume(){
        super.onResume();
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

    /**
     * setListViewEmptyText:数据为空时提示用户增加数据
     */
    private void setListViewEmptyText(ListView mListView){
        View emptyView = LayoutInflater.from(mListView.getContext()).inflate(R.layout.item_empty_view, (ViewGroup) mListView.getParent(), false);
        if(null == emptyView)return;
        emptyView.setOnClickListener(this);
        ((ViewGroup)mListView.getParent()).addView(emptyView);
        mListView.setEmptyView(emptyView);
    }

    private void setListViewEmptyText2(ListView mList){
        TextView emptyView = new TextView(mList.getContext());
        emptyView.setOnClickListener(this);
        emptyView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        emptyView.setText(R.string.no_data_tips);
        emptyView.setTextSize(20);
        emptyView.setGravity(android.view.Gravity.CENTER);
        emptyView.setVisibility(View.GONE);
        ((ViewGroup)mList.getParent()).addView(emptyView);
        mList.setEmptyView(emptyView);
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


    @Override
    public void onClick(View v) {
        onCreateDataConfirm(0);
    }

    private Dialog mDialog = null;
    private int position_m = -1;
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        position_m = position;
        if(null == mDialog) {
            mDialog = new AlertDialog.Builder(mActivity)
                    .setTitle(mActivity.getString(R.string.delete_item))
                    .setNegativeButton(android.R.string.cancel, null)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            DataBank.get(mActivity).delData(mData.get(position_m));
                            updateUI();
                        }
                    })
                    .create();
        }
        mDialog.show();
    }
}
