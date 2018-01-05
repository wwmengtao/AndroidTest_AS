package com.example.testmodule.notification.views;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.WindowManager;

import com.example.testmodule.BaseAcitivity;
import com.example.testmodule.R;

public class NotiAppActivity extends BaseAcitivity implements NotiAppFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noti_app);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initFragment();
    }

    private void initFragment(){
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if(fragment ==null){
            fragment = getFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }

    private Fragment getFragment(){
        return NotiAppFragment.newInstance("param1", "param2");
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
