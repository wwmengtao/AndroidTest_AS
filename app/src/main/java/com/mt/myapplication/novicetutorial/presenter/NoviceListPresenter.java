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

import android.content.Intent;
import android.support.annotation.NonNull;

import com.fernandocejas.android10.sample.domain.UserNT;
import com.fernandocejas.android10.sample.domain.exception.DefaultErrorBundle;
import com.fernandocejas.android10.sample.domain.exception.ErrorBundle;
import com.fernandocejas.android10.sample.domain.interactor.DefaultObserver;
import com.fernandocejas.android10.sample.domain.interactor.GetUserNTList;
import com.fernandocejas.android10.sample.domain.interactor.GetUserNTList.Params;
import com.mt.myapplication.novicetutorial.com.fernandocejas.android10.sample.presentation.exception.ErrorMessageFactory;
import com.mt.myapplication.novicetutorial.com.fernandocejas.android10.sample.presentation.mapper.UserModelDataNTMapper;
import com.mt.myapplication.novicetutorial.com.fernandocejas.android10.sample.presentation.model.UserModelNT;
import com.mt.myapplication.novicetutorial.view.interfaces.NoviceRecyclerView;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import static com.mt.myapplication.novicetutorial.view.activities.NoviceListActivity.NOVICE_LIST_ACTIVITY_KEY;

/**
 * {@link Presenter} that controls communication between views and models of the presentation
 * layer.
 */
public class NoviceListPresenter implements Presenter {
  private NoviceRecyclerView mNoviceRecyclerView;
  private final GetUserNTList mGetUserNTList;
  private final UserModelDataNTMapper mUserModelDataNTMapper;
  private GetUserNTList.Params mParams;

  @Inject
  public NoviceListPresenter(GetUserNTList mGetUserNTList, UserModelDataNTMapper mUserModelDataNTMapper){
    this.mGetUserNTList = mGetUserNTList;
    this.mUserModelDataNTMapper = mUserModelDataNTMapper;
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

  public void onUserClicked(UserModelNT userModel) {
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
      this.mGetUserNTList.dispose();
      this.mNoviceRecyclerView = null;
  }


  private void showErrorMessage(ErrorBundle errorBundle) {
    String errorMessage = ErrorMessageFactory.create(this.mNoviceRecyclerView.context(),
            errorBundle.getException());
    this.mNoviceRecyclerView.showError(errorMessage);
  }

  private void showUsersCollectionInView(Collection<UserNT> usersCollection) {
    final Collection<UserModelNT> userModelsCollection =
            this.mUserModelDataNTMapper.transform(usersCollection);
    this.mNoviceRecyclerView.setUserList(userModelsCollection);
  }

  //获取数据
  private void getUserList() {
    Intent mIntent = mNoviceRecyclerView.getViewIntent();
    UserModelNT userModel = (UserModelNT)mIntent.getParcelableExtra(NOVICE_LIST_ACTIVITY_KEY);
    String xmlFileName = userModel.getKey();//xmlFileName内容为functionkeyscontent.xml之类的xml文件名称
    if(null == xmlFileName)return;
    this.mParams = new GetUserNTList.Params(Params.DataType.COLLECTION_DATA_LEVEL2, xmlFileName, null);//第一个界面解析xmlfiles.xml中一级标题
    this.mGetUserNTList.execute(new UserRecyclerViewObserver(), this.mParams);
  }

  private final class UserRecyclerViewObserver extends DefaultObserver<List<UserNT>> {
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

    @Override public void onNext(List<UserNT> users) {
      NoviceListPresenter.this.showUsersCollectionInView(users);
    }
  }
}
