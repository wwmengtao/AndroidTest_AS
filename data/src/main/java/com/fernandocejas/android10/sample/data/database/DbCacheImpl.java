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
      final List<UserEntityNT> userEntityList = (List<UserEntityNT>)mDataManager.query(params);
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
      final UserEntityNT userEntity = mDataManager.query(params.getKey(), params);
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
    ALog.Log("DbCacheImpl_put_List");
    if (userEntityList != null) {
      if (!isCached(params)) {
        CacheWriter mCacheWriter = new CacheWriter(this.mDataManager, userEntityList, params);
        mCacheWriter.setCacheType(CacheWriter.CACHE_TYPE.PUT);
        this.executeAsynchronously(mCacheWriter);
      }
    }
  }

  @Override public void put(UserEntityNT userEntity, GetUserNTList.Params params) {
    ALog.Log("DbCacheImpl_put");
    if (userEntity != null) {
      if (!isCached(params)) {
        CacheWriter mCacheWriter = new CacheWriter(this.mDataManager, userEntity, params);
        mCacheWriter.setCacheType(CacheWriter.CACHE_TYPE.PUT);
        this.executeAsynchronously(mCacheWriter);
      }
    }
  }

  @Override
  public void update(UserEntityNT userEntity, GetUserNTList.Params params) {
    ALog.Log("DbCacheImpl_update");
    CacheWriter mCacheWriter = new CacheWriter(this.mDataManager, userEntity, params);
    mCacheWriter.setCacheType(CacheWriter.CACHE_TYPE.UPDATE);
    this.executeAsynchronously(mCacheWriter);
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
    public static enum CACHE_TYPE{
      PUT,
      UPDATE
    }
    private final DataManager mDataManager;
    private final GetUserNTList.Params params;
    private UserEntityNT mUserEntityNT;
    private List<UserEntityNT> mUserEntityNTList;
    private CACHE_TYPE mCacheType;
    public CacheWriter(DataManager mDataManager, UserEntityNT userEntity, GetUserNTList.Params params) {
      this.mDataManager = mDataManager;
      this.params = params;
      this.mUserEntityNT = userEntity;
      this.mUserEntityNTList = null;
    }

    public void setCacheType(CACHE_TYPE mCacheType){
      this.mCacheType = mCacheType;
    }

    public CacheWriter(DataManager mDataManager, List<UserEntityNT> userEntityList, GetUserNTList.Params params) {
      this.mDataManager = mDataManager;
      this.params = params;
      this.mUserEntityNTList = userEntityList;
      this.mUserEntityNT = null;
    }

    @Override public void run() {
//      this.fileManager.writeToFile(fileToWrite, fileContent);
      if(null == this.mUserEntityNTList) {
        if (mCacheType == CACHE_TYPE.PUT) {
          this.mDataManager.put(this.mUserEntityNT, params);
        } else if (mCacheType == CACHE_TYPE.UPDATE) {
          this.mDataManager.update(this.mUserEntityNT, params);
        }
      }else{
          if(mCacheType == CACHE_TYPE.PUT){
            this.mDataManager.put(this.mUserEntityNTList, params);
          }
        }
    }
  }

}
