package com.example.androidcommon.database;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by M.T on 2018/3/25.
 */

public class DbTransaction {
    /**
     * 多线程操作写入数据库时，需要使用事务，以免出现同步问题。
     * Android通过SQLiteOpenHelper获取数据库SQLiteDatabase实例，Helper中会自动缓存已经打开的SQLiteDatabase实例，单个App中应该
     * 使用SQLiteOpenHelper的单例模式保证数据库连接唯一。由于SQLite自身是数据库级锁，单个数据库操作是保证线程安全的(不能同时写入)，
     * transaction是一次原子操作，因此处于事务中的操作是线程安全的。
     * @param mSQLiteDatabase
     * @param table
     * @param nullColumnHack
     * @param values
     */
    public static void dbInsertTransaction(SQLiteDatabase mSQLiteDatabase, String table, String nullColumnHack, ContentValues values){
        if(null == mSQLiteDatabase){
            throw new IllegalArgumentException("DbTransaction.dbInsertTransaction: parameter mSQLiteDatabase should not be null!");
        }
        mSQLiteDatabase.beginTransaction();
        try{
            mSQLiteDatabase.insert(table, nullColumnHack, values);
            //其他操作
            mSQLiteDatabase.setTransactionSuccessful();//设置事务处理成功，不设置会自动回滚不提交。
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            mSQLiteDatabase.endTransaction();
        }
    }
}
