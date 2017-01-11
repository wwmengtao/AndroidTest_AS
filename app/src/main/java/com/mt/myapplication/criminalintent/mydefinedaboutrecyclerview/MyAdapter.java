package com.mt.myapplication.criminalintent.mydefinedaboutrecyclerview;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.mt.androidtest_as.R;
import com.mt.myapplication.criminalintent.CrimeListActivity;
import com.mt.myapplication.criminalintent.CrimeListFragment;
import com.mt.myapplication.criminalintent.crimebasedata.Crime;

import java.util.List;

/**
 * Created by Mengtao1 on 2016/12/20.
 */

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder>{
    private List<Crime> mCrimes = null;
    private Context mContext = null;
    private CrimeListActivity parentActivity = null;
    private CrimeListFragment mFragment = null;
    private View.OnClickListener myOnClickListener = null;

    public MyAdapter(CrimeListFragment mFragment){
        this.mFragment = mFragment;
        mContext = mFragment.getActivity();
        myOnClickListener = mFragment;
    }

    public void setParentActivity(Activity activity){
        parentActivity = (CrimeListActivity)activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(null == mCrimes||0 == mCrimes.size()){
            return null;
        }
        View mView = LayoutInflater.from(mContext).inflate(R.layout.list_item_crimelist, parent, false);
        mView.setOnClickListener(myOnClickListener);
        MyViewHolder mMyViewHolder = new MyViewHolder(mView);
        mMyViewHolder.setParentActivity(parentActivity);
        return mMyViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if(null == mCrimes||0 == mCrimes.size()){
            return;
        }
        holder.bindData(mCrimes.get(position), position == mFragment.getCurrentClickedItemPosition());
    }

    @Override
    public int getItemCount() {
        if(null == mCrimes||0 == mCrimes.size()){
            return 0;
        }
        return mCrimes.size();
    }

    public void setCrimes(List<Crime> crimes) {
        mCrimes = crimes;
    }
}
