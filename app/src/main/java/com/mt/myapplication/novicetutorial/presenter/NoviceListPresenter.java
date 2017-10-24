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
import com.mt.myapplication.novicetutorial.model.domain.User;
import com.mt.myapplication.novicetutorial.model.domain.exception.DefaultErrorBundle;
import com.mt.myapplication.novicetutorial.model.domain.exception.ErrorBundle;
import com.mt.myapplication.novicetutorial.model.domain.exception.ErrorMessageFactory;
import com.mt.myapplication.novicetutorial.model.domain.interactor.DefaultObserver;
import com.mt.myapplication.novicetutorial.model.mapper.UserModelDataMapper;
import com.mt.myapplication.novicetutorial.view.interfaces.NoviceRecyclerView;

import javax.inject.Inject;

/**
 * {@link Presenter} that controls communication between views and models of the presentation
 * layer.
 */
public class NoviceListPresenter implements Presenter {
  private NoviceRecyclerView mNoviceRecyclerView;
  private final UserModelDataMapper mUserModelDataMapper;

  @Inject
  public NoviceListPresenter(){
    this.mUserModelDataMapper = new UserModelDataMapper();
  }

  public void setView(@NonNull NoviceRecyclerView view) {
    mNoviceRecyclerView = view;
  }

  /**
   * Initializes the presenter by start retrieving the user list.
   */
  public void initialize() {
    this.loadUserList();
  }

  public void onUserClicked(UserModel userModel) {
    mNoviceRecyclerView.showUser(userModel);
  }

  /**
   * Loads all users.
   */
  private void loadUserList() {
    hideViewRetry();
    showViewLoading();
    getUserList();
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


  private void showErrorMessage(ErrorBundle errorBundle) {
    String errorMessage = ErrorMessageFactory.create(this.mNoviceRecyclerView.context(),
            errorBundle.getException());
    this.mNoviceRecyclerView.showError(errorMessage);
  }

  private void showUserDetailsInView(User user) {
    final UserModel userModel = this.mUserModelDataMapper.transform(user);
    this.mNoviceRecyclerView.showUser(userModel);
  }

  private final class UserDetailsObserver extends DefaultObserver<User> {

    @Override public void onComplete() {
      NoviceListPresenter.this.hideViewLoading();
    }

    @Override public void onError(Throwable e) {
      NoviceListPresenter.this.hideViewLoading();
      NoviceListPresenter.this.showErrorMessage(new DefaultErrorBundle((Exception) e));
      NoviceListPresenter.this.showViewRetry();
    }

    @Override public void onNext(User user) {
      NoviceListPresenter.this.showUserDetailsInView(user);
    }
  }
}
