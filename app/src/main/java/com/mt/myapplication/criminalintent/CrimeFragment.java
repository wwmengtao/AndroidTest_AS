package com.mt.myapplication.criminalintent;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.mt.androidtest_as.BuildConfig;
import com.mt.androidtest_as.R;
import com.mt.androidtest_as.alog.ALog;
import com.mt.androidtest_as.alog.ALogFragment;
import com.mt.myapplication.criminalintent.crimebasedata.Crime;
import com.mt.myapplication.criminalintent.crimebasedata.CrimeLab;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class CrimeFragment extends ALogFragment {
    public static final int CF_REQUEST_CODE_DATE = 0X10;
    public static final int CF_REQUEST_CODE_DETAILED_CRIME_PIC = 0X11;
    public static final String CRIME_ID = "CrimeFragment_CRIME_ID";
    public static final String CRIME_DATE="CrimeFragment_CRIME_DATE";
    public static final String DIALOG_DATE = "DialogDate";
    public static final String DIALOG_PIC_DETAIL = "DialogPicDetail";
    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mSolvedCheckbox;
    private Button mReportButton;
    private Button mSuspectButton;
    private ImageButton mPhotoButton;
    private ImageView mPhotoView;
    private File mPhotoFile;
    private static final int REQUEST_CONTACT = 1;
    private static final int REQUEST_PHOTO= 2;
    private Callbacks mCallbacks = null;
    private Activity mActivity = null;
    public static Fragment newFragment(UUID id){
        Bundle mBundle = new Bundle();
        mBundle.putSerializable(CRIME_ID,id);
        CrimeFragment mCrimeFragment = new CrimeFragment();
        mCrimeFragment.setArguments(mBundle);
        return mCrimeFragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
        if(CrimeListActivity.isFragmentContainerDetailedExisted())mCallbacks = (Callbacks)activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    public interface Callbacks{
        void onCrimeUpdated();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        UUID mID = (UUID)getArguments().getSerializable(CRIME_ID);
        mCrime = CrimeLab.get(getActivity()).getCrime(mID);
        mPhotoFile = CrimeLab.get(getActivity()).getPhotoFile(mCrime);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime, container, false);

        mTitleField = (EditText) v.findViewById(R.id.crime_title);
        mTitleField.setText(mCrime.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCrime.setTitle(s.toString());
                updateCrime();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mDateButton = (Button) v.findViewById(R.id.crime_date);
        mDateButton.setText(mCrime.getDate().toString());
        mDateButton.setEnabled(true);
        mDateButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int mOrientation = getActivity().getApplicationContext().getResources().getConfiguration().orientation;
                if(mOrientation == Configuration.ORIENTATION_LANDSCAPE){
                    DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getDate());
                    dialog.setTargetFragment(CrimeFragment.this, CrimeFragment.CF_REQUEST_CODE_DATE);
                    dialog.show(getActivity().getSupportFragmentManager(),DIALOG_DATE);
                }else{
                    Intent it = DatePickerActivity.newIntent(getActivity(),mCrime.getDate());
                    startActivityForResult(it,CrimeFragment.CF_REQUEST_CODE_DATE);
                }

            }
        });

        mSolvedCheckbox = (CheckBox) v.findViewById(R.id.crime_solved);
        mSolvedCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setSolved(isChecked);
                updateCrime();
            }
        });
        mSolvedCheckbox.setChecked(mCrime.isReSolved());

        mReportButton = (Button)v.findViewById(R.id.crime_report);
        mReportButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);//public static final String ACTION_SEND = "android.intent.action.SEND";
                i.setType("text/plain");
                /**
                 * 例如某AndroidManifest.xml有如下内容：
                 *<activity
                     android:name="SetURLInteractive"
                     android:label="Dream URL (Interactive)"
                     android:theme="@android:style/Theme.Translucent.NoTitleBar">

                     <intent-filter>
                         <action android:name="android.intent.action.SEND" />
                         <category android:name="android.intent.category.DEFAULT" />
                         <data android:mimeType="text/plain" />
                     </intent-filter>
                 </activity>
                 */
                i.putExtra(Intent.EXTRA_TEXT, getCrimeReport());
                i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject));//<string name="crime_report_subject">CriminalIntent Crime Report</string>
                i = Intent.createChooser(i, getString(R.string.send_report));
                startActivity(i);
            }
        });

        final Intent pickContact = new Intent(Intent.ACTION_PICK,
                ContactsContract.Contacts.CONTENT_URI);
        mSuspectButton = (Button)v.findViewById(R.id.crime_suspect);
        mSuspectButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivityForResult(pickContact, CrimeFragment.REQUEST_CONTACT);
            }
        });

        if (mCrime.getSuspect() != null) {
            mSuspectButton.setText(mCrime.getSuspect());
        }
        PackageManager packageManager = getActivity().getPackageManager();
        if(null==packageManager.resolveActivity(pickContact,PackageManager.MATCH_DEFAULT_ONLY)){
            /**
             * PackageManager.MATCH_DEFAULT_ONLY表明仅仅查找带有"android.intent.action.CATEGORY_DEFAULT"的action标记的Activity
             */
            mSuspectButton.setEnabled(false);
        }

        mPhotoButton = (ImageButton) v.findViewById(R.id.crime_camera);
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        boolean canTakePhoto = mPhotoFile != null &&
                captureImage.resolveActivity(packageManager) != null;
        mPhotoButton.setEnabled(canTakePhoto);

        if (canTakePhoto) {
            Uri photoURI = null;
            if (Build.VERSION.SDK_INT >= 24) {
                photoURI = FileProvider.getUriForFile(getContext(),
                        BuildConfig.APPLICATION_ID + ".provider", mPhotoFile);
                /**
                 * photoURI以及mPhotoFile的log信息如下：
                 * photoURI: content://com.mt.androidtest_as.provider/external_files/Android/data/com.mt.androidtest_as/files/Pictures/IMGxxx.jpg
                 * mPhotoFile:/storage/emulated/0/Android/data/com.mt.androidtest_as/files/Pictures/IMGxxx.jpg
                 */

                ALog.Log("photoURI: "+photoURI.toString()+" mPhotoFile:"+mPhotoFile.getAbsolutePath());
            } else {
                photoURI = Uri.fromFile(mPhotoFile);
            }
            captureImage.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
        }

        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(captureImage, CrimeFragment.REQUEST_PHOTO);
            }
        });

        mPhotoView = (ImageView) v.findViewById(R.id.crime_photo);
        mPhotoView.setScaleType(ImageView.ScaleType.FIT_XY);
        mPhotoView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(null==mPhotoFile || !mPhotoFile.exists())return;
                int mOrientation = getActivity().getApplicationContext().getResources().getConfiguration().orientation;
                if(mOrientation == Configuration.ORIENTATION_LANDSCAPE){
                    PicDetailsFragment dialog = PicDetailsFragment.newInstance(mPhotoFile);
                    dialog.setTargetFragment(CrimeFragment.this, CrimeFragment.CF_REQUEST_CODE_DETAILED_CRIME_PIC);
                    dialog.show(getActivity().getSupportFragmentManager(),DIALOG_PIC_DETAIL);
                }else{
                    Intent it = PicDetailsActivity.newIntent(getActivity(),mPhotoFile);
                    startActivityForResult(it,CrimeFragment.CF_REQUEST_CODE_DETAILED_CRIME_PIC);
                }

            }
        });
        PictureUtils.updateImageView(mPhotoView,mPhotoFile,getActivity());
        return v;
    }

    public CheckBox getSolvedCheckbox(){
        return mSolvedCheckbox;
    }

    public void updateCrime(){
        CrimeLab.get(getActivity())
                .updateCrime(mCrime);
        if(CrimeListActivity.isFragmentContainerDetailedExisted())mCallbacks.onCrimeUpdated();
    }

    @Override
    public void onPause() {
        super.onPause();

        CrimeLab.get(getActivity())
                .updateCrime(mCrime);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CrimeFragment.CF_REQUEST_CODE_DATE && resultCode == DatePickerFragment.DPF_RESULT_CODE) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.DPF_DATE);
            mCrime.setDate(date);
            mDateButton.setText(mCrime.getDate().toString());
            updateCrime();
        }else if (requestCode == CrimeFragment.CF_REQUEST_CODE_DATE && resultCode == DatePickerFragment2.DPF_RESULT_CODE2) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.DPF_DATE);
            mCrime.setDate(date);
            mDateButton.setText(mCrime.getDate().toString());
            updateCrime();
        }else if (requestCode == CrimeFragment.REQUEST_CONTACT && data != null) {
            Uri contactUri = data.getData();
            // Specify which fields you want your query to return
            // values for.
            String[] queryFields = new String[] {
                    ContactsContract.Contacts.DISPLAY_NAME,
            };
            // Perform your query - the contactUri is like a "where"
            // clause here
            ContentResolver resolver = getActivity().getContentResolver();
            Cursor c = resolver
                    .query(contactUri, queryFields, null, null, null);

            try {
                // Double-check that you actually got results
                if (c.getCount() == 0) {
                    return;
                }

                // Pull out the first column of the first row of data -
                // that is your suspect's name.
                c.moveToFirst();

                String suspect = c.getString(0);
                mCrime.setSuspect(suspect);
                mSuspectButton.setText(suspect);
            } finally {
                c.close();
            }
        }else if (requestCode == CrimeFragment.REQUEST_PHOTO) {
            PictureUtils.updateImageView(mPhotoView,mPhotoFile,getActivity());
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        boolean isFragmentContainerDetailedExisted = CrimeListActivity.isFragmentContainerDetailedExisted();
        ALog.Log("isFragmentContainerDetailedExisted:"+isFragmentContainerDetailedExisted);
        if(!isFragmentContainerDetailedExisted)inflater.inflate(R.menu.fragment_crime, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_delete_crime:
                Dialog mDialog = new AlertDialog.Builder(mActivity)
                        .setTitle(getString(R.string.delete_this_item))
                        .setNegativeButton(android.R.string.cancel,null)
                        .setPositiveButton(android.R.string.ok,new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                CrimeLab.get(getActivity().getApplicationContext()).delCrime(mCrime);
                                mActivity.finish();
                            }
                        })
                        .create();
                List<Crime> mCrimes = CrimeLab.get(mActivity).getCrimes();
                if(null != mCrimes && mCrimes.size()>0)mDialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private String getCrimeReport() {
        String solvedString = null;
        if (mCrime.isReSolved()) {
            solvedString = getString(R.string.crime_report_solved);
        } else {
            solvedString = getString(R.string.crime_report_unsolved);
        }
        String dateFormat = "EEE, MMM dd";
        String dateString = DateFormat.format(dateFormat, mCrime.getDate()).toString();
        String suspect = mCrime.getSuspect();
        if (suspect == null) {
            suspect = getString(R.string.crime_report_no_suspect);
        } else {
            suspect = getString(R.string.crime_report_suspect, suspect);
        }
        String report = getString(R.string.crime_report,
                mCrime.getTitle(), dateString, solvedString, suspect);
        return report;
    }
}
