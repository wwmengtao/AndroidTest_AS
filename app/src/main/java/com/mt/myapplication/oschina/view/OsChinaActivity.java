package com.mt.myapplication.oschina.view;

import android.support.v4.app.Fragment;

import com.mt.androidtest_as.alog.BaseActivity;

import java.util.ArrayList;
import java.util.List;


public class OsChinaActivity extends BaseActivity {
    private List<TurnBackListener> mTurnBackListeners = new ArrayList<>();

    @Override
    public Fragment getFragment() {
        return DynamicTabFragment.newInstance(getApplicationContext());
    }

    public interface TurnBackListener {
        boolean onTurnBack();
    }

    public void addOnTurnBackListener(TurnBackListener l) {
        this.mTurnBackListeners.add(l);
    }

    public void toggleNavTabView(boolean isShowOrHide) {

    }
}
