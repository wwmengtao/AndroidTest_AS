package com.fernandocejas.android10.sample.data.database;

/**
 * 数据库架构，主要罗列数据库中数据表的内容
 * Created by Mengtao1 on 2016/12/15.
 */

public class DbSchema {
    public static final class FirstLevelTitleTable{//一级标题指的是玩家指南第一个界面用户所能看到的内容标题
        public static final String NAME = "firstleveltitle";//数据表名称
        public static final class Cols{
            public static final String TITLE = "title";
            public static final String BACKGROUND = "background";
        }
    }
}
