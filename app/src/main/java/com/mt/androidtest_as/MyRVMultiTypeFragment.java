package com.mt.androidtest_as;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mt.androidtest_as.alog.ALog;
import com.mt.androidtest_as.alog.ALogFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * MyRVMultiTypeFragment：用于显示多种布局，包括EmptyView、HeaderView以及FooterView。
 * Created by Mengtao1 on 2017/2/15.
 */

public class MyRVMultiTypeFragment extends ALogFragment {
    private Activity mActivity = null;
    private RecyclerView mRecyclerView = null;
    private BaseAdapter mBaseAdapter = null;
    private MultiTypeAdapter mMultiTypeAdapter = null;
    private Handler mHandler = null;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        setHasOptionsMenu(true);
        mActivity = activity;
        mHandler = getHandler();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_myrecyclerview, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.my_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mBaseAdapter = new BaseAdapter();
        mMultiTypeAdapter = new MultiTypeAdapter(mBaseAdapter);
        mRecyclerView.setAdapter(mMultiTypeAdapter);
        return v;
    }

    private class BaseAdapter extends RecyclerView.Adapter<BaseViewHolder> {
        public List<String> mData = new ArrayList<>();

        public BaseAdapter() {

        }

        @Override
        public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View mView = LayoutInflater.from(mActivity).inflate(R.layout.list_item, parent, false);
            return new BaseViewHolder(mView,viewType);
        }


        @Override
        public void onBindViewHolder(BaseViewHolder holder, int position) {
            ((TextView) holder.getSubView(R.id.tv)).setText(mData.get(position));
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }
    }

    private class BaseViewHolder extends RecyclerView.ViewHolder {
        private View mView = null;
        private View mSubView = null;
        private int viewType = -1;
        private int position = -1;

        public BaseViewHolder(View itemView,int viewType) {
            super(itemView);
            mView = itemView;
            this.viewType = viewType;
        }
        public View getSubView(int id){
            if(null == mSubView){
                mSubView = mView.findViewById(id);
            }
            return mSubView;
        }
        public void setPosition(int position){
            this.position = position;
        }

        public int getPostion(){
            return position;
        }
        public void setViewType(int viewType){
            this.viewType = viewType;
        }
        public int getViewType(){
            return viewType;
        }
    }

    private class MultiTypeAdapter extends RecyclerView.Adapter<BaseViewHolder>{
        private BaseAdapter mInnerAdapter = null;
        private static final int BASE_EMPTY_VIEW_TYPE  = 0x000001;
        private static final int BASE_HEADER_VIEW_TYPE = 0x100100;
        private static final int BASE_ITEM_VIEW_TYPE   = 0xF00000;
        private static final int BASE_FOOTER_VIEW_TYPE = 0x101000;
        private SparseArray<Integer> mEmptyViews  = new SparseArray<>();
        private SparseArray<Integer> mHeaderViews = new SparseArray<>();
        private SparseArray<Integer> mFooterViews = new SparseArray<>();

        public MultiTypeAdapter(BaseAdapter mInnerAdapter){
            this.mInnerAdapter = mInnerAdapter;
            initViews();
        }

        /**
         * initViews：为各种类型的View添加标记
         */
        private void initViews(){
            addEmptyViews(0x0001);
            addHeaderViews(0x1000);
            addHeaderViews(0x1000);
            addFooterViews(0x2000);
            addFooterViews(0x2000);
        }

        @Override
        public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            BaseViewHolder mBaseViewHolder = null;
            View mView = null;
            if(null != mEmptyViews.get(viewType)){
                mView = LayoutInflater.from(mActivity).inflate(R.layout.item_empty_view, mRecyclerView, false);
                mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                for (int i = 0; i < 10; i++) {
                                    mInnerAdapter.mData.add("Item: " + i);
                                }
                                notifyDataSetChanged();
                            }
                        });
                    }
                });
            }else if(null != mHeaderViews.get(viewType)){
                mView = LayoutInflater.from(mActivity).inflate(R.layout.item_header_view, parent, false);
            }else if(null != mFooterViews.get(viewType)){
                mView = LayoutInflater.from(mActivity).inflate(R.layout.item_footer_view, parent, false);
            }
            if(null != mView){
                mBaseViewHolder = new BaseViewHolder(mView, viewType);
            }else{
                mBaseViewHolder = mInnerAdapter.onCreateViewHolder(parent, viewType);
            }
            mBaseViewHolder.setViewType(viewType);
            return mBaseViewHolder;
        }

        @Override
        public void onBindViewHolder(BaseViewHolder holder, int position) {
            boolean notEmpty = BASE_EMPTY_VIEW_TYPE != holder.getViewType();
            if(notEmpty && !mMenu.hasVisibleItems()){
                mMenu.setGroupVisible(0,true);//如果当前RecyclerView中有数据的话就显示菜单栏
            }
            if(isHeaderView(position) && notEmpty){
                holder.setPosition(position);
                decorateHeaderView(holder);
                return;
            }
            if(needLoadMore(position)){
                decorateFooterView(holder);
                return;
            }
            if(BASE_ITEM_VIEW_TYPE == holder.viewType){
                mInnerAdapter.onBindViewHolder(holder, position - getFooterViewCount());
            }
        }

        private boolean isHeaderView(int position){
            return position < getHeaderViewCount();
        }

        private void decorateHeaderView(BaseViewHolder holder){
            TextView tv = (TextView) holder.getSubView(R.id.header_view);
            tv.setText("HeaderView: "+holder.getPostion());
        }

        private boolean needLoadMore(int position){
            if(getHeaderViewCount() + getRealItemCount() + getFooterViewCount()-1 == position){
                return true;
            }
            return false;
        }

        private void decorateFooterView(BaseViewHolder holder){
            TextView tv = (TextView) holder.getSubView(R.id.footer_view);
            tv.setText("LoadMoreData");
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            for(int i=0;i<6;i++){
                                mInnerAdapter.mData.add("Item: " + mInnerAdapter.mData.size());
                            }
                            notifyDataSetChanged();
                        }
                    });
                }
            });
        }

        @Override
        public int getItemCount() {
            if (null == mInnerAdapter.mData || mInnerAdapter.mData.size() == 0) {
                return 1;
            }
            return getHeaderViewCount() + getRealItemCount() + getFooterViewCount();
        }

        @Override
        public int getItemViewType(int position) {
            //在这里进行判断，如果我们的集合的长度为0时，我们就使用emptyView的布局
            if (null == mInnerAdapter.mData || mInnerAdapter.mData.size() == 0) {
                return mEmptyViews.keyAt(0);
            }
            if(isHeaderViewPos(position)){
                return mHeaderViews.keyAt(position);
            }else if (isFooterViewPos(position)){
                return mFooterViews.keyAt(position - getHeaderViewCount() - getRealItemCount());
            }
            //如果有数据，则使用ITEM的布局
            return BASE_ITEM_VIEW_TYPE;
        }
        private boolean isHeaderViewPos(int position){
            return position < getHeaderViewCount();
        }

        private boolean isFooterViewPos(int position){
            return position >= getHeaderViewCount() + getRealItemCount();
        }

        private void addEmptyViews(Integer integer){
            mEmptyViews.put(mEmptyViews.size() + BASE_EMPTY_VIEW_TYPE, integer);
        }

        private void addHeaderViews(Integer integer){
            mHeaderViews.put(getHeaderViewCount() + BASE_HEADER_VIEW_TYPE, integer);
        }
        
        private int getHeaderViewCount(){
            return mHeaderViews.size();
        }

        private void addFooterViews(Integer integer){
            mFooterViews.put(getFooterViewCount() + BASE_FOOTER_VIEW_TYPE, integer);
        }

        private int getFooterViewCount(){
            return mFooterViews.size();
        }

        private int getRealItemCount(){
            return mInnerAdapter.mData.size();
        }
    }

    private Menu mMenu = null;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_recycler_view, menu);
        mMenu = menu;
        mMenu.setGroupVisible(0,false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_linear:
                mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
                break;
            case R.id.action_grid:
                mRecyclerView.setLayoutManager(new GridLayoutManager(mActivity, 2));
                break;
            case R.id.action_staggered:
                mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                break;
        }
        mRecyclerView.setAdapter(mMultiTypeAdapter);
        return super.onOptionsItemSelected(item);
    }
}
