package com.mt.myapplication.novicetutorial.model.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mt.androidtest_as.alog.ALog;
import com.mt.myapplication.criminalintent.database.CrimeDbSchema.CrimeTable;

/**
 * SQLiteOpenHelper是一个辅助类，用来管理数据库的创建和版本，它提供两个方面的功能
 * 第一，getReadableDatabase()、getWritableDatabase()可以获得SQLiteDatabase对象，通过该对象可以对数据库进行操作
 * 第二，提供了onCreate()、onUpgrade()两个回调函数，允许我们再创建和升级数据库时，进行自己的操作
 */
public class DataBaseHelper extends SQLiteOpenHelper {
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "crimesRecord.db";
	private static volatile DataBaseHelper sInstance = null;

    private DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

	public static DataBaseHelper getInstance(Context context)	{
		if (sInstance == null){
			synchronized (DataBaseHelper.class){
				if (sInstance == null){
					sInstance = new DataBaseHelper(context);
				}
			}
		}
		return sInstance;
	}

	//onCreate：该函数是在第一次创建的时候执行，实际上是第一次得到SQLiteDatabase对象的时候才会调用这个方法
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		ALog.Log("create a database");
		//execSQL用于执行SQL语句
		db.execSQL("create table "+CrimeTable.NAME+"("
				+" _id integer primary key autoincrement, "
				+CrimeTable.Cols.UUID+", "
				+CrimeTable.Cols.TITLE+", "
				+CrimeTable.Cols.DATE+", "
				+CrimeTable.Cols.SOLVED+", "
				+CrimeTable.Cols.SUSPECT+")"
		);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		// TODO Auto-generated method stub
		ALog.Log("upgrade a database");
        try {
            db.execSQL("drop table if exists "+CrimeTable.NAME);
            onCreate(db);
        } catch (SQLException e) {
            e.printStackTrace();
        }
	}
}

