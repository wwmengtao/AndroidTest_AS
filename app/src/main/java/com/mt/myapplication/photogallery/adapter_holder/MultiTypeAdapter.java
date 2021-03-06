package com.mt.myapplication.photogallery.adapter_holder;

import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mt.androidtest_as.R;
import com.mt.myapplication.photogallery.data.PhotoInfo;

import java.util.List;

/**
 * MultiTypeAdapter：适配显示多种类型视图Item
 * Created by Mengtao1 on 2017/2/16.
 */

public class MultiTypeAdapter extends RecyclerView.Adapter<ViewHolder>{
    private RecyclerView mRecyclerView = null;
    private BaseAdapter mInnerAdapter = null;
    private static final int BASE_EMPTY_VIEW_TYPE  = 0x000001;
    private static final int BASE_HEADER_VIEW_TYPE = 0x100100;
    private static final int BASE_ITEM_VIEW_TYPE   = 0xF00000;
    private static final int BASE_FOOTER_VIEW_TYPE = 0x101000;
    private SparseArray<Integer> mEmptyViews  = new SparseArray<>();
    private SparseArray<Integer> mHeaderViews = new SparseArray<>();
    private SparseArray<Integer> mFooterViews = new SparseArray<>();
    private View.OnClickListener mLoadMoreListener = null;

    public MultiTypeAdapter(BaseAdapter mInnerAdapter, RecyclerView mRecyclerView){
        this.mInnerAdapter = mInnerAdapter;
        this.mRecyclerView = mRecyclerView;
    }

    public void setOnLoadMoreListener(View.OnClickListener mLoadMoreListener){
        this.mLoadMoreListener = mLoadMoreListener;
    }

    private View mFootView = null;

