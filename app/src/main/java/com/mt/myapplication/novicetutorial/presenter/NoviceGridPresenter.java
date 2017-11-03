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

import com.fernandocejas.android10.sample.data.ALog;
import com.fernandocejas.android10.sample.domain.UserNT;
import com.fernandocejas.android10.sample.domain.exception.DefaultErrorBundle;
import com.fernandocejas.android10.sample.domain.exception.ErrorBundle;
import com.fernandocejas.android10.sample.domain.interactor.DefaultObserver;
import com.fernandocejas.android10.sample.domain.interactor.GetUserListDetails;
import com.fernandocejas.android10.sample.domain.interactor.GetUserListDetails.Params;
import com.mt.myapplication.novicetutorial.com.fernandocejas.android10.sample.presentation.exception.ErrorMessageFactory;
import com.mt.myapplication.novicetutorial.com.fernandocejas.android10.sample.presentation.mapper.UserModelDataNTMapper;
import com.mt.myapplication.novicetutorial.com.fernandocejas.android10.sample.presentation.model.UserModelNT;
import com.mt.myapplication.novicetutorial.view.interfaces.NoviceRecyclerView;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

/**
 * {@link Presenter} that controls communication between views and models of the presentation
 * layer.
 */
public class NoviceGridPresenter implements Presenter {
  private NoviceRecyclerView mNoviceRecyclerView;
  private final GetUserListDetails mGetUserListDetails;//获取标题对应的一类数据，如功能键，桌面等
  private final UserModelDataNTMapper mUserModelDataNTMapper;
  private Params mParams;

  @Inject
  public NoviceGridPresenter(GetUserListDetails getUserListUserCase, UserModelDataNTMapper mUserModelDataNTMapper){
    this.mGetUserListDetails = getUserListUserCase;
    this.mUserModelDataNTMapper = mUserModelDataNTMapper;
    this.mParams = new Params(Params.DataType.COLLECTION_DATA_LEVEL1,"xmlfiles.xml", null);//第一个界面解析xmlfiles.xml中一级标题
  }

  public void onUserClicked(UserModelNT userModel) {
    mNoviceRecyclerView.viewUser(userModel);
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
    this.mGetUserListDetails.execute(new UserRecyclerViewObserver(), mParams);
  }

  private final class UserRecyclerViewObserver extends DefaultObserver<List<UserNT>> {

    @Override public void onComplete() {
      NoviceGridPresenter.this.hideViewLoading();
    }

    @Override public void onError(Throwable e) {
      NoviceGridPresenter.this.hideViewLoading();
      NoviceGridPresenter.this.showErrorMessage(new DefaultErrorBundle((Exception) e));
      NoviceGridPresenter.this.showViewRetry();
    }

    @Override public void onNext(List<UserNT> users) {
      NoviceGridPresenter.this.showUsersCollectionInView(users);
      visitCollection(users);
    }
  }
  private void visitCollection(Collection<UserNT> mUserEntityCollection){
    if(null != mUserEntityCollection && mUserEntityCollection.size() > 0){
      ALog.Log("mUserEntityCollection.size(): "+mUserEntityCollection.size());
      for(UserNT mUserEntityNT : mUserEntityCollection){
        ALog.Log("key: "+mUserEntityNT.getKey());
        ALog.Log("adj: "+mUserEntityNT.getAdjunction());
        ALog.Log("pic: "+mUserEntityNT.getPic());
        ALog.Log("ind: "+mUserEntityNT.getIndex());
      }
    }
  }
  @Override
  public void resume() {}

  @Override
  public void pause() {}

  @Override
  public void destroy() {
    this.mGetUserListDetails.dispose();
    this.mNoviceRecyclerView = null;
  }

}
