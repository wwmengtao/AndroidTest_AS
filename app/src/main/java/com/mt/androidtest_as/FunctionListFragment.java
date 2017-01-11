package com.mt.androidtest_as;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mt.androidtest_as.alog.ALog;
import com.mt.androidtest_as.alog.ALogFragment;
import com.mt.androidtest_as.data.FLBank;
import com.mt.androidtest_as.myrecyclerview.FLAdapter;
import com.mt.androidtest_as.myrecyclerview.MyRvViewHolder;

import java.util.List;

/**
 * Created by Mengtao1 on 2016/12/22.
 */

public class FunctionListFragment extends ALogFragment implements View.OnClickListener{
    private Activity mActivity = null;
    private RecyclerView mRecyclerView = null;
    private FLAdapter mAdapter = null;
    private List<String> mData = null;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        ALog.Log("FunctionListFragment:onCreate");
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_function_list, container, false);
        mRecyclerView = (RecyclerView)v.findViewById(R.id.function_list_rv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL));
        mAdapter = new FLAdapter(this);
        mAdapter.setOnClickListener(this);
        mData = FLBank.get(mActivity).getData();
        mAdapter.setData(mData);
        mRecyclerView.setAdapter(mAdapter);
        return v;
    }

    @Override
    public void onClick(View v) {
        int position = ((MyRvViewHolder)v.getTag()).getAdapterPosition();
        List<String> componentsName = FLBank.get(mActivity).getData();
        String compName = componentsName.get(position);
        if(compName.toLowerCase().endsWith("fragment") || compName.toLowerCase().endsWith("activity")){
            Intent mIntent = null;
            if(compName.toLowerCase().endsWith("fragment")){
                mIntent = MainFragmentActivity.newIntent(mActivity,position);
            }else{
                String activityName = getActivityName(compName);
                if(null != activityName){
                    mIntent = new Intent();
                    mIntent.setComponent(new ComponentName(mActivity.getPackageName(), activityName));
                }
            }
            mActivity.startActivity(mIntent);
        }
    }
}
