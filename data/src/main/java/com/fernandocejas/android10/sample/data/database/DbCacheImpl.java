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
package com.fernandocejas.android10.sample.data.database;

import android.content.Context;

import com.fernandocejas.android10.sample.data.ALog;
import com.fernandocejas.android10.sample.data.cache.UserCache;
import com.fernandocejas.android10.sample.data.entity.UserEntityNT;
import com.fernandocejas.android10.sample.data.exception.UserNotFoundException;
import com.fernandocejas.android10.sample.domain.executor.ThreadExecutor;
import com.fernandocejas.android10.sample.domain.interactor.GetUserNTList;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

/**
 * {@link UserCache} implementation.
 */
@Singleton
public class DbCacheImpl implements DbCache {

  private final Context context;
  private final DataManager mDataManager;
  private final ThreadExecutor threadExecutor;

  @Inject
  DbCacheImpl(Context context, ThreadExecutor executor) {
    if (context == null || executor == null) {
      throw new IllegalArgumentException("Invalid null parameter");
    }
    this.context = context.getApplicationContext();
    this.threadExecutor = executor;
    this.mDataManager = DataManager.get(this.context);
  }

  @Override
  public Observable<List<UserEntityNT>> getCollection(final GetUserNTList.Params params) {
    return Observable.create(emitter -> {
      final List<UserEntityNT> userEntityList = (List<UserEntityNT>)mDataManager.getUserEntityCollection(params);
      if (userEntityList != null) {
        emitter.onNext(userEntityList);
        emitter.onComplete();
      } else {
        emitter.onError(new UserNotFoundException());
      }
    });
  }

  @Override
  public Observable<UserEntityNT> get(final GetUserNTList.Params params) {
    return Observable.create(emitter -> {
      final UserEntityNT userEntity = mDataManager.queryUserEntityNT(params.getKey(), params);
      if (userEntity != null) {
        emitter.onNext(userEntity);
        emitter.onComplete();
      } else {
        emitter.onError(new UserNotFoundException());
      }
    });
  }


  @Override
  public void put(List<UserEntityNT> userEntityList, GetUserNTList.Params params) {
    ALog.Log("DbCacheImpl_putUserEntityList");
    if (userEntityList != null) {
      if (!isCached(params)) {
        this.executeAsynchronously(new CacheWriter(this.mDataManager, userEntityList, params));
      }
    }
  }

  @Override public void put(UserEntityNT userEntity, GetUserNTList.Params params) {
    ALog.Log("DbCacheImpl_putUserEntity");
    if (userEntity != null) {
      if (!isCached(params)) {
        this.executeAsynchronously(new CacheWriter(this.mDataManager, userEntity, params));
      }
    }
  }

  @Override public boolean isCached(GetUserNTList.Params params) {
    if(null == params)return false;
    return this.mDataManager.exists(params);
  }

  @Override
  public boolean isExpired() {
    return false;
  }

  @Override
  public void evictAll() {

  }

  /**
   * Executes a {@link Runnable} in another Thread.
   *
   * @param runnable {@link Runnable} to execute
   */
  private void executeAsynchronously(Runnable runnable) {
    this.threadExecutor.execute(runnable);
  }

  /**
   * {@link Runnable} class for writing to disk.
   */
  private static class CacheWriter implements Runnable {
    private final DataManager mDataManager;
    private final GetUserNTList.Params params;
    private UserEntityNT userEntity;
    private List<UserEntityNT> userEntityList;

    public CacheWriter(DataManager mDataManager, UserEntityNT userEntity, GetUserNTList.Params params) {
      this.mDataManager = mDataManager;
      this.params = params;
      this.userEntity = userEntity;
      this.userEntityList = null;
    }

    public CacheWriter(DataManager mDataManager, List<UserEntityNT> userEntityList, GetUserNTList.Params params) {
      this.mDataManager = mDataManager;
      this.params = params;
      this.userEntityList = userEntityList;
      this.userEntity = null;
    }

    @Override public void run() {
//      this.fileManager.writeToFile(fileToWrite, fileContent);
      if(null == this.userEntityList)
        this.mDataManager.put(this.userEntity, params);
      else
        this.mDataManager.put(this.userEntityList, params);
    }
  }

}
