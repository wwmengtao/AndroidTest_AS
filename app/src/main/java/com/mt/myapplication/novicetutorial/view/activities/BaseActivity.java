package com.mt.myapplication.novicetutorial.view.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.mt.androidtest_as.R;
import com.mt.androidtest_as.alog.ALogActivity;
import com.mt.androidtest_as.alog.AndroidTest_AS_Application;
import com.mt.myapplication.novicetutorial.com.fernandocejas.android10.sample.presentation.di.HasComponent;
import com.mt.myapplication.novicetutorial.com.fernandocejas.android10.sample.presentation.di.components.ApplicationComponent;
import com.mt.myapplication.novicetutorial.com.fernandocejas.android10.sample.presentation.di.components.DaggerUserComponent;
import com.mt.myapplication.novicetutorial.com.fernandocejas.android10.sample.presentation.di.components.UserComponent;
import com.mt.myapplication.novicetutorial.com.fernandocejas.android10.sample.presentation.di.modules.ActivityModule;
import com.mt.myapplication.novicetutorial.com.fernandocejas.android10.sample.presentation.navigation.Navigator;

import javax.inject.Inject;

/**
 * Base {@link Activity} class for every Activity in this application.
 */
public abstract class BaseActivity extends ALogActivity implements HasComponent<UserComponent> {
  private UserComponent userComponent;

  @Inject
  Navigator navigator;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.initializeInjector();
    setContentView(getResourceID());
    FragmentManager fm = getSupportFragmentManager();
    Fragment fragment = fm.findFragmentById(R.id.fragment_container);

    if (fragment == null) {
      fragment = getFragment();
      fm.beginTransaction()
              .add(R.id.fragment_container, fragment)
              .commit();
    }
  }

  public abstract Fragment getFragment();

  @LayoutRes
  protected Integer getResourceID(){//用于规定BaseActivity的布局文件
    return R.layout.activity_base;
  }

  /**
   * Get the Main Application component for dependency injection.
   *
   * @return {@link com.mt.myapplication.novicetutorial.com.fernandocejas.android10.sample.presentation.di.components.ApplicationComponent}
   */
  protected ApplicationComponent getApplicationComponent() {
    return ((AndroidTest_AS_Application) getApplication()).getApplicationComponent();
  }

  @Override public UserComponent getComponent() {
    return userComponent;
  }


  private void initializeInjector() {
    this.userComponent = DaggerUserComponent.builder()
            .applicationComponent(getApplicationComponent())
            .activityModule(getActivityModule())
            .build();
  }

  /**
   * Get an Activity module for dependency injection.
   *
   * @return {@link com.mt.myapplication.novicetutorial.com.fernandocejas.android10.sample.presentation.di.modules.ActivityModule}
   */
  protected ActivityModule getActivityModule() {
    return new ActivityModule(this);
  }
}
