package com.mt.myapplication.photogallery;

import android.app.Activity;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mt.androidtest_as.MyRVMultiTypeFragment;
import com.mt.androidtest_as.R;
import com.mt.androidtest_as.alog.ALog;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by Mengtao1 on 2017/2/16.
 */

/**
 * BaseAdapter：显示基础数据的Adapter，不包含空视图，HeaderView以及FooterView
 */
public class BaseAdapter extends RecyclerView.Adapter<ViewHolder> {

    private List<PhotoInfo> mPhotoInfos;
    private SparseArrayCompat<Integer> dataAddRecorder;
    private int dataLoadIndex = 0;
    //
    private Set<ViewHolder> mHashSet;//统计系统中ViewHolder的个数

    public BaseAdapter() {
        mPhotoInfos = new ArrayList<>();
        dataAddRecorder = new SparseArrayCompat<>();
        mHashSet = new HashSet<>();
    }

    public List<PhotoInfo> getData(){
        return mPhotoInfos;
    }

    public void addNewData(List<PhotoInfo> newData){
        mPhotoInfos.addAll(newData);
        dataAddRecorder.put(dataLoadIndex, newData.size());
        dataLoadIndex++;
    }

    /**
     * getDataAddRecorder：获取数据加载记录，比如{第N次加载M条数据，第N+1次加载M+1条数据...}
     * @return
     */
    public SparseArrayCompat<Integer> getDataAddRecorder(){
        return dataAddRecorder;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View mView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_photo_gallery, viewGroup, false);
        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(ViewHolder photoHolder, int position) {
        PhotoInfo galleryItem = mPhotoInfos.get(position);
        photoHolder.bindPhotoInfo(galleryItem);
        //
        mHashSet.add(photoHolder);
        if((mPhotoInfos.size()-1) == position)showHashSetValues();
    }

    @Override
    public int getItemCount() {
        return mPhotoInfos.size();
    }

    public void showHashSetValues(){
        ALog.Log("/--------------showHashSetValues--------------/");
        ALog.Log("mHashSet.size: "+mHashSet.size());
        Iterator it = mHashSet.iterator();
        while (it.hasNext()){
            ALog.Log("HashValue: "+it.next());
        }
        ALog.Log("/--------------showHashSetValues--------------/");
    }
}