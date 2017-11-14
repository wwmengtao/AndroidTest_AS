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

import com.mt.androidtest_as.alog.ALog;
import com.mt.myapplication.novicetutorial.com.fernandocejas.android10.sample.presentation.model.UserModelNT;
import com.mt.myapplication.novicetutorial.model.MessageEvent;
import com.mt.myapplication.novicetutorial.view.interfaces.NoviceRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Collection;

import javax.inject.Inject;

/**
 * {@link Presenter} that controls communication between views and models of the presentation
 * layer.
 */
public class NoviceViewPagerPresenter implements Presenter {
  private NoviceRecyclerView mNoviceRecyclerView;
//  private final GetUserNTList mGetUserNTList;
//  private final UserModelDataNTMapper mUserModelDataNTMapper;

  @Inject Context mContext;
//  @Inject public NoviceViewPagerPresenter(GetUserNTList mGetUserNTList, UserModelDataNTMapper mUserModelDataNTMapper){
//    this.mGetUserNTList = mGetUserNTList;
//    this.mUserModelDataNTMapper = mUserModelDataNTMapper;
//  }
  @Inject public NoviceViewPagerPresenter(){

  }
  public void setView(@NonNull NoviceRecyclerView view) {
    mNoviceRecyclerView = view;
  }

  /**
   * Initializes the presenter by start retrieving the user list.
   */
  public void initialize() {
    EventBus.getDefault().register(this);
  }

  public void onUserClicked(UserModelNT userModel) {
    mNoviceRecyclerView.viewUser(userModel);
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
//      this.mGetUserNTList.dispose();
      this.mNoviceRecyclerView = null;
      EventBus.getDefault().unregister(this);
  }

  //为ViewPager类型视图设置数据
  private void setUserList(UserModelNT mUserModelNT, Collection<UserModelNT> mUserModelNTCollection) {
    hideViewRetry();
    showViewLoading();
    this.mNoviceRecyclerView.setUserList(mUserModelNT, mUserModelNTCollection);
  }

  @Subscribe(threadMode = ThreadMode.POSTING, sticky = true)
  public void onMessage(MessageEvent event) {
    if(event.getEventType() != MessageEvent.EVENT_TYPE.VIEWPAGER)return;
    UserModelNT mUserModelNT = event.getData();
    ALog.fillInStackTrace("onMessage_mUserModelNT: "+mUserModelNT.getKey());
    Collection<UserModelNT> mUserModelNTCollection = event.getDataCollection();
    setUserList(mUserModelNT, mUserModelNTCollection);
    //取消事件只允许在ThreadMode在ThreadMode.PostThread的事件处理方法中
    EventBus.getDefault().cancelEventDelivery(event);
  }

  public boolean userModelNTsEqual(UserModelNT user1, UserModelNT user2){
    boolean equal = false;
    if(user1.getKey()!=null && user2.getKey()!=null){
      if(user1.getKey().equals(user2.getKey())){
        equal = true;
      }
    }
    return equal;
  }

}
