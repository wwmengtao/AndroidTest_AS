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

import com.mt.androidtest_as.alog.ALogFragment;
import com.mt.androidtest_as.myrecyclerview.RvStaggeredAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mengtao1 on 2017/4/1.
 */

public class RvStaggeredAnimatorFragment extends ALogFragment {
    private Activity mActivity = null;
    private RecyclerView mRecyclerView;
    private List<String> mDatas;
    private RvStaggeredAdapter mStaggeredHomeAdapter;

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
        mStaggeredHomeAdapter = new RvStaggeredAdapter(getContext(), mDatas);
        mRecyclerView.setAdapter(mStaggeredHomeAdapter);
        //
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mActivity, DividerItemDecoration.HORIZONTAL));
        // 设置item动画，更新数据集不是用mStaggeredHomeAdapter的notifyDataSetChanged()方法，而是
        // notifyItemInserted(position)与notifyItemRemoved(position)，否则没有动画效果。
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        return v;
    }

    protected void initData(){
        mDatas = new ArrayList<>();
        for (int i = 'A'; i < 'z'; i++){
            mDatas.add("" + (char) i);
        }
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
