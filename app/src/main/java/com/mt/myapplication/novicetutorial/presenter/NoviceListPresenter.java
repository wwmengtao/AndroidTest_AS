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

import android.support.annotation.NonNull;

import com.fernandocejas.android10.sample.domain.User;
import com.fernandocejas.android10.sample.domain.exception.DefaultErrorBundle;
import com.fernandocejas.android10.sample.domain.exception.ErrorBundle;
import com.fernandocejas.android10.sample.domain.interactor.DefaultObserver;
import com.fernandocejas.android10.sample.domain.interactor.GetUserList;
import com.mt.myapplication.novicetutorial.com.fernandocejas.android10.sample.presentation.exception.ErrorMessageFactory;
import com.mt.myapplication.novicetutorial.com.fernandocejas.android10.sample.presentation.mapper.UserModelDataMapper;
import com.mt.myapplication.novicetutorial.com.fernandocejas.android10.sample.presentation.model.UserModel;
import com.mt.myapplication.novicetutorial.view.interfaces.NoviceRecyclerView;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

/**
 * {@link Presenter} that controls communication between views and models of the presentation
 * layer.
 */
public class NoviceListPresenter implements Presenter {
  private NoviceRecyclerView mNoviceRecyclerView;
  private final GetUserList mGetUserList;
  private final UserModelDataMapper mUserModelDataMapper;
  @Inject
  public NoviceListPresenter(GetUserList getUserListUserCase, UserModelDataMapper userModelDataMapper){
    this.mGetUserList = getUserListUserCase;
    this.mUserModelDataMapper = userModelDataMapper;
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
    mNoviceRecyclerView.viewUser(userModel);
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

  @Override
  public void resume() {}

  @Override
  public void pause() {}

  @Override
  public void destroy() {
      this.mGetUserList.dispose();
      this.mNoviceRecyclerView = null;
  }


  private void showErrorMessage(ErrorBundle errorBundle) {
    String errorMessage = ErrorMessageFactory.create(this.mNoviceRecyclerView.context(),
            errorBundle.getException());
    this.mNoviceRecyclerView.showError(errorMessage);
  }

  private void showUsersCollectionInView(Collection<User> usersCollection) {
    final Collection<UserModel> userModelsCollection =
            this.mUserModelDataMapper.transform(usersCollection);
    this.mNoviceRecyclerView.setUserList(userModelsCollection);
  }

  //获取数据
  private void getUserList() {
    this.mGetUserList.execute(new UserRecyclerViewObserver(), null);
  }

  private final class UserRecyclerViewObserver extends DefaultObserver<List<User>> {
    /**
     * 根据Observable协议的定义，onNext可能会被调用零次或者很多次，最后会有一次onCompleted或onError调用
     * （不会同时），传递数据给onNext通常被称作发射，onCompleted和onError被称作通知。
     */
    @Override public void onComplete() {
      NoviceListPresenter.this.hideViewLoading();
    }

    @Override public void onError(Throwable e) {
      NoviceListPresenter.this.hideViewLoading();
      NoviceListPresenter.this.showErrorMessage(new DefaultErrorBundle((Exception) e));
      NoviceListPresenter.this.showViewRetry();
    }

    @Override public void onNext(List<User> users) {
      NoviceListPresenter.this.showUsersCollectionInView(users);
    }
  }
}
