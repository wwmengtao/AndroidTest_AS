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
package com.fernandocejas.android10.sample.data.database.xmlOps;

import android.content.Context;
import android.support.annotation.NonNull;

import com.fernandocejas.android10.sample.data.ALog;
import com.fernandocejas.android10.sample.data.database.DbCache;
import com.fernandocejas.android10.sample.data.entity.UserEntityNT;
import com.fernandocejas.android10.sample.data.entity.mapper.UserEntityXmlMapper;
import com.fernandocejas.android10.sample.data.exception.NetworkConnectionException;
import com.fernandocejas.android10.sample.domain.interactor.GetUserListDetails;

import java.util.List;

import io.reactivex.Observable;

/**
 * {@link ParseXml} implementation for retrieving data from the xml files.
 */
public class ParseXmlImpl implements ParseXml {

  private final Context context;
  private final UserEntityXmlMapper mUserEntityXmlMapper;

  /**
   * Constructor of the class
   *
   * @param context {@link Context}.
   * @param userEntityXmlMapper {@link UserEntityXmlMapper}.
   */
  public ParseXmlImpl(Context context, UserEntityXmlMapper userEntityXmlMapper) {
    if (context == null || userEntityXmlMapper == null) {
      throw new IllegalArgumentException("The constructor parameters cannot be null!!!");
    }
    this.context = context.getApplicationContext();
    this.mUserEntityXmlMapper = userEntityXmlMapper;
  }

//    下列JAVA1.7及以下版本语法方式可以简写为JAVA1.8版本的e->{}方式
//    Observable.create(new ObservableOnSubscribe<Integer>() {
//      @Override
//      public void subscribe(ObservableEmitter<Integer> e) throws Exception {
//        e.onNext(1);
//        e.onNext(2);
//        e.onNext(3);
//        e.onNext(4);
//        e.onComplete();
//      }
//    });

  @Override
  public Observable<List<UserEntityNT>> userEntityNTList(@NonNull GetUserListDetails.Params params, DbCache dbCache) {
    if(null == params)return null;
    ALog.Log("ParseXmlImpl_userEntityNTList");
    String key = params.getFileName();
    List<UserEntityNT> mUserEntityList = mUserEntityXmlMapper.transformUserEntityCollection(params);
    dbCache.put(mUserEntityList, params);//首先将xml文件解析的数据保存在数据库中
    return Observable.create(emitter -> {
      try {
        if (key != null) {
          emitter.onNext(mUserEntityList);
          emitter.onComplete();
        } else {
          emitter.onError(new NetworkConnectionException());
        }
      } catch (Exception e) {
        emitter.onError(new NetworkConnectionException(e.getCause()));
      }
    });
  }

  @Override
  /**
   * 解析xml中单一数据这个功能暂时不用
   */
  public Observable<UserEntityNT> userEntityNT(GetUserListDetails.Params params, DbCache dbCache) {
    if(null == params)return null;
    String key = params.getKey();
    return Observable.create(emitter -> {
      try {
        if (key != null) {
          emitter.onNext(mUserEntityXmlMapper.transformUserEntity(params));
          emitter.onComplete();
        } else {
          emitter.onError(new NetworkConnectionException());
        }
      } catch (Exception e) {
        emitter.onError(new NetworkConnectionException(e.getCause()));
      }
    });
  }
}
