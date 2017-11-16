package com.mt.myapplication.novicetutorial.view.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mt.androidtest_as.R;
import com.mt.myapplication.novicetutorial.com.fernandocejas.android10.sample.presentation.di.components.UserComponent;
import com.mt.myapplication.novicetutorial.com.fernandocejas.android10.sample.presentation.model.UserModelNT;
import com.mt.myapplication.novicetutorial.presenter.NoviceViewPagerPresenter;
import com.mt.myapplication.novicetutorial.view.interfaces.NoviceRecyclerView;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by mengtao1 on 2017/11/13.
 */

public class NoviceViewPagerFragment extends BaseFragment implements NoviceRecyclerView{
    private Unbinder mUnbinder;
    private Activity mActivity = null;

    @Inject NoviceViewPagerPresenter mNoviceViewPagerPresenter;

    @BindView(R.id.view_item_view_pager) ViewPager mViewPager;

    @Override public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getComponent(UserComponent.class).inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_novice_viewpager, container, false);
        mUnbinder = ButterKnife.bind(this, v);
        return v;
    }

    @Override public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.mNoviceViewPagerPresenter.setView(this);
        if (savedInstanceState == null) {
            this.loadUserList();
        }
    }

    @Override
    public void onActivityFinish(){//NoviceViewPagerActivity在finish时的回调
        mNoviceViewPagerPresenter.returnCurrentIndexToCaller(mViewPager.getCurrentItem());
    }

    @Override
    public void onDestroyView(){
        mUnbinder.unbind();
        super.onDestroyView();
    }

    @Override public void onDestroy() {
        super.onDestroy();
        this.mNoviceViewPagerPresenter.destroy();

    }

    /**
     * Loads all users.
     */
    private void loadUserList() {
        this.mNoviceViewPagerPresenter.initialize();
    }

    @Override
    public Context context() {
        return null;
    }

    @Override
    public Intent getViewIntent() {
        return null;
    }

    @Override
    public void setUserList(final Collection<UserModelNT> userModelCollection) {

    }

    @Override
    public void setUserList(final UserModelNT mUserModelNT, final Collection<UserModelNT> userModelCollection) {
        final List<UserModelNT> mData = (List<UserModelNT>)userModelCollection;
        mViewPager.setAdapter(new FragmentPagerAdapter(((FragmentActivity)mActivity).getSupportFragmentManager()){

            @Override
            public int getCount() {
                return mData.size();
            }

            @Override
            public Fragment getItem(int position) {
                return PagerItemFragment.newFragment(mData.get(position));
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener(){
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            /**
             * 之所以在此处设置Activity标题，因为onPageSelected判断当前处于正中间的item
             */
            public void onPageSelected(int position) {
                mActivity.setTitle(getString(mData.get(position).getKey()));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        //以下设置ViewPager的当前item
        int currentIndex = mUserModelNT.getIndex();
        if(currentIndex >= 0){
            mViewPager.setCurrentItem(currentIndex);
            mActivity.setTitle(getString(mUserModelNT.getKey()));
        }
    }

    @Override
    public void viewUser(UserModelNT userModel) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showRetry() {

    }

    @Override
    public void hideRetry() {

    }

    @Override
    public void showError(String message) {

    }

    @Override
    public void setCurrentItemBackGround(int currentIndex){
    }

    @Override
    public void onUserAdapterItemClicked(UserModelNT userModel) {

    }
}
