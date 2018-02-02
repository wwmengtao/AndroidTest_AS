package com.example.protoui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.androidcommon.alog.CrashBaseActivity;
import com.example.protoui.travelmode.LocationUtils;
import com.example.protoui.travelmode.RouteInfoFetcher;
import com.example.protoui.travelmode.SuggestFactoriesTask;
import com.example.protoui.travelmode.recyclerviewinfo.SAppAdapter;
import com.example.protoui.travelmode.recyclerviewinfo.SAppInfo;
import com.example.protoui.travelmode.route.RouteInfo;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.observers.DisposableObserver;

public class MainActivity extends CrashBaseActivity implements View.OnClickListener, SuggestFactoriesTask.OnGetListRouteInfoListener {
    private static final String TAG = "MainActivity ";
    private Context mContext = null;
    private LocationUtils mLocationUtils = null;
    private DrawerLayout mDrawerLayout;
    private RecyclerView mRecyclerView = null;
    private SAppAdapter mSAppAdapter = null;
    private RouteInfoFetcher mRouteInfoFetcher = null;
    //
    private ImageView mImageView = null;
    private TextView mTextView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = getApplicationContext();
        mLocationUtils = LocationUtils.getInstance(mContext);
        mRouteInfoFetcher = RouteInfoFetcher.get(this.getApplicationContext());
        initViews();
    }

    private void initViews(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);
        //
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }
        //
        initRecyclerView();
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initRecyclerView(){
        mTextView = (TextView)findViewById(R.id.user_info_name);
        mImageView = (ImageView)findViewById(R.id.user_info_iv);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ALog.Log("onClick");
                mContext.startActivity(SignInActivity.getLaunchIntent(mContext));
            }
        });
        //
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
        mRouteInfoFetcher.init();
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
    public void onResume(){
        mRouteInfoFetcher.resume();
        super.onResume();
        updateGoogleUI();
    }

    @Override
    public void onPause(){
        mRouteInfoFetcher.pause();
        super.onPause();
    }

    @Override
    public void onDestroy(){
        mRouteInfoFetcher.destroy();
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        Intent intent = (Intent)view.getTag();
        if(intent != null){
            mContext.startActivity(intent);
        }
    }

    private long preTime = -1, nowTime = -1;
    private DisposableObserver<List<RouteInfo>> getObserver() {
        return new DisposableObserver<List<RouteInfo>>() {

            @Override
            public void onNext(List<RouteInfo> value) {
                nowTime = System.currentTimeMillis();
                if(preTime < 0)preTime = nowTime;
                ALog.Log4(TAG + "onNext: "+value.size()+" Time: "+(nowTime - preTime)/1000);
                if(null != value)onGetListRouteInfo(value);
                mRouteInfoFetcher.resume();
                preTime = nowTime;
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

    private void updateGoogleUI() {
        String googleAccountName = null;
        Uri googleAccountPicUri = null;
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(mContext);
        if (account != null) {
            googleAccountName = account.getDisplayName();
            googleAccountPicUri = account.getPhotoUrl();

        } else {
            googleAccountName = mContext.getString(R.string.no_google_account_login);
        }
        Glide.with(mContext)
                .load((null != googleAccountPicUri) ? googleAccountPicUri : R.drawable.googleg_color)
                .into(mImageView);
        mTextView.setText(googleAccountName);
    }
}
