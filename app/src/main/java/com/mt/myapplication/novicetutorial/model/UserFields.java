package com.mt.myapplication.novicetutorial.model;

/**
 * UserFields代表了数据表中的列元素
 * Created by Mengtao1 on 2017/10/24.
 */

public class UserFields {
    public static final class CrimeTable{
        public static final String NAME = "crimes";
        public static final class Cols{
            public static final String UUID = "uuid";
            public static final String DATE = "date";
            public static final String SOLVED = "resolved";
            public static final String TITLE = "title";
            public static final String SUSPECT = "suspect";
        }
    }
}
