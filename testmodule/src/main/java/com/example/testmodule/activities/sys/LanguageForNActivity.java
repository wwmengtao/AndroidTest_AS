package com.example.testmodule.activities.sys;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;

import com.example.framework_o.language.LocaleListEditor;
import com.example.testmodule.ALog;
import com.example.testmodule.BaseAcitivity;
import com.example.testmodule.R;

public class LanguageForNActivity extends BaseAcitivity implements FragmentManager.OnBackStackChangedListener{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_for_n);
        FragmentManager fm = getFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragment = getFragment();
            if(null == fragment)return;
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }

    public Fragment getFragment(){
        getFragmentManager().addOnBackStackChangedListener(this);
        return (android.os.Build.VERSION.SDK_INT < 24) ? null : new LocaleListEditor();// If not android 7.x, return
    }

    @Override
    public void onBackStackChanged() {
        // TODO Auto-generated method stub
        /**
         * getBackStackEntryCount：可以判断FragmentManager的Fragment回退层级，0表示第一级，此处为LocaleListEditor
         */
        final int count = getFragmentManager().getBackStackEntryCount();

        String str = LocaleListEditor.class.getName();
        Fragment fm = getFragmentManager().findFragmentById(R.id.fragment_container);//R.id.main_content
        ALog.Log("onBackStackChanged_BackStackEntryCount: "+count+" CurrentFragment: "+fm.toString()+" str: "+str);
    }
}
