package com.fernandocejas.android10.sample.data.database;

import com.fernandocejas.android10.sample.data.database.xmlOps.XmlItemTags;

/**
 * 数据库架构，主要罗列数据库中数据表的内容
 * Created by Mengtao1 on 2016/12/15.
 */

public class DbSchema {
    public static final class Level1TitleTable{//一级标题指的是玩家指南第一个界面用户所能看到的内容标题
        public static final String NAME = XmlItemTags.LEVEL1_ITEM_TAGS.ROOT_FILE_NAME;//数据表名称
        public static final class Cols{
            public static final String KEY = "title";
            public static final String ADJUNCTION = "adjunction";
            public static final String PIC = "pic";
            public static final String NUM = "number";
        }
    }
}
