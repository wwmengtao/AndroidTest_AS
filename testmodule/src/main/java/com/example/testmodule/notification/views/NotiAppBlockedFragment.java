package com.example.testmodule.notification.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.testmodule.ALog;
import com.example.testmodule.R;
import com.example.testmodule.notification.mylistview.AppInfoAdapter;
import com.example.testmodule.notification.notifiutils.NotifyBlockManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class NotiAppBlockedFragment extends Fragment implements AppInfoAdapter.OnItemViewClickListener{
    public static final String TAG = "NotiAppBlockedFragment";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Context mContext = null;
    private Unbinder mUnbinder = null;
    private NotifyBlockManager mNotifyBlockManager = null;
    private AppInfoAdapter mAppInfoAdapter = null;

    @BindView(R.id.tv_app_block_title) TextView mTVTitle;
    @BindView(R.id.rv_blocked_apps) RecyclerView mRecyclerView;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NotiAppBlockedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShowAppsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NotiAppBlockedFragment newInstance(String param1, String param2) {
        NotiAppBlockedFragment fragment = new NotiAppBlockedFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_show_apps, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        initViews();
        return view;
    }

    private void initViews(){
        initRecyclerView();
    }

    private void initRecyclerView(){
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        mAppInfoAdapter = new AppInfoAdapter(mContext);
        mAppInfoAdapter.setOnItemViewClickListener(this);
        mAppInfoAdapter.setLayoutType(AppInfoAdapter.LayoutType.GridLayoutManager);
        mRecyclerView.setAdapter(mAppInfoAdapter);
        //set divider
        Drawable divider = getContext().getResources().getDrawable(R.drawable.appinfodivider);
        DividerItemDecoration mDividerItemDecoration;
        mDividerItemDecoration = new DividerItemDecoration(mContext, DividerItemDecoration.HORIZONTAL);
        mDividerItemDecoration.setDrawable(divider);
        mRecyclerView.addItemDecoration(mDividerItemDecoration);
        mDividerItemDecoration = new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL);
        mDividerItemDecoration.setDrawable(divider);
        mRecyclerView.addItemDecoration(mDividerItemDecoration);
        //
    }

    @Override
    public void onResume(){
        super.onResume();
        mAppInfoAdapter.setData(mNotifyBlockManager.getAppsInfo(NotifyBlockManager.APP_TYPE.FLAG_WHITE_LIST_NOTI_BLOCKED));
        mAppInfoAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemViewClick(int position, Intent intent) {
        ALog.Log("onItemViewClick");
        mAppInfoAdapter.setBlocked(position);
    }

    @OnClick(R.id.returntoparent)
    public void returnToParent(){
        getFragmentManager().
            popBackStack(NotiAppUnblockedFragment.PARENT_FRAGMENT,
                FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context.getApplicationContext();
        this.mNotifyBlockManager = NotifyBlockManager.get(mContext);
    }

    @Override public void onDestroyView() {
        mUnbinder.unbind();
        super.onDestroyView();
        ALog.Log("onDestroyView");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        ALog.Log("onDetach");
    }
}
