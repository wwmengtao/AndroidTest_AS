package com.mt.myapplication.criminalintent;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.mt.androidtest_as.R;
import com.mt.androidtest_as.alog.ALog;
import com.mt.androidtest_as.alog.ALogFragment;
import com.mt.myapplication.criminalintent.crimebasedata.Crime;
import com.mt.myapplication.criminalintent.crimebasedata.CrimeLab;
import com.mt.myapplication.criminalintent.mydefinedaboutrecyclerview.MyAdapter;
import com.mt.myapplication.criminalintent.mydefinedaboutrecyclerview.MyRecyclerView;

import java.util.List;

/**
 * Created by Mengtao1 on 2016/12/8.
 */

public class CrimeListFragment extends ALogFragment implements View.OnClickListener{
    private Activity mActivity = null;
    public static final int REQUEST_CODE = 0x10;
    private MyRecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private List<Crime> mData = null;
    private int itemClickPosition = -1;
    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";
    private static final String SAVED_ITEM_POSITION = "item_position";
    private boolean mSubtitleVisible;
    private TextView emptyView = null;
    private Callbacks mCallbacks = null;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
        mCallbacks = (Callbacks) mActivity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        /**
         * 如果托管Activity因为内存吃紧或者转屏被销毁然后重建的话，如果在onSaveInstanceState保存数据，那么此时
         * savedInstanceState将不为null，从而可以做数据恢复工作，当然也可以在onRestoreInstanceState中恢复数据，
         * 只不过onRestoreInstanceState的调用是在托管Activity确实被系统销毁了的情况下。onRestoreInstanceState和
         * 此处都可以做数据恢复工作
         */
        if (savedInstanceState != null) {
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
            itemClickPosition = savedInstanceState.getInt(SAVED_ITEM_POSITION);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        View v = inflater.inflate(R.layout.activity_crime_list, container, false);
        mRecyclerView = (MyRecyclerView)v.findViewById(R.id.crime_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        emptyView = (TextView)v.findViewById(R.id.empty_view);
        emptyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAnItem();
            }
        });
        mRecyclerView.setEmptyView(emptyView);
        mAdapter = new MyAdapter(this);
        mAdapter.setParentActivity(mActivity);
        mRecyclerView.setAdapter(mAdapter);
        return v;
    }

    @Override
    public void onResume(){
        super.onResume();
        updateUI();
    }

    public void updateUI(){
        mData = CrimeLab.get(mActivity).getCrimes();
        mAdapter.setCrimes(mData);
        mAdapter.notifyDataSetChanged();//全部数据更新
        //
        updateSubtitle();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
        outState.putInt(SAVED_ITEM_POSITION,itemClickPosition);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list, menu);

        MenuItem subtitleItem = menu.findItem(R.id.menu_item_show_subtitle);
        if (mSubtitleVisible) {
            subtitleItem.setTitle(R.string.hide_subtitle);
        } else {
            subtitleItem.setTitle(R.string.show_subtitle);
        }

        boolean isFragmentContainerDetailedExisted = CrimeListActivity.isFragmentContainerDetailedExisted();
        if(!isFragmentContainerDetailedExisted)menu.removeItem(R.id.menu_item_delete_crime2);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        List<Crime> mCrimes = null;
        Dialog mDialog = null;
        switch (item.getItemId()) {
            case R.id.menu_item_new_crime:
                addAnItem();
                break;
            case R.id.menu_item_show_subtitle:
                mSubtitleVisible = !mSubtitleVisible;
                mActivity.invalidateOptionsMenu();
                updateSubtitle();
                break;
            case R.id.menu_item_delete_all:
                mDialog = new AlertDialog.Builder(mActivity)
                        .setTitle(getString(R.string.delete_all_items)+" "+getString(R.string.are_you_sure))
                        .setNegativeButton(android.R.string.cancel,null)
                        .setPositiveButton(android.R.string.ok,new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                CrimeLab.get(mActivity).delAllCrimes();
                                updateUI();
                                mCallbacks.onItemSelected(null);
                                setitemClickPosition(-1);
                            }
                        })
                        .create();
                mCrimes = CrimeLab.get(mActivity).getCrimes();
                if(null != mCrimes && mCrimes.size()>0)mDialog.show();
                break;
            case R.id.menu_item_delete_crime2:
                if(itemClickPosition < 0)return true;
                CrimeLab.get(getActivity().getApplicationContext()).delCrime(mData.get(itemClickPosition));
                updateUI();
                mCallbacks.onItemSelected(getNextDataAfterDel());
                break;
            case R.id.menu_item_add_demo_crimes:
                mDialog = new AlertDialog.Builder(mActivity)
                        .setTitle(getString(R.string.add_demo_items)+" "+getString(R.string.are_you_sure))
                        .setNegativeButton(android.R.string.cancel,null)
                        .setPositiveButton(android.R.string.ok,new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                CrimeLab.get(mActivity).generateDemoCrimes();
                                updateUI();
                                mCallbacks.onItemSelected(mData.get(0));
                                setitemClickPosition(0);
                            }
                        })
                        .create();
                mCrimes = CrimeLab.get(mActivity).getCrimes();
                if(null == mCrimes || 0 == mCrimes.size())mDialog.show();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    /**
     * 当删除一个item之后，此时马上获取下一个item并使其为选中状态
     * @return
     */
    private Crime getNextDataAfterDel(){
        Crime mCrime = null;
        if (mData.size() == itemClickPosition) {
            if(itemClickPosition > 0)mCrime = mData.get(itemClickPosition-1);
            itemClickPosition--;
        } else {
            if(itemClickPosition >= 0)mCrime = mData.get(itemClickPosition);
        }
        return mCrime;
    }

    /**
     * 当没有数据的时候提示用户创建一条信息
     */
    private void addAnItem(){
        Crime crime = new Crime();
        CrimeLab.get(mActivity).addCrime(crime);
        setitemClickPosition(mData.size());
        mCallbacks.onItemSelected(crime);
        updateUI();
    }

    private void updateSubtitle() {
        CrimeLab crimeLab = CrimeLab.get(mActivity);
        int crimeCount = crimeLab.getCrimes().size();
        String subtitle = mActivity.getResources()
                .getQuantityString(R.plurals.subtitle_plurals,crimeCount ,crimeCount);
        if (!mSubtitleVisible) {
            subtitle = null;
        }

        AppCompatActivity activity = (AppCompatActivity) mActivity;
        activity.getSupportActionBar().setSubtitle(subtitle);
    }

    @Override
    public void onClick(View v) {
        setitemClickPosition(mRecyclerView.getChildAdapterPosition(v));
        mCallbacks.onItemSelected(mData.get(itemClickPosition));
        mAdapter.notifyDataSetChanged();
    }

    public interface Callbacks{
        void onItemSelected(Crime mCrime);
    }

    public int getCurrentClickedItemPosition(){
        return itemClickPosition;
    }

    /**
     * setitemClickPosition:设置CrimeListFragment当前用户选择的item位置
     * @param position
     */
    public void setitemClickPosition(int position){
        itemClickPosition = position;
        mAdapter.notifyDataSetChanged();
    }

    /**
     * resetCurrentClickedItemPosition：重置CrimeListFragment当前用户选择的item位置
     */
    public void resetCurrentClickedItemPosition(){
        setitemClickPosition(-1);
        mAdapter.notifyDataSetChanged();
    }

    public RecyclerView getRecyclerView(){
        return mRecyclerView;
    }
}
