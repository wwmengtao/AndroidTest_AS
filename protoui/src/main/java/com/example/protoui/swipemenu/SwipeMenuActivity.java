package com.example.protoui.swipemenu;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.protoui.ALog;
import com.example.protoui.R;
import com.example.protoui.SignInActivity;
import com.example.protoui.swipemenu.data.RVData;
import com.example.protoui.swipemenu.recyclerview.SwipeMenuLeftAdapter;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Mengtao1 on 2018/02/07.
 */
public class SwipeMenuActivity extends AppCompatActivity implements SwipeMenuLeftAdapter.OnItemViewClickListener{
    private Unbinder mUnbinder = null;
    private Context mContext = null;
    private OnActivityViewClickListener mOnItemViewClickListener = null;
    //
    String googleAccountName = null;
    String googleAccountWhere = null;
    Uri googleAccountPicUri = null;

    @BindView(R.id.main_swipemenu) SwipeMenu mMainSwipemenu;
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

    public static Intent getLaunchIntent(Context mContext){
        Intent mIntent = new Intent(mContext,SwipeMenuActivity.class);
        return mIntent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_menu);
        mContext = getApplicationContext();
        mUnbinder = ButterKnife.bind(this);
        initFragment();
        initSwipeMenuLeftRecyclerView();
    }

    @OnClick({R.id.user_info_iv, R.id.user_info_iv2})
    public void onUserInfoIVClicked(){
        mContext.startActivity(SignInActivity.getLaunchIntent2(mContext, false));
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
        if(null != mOnItemViewClickListener && 0 != position){
            mOnItemViewClickListener.onActivityViewClick(mRVData.get(position).getTitle());
        }
        mMainSwipemenu.hideMenu();
    }

    public void setOnActivityViewClickListener(OnActivityViewClickListener listener){
        this.mOnItemViewClickListener = listener;
    }

    public interface OnActivityViewClickListener{
        void onActivityViewClick(String str);
    }

    private void initFragment(){
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if(fragment == null){
            fragment = getFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }

    private Fragment getFragment(){
        return SwipeMenuFragment.newInstance("param1", "param2");
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
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus){
            getGoogleAccountInfo();
            updateGoogleUI();
        }
    }

    private void getGoogleAccountInfo(){
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(mContext);
        if (account != null) {
            googleAccountName = account.getDisplayName();
            googleAccountWhere = mContext.getString(R.string.google_account_from_moto);
            googleAccountPicUri = account.getPhotoUrl();
        } else {
            googleAccountName = mContext.getString(R.string.google_account_who);
            googleAccountWhere = mContext.getString(R.string.google_account_where);
        }
    }

    private void updateGoogleUI() {
        ALog.Log("updateGoogleUI");
        //
        Glide.with(mContext)
                .load((null != googleAccountPicUri) ? googleAccountPicUri : R.drawable.googleg_color)
                .into(mImageView);

        mTextView.setText(googleAccountName);
//        int widthOfText = (int)(mTextView.getPaint().measureText(googleAccountName));
//        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)mSwipeMenuRightHeaderNL.getLayoutParams();
//        params.width = widthOfText;
//        mSwipeMenuRightHeaderNL.setLayoutParams(params);
//        ALog.Log("widthOfText: "+widthOfText+" "+mTextView.getWidth());
        mTextViewPlace.setText(googleAccountWhere);
        //
        Glide.with(mContext)
                .load((null != googleAccountPicUri) ? googleAccountPicUri : R.drawable.googleg_color)
                .into(mImageView2);
        mTextView2.setText(googleAccountName);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mUnbinder.unbind();
    }

}
