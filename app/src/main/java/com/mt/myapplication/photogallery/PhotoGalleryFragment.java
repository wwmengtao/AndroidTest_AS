package com.mt.myapplication.photogallery;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mt.androidtest_as.R;
import com.mt.androidtest_as.alog.ALog;
import com.mt.androidtest_as.alog.ALogFragment;

import java.io.File;
import java.io.IOException;
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_photo_gallery, container, false);
        mPhotoRecyclerView = (RecyclerView) v.findViewById(R.id.fragment_photo_gallery_recycler_view);
        mPhotoRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.HORIZONTAL));
        mPhotoRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        mBaseAdapter = new BaseAdapter();
        mMultiTypeAdapter = new MultiTypeAdapter(mBaseAdapter, mPhotoRecyclerView);
        mMultiTypeAdapter.setOnLoadMoreListener(MyOnclickListener);
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(getActivity(), 4);
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
            List<PhotoInfo> data = getData();
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
            ALog.Log("progress: "+progress);
        }

        @Override
        protected void onPostExecute(List<PhotoInfo> items) {
            if(null == items)return;
            updateAdapter();
        }

    }

    /**
     * getData:获取数据
     * @return
     */
    private List<PhotoInfo> getData(){
        //方法1：从网络读取Json信息
        //List<PhotoInfo> data = new FlickrFetchr().fetchItems();
        //方法2：从assets文件中读取Json信息
        String assetDir = "Json";
        String fileName = String.format("photosJsonInfo%d.txt", dataLoadCount);
        if(!ifFileExist(assetDir, fileName))return null;
        String assetFileName = assetDir + File.separator + fileName;
        List<PhotoInfo> data = new FlickrFetchr().fetchItems2(getContext(), assetFileName);//直接从assets文件中解析Json信息
        return data;
    }

    private String[] assetFiles = null;
    private String[] getAssetFiles(String assetDir){
        if(null != assetFiles)return assetFiles;
        try {
            assetFiles = mActivity.getAssets().list(assetDir);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return assetFiles;
    }

    private boolean ifFileExist(String assetDir, String assetFileName){
        if(null == assetDir || null == assetFileName)return false;
        if(null == assetFiles)assetFiles=getAssetFiles(assetDir);
        if(null == assetFiles)return false;
        for(String str:assetFiles){
            if(str.equals(assetFileName))return true;
        }
        return false;
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


    public void showDataLoadingStop(){
        View mFootView = mMultiTypeAdapter.getFootView();
        if(null == mFootView)return;
        ALog.Log("showDataLoadingStop");
        LinearLayout ll = (LinearLayout)mFootView.findViewById(R.id.progress_data_loading);
        TextView tv = (TextView)mFootView.findViewById(R.id.footer_view);
        ll.setVisibility(View.INVISIBLE);
        tv.setVisibility(View.VISIBLE);
        tv.setText("Add data loaded!");
        tv.setEnabled(false);
        tv.setBackgroundColor(getResources().getColor(R.color.orangered));
    }

    private Runnable MyRunnable = new Runnable() {
        @Override
        public void run() {
            mFetchItemsTask = new FetchItemsTask();
            mFetchItemsTask.execute();
        }
    };

    @Override
    public void onDestroy() {
        if(null !=mFetchItemsTask && AsyncTask.Status.FINISHED != mFetchItemsTask.getStatus()){
            mFetchItemsTask.cancel(true);
        }
        super.onDestroy();
    }
}
