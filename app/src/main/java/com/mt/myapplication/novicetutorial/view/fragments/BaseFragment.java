/**
 * Copyright (C) 2014 android10.org. All rights reserved.
 *
 * @author Fernando Cejas (the android10 coder)
 */
package com.mt.myapplication.novicetutorial.view.fragments;

import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.mt.androidtest_as.alog.ALogFragment;
import com.mt.myapplication.novicetutorial.com.fernandocejas.android10.sample.presentation.di.HasComponent;
import com.mt.myapplication.novicetutorial.com.fernandocejas.android10.sample.presentation.model.UserModelNT;
import com.mt.myapplication.novicetutorial.view.activities.BaseActivity;


/**
 * Base {@link Fragment} class for every fragment in this application.
 */
public abstract class BaseFragment extends ALogFragment implements BaseActivity.OnActivityFinishListener {
  /**
   * Shows a {@link Toast} message.
   *
   * @param message An string representing a message to be shown.
   */
  protected void showToastMessage(String message) {
    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
  }

  /**
   * Gets a component for dependency injection by its type.
   */
  @SuppressWarnings("unchecked")
  protected <C> C getComponent(Class<C> componentType) {
    return componentType.cast(((HasComponent<C>) getActivity()).getComponent());
  }

  /**
   * Interface for listening user list events.
   */
  public interface OnFragmentClickListener {
    void onFragmentClicked(final UserModelNT userModel);//表明监听的是Fragment内的点击事件
  }
}
