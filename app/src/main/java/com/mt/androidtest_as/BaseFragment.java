package com.mt.androidtest_as;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


public abstract class BaseFragment extends Fragment {
    private Activity mActivity = null;
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
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
    private int initialDataNumber = 0;
    Dialog mCreateDataDialog = null;

    private void createData(){
        if(null == mCreateDataDialog) {
            String[] arrayNum = getResources().getStringArray(R.array.data_number);
            mCreateDataDialog = new AlertDialog.Builder(mActivity)
                    .setTitle(getString(R.string.add_some_data))
                    .setNegativeButton(android.R.string.cancel, null)
                    .setSingleChoiceItems(arrayNum, 0, new android.content.DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int which) {
                            initialDataNumber = (which + 1) * 5;
                        }
                    })
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            initialDataNumber = (0 == initialDataNumber) ? 5 : initialDataNumber;
                            onAfterCreateDataConfirm(initialDataNumber);
                        }
                    })
                    .create();
        }
        if(ifCreateDataDialogShow())mCreateDataDialog.show();
    }

    public abstract void onAfterCreateDataConfirm(int initialDataNumber);
    public abstract boolean ifCreateDataDialogShow();

    Dialog mClearAllDataDialog = null;
    private void clearAllData(){
        if(null == mCreateDataDialog) {
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
        if(ifClearAllDataDialogShow())mClearAllDataDialog.show();
    }

    public abstract void onClearAllDataConfirm();

    public abstract boolean ifClearAllDataDialogShow();

    public abstract void updateUI();
}
