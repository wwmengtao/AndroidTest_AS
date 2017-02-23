package com.mt.myapplication.photogallery;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.mt.androidtest_as.R;
import com.mt.androidtest_as.alog.ALog;

/**
 * Created by Mengtao1 on 2017/2/16.
 */

public class ViewHolder extends RecyclerView.ViewHolder {
    private View mView;
    private View mSubView = null;
    private TextView tv_title = null;
    private TextView tv_id = null;
    private TextView tv_url = null;
    private TextView tv_wh = null;

    public ViewHolder(View itemView) {
        super(itemView);
        this.mView = itemView;
        tv_title = (TextView)itemView.findViewById(R.id.tv_title);
        tv_id = (TextView)itemView.findViewById(R.id.tv_id);
        tv_url = (TextView)itemView.findViewById(R.id.tv_url);
        tv_wh = (TextView)itemView.findViewById(R.id.tv_wh);
    }

    public View getSubView(int id){
        if(null == mSubView){
            mSubView = mView.findViewById(id);
        }
        return mSubView;
    }

    public void bindPhotoInfo(PhotoInfo item) {
        String str = null;
        setText(tv_title, item.getTitle(), "Title");
        setText(tv_id, item.getId(), "Id");
        setText(tv_url, item.getUrl(), "Url");
        setText(tv_wh, item.getWidth()+"*"+item.getHeight(), "Width*Height");
    }

    public void setText(TextView tv, String str, String index){
        if(null != str){
            tv.setText(index+":\n"+str);
        }else{
            tv.setText(index+":N/A");
        }
    }
}