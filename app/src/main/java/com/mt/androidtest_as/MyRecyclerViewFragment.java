package com.mt.androidtest_as;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.content.res.TypedArray;
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
import com.mt.androidtest_as.myrecyclerview.MyRvViewHolder;

import java.util.List;

import static com.mt.androidtest_as.myrecyclerview.MyRvAdapter.VIEW_TYPE_EMPTY;

/**
 * Created by Mengtao1 on 2016/12/22.
 */

public class MyRecyclerViewFragment extends BaseFragment {
    private Activity mActivity = null;
    private RecyclerView mRecyclerView = null;
    private MyRvAdapter mBaseAdapter = null;
    private List<BaseData> mData = null;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_myrecyclerview, container, false);
        mRecyclerView = (RecyclerView)v.findViewById(R.id.my_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mBaseAdapter = new MyRvAdapter(this);
        mRecyclerView.setAdapter(mBaseAdapter);
        setAccentColorForEmptyView();
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL));
        return v;
    }

    /**
     * setAccentColorForEmptyView:获取styles.xml中的属性颜色值
     */
    private void setAccentColorForEmptyView(){
        Resources.Theme theme = mActivity.getTheme();
        int []colorAccentId = {R.attr.colorAccent};
        TypedArray a = theme.obtainStyledAttributes(R.style.AppTheme, colorAccentId);
        int colorAccent = a.getColor(0, 0);
        a.recycle();
        int []colorMyColorID = {R.attr.custom_color1};
        a = theme.obtainStyledAttributes(R.style.MyColorStyle, colorMyColorID);
        int colorMyColor = a.getColor(0, 0);
        a.recycle();
        mBaseAdapter.setColorForEmptyView(colorAccent, colorMyColor);
    }

    @Override
    public void onResume(){
        super.onResume();
        updateUI();
    }

    @Override
    public void updateUI(){
        mData = DataBank.get(mActivity).getData();
        setBaseData(mData);
        mBaseAdapter.setData(mData);
        mBaseAdapter.notifyDataSetChanged();
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
        MyRvViewHolder holder = (MyRvViewHolder)v.getTag();
        int viewType = holder.viewType;
        if(VIEW_TYPE_EMPTY == viewType){
            int initialDataNumber = 5;
            DataBank.get(mActivity).generateData(initialDataNumber);
            updateUI();
            return;
        }
        position = holder.getAdapterPosition();
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
