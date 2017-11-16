package com.fernandocejas.android10.sample.data.database;

import android.content.ContentValues;
import android.database.Cursor;

import com.fernandocejas.android10.sample.data.database.xmlOps.XmlItemTags;
import com.fernandocejas.android10.sample.data.entity.UserEntityNT;
import com.fernandocejas.android10.sample.domain.interactor.GetUserNTList;

/**
 * 数据库架构，主要罗列数据库中数据表的内容
 * Created by Mengtao1 on 2016/12/15.
 */

public class DbSchema {
    public static final class Level1TitleTable{//一级标题指的是玩家指南第一个界面用户所能看到的内容标题
        public static final String NAME = XmlItemTags.LEVEL1_ITEM_TAGS.ROOT_FILE_NAME;//数据表名称
        public static final class Cols{
            public static final String KEY = "title";
            public static final String ADJ = "adjunction";
            public static final String PIC = "pic";
            public static final String NUM = "number";
            public static final String SUM = "total";
        }

        /**
         * createLevel1TableSql：创建数据表的SQL语句，注意：
         * 1)数据表名称不能包含".";2)数据表中，列名不能用"index"(大小写都不允许)，否则报错。
         * @param tableName
         * @return
         */
        public static String createTableSql(String tableName){
            return "CREATE TABLE " + tableName+" (" +
                    "_id" + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    Cols.KEY +  " TEXT," +
                    Cols.ADJ +  " TEXT," +
                    Cols.PIC +  " TEXT," +
                    Cols.NUM + " INTEGER," +
                    Cols.SUM + " INTEGER" + ");";
        }
    }

    public static final class Level2TitleTable{//二级标题指的是玩家指南由第一个界面点击进入的下一个界面标题
        public static final class Cols{
            public static final String KEY = "title";
            public static final String ADJ = "adjunction";
            public static final String PIC = "pic";
            public static final String NUM = "number";
        }

        public static String createTableSql(String tableName){
            return "CREATE TABLE " + tableName+" (" +
                    "_id" + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    Cols.KEY +  " TEXT," +
                    Cols.ADJ +  " TEXT," +
                    Cols.PIC +  " TEXT," +
                    Cols.NUM + " INTEGER" + ");";
        }
    }

    /**
     * getContentValues：获取数据库查询/增加所需的ContentValues内容
     * @param mUserEntity
     * @param mParams
     * @return
     */
    public static ContentValues getContentValues(UserEntityNT mUserEntity, GetUserNTList.Params mParams) {
        ContentValues values = new ContentValues();
        values.put(DbSchema.Level1TitleTable.Cols.KEY, mUserEntity.getKey());
        values.put(DbSchema.Level1TitleTable.Cols.ADJ, mUserEntity.getAdjunction());
        values.put(DbSchema.Level1TitleTable.Cols.PIC, mUserEntity.getPic());
        if(mParams.getDataType() == GetUserNTList.Params.DataType.COLLECTION_DATA_LEVEL1) {
            values.put(DbSchema.Level1TitleTable.Cols.NUM, mUserEntity.getNumber());
            values.put(DbSchema.Level1TitleTable.Cols.SUM, mUserEntity.getSum());
        }
        return values;
    }

    /**
     * DbCursorWrapper：Cursor的包裹操作
     */
    public static class DbCursorWrapper extends android.database.CursorWrapper {
        private GetUserNTList.Params mParams;
        public DbCursorWrapper(Cursor cursor, GetUserNTList.Params mParams) {
            super(cursor);
            this.mParams = mParams;
        }

        public UserEntityNT getUserEntityNT() {
            boolean isLevel1 = mParams.getDataType() == GetUserNTList.Params.DataType.COLLECTION_DATA_LEVEL1;
            String key = getString(getColumnIndex(Level1TitleTable.Cols.KEY));
            String adj = getString(getColumnIndex(Level1TitleTable.Cols.ADJ));
            String pic = getString(getColumnIndex(Level1TitleTable.Cols.PIC));
            //
            UserEntityNT mUserEntityNT = new UserEntityNT(key);
            mUserEntityNT.setAdjunction(adj);
            mUserEntityNT.setPic(pic);
            if(isLevel1){
                mUserEntityNT.setNumber(getInt(getColumnIndex(Level1TitleTable.Cols.NUM)));
                mUserEntityNT.setSum(getInt(getColumnIndex(Level1TitleTable.Cols.SUM)));
            }
            return mUserEntityNT;
        }
    }
}
