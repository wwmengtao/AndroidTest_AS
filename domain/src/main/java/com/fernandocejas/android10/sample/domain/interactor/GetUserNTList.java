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

import com.fernandocejas.android10.sample.domain.UserNT;
import com.fernandocejas.android10.sample.domain.executor.PostExecutionThread;
import com.fernandocejas.android10.sample.domain.executor.ThreadExecutor;
import com.fernandocejas.android10.sample.domain.repository.UserRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * This class is an implementation of {@link UseCase} that represents a use case for
 * retrieving a collection of all {@link UserNT}.
 */
public class GetUserNTList extends UseCase<List<UserNT>, GetUserNTList.Params> {

  private final UserRepository userRepository;

  @Inject
  GetUserNTList(UserRepository userRepository, ThreadExecutor threadExecutor,
                     PostExecutionThread postExecutionThread) {
    super(threadExecutor, postExecutionThread);
    this.userRepository = userRepository;
  }

  @Override Observable<List<UserNT>> buildUseCaseObservable(GetUserNTList.Params params) {
    System.out.println("buildUseCaseObservable");
    return this.userRepository.users(params);
  }

  public static final class Params {
    public static enum DataType{
      COLLECTION_DATA_LEVEL1,//玩家教程一级标题数据
      COLLECTION_DATA_LEVEL2,//玩家教程二级标题数据
      SINGLE_DATA//玩家教程二级标题数据
    }
    private DataType mDataType = null;
    private String fileName = null;//xml文件名称
    private String tableName = null;//对应数据库的数据表
    private String key = null;//对应数据表中的主键，适用于单个数据

    public Params(){

    }

    public Params(DataType mDataType, String fileName, String key) {
      this.mDataType = mDataType;
      this.fileName = fileName;
      this.tableName = fileName.replace(".xml","");
      this.key = key;
    }

    public void setDataType(DataType mDataType){
      this.mDataType = mDataType;
    }
    public DataType getDataType(){
      return mDataType;
    }

    public void setFileName(String fileName){
      this.fileName = fileName;
    }
    public String getFileName(){
      return fileName;
    }

    public void setTableName(String tableName){
      this.tableName = tableName;
    }
    public String getTableName(){
      return tableName;
    }

    public void setKey(String key){
      this.key = key;
    }
    public String getKey(){
      return key;
    }

    public String toString(){
      String str =
        "/----------------GetUserNTList.Params.toString----------------/" + "\n" +
        "mDataType: "+mDataType + "\n" +
        "fileName: "+fileName + "\n" +
        "tableName: "+tableName + "\n" +
        "key: "+key + "\n" +
        "/----------------GetUserNTList.Params.toString----------------/";
      return str;
    }
  }
}
