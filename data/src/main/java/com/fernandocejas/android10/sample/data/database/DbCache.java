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

import com.fernandocejas.android10.sample.data.entity.UserEntityNT;
import com.fernandocejas.android10.sample.domain.interactor.GetUserNTList;

import java.util.List;

import io.reactivex.Observable;

/**
 * An interface representing a user Cache.
 */
public interface DbCache {
  Observable<UserEntityNT> get(final GetUserNTList.Params params);
  Observable<List<UserEntityNT>> getCollection(final GetUserNTList.Params params);
  void update(UserEntityNT userEntity,GetUserNTList.Params params);
  void put(UserEntityNT userEntity,GetUserNTList.Params params);
  void put(List<UserEntityNT> userEntityList,GetUserNTList.Params params);

  boolean isCached(final GetUserNTList.Params params);

  boolean isExpired();
  void evictAll();
}
