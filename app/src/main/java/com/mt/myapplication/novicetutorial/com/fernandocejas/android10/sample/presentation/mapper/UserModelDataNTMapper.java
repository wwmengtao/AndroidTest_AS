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
package com.mt.myapplication.novicetutorial.com.fernandocejas.android10.sample.presentation.mapper;

import com.fernandocejas.android10.sample.domain.User;
import com.fernandocejas.android10.sample.domain.UserNT;
import com.mt.myapplication.novicetutorial.com.fernandocejas.android10.sample.presentation.di.PerActivity;
import com.mt.myapplication.novicetutorial.com.fernandocejas.android10.sample.presentation.model.UserModel;
import com.mt.myapplication.novicetutorial.com.fernandocejas.android10.sample.presentation.model.UserModelNT;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import javax.inject.Inject;

/**
 * Mapper class used to transform {@link User} (in the domain layer) to {@link UserModel} in the
 * presentation layer.
 */
@PerActivity
public class UserModelDataNTMapper {

  @Inject
  public UserModelDataNTMapper() {}

  /**
   * Transform a {@link UserNT} into an {@link UserModelNT}.
   *
   * @param userNT Object to be transformed.
   * @return {@link UserModelNT}.
   */
  public UserModelNT transform(UserNT userNT) {
    if (userNT == null) {
      throw new IllegalArgumentException("Cannot transform a null value");
    }
    final UserModelNT userModel = new UserModelNT();
    userModel.setKey(userNT.getKey());
    userModel.setAdjunction(userNT.getAdjunction());
    userModel.setPic(userNT.getPic());
    userModel.setIndex(userNT.getIndex());
    return userModel;
  }

  /**
   * Transform a Collection of {@link User} into a Collection of {@link UserModel}.
   *
   * @param usersCollection Objects to be transformed.
   * @return List of {@link UserModel}.
   */
  public Collection<UserModelNT> transform(Collection<UserNT> usersCollection) {
    Collection<UserModelNT> userModelsCollection;

    if (usersCollection != null && !usersCollection.isEmpty()) {
      userModelsCollection = new ArrayList<>();
      for (UserNT user : usersCollection) {
        userModelsCollection.add(transform(user));
      }
    } else {
      userModelsCollection = Collections.emptyList();
    }

    return userModelsCollection;
  }
}
