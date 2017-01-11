package com.mt.myapplication.criminalintent;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;


import com.mt.androidtest_as.R;
import com.mt.androidtest_as.alog.ALogDialogFragment;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Mengtao1 on 2016/12/9.
 */

public class DatePickerFragment extends ALogDialogFragment {
    public static final String DPF_DATE="DatePickerFragment_CRIME_DATE";
    public static final int DPF_RESULT_CODE = 0x1101;
    public static DatePickerFragment newInstance(Date date){
        Bundle mBundle = new Bundle();
        mBundle.putSerializable(CrimeFragment.CRIME_DATE,date);
        DatePickerFragment mDialogFragment = new DatePickerFragment();
        mDialogFragment.setArguments(mBundle);
        return mDialogFragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Date mDate = (Date)getArguments().getSerializable(CrimeFragment.CRIME_DATE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mDate);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_date, null);
        final DatePicker mDatePicker = (DatePicker)v.findViewById(R.id.dialog_date_date_picker);
        mDatePicker.init(year, month, day, null);
        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.date_picker_title)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int year = mDatePicker.getYear();
                                int month = mDatePicker.getMonth();
                                int day = mDatePicker.getDayOfMonth();
                                Date date = new GregorianCalendar(year, month, day).getTime();
                                sendResult(DPF_RESULT_CODE, date);
                            }
                        })
                .create();
    }

    public void sendResult(int resultCode, Date date){
        Intent mIntent = new Intent();
        mIntent.putExtra(DPF_DATE,date);
        getTargetFragment().onActivityResult(getTargetRequestCode(),DatePickerFragment.DPF_RESULT_CODE,mIntent);
    }
}
