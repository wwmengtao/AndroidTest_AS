/**
 * Copyright (C) 2015 Fernando Cejas Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mt.myapplication.novicetutorial.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.mt.myapplication.novicetutorial.model.UserModel;
import com.mt.myapplication.novicetutorial.model.mapper.UserModelDataMapper;
import com.mt.myapplication.novicetutorial.view.interfaces.NoviceRecyclerView;

import javax.inject.Inject;

/**
 * {@link Presenter} that controls communication between views and models of the presentation
 * layer.
 */
public class NoviceGridPresenter implements Presenter {
  private NoviceRecyclerView mNoviceRecyclerView;
  private final UserModelDataMapper mUserModelDataMapper;

  @Inject
  public NoviceGridPresenter(){
    mUserModelDataMapper = new UserModelDataMapper();
  }

  public void onUserClicked(UserModel userModel) {
    mNoviceRecyclerView.showUser(userModel);
  }

  public void setView(@NonNull NoviceRecyclerView view) {
    this.mNoviceRecyclerView = view;
  }

  /**
   * Initializes the presenter by start retrieving the user list.
   */
  public void initialize() {
    this.loadUserList();
  }

  /**
   * Loads all users.
   */
  private void loadUserList() {
    this.hideViewRetry();
    this.showViewLoading();
    this.getUserList();
  }

  private void showViewLoading() {
    this.mNoviceRecyclerView.showLoading();
  }

  private void hideViewLoading() {
    this.mNoviceRecyclerView.hideLoading();
  }

  private void showViewRetry() {
    this.mNoviceRecyclerView.showRetry();
  }

  private void hideViewRetry() {
    this.mNoviceRecyclerView.hideRetry();
  }

  //获取数据
  private void getUserList() {

  }

  @Override
  public void resume() {}

  @Override
  public void pause() {}

  @Override
  public void destroy() {
  }

}
