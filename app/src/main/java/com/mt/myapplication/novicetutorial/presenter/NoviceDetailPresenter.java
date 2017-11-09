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
import com.fernandocejas.android10.sample.domain.interactor.GetUserNTDetails;
import com.fernandocejas.android10.sample.domain.interactor.GetUserNTList;
import com.mt.androidtest_as.alog.ALog;
import com.mt.myapplication.novicetutorial.com.fernandocejas.android10.sample.presentation.exception.ErrorMessageFactory;
import com.mt.myapplication.novicetutorial.com.fernandocejas.android10.sample.presentation.mapper.UserModelDataNTMapper;
import com.mt.myapplication.novicetutorial.com.fernandocejas.android10.sample.presentation.model.UserModelNT;
import com.mt.myapplication.novicetutorial.view.interfaces.NoviceDetailView;

import javax.inject.Inject;

import static com.mt.myapplication.novicetutorial.view.activities.NoviceListActivity.NOVICE_LIST_ACTIVITY_TABLE;

/**
 * {@link Presenter} that controls communication between views and models of the presentation
 * layer.
 */
public class NoviceDetailPresenter implements Presenter {

    private NoviceDetailView mNoviceDetailView;

    private final GetUserNTDetails mGetUserNTDetails;
    private final UserModelDataNTMapper mUserModelDataNTMapper;
    private GetUserNTList.Params mParams;
    @Inject
    public NoviceDetailPresenter(GetUserNTDetails mGetUserNTDetails,
                                 UserModelDataNTMapper mUserModelDataNTMapper) {
        this.mGetUserNTDetails = mGetUserNTDetails;
        this.mUserModelDataNTMapper = mUserModelDataNTMapper;
    }

    public void setView(@NonNull NoviceDetailView view) {
        this.mNoviceDetailView = view;
    }

    @Override public void resume() {}

    @Override public void pause() {}

    @Override public void destroy() {
        this.mGetUserNTDetails.dispose();
        this.mNoviceDetailView = null;
    }

    /**
     * Initializes the presenter by showing/hiding proper views
     * and retrieving user details.
     */
    public void initialize(UserModelNT userModel) {
        this.hideViewRetry();
        this.showViewLoading();
        this.getUserDetails(userModel);
    }

    private void showViewLoading() {
        this.mNoviceDetailView.showLoading();
    }

    private void hideViewLoading() {
        this.mNoviceDetailView.hideLoading();
    }

    private void showViewRetry() {
        this.mNoviceDetailView.showRetry();
    }

    private void hideViewRetry() {
        this.mNoviceDetailView.hideRetry();
    }

    private void showErrorMessage(ErrorBundle errorBundle) {
        String errorMessage = ErrorMessageFactory.create(this.mNoviceDetailView.context(),
                errorBundle.getException());
        this.mNoviceDetailView.showError(errorMessage);
    }

    private void showUserDetailsInView(UserNT user) {
        final UserModelNT userModel = this.mUserModelDataNTMapper.transform(user);
        this.mNoviceDetailView.showUser(userModel);
    }

    /**
     * getUserDetails：此函数用于展示RX2从数据库中获取单个数据的方法，其实可以直接用userModel显示内容
     * @param userModel
     */
    private void getUserDetails(UserModelNT userModel) {
        ALog.Log("getUserDetails_userModel: "+userModel.toString());
        Intent mIntent = mNoviceDetailView.getViewIntent();
        String tableName = mIntent.getStringExtra(NOVICE_LIST_ACTIVITY_TABLE);
        ALog.Log("getUserDetails_userModel_tableName: "+tableName);
        mParams = new GetUserNTList.Params();
        mParams.setDataType(GetUserNTList.Params.DataType.SINGLE_DATA);
        mParams.setTableName(tableName);
        mParams.setKey(userModel.getKey());
        this.mGetUserNTDetails.execute(new UserDetailsObserver(), mParams);
    }

    private final class UserDetailsObserver extends DefaultObserver<UserNT> {

        @Override public void onComplete() {
            NoviceDetailPresenter.this.hideViewLoading();
        }

        @Override public void onError(Throwable e) {
            NoviceDetailPresenter.this.hideViewLoading();
            NoviceDetailPresenter.this.showErrorMessage(new DefaultErrorBundle((Exception) e));
            NoviceDetailPresenter.this.showViewRetry();
        }

        @Override public void onNext(UserNT user) {
            NoviceDetailPresenter.this.showUserDetailsInView(user);
        }
    }
}
