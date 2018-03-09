package com.example.testmodule.image;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.example.testmodule.ALog;
import com.example.testmodule.R;
import com.example.testmodule.ui.listview.ViewHolder;
import com.example.testmodule.ui.listview.ViewHolder.ImageViewParas;

import static com.example.testmodule.image.ImageLoader.IsLogRun;

/**
 *
 * @author Mengtao1
 *
 */
public class BitmapAdapter2 extends CommonBaseAdapter<String>{
	private Context mContext = null;

	public BitmapAdapter2(Context context, List<String> mDatas){
		super(context, mDatas);
		mContext = context.getApplicationContext();
	}

	private ViewHolder mViewHolder=null;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		boolean needDoAdditionalWork = (null==convertView)?true:false;
		if(IsLogRun) ALog.Log("position:"+position+" needDoAdditionalWork:"+needDoAdditionalWork);
		mViewHolder = ViewHolder.get(mContext, convertView, parent, R.layout.item_getview_bitmap, position);
		if(needDoAdditionalWork)doAdditionalWork();
		String url = getItem(position);
		mViewHolder.setImageByUrl(R.id.myimageview, url);
		return mViewHolder.getConvertView();
	}

	public void doAdditionalWork(){
		View convertView = mViewHolder.getConvertView();
		//设置GridView的网格高度
		int heightOfGridCell = 2*80;
		AbsListView.LayoutParams params = new AbsListView.LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				heightOfGridCell);
		convertView.setLayoutParams(params);
		ImageView mImageView =mViewHolder.getView(R.id.myimageview);
		mImageView.setScaleType(ScaleType.FIT_XY);//非等比例缩放，铺满整个ImageView
		if(0==ImageViewParas.defaultWidth||0==ImageViewParas.defaultHeight){
			convertView.measure(0, 0);
			//下列数值仅仅作为采样用的宽高参考值(一般不会是真实数值)，一般会小于 displayMetrics.widthPixels、 displayMetrics.heightPixels
			ImageViewParas.defaultWidth = mImageView.getMeasuredWidth();
			ImageViewParas.defaultHeight = mImageView.getMeasuredHeight();
		}
	}
}
