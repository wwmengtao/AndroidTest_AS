package com.mt.myapplication.criminalintent;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;


import com.mt.androidtest_as.R;
import com.mt.androidtest_as.alog.ALogDialogFragment;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Mengtao1 on 2016/12/9.
 */

public class DatePickerFragment2 extends ALogDialogFragment implements View.OnClickListener{
    public static final String DPF_DATE="DatePickerFragment_CRIME_DATE";
    public static final int DPF_RESULT_CODE2 = 0x1102;
    private DatePicker mDatePicker=null;
    public static DatePickerFragment2 newInstance(Date date){
        Bundle mBundle = new Bundle();
        mBundle.putSerializable(CrimeFragment.CRIME_DATE,date);
        DatePickerFragment2 mDialogFragment = new DatePickerFragment2();
        mDialogFragment.setArguments(mBundle);
        return mDialogFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_date2, null);
        mDatePicker = (DatePicker)v.findViewById(R.id.dialog_date_date_picker);
        final Date mDate = (Date)getArguments().getSerializable(CrimeFragment.CRIME_DATE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mDate);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        mDatePicker.init(year, month, day, null);
        Button mbtn = (Button)v.findViewById(R.id.ok);
        mbtn.setOnClickListener(this);
        return v;
    }

    public void sendResult(int resultCode, Date date){
        Intent mIntent = new Intent();
        mIntent.putExtra(DPF_DATE,date);
        getActivity().setResult(resultCode, mIntent);
    }

    @Override
    public void onClick(View v) {
        int year = mDatePicker.getYear();
        int month = mDatePicker.getMonth();
        int day = mDatePicker.getDayOfMonth();
        Date date = new GregorianCalendar(year, month, day).getTime();
        sendResult(DatePickerFragment2.DPF_RESULT_CODE2, date);
        getActivity().finish();
    }
}
