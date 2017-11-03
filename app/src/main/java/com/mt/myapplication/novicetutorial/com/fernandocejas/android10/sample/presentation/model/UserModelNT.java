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

/**
 * Class that represents a user in the presentation layer.
 */
public class UserModelNT {
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
}
