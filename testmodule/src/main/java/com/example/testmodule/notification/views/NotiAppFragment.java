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
import com.example.testmodule.notification.notifiutils.NotifyBlockManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NotiAppFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NotiAppFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotiAppFragment extends Fragment implements AppInfoAdapter.OnItemViewClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Context mContext = null;
    private Unbinder mUnbinder = null;
    private NotifyBlockManager mNotifyBlockManager = null;
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
    @BindView(R.id.decline) TextView mTVDecline;

    private OnFragmentInteractionListener mListener;

    public NotiAppFragment() {
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
    public static NotiAppFragment newInstance(String param1, String param2) {
        NotiAppFragment fragment = new NotiAppFragment();
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
        View view = inflater.inflate(R.layout.fragment_noti_app, container, false);
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
        mRecyclerViewEvent.setAdapter(mAppInfoAdapter);
        DividerItemDecoration decoration = new DividerItemDecoration(mContext, DividerItemDecoration.HORIZONTAL);
        decoration.setDrawable(mContext.getResources().getDrawable(R.drawable.appinfodivider));
        mRecyclerViewEvent.addItemDecoration(decoration);
    }

    @Override
    public void onResume(){
        super.onResume();
        mAppInfoAdapter.setData(mNotifyBlockManager.getAppsInfo(NotifyBlockManager.APP_TYPE.FLAG_NO_SYSTEM));
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
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        this.mContext = context.getApplicationContext();
        this.mNotifyBlockManager = NotifyBlockManager.get(mContext);
    }

    @Override
    public void onItemViewClick(int position, Intent intent) {
        if(null != intent){
            mContext.startActivity(intent);
        }else if(position == mAppInfoAdapter.getItemCount() - 1){
            ALog.Log("heiehi");
        }
    }

    @OnClick(R.id.decline)
    public void decline(){//should
        getActivity().finish();
    }

    @Override public void onDestroyView() {
        mUnbinder.unbind();
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        mNotifyBlockManager.clear();
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
