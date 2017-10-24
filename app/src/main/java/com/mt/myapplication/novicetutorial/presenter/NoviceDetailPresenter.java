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

import com.mt.myapplication.novicetutorial.model.UserModel;
import com.mt.myapplication.novicetutorial.model.domain.User;
import com.mt.myapplication.novicetutorial.model.domain.exception.DefaultErrorBundle;
import com.mt.myapplication.novicetutorial.model.domain.exception.ErrorBundle;
import com.mt.myapplication.novicetutorial.model.domain.exception.ErrorMessageFactory;
import com.mt.myapplication.novicetutorial.model.domain.interactor.DefaultObserver;
import com.mt.myapplication.novicetutorial.model.domain.interactor.GetUserDetails;
import com.mt.myapplication.novicetutorial.model.domain.interactor.GetUserDetails.Params;
import com.mt.myapplication.novicetutorial.model.mapper.UserModelDataMapper;
import com.mt.myapplication.novicetutorial.view.interfaces.NoviceDetailView;

import javax.inject.Inject;

/**
 * {@link Presenter} that controls communication between views and models of the presentation
 * layer.
 */
public class NoviceDetailsPresenter implements Presenter {

    private NoviceDetailView mNoviceDetailView;

    private final GetUserDetails getUserDetailsUseCase;
    private final UserModelDataMapper userModelDataMapper;

    @Inject
    public NoviceDetailsPresenter(GetUserDetails getUserDetailsUseCase,
                                UserModelDataMapper userModelDataMapper) {
        this.getUserDetailsUseCase = getUserDetailsUseCase;
        this.userModelDataMapper = userModelDataMapper;
    }

    public void setView(@NonNull NoviceDetailView view) {
        this.mNoviceDetailView = view;
    }

    @Override public void resume() {}

    @Override public void pause() {}

    @Override public void destroy() {
        this.getUserDetailsUseCase.dispose();
        this.mNoviceDetailView = null;
    }

    /**
     * Initializes the presenter by showing/hiding proper views
     * and retrieving user details.
     */
    public void initialize(int userId) {
        this.hideViewRetry();
        this.showViewLoading();
        this.getUserDetails(userId);
    }

    private void getUserDetails(int userId) {
        this.getUserDetailsUseCase.execute(new UserDetailsObserver(), Params.forUser(userId));
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

    private void showUserDetailsInView(User user) {
        final UserModel userModel = this.userModelDataMapper.transform(user);
        this.mNoviceDetailView.showUser(userModel);
    }

    private final class UserDetailsObserver extends DefaultObserver<User> {

        @Override public void onComplete() {
            NoviceDetailsPresenter.this.hideViewLoading();
        }

        @Override public void onError(Throwable e) {
            NoviceDetailsPresenter.this.hideViewLoading();
            NoviceDetailsPresenter.this.showErrorMessage(new DefaultErrorBundle((Exception) e));
            NoviceDetailsPresenter.this.showViewRetry();
        }

        @Override public void onNext(User user) {
            NoviceDetailsPresenter.this.showUserDetailsInView(user);
        }
    }
}
