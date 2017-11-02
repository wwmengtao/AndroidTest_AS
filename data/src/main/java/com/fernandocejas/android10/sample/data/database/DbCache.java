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
package com.fernandocejas.android10.sample.data.database;

import com.fernandocejas.android10.sample.data.entity.UserEntity;
import com.fernandocejas.android10.sample.domain.interactor.GetUserListDetails;

import java.util.List;

import io.reactivex.Observable;

/**
 * An interface representing a user Cache.
 */
public interface DbCache {
  Observable<UserEntity> get(final GetUserListDetails.Params params);
  Observable<List<UserEntity>> getCollection(final GetUserListDetails.Params params);

  void put(UserEntity userEntity,GetUserListDetails.Params params);
  void put(List<UserEntity> userEntityList,GetUserListDetails.Params params);

  boolean isCached(final GetUserListDetails.Params params);

  boolean isExpired();
  void evictAll();
}
