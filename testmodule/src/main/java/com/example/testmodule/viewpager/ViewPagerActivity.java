package com.example.testmodule.viewpager;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.testmodule.ALog;
import com.example.testmodule.BaseAcitivity;
import com.example.testmodule.R;
import com.example.testmodule.utils.DpObserverInfo;
import com.example.testmodule.viewpager.adapter.RecyclingPagerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;

public class ViewPagerActivity extends BaseAcitivity {

    //    private ClipViewPager mViewPager;
    private ViewPager mViewPager;
    private TubatuAdapter mPagerAdapter;
    DisposableObserver<Long> mDpObserver = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager.setPageTransformer(true, new ScalePageTransformer());
        mPagerAdapter = new TubatuAdapter(this);
        mViewPager.setAdapter(mPagerAdapter);
        initData();
    }

    private void initData() {
        List<Integer> list = new ArrayList<>();
        list.add(R.drawable.style_xiandai);
        list.add(R.drawable.style_jianyue);
        list.add(R.drawable.style_oushi);
        list.add(R.drawable.style_zhongshi);
        list.add(R.drawable.style_meishi);
        list.add(R.drawable.style_dzh);
        list.add(R.drawable.style_dny);
        list.add(R.drawable.style_rishi);

        //设置OffscreenPageLimit
        mViewPager.setOffscreenPageLimit(Math.min(list.size(), 5));
        mPagerAdapter.addAll(list);
        setInfiniteViewPager();
    }

    /**
     * 实现ViewPager无限循环，但是瑕疵是：当前页跳转到0的时候，会出现一个快速遍历的过程
     */
    private void setInfiniteViewPager(){
        InfiniteObserver mIfObserver = new InfiniteObserver();
        Observable<Long> observable
                = Observable.interval(2, 2, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread());
        mDpObserver = observable.subscribeWith(mIfObserver);
    }

    private class InfiniteObserver<Long> extends DpObserverInfo.BaseObserver<Long> {

        @Override
        public void onNext(Long aLong) {
            ALog.Log(TAG+"onNext");
            int currentIndex = mViewPager.getCurrentItem();
            if (++currentIndex == mPagerAdapter.getCount()) {
                mViewPager.setCurrentItem(0, true);
            } else {
                mViewPager.setCurrentItem(currentIndex, true);
            }
        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onComplete() {

        }
    }

    @Override
    public void onDestroy(){
        mDpObserver.dispose();
        super.onDestroy();
    }

    public static class TubatuAdapter extends RecyclingPagerAdapter {

        private final List<Integer> mList;
        private final Context mContext;

        public TubatuAdapter(Context context) {
            mList = new ArrayList<>();
            mContext = context;
        }

        public void addAll(List<Integer> list) {
            mList.addAll(list);
            notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup container) {
            ImageView imageView = null;
            if (convertView == null) {
                imageView = new ImageView(mContext);
            } else {
                imageView = (ImageView) convertView;
            }
            imageView.setTag(position);
            imageView.setImageResource(mList.get(position));
            return imageView;
        }

        @Override
        public int getCount() {
            return mList.size();
        }
    }
}
