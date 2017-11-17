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
package com.fernandocejas.android10.sample.data.repository.datasource;

import android.content.Context;
import android.support.annotation.NonNull;

import com.fernandocejas.android10.sample.data.ALog;
import com.fernandocejas.android10.sample.data.cache.UserCache;
import com.fernandocejas.android10.sample.data.database.DataManager;
import com.fernandocejas.android10.sample.data.database.DbCache;
import com.fernandocejas.android10.sample.data.database.xmlOps.ParseXml;
import com.fernandocejas.android10.sample.data.database.xmlOps.ParseXmlImpl;
import com.fernandocejas.android10.sample.data.entity.mapper.UserEntityJsonMapper;
import com.fernandocejas.android10.sample.data.entity.mapper.UserEntityXmlMapper;
import com.fernandocejas.android10.sample.data.net.RestApi;
import com.fernandocejas.android10.sample.data.net.RestApiImpl;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Factory that creates different implementations of {@link UserDataStore}.
 */
@Singleton
public class UserDataStoreFactory {

  private final Context context;
  private final UserCache userCache;
  private final DbCache dbCache;
  @Inject
  UserDataStoreFactory(@NonNull Context context, @NonNull UserCache userCache, @NonNull DbCache dbCache) {
    this.context = context.getApplicationContext();
    this.userCache = userCache;
    this.dbCache = dbCache;
  }

  /**
   * Create {@link UserDataStore} from a user id.
   */
  public UserDataStore create(int userId) {
    UserDataStore userDataStore;

    if (!this.userCache.isExpired() && this.userCache.isCached(userId)) {
      userDataStore = new DiskUserDataStore(this.userCache);
    } else {
      userDataStore = createCloudDataStore();
    }

    return userDataStore;
  }

  /**
   * Create {@link UserDataStore} to retrieve data from the Cloud.
   */
  public UserDataStore createCloudDataStore() {
    final UserEntityJsonMapper userEntityJsonMapper = new UserEntityJsonMapper();
    final RestApi restApi = new RestApiImpl(this.context, userEntityJsonMapper);

    return new CloudUserDataStore(restApi, this.userCache);
  }

  //解析玩家教程数据使用这个
  public UserDataStore createDBDataStore() {
    ALog.Log("createDBDataStore");
    final UserEntityXmlMapper mUserEntityXmlMapper = new UserEntityXmlMapper(DataManager.get(context));
    final ParseXml mParseXml = new ParseXmlImpl(this.context, mUserEntityXmlMapper);
    return new DBUserDataStore(mParseXml, this.dbCache);
  }
}
