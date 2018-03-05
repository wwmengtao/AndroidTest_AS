package com.example.testmodule.activities.ui.swipemenu;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.testmodule.ALog;
import com.example.testmodule.R;
import com.example.testmodule.ui.swipemenu.MotoActionFragment;
import com.example.testmodule.ui.swipemenu.SwipeMenu;
import com.example.testmodule.ui.swipemenu.SwipeMenuRightFragmentMain;
import com.example.testmodule.ui.swipemenu.data.RVData;
import com.example.testmodule.ui.swipemenu.recyclerview.SwipeMenuLeftAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Mengtao1 on 2018/02/07.
 */
public class SwipeMenuActivity extends AppCompatActivity implements SwipeMenuLeftAdapter.OnItemViewClickListener,
        View.OnClickListener{
    private Unbinder mUnbinder = null;
    private Context mContext = null;
    private OnActivityViewClickListener mOnItemViewClickListener = null;

    @BindView(R.id.main_swipemenu)
    SwipeMenu mMainSwipemenu;
    @BindView(R.id.swipe_menu_left_header) RelativeLayout mSwipeMenuLeftHeader;
    @BindView(R.id.user_info_iv) ImageView mImageView;
    @BindView(R.id.user_info_name_loc) LinearLayout mSwipeMenuRightHeaderNL;
    @BindView(R.id.user_info_name) TextView mTextView;
    @BindView(R.id.user_info_from_place) TextView mTextViewPlace;
    @BindView(R.id.user_info_iv2) ImageView mImageView2;
    @BindView(R.id.user_info_name2) TextView mTextView2;
    //
    @BindView(R.id.rv_swipe_menu_left) RecyclerView RVSwipeMenuLeft;
    private SwipeMenuLeftAdapter mSwipeMenuLeftAdapter = null;
    private List<RVData> mRVData = null;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ALog.Log("SwipeMenuActivity_onCreate");
        setContentView(R.layout.activity_swipe_menu2);
        mContext = getApplicationContext();
        mUnbinder = ButterKnife.bind(this);
        initFragment();
        initSwipeMenuLeftRecyclerView();
    }

    @OnClick(R.id.main_btn_menu)
    public void onMainBtnClicked(){
        if (mMainSwipemenu.isMenuShowing()) {
            mMainSwipemenu.hideMenu();
        } else {
            mMainSwipemenu.showMenu();
        }
    }

    private void initSwipeMenuLeftRecyclerView(){
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(mContext);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        RVSwipeMenuLeft.setLayoutManager(mLinearLayoutManager);
        mSwipeMenuLeftAdapter = new SwipeMenuLeftAdapter(mContext);
        mSwipeMenuLeftAdapter.setOnItemViewClickListener(this);
        mRVData = RVData.getData();
        mSwipeMenuLeftAdapter.setData(mRVData);
        RVSwipeMenuLeft.setAdapter(mSwipeMenuLeftAdapter);
        //set divider
        Drawable divider = getResources().getDrawable(R.drawable.appinfodivider);
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL);
        mDividerItemDecoration.setDrawable(divider);
        RVSwipeMenuLeft.addItemDecoration(mDividerItemDecoration);
        //
    }

    @Override
    public void onItemViewClick(int position) {
        ALog.Log("onItemViewClick: "+position);
        mMainSwipemenu.hideMenu();
        navigateToCertainFragment((0 == position));
    }

    public void setOnActivityViewClickListener(OnActivityViewClickListener listener){
        this.mOnItemViewClickListener = listener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.main_btn_menu:
                onMainBtnClicked();
                break;
        }
    }

    public interface OnActivityViewClickListener{
        void onActivityViewClick(String str);
    }

    FragmentManager fm = null;
    private void initFragment(){
        fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if(fragment == null){
            fragment = getMainFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }

    private Fragment getMainFragment(){
        return SwipeMenuRightFragmentMain.newInstance("param1", "param2");
    }

    private void navigateToCertainFragment(boolean mainScreen){
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if(fragment != null) {
            ALog.Log("fragment: "+fragment.toString());
        }
        fm.beginTransaction()
                .replace(R.id.fragment_container, mainScreen ? getMainFragment() : getCertainFragment())
                .commit();
    }

    private Fragment getCertainFragment(){
        return MotoActionFragment.newInstance("param1", "param2");
    }

    @Override
    public void onBackPressed() {
        if (mMainSwipemenu.isMenuShowing()) {
            mMainSwipemenu.hideMenu();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mUnbinder.unbind();
    }

}