    public View getFootView(){
        return mFootView;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder mBaseViewHolder = null;
        View mView = null;
        if(null != mEmptyViews.get(viewType)){
            mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_empty_view, mRecyclerView, false);
            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showEmptyViewDataLoading(v);
                    mLoadMoreListener.onClick(v);
                }
            });
        }else if(null != mHeaderViews.get(viewType)){
            mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_header_view, parent, false);
        }else if(null != mFooterViews.get(viewType)){
            mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_footer_view, parent, false);
            TextView tv = (TextView) mView.findViewById(R.id.footer_view);
            tv.setText("LoadMoreData");
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDataLoadProgress(true);
                    mLoadMoreListener.onClick(v);
                }
            });
            mFootView = mView;
        }
        if(null != mView){
            mBaseViewHolder = new ViewHolder(mView);
        }else{
            mBaseViewHolder = mInnerAdapter.onCreateViewHolder(parent, viewType);
        }
        return mBaseViewHolder;
    }

    private void showEmptyViewDataLoading(View v){
        LinearLayout ll = (LinearLayout)v.findViewById(R.id.empty_view_loading);
        TextView tv = (TextView)v.findViewById(R.id.empty_view);
        ll.setVisibility(View.VISIBLE);
        tv.setVisibility(View.INVISIBLE);
        tv.setTextColor(mRecyclerView.getContext().getResources().getColor(R.color.white));
        tv.setBackgroundColor(mRecyclerView.getContext().getResources().getColor(R.color.red));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        boolean isEmpty = BASE_EMPTY_VIEW_TYPE == holder.getItemViewType();
        if(isEmpty)return;
        if(-1 != isHeaderViewPos(position) && !isEmpty){
            decorateHeaderView(holder);
            return;
        }
        if(needLoadMore(position)){
            showDataLoadProgress(false);
            return;
        }
        if(BASE_ITEM_VIEW_TYPE == holder.getItemViewType()){
            mInnerAdapter.onBindViewHolder(holder, position);
        }
    }

    public void showDataLoadProgress(boolean show){
        if(null == mFootView)return;
        LinearLayout ll = (LinearLayout)mFootView.findViewById(R.id.progress_data_loading);
        TextView tv = (TextView)mFootView.findViewById(R.id.footer_view);
        ll.setVisibility(show? View.VISIBLE:View.INVISIBLE);
        tv.setVisibility((!show)? View.VISIBLE:View.INVISIBLE);
    }

    private void decorateHeaderView(ViewHolder holder){
        TextView tv = (TextView) holder.getSubView(R.id.header_view);
        int pageIndex = getPageIndex(holder.getLayoutPosition());
        if(-1 == pageIndex)return;
        SparseArrayCompat<Integer> dataAddRecorder = mInnerAdapter.getDataAddRecorder();
        int dataNum = dataAddRecorder.get(pageIndex) - 1;//因为在PhotoGalleryFragment中，每次加载数据都有data.add(0,null)占位数据存在，因此减1
        tv.setText("Pager: "+ (pageIndex+1) +" DataNum:"+dataNum);
    }

    /**
     * getPageIndex：获取加载数据次数序号
     * @param positon
     * @return
     */
    private int getPageIndex(int positon){
        for(int i=0;i<mHeaderViews.size();i++){
            if(mHeaderViews.get(mHeaderViews.keyAt(i)) == positon){
                return i;
            }
        }
        return -1;
    }

    /**
     * needLoadMore:判断position位置是否应该显示FooterView
     * @param position
     * @return
     */
    private boolean needLoadMore(int position){
        if(getRealItemCount() + getFooterViewCount()-1 == position){
            return true;
        }
        return false;
    }

    @Override
    public int getItemCount() {
        if (null == mInnerAdapter.getData() || mInnerAdapter.getData().size() == 0) {
            return 1;
        }
        return getRealItemCount() + getFooterViewCount();
    }

    @Override
    public int getItemViewType(int position) {
        //在这里进行判断，如果我们的集合的长度为0时，我们就使用emptyView的布局
        if (null == mInnerAdapter.getData() || mInnerAdapter.getData().size() == 0) {
            return mEmptyViews.keyAt(0);
        }
        int headerViewsKey = -1;
        if(-1 != (headerViewsKey=isHeaderViewPos(position))){
            return headerViewsKey;
        }else if (isFooterViewPos(position)){
            return mFooterViews.keyAt(position - getRealItemCount());
        }
        //如果有数据，则使用ITEM的布局
        return BASE_ITEM_VIEW_TYPE;
    }

    public int isHeaderViewPos(int position){
        if(0 == mHeaderViews.size())return -1;
        for(int i=0;i<mHeaderViews.size();i++){
            if(position == mHeaderViews.get(mHeaderViews.keyAt(i))){
                return mHeaderViews.keyAt(i);
            }
        }
        return -1;
    }

    public boolean isFooterViewPos(int position){
        return position == getRealItemCount();
    }

    public void addEmptyViews(Integer integer){
        mEmptyViews.put(mEmptyViews.size() + BASE_EMPTY_VIEW_TYPE, integer);
    }

    /**
     *addHeaderViews:标识当前添加了一个HeaderView
     * @param position:记录当前HeaderView的添加位置
     */
    public void addHeaderViews(Integer position){
        mHeaderViews.put(getHeaderViewCount() + BASE_HEADER_VIEW_TYPE, position);
    }

    public int getHeaderViewCount(){
        return mHeaderViews.size();
    }

    public void addFooterViews(Integer integer){
        mFooterViews.put(getFooterViewCount() + BASE_FOOTER_VIEW_TYPE, integer);
    }

    public int getFooterViewCount(){
        return mFooterViews.size();
    }

    /**
     * getRealItemCount:获取除去FooterView外所有显示的Item数量，包括HeaderView
     * @return
     */
    public int getRealItemCount(){
        return mInnerAdapter.getData().size();
    }

    public void addNewData(List<PhotoInfo> data){
        addHeaderViews(mInnerAdapter.getData().size());//每加载一次数据就新增一个HeaderView
        mInnerAdapter.addNewData(data);
    }

    @Override
    public void onViewAttachedToWindow(ViewHolder holder){
        //为瀑布流StaggeredGridLayoutManager的Item设置跨列Item
        if (-1 != isHeaderViewPos(holder.getLayoutPosition()) || isFooterViewPos(holder.getLayoutPosition())){
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams){
                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
                p.setFullSpan(true);
            }
        }
    }
}
