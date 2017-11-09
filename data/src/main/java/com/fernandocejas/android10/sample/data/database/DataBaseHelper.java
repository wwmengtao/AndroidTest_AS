package com.fernandocejas.android10.sample.data.database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.fernandocejas.android10.sample.data.ALog;
import com.fernandocejas.android10.sample.data.database.DbSchema.Level1TitleTable;

import java.util.ArrayList;
import java.util.List;

/**
 * SQLiteOpenHelper是一个辅助类，用来管理数据库的创建和版本，它提供两个方面的功能
 * 第一，getReadableDatabase()、getWritableDatabase()可以获得SQLiteDatabase对象，通过该对象可以对数据库进行操作
 * 第二，提供了onCreate()、onUpgrade()两个回调函数，允许我们再创建和升级数据库时，进行自己的操作
 */
public class DataBaseHelper extends SQLiteOpenHelper {
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "novicetutorial.db";
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
		ALog.Log("DataBaseHelper_onCreate");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		// TODO Auto-generated method stub
		ALog.Log("upgrade a database");
        try {
			dropTables();
            onCreate(db);
        } catch (SQLException e) {
            e.printStackTrace();
        }
	}

	public void createTable(String tableName){
    	if(tabIsExist(tableName))return;//如果数据表存在就不创建了
		ALog.Log("create table: "+tableName);
		try{
			//execSQL用于执行SQL语句，注意：1)数据表名称不能包含".";2)数据表中，列名不能用"index"(大小写都不允许)，否则报错。
			this.getWritableDatabase().execSQL("CREATE TABLE " + tableName+" (" +
					"_id" + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
					Level1TitleTable.Cols.KEY +  " TEXT," +
					Level1TitleTable.Cols.ADJUNCTION +  " TEXT," +
					Level1TitleTable.Cols.PIC +  " TEXT," +
					Level1TitleTable.Cols.NUM + " INTEGER" + ");"
			);}
		catch (SQLException e){
			ALog.Log("createTable_SQLException\n"+e.fillInStackTrace());
		}
	}

	public void dropTables(){
		List<String> allTableNames = getAllTables();
		if(allTableNames.size()>0){
			SQLiteDatabase db = null;
			try {
				db = this.getReadableDatabase();//此this是继承SQLiteOpenHelper类得到的
				for (String tableName : allTableNames) {
					db.execSQL("drop table if exists " + tableName);
				}
			}catch (Exception e){
				ALog.Log("dropTables_Exception\n"+e.fillInStackTrace());
			}
		}
	}


	/**
	 * 获取数据库文件中所有数据表名称(不包含不感兴趣的数据表名称，例如android_metadata、sqlite_sequence)
	 * */
	public List<String> getAllTables(){
		List<String> allTableNames = new ArrayList<>();
		SQLiteDatabase db = null;
		Cursor cursor = null;
		String tableName = null;
		try {
			db = this.getReadableDatabase();//此this是继承SQLiteOpenHelper类得到的
			String sql = "SELECT name FROM sqlite_master WHERE type='table' AND name NOT LIKE 'sqlite_%' AND name NOT LIKE 'android_%'";
			cursor = db.rawQuery(sql, null);
			if (cursor.moveToFirst()) {
				while ( !cursor.isAfterLast() ) {
					tableName = cursor.getString(0);
					allTableNames.add(tableName);
					ALog.Log("tableName: "+tableName);
					cursor.moveToNext();
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}finally {
			if(null != cursor && !cursor.isClosed()){
				cursor.close() ;
			}
		}
		return allTableNames;
	}

	public boolean tabIsExist(String tabName){
		boolean result = false;
		if(tabName == null){
			return false;
		}
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			db = this.getReadableDatabase();//此this是继承SQLiteOpenHelper类得到的
			String sql = "SELECT count(*) FROM sqlite_master WHERE type='table' AND name='"+tabName+"'";
			cursor = db.rawQuery(sql, null);
			if(cursor.moveToNext()){
				int count = cursor.getInt(0);
				ALog.Log("count: "+count);
				if(count>0){
					result = true;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}finally {
			if(null != cursor && !cursor.isClosed()){
				cursor.close() ;
			}
		}
		return result;
	}

	public void close() {
		sInstance.close();
	}
}

