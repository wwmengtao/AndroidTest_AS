package com.mt.myapplication.photogallery.adapter_holder;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mt.androidtest_as.R;
import com.mt.myapplication.photogallery.data.PhotoInfo;

/**
 * Created by Mengtao1 on 2017/2/16.
 */

public class ViewHolder extends RecyclerView.ViewHolder {
    private View mView;
    private SparseArray<View> mViews = null;

    public ViewHolder(View itemView) {
        super(itemView);
        mViews = new SparseArray<>();
        this.mView = itemView;
    }

    public View getSubView(int id){
        View mSubView = mViews.get(id);
        if(null == mSubView){
            mSubView = mView.findViewById(id);
            mViews.put(id, mSubView);
        }
        return mSubView;
    }

    public void bindPhotoInfo(PhotoInfo item) {
        if(null == item)return;
        setText((TextView)getSubView(R.id.tv_title), item.getTitle(), "Title");
        setText((TextView)getSubView(R.id.tv_id),    item.getId(), "Id");
        setText((TextView)getSubView(R.id.tv_url),   item.getUrl(), "Url");
        setText((TextView)getSubView(R.id.tv_wh),    item.getWidth()+"*"+item.getHeight(), "Wid*Hei");
    }

    public void setText(TextView tv, String str, String index){
        if(null != str){
            tv.setText(index+":\n"+str);
        }else{
            tv.setText(index+":N/A");
        }
    }

    public void bindDrawable(Drawable drawable){
        ((ImageView)getSubView(R.id.list_item_photogallery_image)).setImageDrawable(drawable);
        showPic();
    }

    public void showDataOrPic(){
        LinearLayout ll = (LinearLayout)getSubView(R.id.list_item_photogallery_datainfo);
        ImageView iv = (ImageView)getSubView(R.id.list_item_photogallery_image);
        int VisiblityOfView = ll.getVisibility();
        if(View.VISIBLE == VisiblityOfView){
            ll.setVisibility(View.INVISIBLE);
            iv.setVisibility(View.VISIBLE);
        }else{
            ll.setVisibility(View.VISIBLE);
            iv.setVisibility(View.INVISIBLE);
        }
    }

    private void showPic(){
        LinearLayout ll = (LinearLayout)getSubView(R.id.list_item_photogallery_datainfo);
        ImageView iv = (ImageView)getSubView(R.id.list_item_photogallery_image);
        ll.setVisibility(View.INVISIBLE);
        iv.setVisibility(View.VISIBLE);
    }
}