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
package com.fernandocejas.android10.sample.data.entity;


/**
 * UserEntityNT used in the data layer.
 */
public class UserEntityNT {
    private static final String TO_STRING = "/----------------UserEntityNT.toString----------------/";
    private String key = null;//key代表玩家教程一二级标题或者三级标题详情标识，代表xml文件名称或者标题
    private String adj = null;//代表标题涵盖的内容
    private String pic = null;//背景图或者其他待显示图片
    private int num = -1;
    private int sum = -1;//统计一级标题下的二级标题数目

    public UserEntityNT(){}
    public UserEntityNT(String key) {
    this.key = key;
    }

    public String getKey() {
    return key;
    }
    public void setKey(String key) {
    this.key = key;
    }

    public String getAdjunction() {
        return adj;
    }
    public void setAdjunction(String adjunction) {
        this.adj = adjunction;
    }

    public String getPic() {
        return pic;
    }
    public void setPic(String pic) {
    this.pic = pic;
    }

    public int getNumber() {
    return num;
    }
    public void setNumber(int index) {
    this.num = index;
    }

    public int getSum(){
        return sum;
    }
    public void setSum(int sum){
        this.sum = sum;
    }

//    public String toString(){
//        String str =
//        TO_STRING + "\n" +
//        "key: "+key + "\n" +
//        "adj: "+adj + "\n" +
//        "pic: "+pic + "\n" +
//        "num: "+num + "\n" +
//        "sum: "+sum + "\n" +
//        TO_STRING + "\n";
//        return str;
//    }
@Override
public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserEntityNT {\n");
    sb.append("  key: ").append(key).append("\n");
    sb.append("  adj: ").append(adj).append("\n");
    sb.append("  pic: ").append(pic).append("\n");
    sb.append("  num: ").append(num).append("\n");
    sb.append("  sum: ").append(sum).append("\n");
    sb.append("}\n");
    return sb.toString();
}
}
