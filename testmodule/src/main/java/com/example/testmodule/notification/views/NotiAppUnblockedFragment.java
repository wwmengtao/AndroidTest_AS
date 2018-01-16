package com.example.testmodule.notification.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.testmodule.ALog;
import com.example.testmodule.R;
import com.example.testmodule.notification.mylistview.AppInfoAdapter;
import com.example.testmodule.notification.notifiutils.MockNotifyBlockManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NotiAppUnblockedFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NotiAppUnblockedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotiAppUnblockedFragment extends BaseFragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String PARENT_FRAGMENT = "NotiAppUnblockedFragment";
    private AppInfoAdapter mAppInfoAdapter = null;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    @BindView(R.id.iv_calendar) ImageView mIVCalendar;
    @BindView(R.id.tv_calendar) TextView mTVCalendar;
    @BindView(R.id.tv_title_calendar) TextView mTVTitleCalendar;
    @BindView(R.id.tv_time_calendar) TextView mTVTimeCalendar;

    @BindView(R.id.iv_title) ImageView mIVTitleEvent;
    @BindView(R.id.tv_title) TextView mTVTitleEvent;
    @BindView(R.id.tv_subtext1) TextView mTVSubTv1Event;
    @BindView(R.id.tv_subtext2) TextView mTVSubTv2Event;
    @BindView(R.id.rv_apps) RecyclerView mRecyclerViewEvent;

    private OnFragmentInteractionListener mListener;

    public NotiAppUnblockedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NotiAppFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NotiAppUnblockedFragment newInstance(String param1, String param2) {
        NotiAppUnblockedFragment fragment = new NotiAppUnblockedFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
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
        View view = inflater.inflate(R.layout.fragment_noti_app_unblocked, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        initViews();
        return view;
    }

    private void initViews(){
        //1.init calendar view
        Pair<String, Drawable> mPair = mNotifyBlockManager.getCalendarInfo();
        mIVCalendar.setImageDrawable(mPair.second);
        mTVCalendar.setText(mPair.first);
        mTVTitleCalendar.setText(mContext.getString(R.string.calendar_title));
        mTVTitleCalendar.getPaint().setFakeBoldText(true);//set text bold
        mTVTimeCalendar.setText(mContext.getString(R.string.calendar_when));
        //2.init block notification view
        mTVTitleEvent.setText(mContext.getString(R.string.notifi_office_title));
        mTVSubTv1Event.setText(mContext.getString(R.string.notifi_office_des));
        mTVSubTv1Event.getPaint().setFakeBoldText(true);//set text bold
        mTVSubTv2Event.setText(mContext.getString(R.string.app_nofity_whitelist));
        initRecyclerView();
    }

    private void initRecyclerView(){
        mAppInfoAdapter = new AppInfoAdapter(mContext);
        mAppInfoAdapter.setOnItemViewClickListener(this);
        mAppInfoAdapter.setOnItemViewLongClickListener(this);
        mAppInfoAdapter.setLayoutType(AppInfoAdapter.LayoutType.LinearLayoutManager);
        mRecyclerViewEvent.setAdapter(mAppInfoAdapter);
        DividerItemDecoration decoration = new DividerItemDecoration(mContext, DividerItemDecoration.HORIZONTAL);
        decoration.setDrawable(mContext.getResources().getDrawable(R.drawable.appinfodivider));
        mRecyclerViewEvent.addItemDecoration(decoration);
    }

    @Override
    public void onResume(){
        super.onResume();
        updateRecyclerView();
    }

    @Override
    public void onWhiteListAppChanged() {
        //如果当前应用不在后台运行，那么更新RecyclerView
        if(!mApplication.isBackground()) {
            ALog.Log(TAG + "_onWhiteListAppChanged");
            mAppExecutors.mainThread().execute(new Runnable() {
                @Override
                public void run() {
                    updateRecyclerView();
                }
            });
        }
    }

    private void updateRecyclerView(){
        mAppInfoAdapter.setData(mNotifyBlockManager.getAppsInfo(MockNotifyBlockManager.APP_TYPE.FLAG_WHITE_LIST_NOTI_UNBLOCKED));
        mAppInfoAdapter.notifyDataSetChanged();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
            ALog.Log("onButtonPressed");
        }
    }

    @Override
    public void onItemViewClick(int position, Intent intent) {
        if(null != intent){
            mContext.startActivity(intent);
        }else if(position == mAppInfoAdapter.getItemCount() - 1){
            ((NotiAppActivity) getActivity()).showNotiAppBlockedFragment();
        }
    }

    @Override
    public void onItemViewLongClick(int position) {
        ALog.Log("onItemViewLongClick");
        mAppInfoAdapter.setBlocked(position);
        updateRecyclerView();
    }

    @OnClick(R.id.decline)
    public void decline(){//should
        getActivity().finish();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
