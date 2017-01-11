package com.mt.myapplication.criminalintent;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.widget.CheckBox;

import com.mt.androidtest_as.R;
import com.mt.myapplication.criminalintent.crimebasedata.Crime;

public class CrimeListActivity extends BaseActivity implements
    CrimeListFragment.Callbacks, CrimeFragment.Callbacks, Handler.Callback{
    private static boolean fragment_container_detail_exist = false;
    private CrimeListFragment mFragment = null;
    private Crime mCrime = null;//平板项目当前CrimeListFragment选中的item对应的Crime对象
    private Handler mHandler = null;

    @Override
    protected Integer getResourceID() {
        return R.layout.activity_masterdetail;
    }

    @Override
    public Fragment getFragment() {
        fragment_container_detail_exist = (null != findViewById(R.id.fragment_container_detail));
        mFragment = new CrimeListFragment();
        return mFragment;
    }

    public static boolean isFragmentContainerDetailedExisted(){
        return fragment_container_detail_exist;
    }

    /**
     * onItemSelected:平板项目左侧CrimeListFragment中点击某个item后的回调函数
     * @param mCrime
     */
    @Override
    public void onItemSelected(Crime mCrime) {
        if(!isFragmentContainerDetailedExisted()){
            if(null == mCrime)return;
            Intent mIntent = CrimePagerActivity.newIntent(this, mCrime.getId());
            startActivityForResult(mIntent, CrimeListFragment.REQUEST_CODE);
        }else {
            setDetailedFragment(mCrime);
        }
    }

    /**
     * 平板设备CrimeFragment的显示设置
     * @param mCrime
     */
    private void setDetailedFragment(Crime mCrime) {
        FragmentManager fm = getSupportFragmentManager();
        if(null != mCrime) {
            setCurrentCrime(mCrime);
            Fragment fragment = CrimeFragment.newFragment(mCrime.getId());
            fm.beginTransaction()
                    .replace(R.id.fragment_container_detail, fragment)
                    .commit();
        }else{//如果此时没有数据的话，那么应该删除R.id.fragment_container_detail处对应的Fragment
            Fragment fragment = fm.findFragmentById(R.id.fragment_container_detail);
            if(null == fragment)return;
            fm.beginTransaction()
                    .remove(fragment)
                    .commit();
        }
    }

    /**
     * onCrimeUpdated：平板项目右侧CrimeFragment数据发生改变后的回调函数
     */
    @Override
    public void onCrimeUpdated() {
        CrimeListFragment listFragment = (CrimeListFragment)getSupportFragmentManager()
                                            .findFragmentById(R.id.fragment_container);
        if(null != listFragment)listFragment.updateUI();
    }

    @Override
    public void onBackPressed(){
        if(null != mFragment && -1 != mFragment.getCurrentClickedItemPosition()){
            mFragment.resetCurrentClickedItemPosition();
            setCurrentCrime(null);
            onItemSelected(null);
        }else {
            super.onBackPressed();
        }
    }

    private void setCurrentCrime(Crime mCrime){
        this.mCrime = mCrime;
    }

    private Crime getCurrentCrime(){
        return mCrime;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
        if(isFragmentContainerDetailedExisted()){//当平板项目转屏时，更新右侧的CrimeFragment
            setDetailedFragment(getCurrentCrime());
        }
    }

    @Override
    public void onDestroy() {
        if(null != mHandler) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
        mCrime = null;
        super.onDestroy();
    }

    public Handler getHandler(){
        if(null == mHandler)mHandler = new Handler(this);
        return mHandler;
    }

    public static final int MSG_UPDATE_DETAIELD_CRIME_FRAGMENT = 0x1001;

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what){
            /**
             * 平板项目点击CrimeListFragment的item上的CheckBox后，更新右侧CrimeFragment处相应的CheckBox
             */
            case MSG_UPDATE_DETAIELD_CRIME_FRAGMENT:
                Crime crimeCurrentListFragment = getCurrentCrime();
                Crime crimeCrimeFragment = (Crime)msg.obj;//平板项目右侧CrimeFragment处对应的Crime对象
                if(null == crimeCurrentListFragment || null == crimeCrimeFragment)return false;
                CrimeFragment fragment = (CrimeFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_container_detail);
                if(null == fragment)return false;
                if(crimeCurrentListFragment.getId().equals(crimeCrimeFragment.getId())) {
                    CheckBox mCheckBox = fragment.getSolvedCheckbox();
                    mCheckBox.setChecked(crimeCrimeFragment.isReSolved());
                }
                break;
            default:
                ;
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        /**
         * super.onActivityResult会将未被处理的result消息继续传给FragmentActivity.mFragments的targetFragment.onActivityResult，
         * 比如CrimeFragment内的onActivityResult函数，如果没有super.onActivityResult，那么平板项目上CrimeFragment内的拍照、
         * 选择日期、选择嫌疑人信息等都无法更新。
         */
        super.onActivityResult(requestCode, resultCode, intent);
        //手机(非平板)项目从CrimePagerActivity返回后，需要确定用户最后编辑的CrimeFragment的位置
        if (requestCode == CrimeListFragment.REQUEST_CODE && resultCode == CrimePagerActivity.RESULT_CODE) {
            CrimeListFragment listFragment = (CrimeListFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            if(null != listFragment){
                int currentCrimePagerItem = intent.getIntExtra(CrimePagerActivity.CURRENT_CRIME_ID_VIEWPAGER, 0);
                listFragment.getRecyclerView().smoothScrollToPosition(currentCrimePagerItem);
                listFragment.setitemClickPosition(currentCrimePagerItem);
            }
        }
    }
}