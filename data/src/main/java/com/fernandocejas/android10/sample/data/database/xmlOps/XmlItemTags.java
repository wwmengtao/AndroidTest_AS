package com.fernandocejas.android10.sample.data.database.xmlOps;

import com.fernandocejas.android10.sample.data.entity.UserEntityNT;

import java.io.File;

import static com.fernandocejas.android10.sample.data.database.xmlOps.XmlOperator.NoviceAssetsPicDir;

/**
 * XmlItemTags：用于记录不同种类xml文件的各级标签名称
 * Created by mengtao1 on 2017/11/3.
 */

public class XmlItemTags {
    public static final class LEVEL1_ITEM_TAGS{
        public static final String ROOT_ELEMENT_TAG = "file";//文件根标签
        public static final String FIRST_ELEMENT_TAG = "item";//文件第一标签
        private static String[] FIRST_ELEMENT_TAGS={"filename","filestring","filebackpic"};//文件第一标签包含的子标签

        public static void setTagValue(UserEntityNT mUserEntityNT, String tag, String value) {
            if(null == value)return;
            if(tag.equals(FIRST_ELEMENT_TAGS[0])){
                mUserEntityNT.setKey(value);
            }else if(tag.equals(FIRST_ELEMENT_TAGS[1])){
                mUserEntityNT.setAdjunction(value);
            }else if(tag.equals(FIRST_ELEMENT_TAGS[2])){
                mUserEntityNT.setPic(NoviceAssetsPicDir+ File.separator+value);
            }
        }
    }

    public static final class LEVEL2_ITEM_TAGS{
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
                mUserEntityNT.setPic(NoviceAssetsPicDir+ File.separator+value);
            }
        }
    }

    /**
     * isFirstElementTag：判断标签tag是否是FIRST_ELEMENT_TAGS中的元素
     * @param tag
     * @param FIRST_ELEMENT_TAGS
     * @return
     */
    private boolean isFirstElementTag(String tag,String[] FIRST_ELEMENT_TAGS){
        if(null == tag||null == FIRST_ELEMENT_TAGS)return false;
        for(String eleTag:FIRST_ELEMENT_TAGS){
            if(eleTag.equals(tag)){
                return true;
            }
        }
        return false;
    }
}
