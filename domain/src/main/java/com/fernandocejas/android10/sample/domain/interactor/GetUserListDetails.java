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

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * This class is an implementation of {@link UseCase} that represents a use case for
 * retrieving a collection of all {@link User}.
 */
public class GetUserListDetails extends UseCase<List<UserNT>, GetUserListDetails.Params> {

  private final UserRepository userRepository;

  @Inject
  GetUserListDetails(UserRepository userRepository, ThreadExecutor threadExecutor,
                     PostExecutionThread postExecutionThread) {
    super(threadExecutor, postExecutionThread);
    this.userRepository = userRepository;
  }

  @Override Observable<List<UserNT>> buildUseCaseObservable(GetUserListDetails.Params params) {
    System.out.println("buildUseCaseObservable");
    return this.userRepository.users(params);
  }

  public static final class Params {
    public static enum DataType{
      COLLECTION_DATA_LEVEL1,//玩家教程一级标题数据
      COLLECTION_DATA_LEVEL2,//玩家教程二级标题数据
      SINGLE_DATA//玩家教程二级标题数据
    }
    private DataType mDataType;
    private String fileName;//xml文件名称
    private String tableName;//对应数据库的数据表
    private String key;//对应数据表中的主键，适用于单个数据
    public Params(DataType mDataType, String fileName, String key) {
      this.mDataType = mDataType;
      this.fileName = fileName;
      this.tableName = fileName.replace(".xml","");
      this.key = key;
    }
    public DataType getDataType(){
      return mDataType;
    }
    public String getFileName(){
      return fileName;
    }
    public String getTableName(){
      return tableName;
    }
    public String getKey(){
      return key;
    }
  }
}
