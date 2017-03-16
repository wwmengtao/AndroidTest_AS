package com.mt.myapplication.photogallery;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mt.androidtest_as.R;
import com.mt.androidtest_as.alog.ALog;
import com.mt.myapplication.photogallery.adapter_holder.BaseAdapter;
import com.mt.myapplication.photogallery.adapter_holder.MultiTypeAdapter;
import com.mt.myapplication.photogallery.data.DataManager;
import com.mt.myapplication.photogallery.data.PhotoInfo;
import com.mt.myapplication.photogallery.tools.ImageDownloader;

import java.util.List;

import static com.mt.myapplication.photogallery.PollService.INTENT_SERVICE_TAG;

public class PhotoGalleryFragment extends VisibleFragment {
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
        setHasOptionsMenu(true);
        mActivity = getActivity();
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

    public void onResume(){
        super.onResume();
        Intent i = mActivity.getIntent();
        String str_Intent = null;
        //以下if语句说明此时的onResume是由于点击通知栏上的通知导致的
        if(null != i && null != (str_Intent = i.getStringExtra(INTENT_SERVICE_TAG))){
            if(str_Intent.equals(INTENT_SERVICE_TAG)){
                ALog.Log("PhotoGalleryFragment_onResume: "+str_Intent);
                fetchItems();
                /**
                 * 下列清除INTENT_SERVICE_TAG对应数值原因是，点击通知开启Fragment后，如果此时用户点击Home键然后
                 * 通过“最近打开项目”重新进入此Fragment的话，可能造成重复执行此部分代码。因此清除INTENT_SERVICE_TAG
                 * 对应数值以保证某一个通知点击进入该Fragment后仅仅执行一次此部分代码。
                 */
                i.removeExtra(INTENT_SERVICE_TAG);
            }
        }
    }

    /**
     * initViews：为各种类型的View添加标记，用于标记各个类型View的数量
     */
    private void initViews(){
        mMultiTypeAdapter.addEmptyViews(0);
        mMultiTypeAdapter.addFooterViews(0);
    }

    /**
     * AsyncTask<para1,para2,para3>中三个参数依次对应doInBackground、onProgressUpdate以及onPostExecute的传入参数类型
     */

    private class FetchItemsTask extends AsyncTask<Void,Integer,List<PhotoInfo>> {
        @Override
        protected List<PhotoInfo> doInBackground(Void... params) {
            List<PhotoInfo> data = DataManager.getDataManager(mActivity).getData();
            if(null == data){
                getHandler().post(ShowDataLoadingStopRunnable);
                return data;
            }
            mMultiTypeAdapter.addNewData(data);
            return data;
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

    private View.OnClickListener MyOnclickListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            fetchItems();
        }
    };

    private void fetchItems(){
        getHandler().removeCallbacks(FetchItemsRunnable);
        getHandler().postDelayed(FetchItemsRunnable, 1500);
    }

    private Runnable FetchItemsRunnable = new Runnable() {
        @Override
        public void run() {
            mFetchItemsTask = new FetchItemsTask();
            mFetchItemsTask.execute();
        }
    };

    @Override
    public void onDetach(){
        ImageDownloader.getImageLoader(mActivity).stopWorking();
        if(null !=mFetchItemsTask && AsyncTask.Status.FINISHED != mFetchItemsTask.getStatus()){
            mFetchItemsTask.cancel(true);
        }
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        DataManager.getDataManager(mActivity).clear();
        super.onDestroy();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater);
        menuInflater.inflate(R.menu.fragment_photo_gallery, menu);
        MenuItem toggleItem = menu.findItem(R.id.menu_item_toggle_polling);
        if (PollService.isServiceAlarmOn(mActivity)) {
            toggleItem.setTitle(R.string.poll_running);
        } else {
            toggleItem.setTitle(R.string.poll_stopped);
        }
        MenuItem canItem = menu.findItem(R.id.menu_item_set_cancel);
        canItem.setTitle(getResultCanceled() ? R.string.result_cancel : R.string.result_ok);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_toggle_polling:
                boolean shouldStartAlarm = !PollService.isServiceAlarmOn(mActivity);
                ALog.Log("shouldStartAlarm: "+shouldStartAlarm);
                PollService.setServiceAlarm(mActivity, shouldStartAlarm);
                mActivity.invalidateOptionsMenu();
                return true;
            case R.id.menu_item_set_cancel:
                setResultCanceled(!getResultCanceled());
                mActivity.invalidateOptionsMenu();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void setResultCanceled(boolean cancel) {//覆写VisibleFragment中的方法
        mResultCanceled = cancel;
    }

    @Override
    protected boolean getResultCanceled(){//覆写VisibleFragment中的方法
        return mResultCanceled;
    }
}
