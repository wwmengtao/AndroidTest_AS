package com.example.testmodule.sysapps.views;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.testmodule.R;


public class RequestPermissionsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int CAMERA_REQUEST_CODE = 0x001;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RequestPermissionsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RequestPermissionsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RequestPermissionsFragment newInstance(String param1, String param2) {
        RequestPermissionsFragment fragment = new RequestPermissionsFragment();
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
        return inflater.inflate(R.layout.fragment_request_permissions, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            //申请CAMERA权限，在Fragment中可以直接申请权限，不要使用ActivityCompat.requestPermissions，否则
            //在Fragment中无法回调onRequestPermissionsResult
            requestPermissions(new String[]{Manifest.permission.CAMERA},
                    CAMERA_REQUEST_CODE);
        }
    }

    @Override
    /**
     * 在Fragment中申请权限，不要使用ActivityCompat.requestPermissions, 直接使用Fragment的requestPermissions方法，否则在Fragment中无法回掉onRequestPermissionsResult
     */
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        switch (requestCode){
            case CAMERA_REQUEST_CODE:
                Toast.makeText(getActivity(), "onRequestPermissionsResult!", Toast.LENGTH_SHORT).show();
        }
    }
}
