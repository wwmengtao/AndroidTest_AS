package com.mt.myapplication.photogallery.adapter_holder;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mt.androidtest_as.R;
import com.mt.androidtest_as.alog.ALog;
import com.mt.myapplication.photogallery.tools.ImageDownloader;
import com.mt.myapplication.photogallery.data.PhotoInfo;

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
    private Activity mActivity;
    private List<PhotoInfo> mPhotoInfos;
    private SparseArrayCompat<Integer> dataAddRecorder;
    private int dataLoadIndex = 0;
    //
    private Set<ViewHolder> mHashSet;//统计系统中ViewHolder的个数

    public BaseAdapter(final Activity mActivity) {
        this.mActivity = mActivity;
        mPhotoInfos = new ArrayList<>();
        dataAddRecorder = new SparseArrayCompat<>();
        mHashSet = new HashSet<>();
        ImageDownloader.getImageLoader(mActivity).setImageLoadListener(new ImageDownloader.ImageDownloadListener<ViewHolder>(){
            @Override
            public void onImageDownloaded(ViewHolder target, Bitmap bitmap){
                Drawable drawable = new BitmapDrawable(mActivity.getResources(), bitmap);
                target.bindDrawable(drawable);
            }
        });
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
        final ViewHolder mViewHolder = new ViewHolder(mView);
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewHolder.showDataOrPic();
            }
        });
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder photoHolder, int position) {
        PhotoInfo galleryItem = mPhotoInfos.get(position);
        photoHolder.bindPhotoInfo(galleryItem);
        //方法1：
        ImageDownloader.getImageLoader(mActivity).queueToDownLoad(photoHolder, galleryItem.getUrl());
        //方法2：Picasso
//        Picasso.with(mActivity)
//                .load(galleryItem.getUrl())
//                .into((ImageView) photoHolder.getSubView(R.id.list_item_photogallery_image));
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