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
package com.fernandocejas.android10.sample.data.entity.mapper;

import com.fernandocejas.android10.sample.data.database.DataManager;
import com.fernandocejas.android10.sample.data.entity.UserEntity;
import com.fernandocejas.android10.sample.data.entity.UserEntityNT;
import com.fernandocejas.android10.sample.domain.User;
import com.fernandocejas.android10.sample.domain.interactor.GetUserListDetails;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * 将Xml中内容解析到其他数据文件中
 * Mapper class used to transform {@link UserEntity} (in the data layer) to {@link User} in the
 * domain layer.
 */
@Singleton
public class UserEntityXmlMapper {
  private final DataManager mDataManager;

  @Inject
  public UserEntityXmlMapper(DataManager mDataManager) {
    this.mDataManager = mDataManager;
  }

  public List<UserEntityNT> transformUserEntityCollection(GetUserListDetails.Params params){
    return (List<UserEntityNT>)mDataManager.UserEntityCollectionXml(params);
  }

  public UserEntityNT transformUserEntity(GetUserListDetails.Params params){
    return mDataManager.UserEntityXml(params);
  }
}
