package com.mt.myapplication.criminalintent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.mt.androidtest_as.R;
import com.mt.androidtest_as.alog.ALogDialogFragment;

import java.io.File;

/**
 * Created by Mengtao1 on 2016/12/9.
 */

public class PicDetailsFragment2 extends ALogDialogFragment {
    public static final String DPF_PIC="PicDetailsFragment2_CRIME_PIC";
    public static final int DPF_RESULT_CODE = 0x1101;

    public static PicDetailsFragment2 newInstance(File file){
        Bundle mBundle = new Bundle();
        mBundle.putSerializable(CrimeFragment.DIALOG_PIC_DETAIL,file);
        PicDetailsFragment2 mDialogFragment = new PicDetailsFragment2();
        mDialogFragment.setArguments(mBundle);
        return mDialogFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final File file = (File)getArguments().getSerializable(CrimeFragment.DIALOG_PIC_DETAIL);
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_detailed_crime_pic, null);
        final ImageView mImageView = (ImageView)v.findViewById(R.id.dialog_detailed_crime_pic);
        mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        PictureUtils.updateImageView(mImageView,file,getActivity());
        return v;
    }
}
