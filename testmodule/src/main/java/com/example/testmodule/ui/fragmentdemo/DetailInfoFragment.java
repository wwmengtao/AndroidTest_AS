package com.example.testmodule.ui.fragmentdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.testmodule.BaseFragment2;
import com.example.testmodule.R;


public class DetailInfoFragment extends BaseFragment2 {
	public static final String ITEM_ID = "item_id";
	InfoContent.Info info;
	
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);
    }
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		if (getArguments().containsKey(ITEM_ID)){
			info = InfoContent.ITEM_MAP.get(getArguments()	.getInt(ITEM_ID)); //��
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		super.onCreateView(inflater, container, savedInstanceState);
		View rootView = inflater.inflate(R.layout.fragment_info_detail,	container, false);
		if (info != null){
			((TextView) rootView.findViewById(R.id.book_title)).setText(info.title);
			((TextView) rootView.findViewById(R.id.book_desc)).setText(info.desc);	
		}
		return rootView;
	}
}
