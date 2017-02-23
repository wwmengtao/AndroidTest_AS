package com.mt.myapplication.photogallery;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mt.androidtest_as.R;
import com.mt.androidtest_as.alog.ALog;
import com.mt.androidtest_as.alog.ALogFragment;

import java.util.List;

public class PhotoGalleryFragment extends ALogFragment {
    private static final String TAG = "PhotoGalleryFragment";
    private Activity mActivity = null;
    private RecyclerView mPhotoRecyclerView;
    private BaseAdapter mBaseAdapter = null;
    private MultiTypeAdapter mMultiTypeAdapter = null;
    private FetchItemsTask mFetchItemsTask = null;

    public static PhotoGalleryFragment newInstance() {
        return new PhotoGalleryFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        HandlerThreadImageDownloader.getImageLoader(mActivity).setImageLoadListener(new HandlerThreadImageDownloader.ImageDownloadListener<ViewHolder>(){
            @Override
            public void onImageDownloaded(ViewHolder target, Bitmap bitmap){
                Drawable drawable = new BitmapDrawable(mActivity.getResources(), bitmap);
                target.bindDrawable(drawable);
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_photo_gallery, container, false);
        mPhotoRecyclerView = (RecyclerView) v.findViewById(R.id.fragment_photo_gallery_recycler_view);
        mPhotoRecyclerView.addItemDecoration(new DividerItemDecoration(mActivity, DividerItemDecoration.HORIZONTAL));
        mPhotoRecyclerView.addItemDecoration(new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL));
        mBaseAdapter = new BaseAdapter(mActivity);
        mMultiTypeAdapter = new MultiTypeAdapter(mBaseAdapter, mPhotoRecyclerView);
        mMultiTypeAdapter.setOnLoadMoreListener(MyOnclickListener);
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(mActivity, 3);
        final int spanCount = mGridLayoutManager.getSpanCount();
        mGridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return (-1!=mMultiTypeAdapter.isHeaderViewPos(position) || mMultiTypeAdapter.isFooterViewPos(position)) ? spanCount : 1;
            }
        });
        mPhotoRecyclerView.setLayoutManager(mGridLayoutManager);
        mPhotoRecyclerView.setAdapter(mMultiTypeAdapter);
        initViews();
        return v;
    }

    private int lastHeaderViewIndex = 0;//lastHeaderViewIndex：用于标记上一个HeaderView的位置

    /**
     * initViews：为各种类型的View添加标记，用于标记各个类型View的数量
     */
    private void initViews(){
        mMultiTypeAdapter.addEmptyViews(0);
        mMultiTypeAdapter.addHeaderViews(lastHeaderViewIndex);
        mMultiTypeAdapter.addFooterViews(0x2000);
    }

    /**
     * AsyncTask<para1,para2,para3>中三个参数依次对应doInBackground、onProgressUpdate以及onPostExecute的传入参数类型
     */
    private int dataLoadCount = 1;
    private class FetchItemsTask extends AsyncTask<Void,Integer,List<PhotoInfo>> {
        @Override
        protected List<PhotoInfo> doInBackground(Void... params) {
            List<PhotoInfo> data = AssetsDataManager.getDataManager(mActivity).getData(dataLoadCount);
            if(null == data){
                getHandler().postDelayed(ShowDataLoadingStopRunnable,1000);
                return data;
            }
            data.add(0,null);//为HeaderView提供占位数据
            mBaseAdapter.addNewData(data);
            if(1 != dataLoadCount){
                lastHeaderViewIndex = lastHeaderViewIndex + data.size();
                mMultiTypeAdapter.addHeaderViews(lastHeaderViewIndex);//每加载一次数据就新增一个HeaderView
            }
            dataLoadCount++;
            //

            for(int i=1;i<101;i++){
                try {
                    if (isCancelled()) {//判断如果为true那么说明已经有请求取消当前任务的信号了，既然无法终止线程的运行，但是可以终止运行在线程中一系列操作
                        break;
                    }
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                publishProgress(i);//更新进度导致onProgressUpdate的调用，从而可以获取进度
            }
            return data;
        }

        @Override
        protected void onProgressUpdate(Integer... params){
            int progress = params[0];
//            ALog.Log("progress: "+progress);
        }

        @Override
        protected void onPostExecute(List<PhotoInfo> items) {
            if(null == items)return;
            updateAdapter();
        }

    }


    private void updateAdapter() {
        mMultiTypeAdapter.notifyDataSetChanged();
    }

    private View.OnClickListener MyOnclickListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            getHandler().removeCallbacks(MyRunnable);
            getHandler().postDelayed(MyRunnable,1000);
        }
    };

    private Runnable ShowDataLoadingStopRunnable = new Runnable() {
        @Override
        public void run() {
            showDataLoadingStop();
        }
    };

    /**
     * showDataLoadingStop：数据加载完毕之后的提示
     */
    public void showDataLoadingStop(){
        View mFootView = mMultiTypeAdapter.getFootView();
        if(null == mFootView)return;
        ALog.Log("showDataLoadingStop");
        LinearLayout ll = (LinearLayout)mFootView.findViewById(R.id.progress_data_loading);
        TextView tv = (TextView)mFootView.findViewById(R.id.footer_view);
        ll.setVisibility(View.INVISIBLE);
        tv.setVisibility(View.VISIBLE);
        tv.setText("All data loaded!");
        tv.setEnabled(false);
        tv.setTextColor(getResources().getColor(R.color.white));
        tv.setBackgroundColor(getResources().getColor(R.color.red));
    }

    private Runnable MyRunnable = new Runnable() {
        @Override
        public void run() {
            mFetchItemsTask = new FetchItemsTask();
            mFetchItemsTask.execute();
        }
    };

    @Override
    public void onDetach(){
        HandlerThreadImageDownloader.getImageLoader(mActivity).stopWorking();
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        if(null !=mFetchItemsTask && AsyncTask.Status.FINISHED != mFetchItemsTask.getStatus()){
            mFetchItemsTask.cancel(true);
        }
        super.onDestroy();
    }
}
