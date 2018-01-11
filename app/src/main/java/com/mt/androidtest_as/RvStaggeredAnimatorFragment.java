package com.mt.androidtest_as;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.mt.androidtest_as.alog.ALog;
import com.example.androidcommon.alog.ALogFragment;
import com.mt.androidtest_as.myrecyclerview.RvStaggeredAnimatorAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mengtao1 on 2017/4/1.
 */

public class RvStaggeredAnimatorFragment extends ALogFragment {
    private Activity mActivity = null;
    private RecyclerView mRecyclerView;
    private List<String> mDatas;
    private RvStaggeredAnimatorAdapter mStaggeredHomeAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.activity_staggeredanimator_recyclerview, container, false);
        initData();
        mRecyclerView = (RecyclerView) v.findViewById(R.id.id_recyclerview_staggered);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3,
                StaggeredGridLayoutManager.VERTICAL));
        mStaggeredHomeAdapter = new RvStaggeredAnimatorAdapter(getContext(), mDatas);
        mRecyclerView.setAdapter(mStaggeredHomeAdapter);
        mStaggeredHomeAdapter.setRecyclerView(mRecyclerView);
        //
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mActivity, DividerItemDecoration.HORIZONTAL));
        //
        DefaultItemAnimator mDefaultItemAnimator = new DefaultItemAnimator();
        mDefaultItemAnimator.setAddDuration(100);//设置增加item渐变时间
        mDefaultItemAnimator.setRemoveDuration(100);//设置删除item渐变时间
        mDefaultItemAnimator.setChangeDuration(100);//设置item变化渐变时间
        mDefaultItemAnimator.setMoveDuration(100);//设置item移动渐变时间
        mRecyclerView.setItemAnimator(mDefaultItemAnimator);
        initAnimatorFinished();
        return v;
    }

    protected void initData(){
        mDatas = new ArrayList<>();
        for (int i = 0; i < 100; i++){
            mDatas.add("No." + i);
        }
    }

    private void initAnimatorFinished(){
        RecyclerView.ItemAnimator itemAnimator = mRecyclerView.getItemAnimator();
        boolean isRunning = itemAnimator.isRunning(new RecyclerView.ItemAnimator.ItemAnimatorFinishedListener() {
            @Override
            public void onAnimationsFinished() {
                ALog.Log("onAnimationsFinished");
            }
        });
        ALog.Log("isRunning: "+isRunning);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_staggered_animator, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.id_action_add:
                mStaggeredHomeAdapter.addData(1);
                break;
            case R.id.id_action_delete:
                mStaggeredHomeAdapter.removeData(1);
                break;
        }
        return true;
    }
}
