package com.fernandocejas.android10.sample.data.database.xmlOps;

import com.fernandocejas.android10.sample.data.entity.UserEntityNT;

import java.io.File;

import static com.fernandocejas.android10.sample.data.database.xmlOps.XmlOperator.NoviceAssetsPicDir;

/**
 * XmlItemTags：用于记录不同种类xml文件的各级标签名称以及存储这些内容的操作
 * Created by mengtao1 on 2017/11/3.
 */

public class XmlItemTags {
    public static final class LEVEL1_ITEM_TAGS {//一级界面xml文件标签
        public static final String ROOT_FILE_NAME = "xmlfiles";//文件名称，可用作数据库的数据表名称
        public static final String ROOT_ELEMENT_TAG = "file";//文件根标签
        public static final String FIRST_ELEMENT_TAG = "item";//文件第一标签
        public static final String[] FIRST_ELEMENT_TAGS = {"filename", "filestring", "filebackpic"};//文件第一标签包含的子标签

        public static void setTagValue(UserEntityNT mUserEntityNT, String tag, String value) {
            if (null == value) return;
            if (tag.equals(FIRST_ELEMENT_TAGS[0])) {
                mUserEntityNT.setKey(value);
            } else if (tag.equals(FIRST_ELEMENT_TAGS[1])) {
                mUserEntityNT.setAdjunction(value);
            } else if (tag.equals(FIRST_ELEMENT_TAGS[2])) {
                mUserEntityNT.setPic(NoviceAssetsPicDir + File.separator + value);//图片存储在assets文件夹中
            }
        }

        //记录一级标题下的二级标题数目
        public static void setTagValue(UserEntityNT mUserEntityNT, int sum) {
            mUserEntityNT.setSum(sum);
        }
    }

    public static final class LEVEL2_ITEM_TAGS{//二级界面xml文件标签
        public static final String ROOT_ELEMENT_TAG = "functionkeyscontent";//文件根标签
        public static final String FIRST_ELEMENT_TAG = "item";//文件第一标签
        private static String[] FIRST_ELEMENT_TAGS={"itemtitle","itemcontent","itemcontentimage"};//文件第一标签包含的子标签

        public static void setTagValue(UserEntityNT mUserEntityNT, String tag, String value) {
            if(null == value)return;
            if(tag.equals(FIRST_ELEMENT_TAGS[0])){
                mUserEntityNT.setKey(value);
            }else if(tag.equals(FIRST_ELEMENT_TAGS[1])){
                mUserEntityNT.setAdjunction(value);
            }else if(tag.equals(FIRST_ELEMENT_TAGS[2])){
                mUserEntityNT.setPic(value);//图片存储在res/drawblexxx文件夹中
            }
        }
    }
}
