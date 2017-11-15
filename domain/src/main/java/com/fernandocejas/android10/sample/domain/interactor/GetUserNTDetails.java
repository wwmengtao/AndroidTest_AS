/**
 * Copyright (C) 2015 Fernando Cejas Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fernandocejas.android10.sample.domain.interactor;

import com.fernandocejas.android10.sample.domain.User;
import com.fernandocejas.android10.sample.domain.UserNT;
import com.fernandocejas.android10.sample.domain.executor.PostExecutionThread;
import com.fernandocejas.android10.sample.domain.executor.ThreadExecutor;
import com.fernandocejas.android10.sample.domain.repository.UserRepository;
import com.fernandocejas.arrow.checks.Preconditions;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * This class is an implementation of {@link UseCase} that represents a use case for
 * retrieving data related to an specific {@link User}.
 */
public class GetUserNTDetails extends UseCase<UserNT, GetUserNTList.Params> {

  private final UserRepository userRepository;

  @Inject
  GetUserNTDetails(UserRepository userRepository, ThreadExecutor threadExecutor,
                   PostExecutionThread postExecutionThread) {
    super(threadExecutor, postExecutionThread);
    this.userRepository = userRepository;
  }

  @Override Observable<UserNT> buildUseCaseObservable(GetUserNTList.Params params) {
    Preconditions.checkNotNull(params);
    System.out.println("buildUseCaseObservable");
    return this.userRepository.user(params);
  }

  @Override Observable<UserNT> buildUseCaseObservable_Update(UserNT mUserNT, GetUserNTList.Params params) {
    Preconditions.checkNotNull(params);
    System.out.println("GetUserNTDetails_buildUseCaseObservable_Update");
    return this.userRepository.updateUserNT(mUserNT, params);
  }

}
