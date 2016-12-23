package com.mt.androidtest_as;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.mt.androidtest_as.data.BaseData;
import com.mt.androidtest_as.data.DataBank;
import com.mt.androidtest_as.myrecyclerview.MyRvAdapter;

import java.util.List;

/**
 * Created by Mengtao1 on 2016/12/22.
 */

public class MyRecyclerViewFragment extends Fragment {
    private Activity mActivity = null;
    private RecyclerView mRecyclerView = null;
    private MyRvAdapter mBaseAdapter = null;
    private List<BaseData> mData = null;
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
        mBaseAdapter = new MyRvAdapter(mActivity);
        mRecyclerView.setAdapter(mBaseAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL));
        mData = DataBank.get(mActivity).getData();
        return v;
    }

    @Override
    public void onResume(){
        super.onResume();
        updateUI();
    }

    private void updateUI(){
        mData = DataBank.get(mActivity).getData();
        mBaseAdapter.setData(mData);
        mBaseAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_base, menu);
//        MenuItem subtitleItem = menu.findItem(R.id.menu_item_show_subtitle);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_clear_all_data:
                clearAllData();
                break;
            case R.id.menu_create_some_data:
                createData();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
    private int initialDataNumber = 0;
    private void createData(){
        if(null == mData || 0 == mData.size()){
            String[] arrayNum = getResources().getStringArray(R.array.data_number);

            Dialog mDialog = new AlertDialog.Builder(mActivity)
                    .setTitle(getString(R.string.add_some_data))
                    .setNegativeButton(android.R.string.cancel,null)
                    .setSingleChoiceItems(arrayNum, 0, new android.content.DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int which) {
                                initialDataNumber = (which+1)*5;
                            }
                        })
                    .setPositiveButton(android.R.string.ok,new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                initialDataNumber = (0 == initialDataNumber)? 5:initialDataNumber;
                                DataBank.get(mActivity).generateData(initialDataNumber);
                                updateUI();
                            }
                        })
                    .create();
            mDialog.show();
        }
    }

    private void clearAllData(){
        Dialog mDialog = new AlertDialog.Builder(mActivity)
                .setTitle(getString(R.string.clear_all_data)+" "+getString(R.string.are_you_sure))
                .setNegativeButton(android.R.string.cancel,null)
                .setPositiveButton(android.R.string.ok,new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DataBank.get(mActivity).clearDataBase();
                        updateUI();
                    }
                })
                .create();
        List<BaseData> mCrimes = DataBank.get(mActivity).getData();
        if(null!=mCrimes && mCrimes.size()>0)mDialog.show();
    }
}
