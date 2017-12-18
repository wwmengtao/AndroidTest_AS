package com.example.protoui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.protoui.travelmode.LocationUtils;
import com.example.protoui.travelmode.RouteInfoFetcher;
import com.example.protoui.travelmode.SuggestFactoriesTask;
import com.example.protoui.travelmode.recyclerviewinfo.SAppAdapter;
import com.example.protoui.travelmode.recyclerviewinfo.SAppInfo;
import com.example.protoui.travelmode.route.RouteInfo;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.observers.DisposableObserver;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SuggestFactoriesTask.OnGetListRouteInfoListener {
    private static final String TAG = "MainActivity ";
    private Context mContext = null;
    private LocationUtils mLocationUtils = null;
    private RecyclerView mRecyclerView = null;
    private SAppAdapter mSAppAdapter = null;
    private RouteInfoFetcher mRouteInfoFetcher = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.travel_mode);
        mContext = getApplicationContext();
        mLocationUtils = LocationUtils.getInstance(mContext);
        mRouteInfoFetcher = RouteInfoFetcher.get(this.getApplicationContext());
        initRecyclerView();
    }

    private void initRecyclerView(){
        mRecyclerView = findViewById(R.id.suggestedappsrv);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(manager);
        DividerItemDecoration decoration = new DividerItemDecoration(mContext, DividerItemDecoration.HORIZONTAL);
        decoration.setDrawable(mContext.getResources().getDrawable(R.drawable.suggestdivider));
        mRecyclerView.addItemDecoration(decoration);
        mSAppAdapter = new SAppAdapter(this, this);
        mRecyclerView.setAdapter(mSAppAdapter);
        loadData();
    }

    private void loadData(){
//        SuggestFactoriesTask mTask = new SuggestFactoriesTask(this);
//        mTask.setOnGetListRouteInfoListener(this);
//        mTask.execute(1);
        mRouteInfoFetcher.setObserver(getObserver());
        mRouteInfoFetcher.start();
    }

    @Override
    public void onGetListRouteInfo(List<RouteInfo> result) {
        List<SAppInfo> mData = new ArrayList<>();
        for(RouteInfo ri : result){
            mData.add(SAppInfo.convert(ri));
        }
        mSAppAdapter.setData(mData);
        mSAppAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStop(){
        mRouteInfoFetcher.stop();
        super.onStop();
    }

    @Override
    public void onClick(View view) {
        Intent intent = (Intent)view.getTag();
        if(intent != null){
            mContext.startActivity(intent);
        }
    }

    private DisposableObserver<List<RouteInfo>> getObserver() {
        return new DisposableObserver<List<RouteInfo>>() {

            @Override
            public void onNext(List<RouteInfo> value) {
                ALog.Log3(TAG + "onNext: "+value.size()+" "+Thread.currentThread().toString());
                onGetListRouteInfo(value);
            }

            @Override
            public void onError(Throwable e) {
                ALog.Log(TAG + e.fillInStackTrace().toString());
            }

            @Override
            public void onComplete() {
                ALog.Log(TAG + "onComplete");
            }
        };
    }
}
