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
package com.mt.myapplication.novicetutorial.com.fernandocejas.android10.sample.presentation.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Class that represents a user in the presentation layer.
 */
public class UserModelNT implements Parcelable {
  private String key = null;//key代表玩家教程一二级标题或者三级标题详情标识，代表xml文件名称或者标题
  private String adjunction = null;//代表标题涵盖的内容
  private String pic = null;//背景图或者其他待显示图片
  private int index = -1;

  public UserModelNT(){}
  public UserModelNT(String key) {
    this.key = key;
  }

  public String getKey() {
    return key;
  }
  public void setKey(String key) {
    this.key = key;
  }

  public String getAdjunction() {
    return adjunction;
  }
  public void setAdjunction(String adjunction) {
    this.adjunction = adjunction;
  }

  public String getPic() {
    return pic;
  }
  public void setPic(String pic) {
    this.pic = pic;
  }

  public int getIndex() {
    return index;
  }
  public void setIndex(int index) {
    this.index = index;
  }

  public String toString(){
    String str =
      "/----------------UserModelNT.toString----------------/" + "\n" +
      "key: "+key + "\n" +
      "adjunction: "+adjunction + "\n" +
      "pic: "+pic + "\n" +
      "index: "+index + "\n" +
      "/----------------UserModelNT.toString----------------/" + "\n";
    return str;
  }

// 1.必须实现Parcelable.Creator接口,否则在获取Person数据的时候，会报错，如下：
// android.os.BadParcelableException:
// Parcelable protocol requires a Parcelable.Creator object called  CREATOR on class com.um.demo.Person
// 2.这个接口实现了从Percel容器读取Person数据，并返回Person对象给逻辑层使用
// 3.实现Parcelable.Creator接口对象名必须为CREATOR，不如同样会报错上面所提到的错；
// 4.在读取Parcel容器里的数据事，必须按成员变量声明的顺序读取数据，不然会出现获取数据出错
// 5.反序列化对象
  public static final Parcelable.Creator<UserModelNT> CREATOR = new Creator(){

    @Override
    public UserModelNT createFromParcel(Parcel source) {
      // TODO Auto-generated method stub
      // 必须按成员变量声明的顺序读取数据，不然会出现获取数据出错
      UserModelNT mUserModelNT = new UserModelNT();
      mUserModelNT.setKey(source.readString());
      mUserModelNT.setAdjunction(source.readString());
      mUserModelNT.setPic(source.readString());
      mUserModelNT.setIndex(source.readInt());
      return mUserModelNT;
    }

    @Override
    public UserModelNT[] newArray(int size) {
      // TODO Auto-generated method stub
      return new UserModelNT[size];
    }
  };

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    /*将UserModelNT的成员写入Parcel:
    * 注：Parcel中的数据是按顺序写入和读取的，即先被写入的就会先被读取出来
    */
    dest.writeString(key);
    dest.writeString(adjunction);
    dest.writeString(pic);
    dest.writeInt(index);
  }

  @Override
  public int describeContents() {
    return 0;
  }

}
