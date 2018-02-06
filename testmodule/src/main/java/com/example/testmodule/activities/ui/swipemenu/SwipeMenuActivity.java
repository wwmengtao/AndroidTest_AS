package com.example.testmodule.activities.ui.swipemenu;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.testmodule.R;
import com.example.testmodule.ui.swipemenu.SwipeMenu;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SwipeMenuActivity extends AppCompatActivity{
    private Unbinder mUnbinder = null;

    @BindView(R.id.main_swipemenu) SwipeMenu mMainSwipemenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_menu);
        mUnbinder = ButterKnife.bind(this);
    }

    @OnClick(R.id.main_btn_menu)
    public void onMainBtnClicked(){
        if (mMainSwipemenu.isMenuShowing()) {
            mMainSwipemenu.hideMenu();
        } else {
            mMainSwipemenu.showMenu();
        }
    }

}
