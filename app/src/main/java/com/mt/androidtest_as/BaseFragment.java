package com.mt.androidtest_as;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.mt.androidtest_as.alog.ALog;
import com.example.androidcommon.alog.ALogFragment;

import java.util.List;


public abstract class BaseFragment<T extends Object> extends ALogFragment implements View.OnClickListener{
    private Activity mActivity = null;
    private List<T> mData = null;
    private Dialog mCreateDataDialog = null;
    private Dialog mClearAllDataDialog = null;
    private int dialogDataIndex = 0;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
    }

    public void setBaseData(List<T> mData){
        this.mData = mData;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_base, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_clear_all_data:
                clearAllData();
                break;
            case R.id.menu_create_some_data:
                createData();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void createData(){
        if(null == mCreateDataDialog) {
            String[] arrayNum = getResources().getStringArray(R.array.data_number);
            mCreateDataDialog = new AlertDialog.Builder(mActivity)
                    .setTitle(getString(R.string.add_some_data))
                    .setNegativeButton(android.R.string.cancel, null)
                    .setSingleChoiceItems(arrayNum, 0, new android.content.DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int which) {
                            dialogDataIndex = which;
                        }
                    })
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ALog.Log("which:"+which);//此时的which一直为-1
                            onCreateDataConfirm(dialogDataIndex);
                        }
                    })
                    .create();
        }
        if(null == mData || 0 == mData.size())mCreateDataDialog.show();
    }

    public abstract void onCreateDataConfirm(int initialDataNumber);

    private void clearAllData(){
        if(null == mClearAllDataDialog) {
            mClearAllDataDialog = new AlertDialog.Builder(mActivity)
                    .setTitle(getString(R.string.clear_all_data) + " " + getString(R.string.are_you_sure))
                    .setNegativeButton(android.R.string.cancel, null)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            onClearAllDataConfirm();
                        }
                    })
                    .create();
        }
        if(null != mData && mData.size()>0)mClearAllDataDialog.show();
    }

    public abstract void onClearAllDataConfirm();

    public abstract void updateUI();
}
