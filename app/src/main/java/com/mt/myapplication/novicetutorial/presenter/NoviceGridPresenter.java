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

import com.fernandocejas.android10.sample.domain.UserNT;
import com.fernandocejas.android10.sample.domain.exception.DefaultErrorBundle;
import com.fernandocejas.android10.sample.domain.exception.ErrorBundle;
import com.fernandocejas.android10.sample.domain.interactor.DefaultObserver;
import com.fernandocejas.android10.sample.domain.interactor.GetUserNTList;
import com.fernandocejas.android10.sample.domain.interactor.GetUserNTList.Params;
import com.mt.androidtest_as.alog.ALog;
import com.mt.myapplication.novicetutorial.com.fernandocejas.android10.sample.presentation.exception.ErrorMessageFactory;
import com.mt.myapplication.novicetutorial.com.fernandocejas.android10.sample.presentation.mapper.UserModelDataNTMapper;
import com.mt.myapplication.novicetutorial.com.fernandocejas.android10.sample.presentation.model.UserModelNT;
import com.mt.myapplication.novicetutorial.model.MessageEvent;
import com.mt.myapplication.novicetutorial.view.interfaces.NoviceRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

/**
 * {@link Presenter} that controls communication between views and models of the presentation
 * layer.
 */
public class NoviceGridPresenter implements Presenter {
  private static final String TAG = "NoviceGridPresenter";
  public static final String ROOT_XMLFILE_NAME = "xmlfiles.xml";
  private NoviceRecyclerView mNoviceRecyclerView;
  private final GetUserNTList mGetUserNTList;//获取标题对应的一类数据，如功能键，桌面等
  private final UserModelDataNTMapper mUserModelDataNTMapper;

  @Inject
  public NoviceGridPresenter(GetUserNTList getUserListUserCase, UserModelDataNTMapper mUserModelDataNTMapper){
    this.mGetUserNTList = getUserListUserCase;
    this.mUserModelDataNTMapper = mUserModelDataNTMapper;
  }

  public void setView(@NonNull NoviceRecyclerView view) {
    this.mNoviceRecyclerView = view;
  }

  /**
   * Initializes the presenter by start retrieving the user list.
   */
  public void initialize() {
    if(!EventBus.getDefault().isRegistered(this)) {
      EventBus.getDefault().register(this);
    }
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
    ALog.visitCollection2(TAG, userModelsCollection);
  }

  //获取数据
  private void getUserList() {
    Params mParams = new Params(Params.DataType.COLLECTION_DATA_LEVEL1, ROOT_XMLFILE_NAME, null);//第一个界面解析xmlfiles.xml中一级标题
    this.mGetUserNTList.execute(new UserRecyclerViewObserver(), mParams);
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
      ALog.visitCollection(TAG+"_onNext", users);
    }
  }

  @Subscribe(threadMode = ThreadMode.POSTING)
  public void onMessage(MessageEvent event) {//此时收到ViewPagar视图返回的当前页序号，保存该序号便可存储用户操作记录
    if(event.getEventType() != MessageEvent.EVENT_TYPE.FROM_LISTVIEW)return;
    int currentIndex = event.getCurrentIndex();
    mNoviceRecyclerView.setCurrentItemBackGround(currentIndex);
    ALog.Log1("NoviceGridPresenter_onMessage_currentIndex: "+currentIndex);
  }

  @Override
  public void resume() {}

  @Override
  public void pause() {}

  @Override
  public void destroy() {
    EventBus.getDefault().unregister(this);
    this.mGetUserNTList.dispose();
    this.mNoviceRecyclerView = null;
  }

}
