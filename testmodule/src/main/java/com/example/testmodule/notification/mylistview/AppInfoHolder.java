package com.example.testmodule.notification.mylistview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.example.testmodule.R;
import com.example.testmodule.notification.model.AppInfo;


/**
 * Created by Mengtao1 on 2016/12/22.
 */

public class AppInfoHolder extends RecyclerView.ViewHolder {
	private Context mContext = null;
	private View rootView = null;
	private ImageView picIV = null;


	public AppInfoHolder(View itemView) {
		super(itemView);
		mContext = itemView.getContext().getApplicationContext();
		rootView = itemView;
		picIV = (ImageView)itemView.findViewById(R.id.pic);
	}

	public void bindData(AppInfo data){
		picIV.setImageDrawable(data.icon);
	}

	public View getRootView(){
		return this.rootView;
	}
}
