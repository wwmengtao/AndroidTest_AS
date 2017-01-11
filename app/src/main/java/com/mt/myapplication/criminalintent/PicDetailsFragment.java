package com.mt.myapplication.criminalintent;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;


import com.mt.androidtest_as.R;
import com.mt.androidtest_as.alog.ALogDialogFragment;

import java.io.File;

/**
 * Created by Mengtao1 on 2016/12/9.
 */

public class PicDetailsFragment extends ALogDialogFragment {
    public static final String DPF_PIC="PicDetailsFragment_CRIME_PIC";
    public static final int DPF_RESULT_CODE = 0x1101;
    public static PicDetailsFragment newInstance(File file){
        Bundle mBundle = new Bundle();
        mBundle.putSerializable(CrimeFragment.DIALOG_PIC_DETAIL,file);
        PicDetailsFragment mDialogFragment = new PicDetailsFragment();
        mDialogFragment.setArguments(mBundle);
        return mDialogFragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final File file = (File)getArguments().getSerializable(CrimeFragment.DIALOG_PIC_DETAIL);
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_detailed_crime_pic, null);
        final ImageView mImageView = (ImageView)v.findViewById(R.id.dialog_detailed_crime_pic);
        mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        PictureUtils.updateImageView(mImageView,file,getActivity());
        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle("Detailed crime pic")
                .setPositiveButton(android.R.string.ok,null)
                .create();
    }

}
