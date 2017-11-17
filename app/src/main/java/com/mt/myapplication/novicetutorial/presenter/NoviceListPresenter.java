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
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.fernandocejas.android10.sample.domain.UserNT;
import com.fernandocejas.android10.sample.domain.exception.DefaultErrorBundle;
import com.fernandocejas.android10.sample.domain.exception.ErrorBundle;
import com.fernandocejas.android10.sample.domain.interactor.DefaultObserver;
import com.fernandocejas.android10.sample.domain.interactor.GetUserNTDetails;
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

import static com.mt.myapplication.novicetutorial.presenter.NoviceGridPresenter.ROOT_XMLFILE_NAME;
import static com.mt.myapplication.novicetutorial.view.activities.NoviceListActivity.NOVICE_LIST_ACTIVITY_KEY;

/**
 * {@link Presenter} that controls communication between views and models of the presentation
 * layer.
 */
public class NoviceListPresenter implements Presenter {
  private static final String TAG = "NoviceListPresenter";
  private NoviceRecyclerView mNoviceRecyclerView;
  private final GetUserNTList mGetUserNTList;
  private final GetUserNTDetails mGetUserNTDetails;
  private final UserModelDataNTMapper mUserModelDataNTMapper;
  private GetUserNTList.Params mParams;
  private Collection<UserModelNT> userModelsCollection = null;//当前数据表显示的所有数据
  //以下定义用户浏览条目的方式
  private SharedPreferences mSharedPreferences = null;
  private SharedPreferences.Editor mSharedPreferencesEditor = null;
  private String preferenceFileName = "NoviceListFragment_ViewItemStyle";
  private String VIEW_ITEM_BY_VIEWPAGER="VIEW_ITEM_BY_VIEWPAGER";
  @Inject Context mContext;
  @Inject
  public NoviceListPresenter(GetUserNTList mGetUserNTList, GetUserNTDetails mGetUserNTDetails,
                             UserModelDataNTMapper mUserModelDataNTMapper){
    this.mGetUserNTList = mGetUserNTList;
    this.mGetUserNTDetails = mGetUserNTDetails;
    this.mUserModelDataNTMapper = mUserModelDataNTMapper;
  }
  public void setView(@NonNull NoviceRecyclerView view) {
    mNoviceRecyclerView = view;
  }

  /**
   * Initializes the presenter by start retrieving the user list.
   */
  public void initialize() {
    if(!EventBus.getDefault().isRegistered(this)) {
      EventBus.getDefault().register(this);
    }
    mSharedPreferences	= mContext.getSharedPreferences(preferenceFileName, Context.MODE_PRIVATE);
    mSharedPreferencesEditor = mSharedPreferences.edit();
    this.loadUserList();
  }

  /**
   * ifViewItemByViewPager：用于判断NoviceListFragment当前用户期望浏览条目的方式是否通过ViewPager
   * @return
   */
  public boolean ifViewItemByViewPager(){
    return mSharedPreferences.getBoolean(VIEW_ITEM_BY_VIEWPAGER, false);
  }

  public void setViewItemByViewPager(boolean viewItemByViewPager){
    mSharedPreferencesEditor.putBoolean(VIEW_ITEM_BY_VIEWPAGER, viewItemByViewPager);
    mSharedPreferencesEditor.commit();
  }

  /**
   * Loads all users.
   */
  private void loadUserList() {
    hideViewRetry();
    showViewLoading();
    getUserList();
  }

  public void smoothScrollToPosition(final RecyclerView mRecyclerView, final int currentIndex){
    if(currentIndex < 0)return;
    mRecyclerView.post(new Runnable() {
      @Override
      public void run() {
        mRecyclerView.smoothScrollToPosition(currentIndex);//直接调用可能不起作用，必须放在View.post里
      }
    });
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
      if(null != this.userModelsCollection)this.userModelsCollection.clear();
      EventBus.getDefault().unregister(this);
  }


  private void showErrorMessage(ErrorBundle errorBundle) {
    String errorMessage = ErrorMessageFactory.create(this.mNoviceRecyclerView.context(),
            errorBundle.getException());
    this.mNoviceRecyclerView.showError(errorMessage);
  }

  private void showUsersCollectionInView(Collection<UserNT> usersCollection) {
    userModelsCollection = this.mUserModelDataNTMapper.transform(usersCollection);
    this.mNoviceRecyclerView.setUserList(userModelsCollection);
  }

  //返回当前数据表中所有数据
  public Collection<UserModelNT> getUserModelNTCollection(){
    return userModelsCollection;
  }

  //获取数据
  private void getUserList() {
    Intent mIntent = mNoviceRecyclerView.getViewIntent();
    UserModelNT userModel = mIntent.getParcelableExtra(NOVICE_LIST_ACTIVITY_KEY);
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

  @Subscribe(threadMode = ThreadMode.POSTING)
  public void onMessage(MessageEvent event) {//此时收到ViewPagar视图返回的当前页序号，保存该序号便可存储用户操作记录
    if(event.getEventType() != MessageEvent.EVENT_TYPE.FROM_VIEWPAGE &&
            event.getEventType() != MessageEvent.EVENT_TYPE.FROM_DETAILVIEW)return;
    int currentIndex = event.getCurrentIndex();
    mNoviceRecyclerView.setCurrentItemBackGround(currentIndex);
    ALog.Log1(TAG+"_onMessage_currentIndex: "+currentIndex);
  }

  /**
   * updateUserEntityNT：将用户最终操作的二级目录信息更新到数据库一级数据表中，此时待更新的数据内容为条目序号。
   * @param mUserModelNT
   */
  public void updateUserEntityNT(UserModelNT mUserModelNT){
    ALog.Log(TAG+"_NoviceListPresenter_updateUserEntityNT: "+mUserModelNT.toString());
    this.mParams = new GetUserNTList.Params();
    this.mParams.setDataType(Params.DataType.COLLECTION_DATA_LEVEL1);
    this.mParams.setTableName(ROOT_XMLFILE_NAME.replace(".xml",""));
    this.mParams.setKey(mUserModelNT.getKey());
    mGetUserNTDetails.execute(new UpdateUserNTObserver(), mUserModelDataNTMapper.transform(mUserModelNT), this.mParams);
  }

  private final class UpdateUserNTObserver extends DefaultObserver<UserNT> {
    private static final String TAG = "UpdateUserNTObserver";
    @Override public void onNext(UserNT mUserNT) {
      // no-op by default.
      if(null != mUserNT){
        ALog.Log(TAG+"_onNext: "+mUserNT.getIndex());
        returnCurrentIndexToCaller(mUserNT.getIndex());
      }
    }

    @Override public void onComplete() {
      // no-op by default.
      ALog.Log(TAG+"_onComplete");
    }

    @Override public void onError(Throwable exception) {
      // no-op by default.
      ALog.Log(TAG+"_onError");
    }
  }

  /**
   * returnCurrentIndexToCaller：NoviceListFragment退出前向上级视图返回当前显示条目的序号
   * @param currentIndex
   */
  public void returnCurrentIndexToCaller(int currentIndex){
    MessageEvent mMessageEvent = new MessageEvent();
    mMessageEvent.setEventType(MessageEvent.EVENT_TYPE.FROM_LISTVIEW);
    mMessageEvent.setCurrentIndex(currentIndex);
    EventBus.getDefault().post(mMessageEvent);
  }
}
