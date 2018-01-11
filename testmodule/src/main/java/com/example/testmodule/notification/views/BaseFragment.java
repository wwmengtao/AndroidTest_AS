package com.example.testmodule.notification.views;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.example.testmodule.application.AppExecutors;
import com.example.testmodule.application.BasicApp;
import com.example.testmodule.notification.mylistview.AppInfoAdapter;
import com.example.testmodule.notification.notifiutils.MockNotifyBlockManager;

import butterknife.Unbinder;

/**
 * BaseFragment：用于处理阻塞、非阻塞Fragment的监听行为
 * Created by mengtao1 on 2018/1/11.
 */

public abstract class BaseFragment extends Fragment implements AppInfoAdapter.OnItemViewClickListener,
        AppInfoAdapter.OnItemViewLongClickListener, MockNotifyBlockManager.OnWhiteListAppChangedListener{
    protected String TAG = null;
    protected Context mContext = null;
    protected BasicApp mApplication = null;
    protected Unbinder mUnbinder = null;
    protected AppExecutors mAppExecutors = null;
    protected MockNotifyBlockManager mNotifyBlockManager = null;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.TAG = getClass().getSimpleName();
        this.mContext = context.getApplicationContext();
        this.mNotifyBlockManager = MockNotifyBlockManager.get(mContext);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApplication = (BasicApp)getActivity().getApplication();
        mAppExecutors = mApplication.getAppExecutors();
        MockNotifyBlockManager.get(mContext).addOnWhiteListAppChangedListener(this);
    }

    @Override public void onDestroyView() {
        if(null != mUnbinder)mUnbinder.unbind();
        MockNotifyBlockManager.get(mContext).removeOnWhiteListAppChangedListener(this);
        super.onDestroyView();
    }
}
