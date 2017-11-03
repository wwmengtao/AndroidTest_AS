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
package com.fernandocejas.android10.sample.data.repository.datasource;

import com.fernandocejas.android10.sample.data.database.DbCache;
import com.fernandocejas.android10.sample.data.database.xmlOps.ParseXml;
import com.fernandocejas.android10.sample.data.entity.UserEntity;
import com.fernandocejas.android10.sample.data.entity.UserEntityNT;
import com.fernandocejas.android10.sample.domain.interactor.GetUserListDetails;

import java.util.List;

import io.reactivex.Observable;

/**
 * {@link UserDataStore} implementation based on connections to the api (Cloud).
 */
class DBUserDataStore implements UserDataStore {

  private final ParseXml parseXml;
  private final DbCache dbCache;

  /**
   * Construct a {@link UserDataStore} based on connections to the api (Cloud).
   *
   * @param parseXml The {@link ParseXml} implementation to use.
   * @param dbCache A {@link DbCache} to cache data retrieved from the data base.
   */
  DBUserDataStore(ParseXml parseXml, DbCache dbCache) {
    this.parseXml = parseXml;
    this.dbCache = dbCache;
  }

  @Override
  public Observable<List<UserEntity>> userEntityList() {
    return null;
  }

  @Override public Observable<List<UserEntityNT>> userEntityNTList(GetUserListDetails.Params params) {
    if(dbCache.isCached(params)){
      return this.dbCache.getCollection(params);
    }
    return this.parseXml.userEntityNTList(params,dbCache);
//    return this.parseXml.userEntityList(params).doOnNext(DBUserDataStore.this.dbCache::put);
  }

  @Override public Observable<UserEntity> userEntityDetails(final int userId) {
    return null;
  }

  @Override
  public Observable<UserEntityNT> userEntityNTDetails(GetUserListDetails.Params params) {
    if(null == params)return null;
    return this.dbCache.get(params);
  }
}
