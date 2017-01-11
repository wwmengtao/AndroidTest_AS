package com.mt.myapplication.criminalintent.mydefinedaboutrecyclerview;

import android.app.Activity;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.mt.androidtest_as.R;
import com.mt.androidtest_as.alog.ALog;
import com.mt.myapplication.criminalintent.CrimeListActivity;
import com.mt.myapplication.criminalintent.crimebasedata.Crime;
import com.mt.myapplication.criminalintent.crimebasedata.CrimeLab;

/**
 * Created by Mengtao1 on 2016/12/20.
 */


public class MyViewHolder extends RecyclerView.ViewHolder{
    public View mView = null;
    public CheckBox mCheckBox = null;
    public TextView tvTitle = null;
    public TextView tvDate = null;
    private CrimeListActivity parentActivity = null;

    public MyViewHolder(View view){
        super(view);
        ALog.Log("MyViewHolder()");
        mView = view;
        mCheckBox = (CheckBox)view.findViewById(R.id.list_item_crime_solved_check_box);
        tvTitle = (TextView)view.findViewById(R.id.list_item_crime_title_text_view);
        tvDate = (TextView)view.findViewById(R.id.list_item_crime_date_text_view);

    }

    public void bindData(final Crime mCrime){
        ALog.Log("MyViewHolder_bindData");
        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setSolved(isChecked);
                CrimeLab.get(mView.getContext()).updateCrime(mCrime);
                /**
                 * 以下应用于平板项目，此时点击左侧CrimeListFragment的item上的CheckBox时，更新右侧CrimeFragment相应
                 * 的CheckBox状态
                 */
                if(CrimeListActivity.isFragmentContainerDetailedExisted() && null!= parentActivity) {
                    Message msg = Message.obtain();
                    msg.what = CrimeListActivity.MSG_UPDATE_DETAIELD_CRIME_FRAGMENT;
                    msg.obj = mCrime;
                    parentActivity.getHandler().sendMessage(msg);
                }
            }
        });
        mCheckBox.setChecked(mCrime.isReSolved());//此句必须写在setOnCheckedChangeListener之后，否则会出现状态错乱
        tvTitle.setText(mCrime.getTitle());
        tvDate.setText(mCrime.getDate().toString());
    }

    public void bindData(final Crime mCrime, boolean itemSelected){
        bindData(mCrime);
        mView.setBackgroundColor(mView.getResources().getColor(itemSelected ? android.R.color.holo_green_light
            : android.R.color.transparent));
    }

    public void setParentActivity(Activity activity){
        parentActivity = (CrimeListActivity)activity;
    }
}