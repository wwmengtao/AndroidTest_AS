package com.mt.androidtest_as;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mt.androidtest_as.alog.ALog;
import com.example.androidcommon.alog.ALogFragment;
import com.mt.androidtest_as.mylistview.MyLvViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mengtao1 on 2016/12/23.
 */

public class MyListViewTestFragment extends ALogFragment implements AdapterView.OnItemClickListener{
    private Activity mActivity = null;
    private ListView mListView = null;
    private MyAdapter mAdapter = null;
    private List<String> mData = null;
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_mylistview, container, false);
        mListView = (ListView)v.findViewById(R.id.my_listview);
        mAdapter = new MyAdapter();
        mAdapter.generateData(10);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
        return v;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ALog.Log("position: "+position);
    }

    public class MyAdapter extends BaseAdapter{
        private List<String> mData = null;

        public MyAdapter(){
            mData = new ArrayList<>();
        }

        public void generateData(int count){
            if(count<=0)return;
            for(int i=0;i<count;i++){
                mData.add("item: "+i);
            }
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Object getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MyLvViewHolder holder = MyLvViewHolder.get(mActivity,convertView,parent,R.layout.list_item,position);
            TextView tv = holder.getView(R.id.tv);
            tv.setText(mData.get(position));
            View cv = holder.getConvertView();
            /**
             * 如果cv注册View.OnClickListener监听器，那么状态选中以及AdapterView.OnItemClickListener都将失效
             */
//            cv.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                }
//            });
            return cv;
        }
    }
}
