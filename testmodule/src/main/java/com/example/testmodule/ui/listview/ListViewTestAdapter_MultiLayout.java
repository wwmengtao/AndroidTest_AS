package com.example.testmodule.ui.listview;

import java.util.ArrayList;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.testmodule.R;


public class ListViewTestAdapter_MultiLayout extends BaseAdapter{
	Context mContext;
	LinearLayout linearLayout = null;
	LayoutInflater inflater;
	TextView tex;
	private static final int VIEW_TYPE_COUNT = 3;
	private static final int TYPE_1 = 0;
	private static final int TYPE_2 = 1;
	private static final int TYPE_3 = 2;
	/**
	 * TypesVSLayouts：用于存储getItemViewType返回值和对应的布局ID
	 */
	private SparseArray<Integer> TypesVSLayouts = null;
	ArrayList<String> listString = new ArrayList<String>();
	public ListViewTestAdapter_MultiLayout(Context context) {
		// TODO Auto-generated constructor stub
		mContext = context.getApplicationContext();
		inflater = LayoutInflater.from(mContext);
		for(int i = 0 ; i < 100 ; i++){
			listString.add(Integer.toString(i)+"  ++++");
		}
		TypesVSLayouts = new SparseArray<>();
		TypesVSLayouts.put(TYPE_1, R.layout.item_getview_test_1);
		TypesVSLayouts.put(TYPE_2, R.layout.item_getview_test_2);
		TypesVSLayouts.put(TYPE_3, R.layout.item_getview_test_3);
	}

	@Override
	public int getCount() {//决定了ListView有多少个item
		// TODO Auto-generated method stub
		return listString.size();
	}

	//每个convert view都会调用此方法，获得当前所需要的view样式
	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		int p = position%6;
		if(p == 0){
			return TYPE_1;
		}else if(p < 3){
			return TYPE_2;
		}
		return TYPE_3;
	}

	@Override
	public int getViewTypeCount() {//返回ListView显示的item的布局种类
		// TODO Auto-generated method stub
		return VIEW_TYPE_COUNT;
	}

	@Override
	public Object getItem(int arg0) {//不会自动调用，需要用户自己调用
		// TODO Auto-generated method stub
		return listString.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	private ViewHolder mViewHolder = null;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		boolean needDoAdditionalWork = (null==convertView)?true:false;
		int viewType = getItemViewType(position);
		mViewHolder = ViewHolder.get(mContext, convertView, parent, TypesVSLayouts.get(viewType), position);
		if(needDoAdditionalWork)doAdditionalWork();
		if(!needDoAdditionalWork){
			CheckBox checkBox = mViewHolder.getView(R.id.checkbox);
			if(null!=checkBox)checkBox.setChecked(true);//对应TYPE_1
		}
		TextView textView = null;
		textView = mViewHolder.getView(R.id.textview);
		textView.setText(""+Integer.toString(position));
		return mViewHolder.getConvertView();
	}

	public void doAdditionalWork(){
		View mConvertView = mViewHolder.getConvertView();
		mConvertView.setBackgroundColor(mContext.getResources().getColor(R.color.white));
		CheckBox checkBox = mViewHolder.getView(R.id.checkbox);
		if(null!=checkBox)checkBox.setChecked(false);//对应TYPE_1
		ImageView imageView = mViewHolder.getView(R.id.imageview);
		if(null!=imageView)imageView.setBackgroundResource(R.drawable.icon);	//对应TYPE_3
	}
}