package com.example.testmodule.sysapps.views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.testmodule.ALog;
import com.example.testmodule.BaseFragment2;
import com.example.testmodule.R;
import com.example.testmodule.notification.model.AppInfo;
import com.example.testmodule.notification.mylistview.AppInfoAdapter;
import com.example.testmodule.sysapps.utils.SysAppsManager;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SysAppFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SysAppFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SysAppFragment extends BaseFragment2 implements AppInfoAdapter.OnItemViewClickListener,
        DialogInterface.OnClickListener, SysAppsManager.OnAppChangedListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    @BindView(R.id.sysappRv) RecyclerView mRecyclerView;
    @BindView(R.id.icon) ImageView icon;
    @BindView(R.id.name) TextView mTVName;
    @BindView(R.id.packageName) TextView mTVPackageName;
    @BindView(R.id.className) TextView mTVClassName;
    @BindView(R.id.sourceDir) TextView mTVSourceDir;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener = null;
    private SysAppsManager mSysAppsManager = null;
    private AppInfoAdapter mAppInfoAdapter = null;

    public SysAppFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SysAppFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SysAppFragment newInstance(String param1, String param2) {
        SysAppFragment fragment = new SysAppFragment();
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
        this.mContext = context;
        this.mSysAppsManager = SysAppsManager.get(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        SysAppsManager.get(mContext).addOnAppChangedListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sys_app2, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        initRecyclerView();
        return view;
    }

    private void initRecyclerView(){
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 5));
        mAppInfoAdapter = new AppInfoAdapter(mContext);
        mAppInfoAdapter.setOnItemViewClickListener(this);
        mAppInfoAdapter.setLayoutType(AppInfoAdapter.LayoutType.GridLayoutManager);
        mRecyclerView.setAdapter(mAppInfoAdapter);
        //set divider
        Drawable divider = getContext().getResources().getDrawable(R.drawable.appinfodivider);
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL);
        mDividerItemDecoration.setDrawable(divider);
        mRecyclerView.addItemDecoration(mDividerItemDecoration);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    private int viewItemClickedPosition = -1;

    @Override
    public void onItemViewClick(int position, Intent intent) {
        //1.show appinfo
        AppInfo mAppInfo = mSysAppsManager.getAppInfoList().get(position);
        icon.setImageDrawable(mAppInfo.icon);
        mTVName.setText(mAppInfo.appName);
        mTVPackageName.setText(mAppInfo.packageName);
        mTVClassName.setText(mAppInfo.className);
        mTVSourceDir.setText(mAppInfo.sourceDir);
        //2.show dialog
        viewItemClickedPosition = position;
        showDialog();
    }

    private DialogInterface mShowDialog;
    private void showDialog() {
        AppInfo mAppInfo = mSysAppsManager.getAppInfoList().get(viewItemClickedPosition);
        mShowDialog = new AlertDialog.Builder(mContext).setTitle(mAppInfo.appName)
                .setMessage("Show what kind of info?")
                .setPositiveButton("AppInfo", this)
                .setNeutralButton("Overlay", this)
                .setNegativeButton("StartActivity", this)
                .show();
    }

    @Override
    /**
     * onClick：对话框被点击后的回调
     */
    public void onClick(DialogInterface dialog, int which) {
        if (dialog == mShowDialog) {
            AppInfo mAppInfo = mSysAppsManager.getAppInfoList().get(viewItemClickedPosition);
            Intent intent = null;
            if(which == DialogInterface.BUTTON_POSITIVE){
                intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" +mAppInfo.packageName));
            }else if(which == DialogInterface.BUTTON_NEGATIVE){
                intent = new Intent(Intent.ACTION_MAIN);
                intent.setClassName(mAppInfo.packageName, mAppInfo.className);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//FLAG_ACTIVITY_NEW_TASK一般配合FLAG_ACTIVITY_CLEAR_TOP使用
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            }else if(which == DialogInterface.BUTTON_NEUTRAL){
                intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + mAppInfo.packageName));
            }
            if(null!=intent)startActivity(intent);
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        updateRecyclerView();
    }

    private void updateRecyclerView(){
        ALog.Log("updateRecyclerView");
        mAppInfoAdapter.setData(mSysAppsManager.getAppInfoList());
        mAppInfoAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAppChanged() {
        ALog.Log(TAG+"_onAppChanged:"+(!mApplication.isBackground() && null != mAppExecutors));
        //如果当前应用不在后台运行，那么更新RecyclerView
        if(!mApplication.isBackground() && null != mAppExecutors) {
            ALog.Log(TAG+"_onAppChanged2");
            mAppExecutors.mainThread().execute(new Runnable() {
                @Override
                public void run() {
                    updateRecyclerView();
                }
            });
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        SysAppsManager.get(mContext).removeOnAppChangedListener(this);
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
