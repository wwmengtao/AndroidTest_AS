package com.mt.myapplication.novicetutorial.model.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;


import com.fernandocejas.android10.sample.domain.User;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import javax.inject.Inject;

import static com.mt.myapplication.criminalintent.database.CrimeDbSchema.CrimeTable;

/**
 * DataManager代表了对某种类型数据的统一管理
 * Created by Mengtao1 on 2016/12/8.
 */

public class DataManager {
    @Inject Context mContext;
    private static volatile DataManager mDataManager=null;
    private Collection<User> mData = null;
    private SQLiteDatabase mSQLiteDatabase = null;
    public DataManager(Context context){
//        mContext = context.getApplicationContext();
        mData = new ArrayList<User>();
        mSQLiteDatabase = DataBaseHelper.getInstance(mContext).getWritableDatabase();
    }

    public static DataManager get(Context context){
        if(null == mDataManager){
            mDataManager = new DataManager(context);
        }
        return mDataManager;
    }

    public Collection<User> getAllData(){
        mData.clear();
        CursorWrapper cursor = queryAllTableData(null, null);
        try{
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                mData.add(cursor.getCrime());
                cursor.moveToNext();
            }
        }finally {
            cursor.close();
        }
        return mData;
    }

    public User getData(int itemId){
        CursorWrapper cursor = queryAllTableData(CrimeTable.Cols.UUID+" = ?", new String[]{Integer.toString(itemId)});
        User mUser=null;
        try{
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                mUser = cursor.getCrime();
                if(mUser.getUserId() == itemId)break;
                cursor.moveToNext();
            }
        }finally {
            cursor.close();
        }
        return mUser;
    }

    private static ContentValues getContentValues(User mUser) {
        ContentValues values = new ContentValues();
        values.put(CrimeTable.Cols.UUID, Integer.toString(mUser.getUserId()));
//        values.put(CrimeTable.Cols.TITLE, mUser.getTitle());
//        values.put(CrimeTable.Cols.DATE, mUser.getDate().getTime());
//        values.put(CrimeTable.Cols.SOLVED, mUser.isReSolved() ? 1 : 0);
//        values.put(CrimeTable.Cols.SUSPECT, mUser.getSuspect());

        return values;
    }

    public void addCrime(User c) {
        mSQLiteDatabase.insert(CrimeTable.NAME, null, getContentValues(c));
    }

    /**
     * 查找SQLite中某数据表中所有数据
     * @param whereClause
     * @param whereArgs
     * @return
     */
    public CursorWrapper queryAllTableData(String whereClause, String[] whereArgs) {
        Cursor cursor = mSQLiteDatabase.query(
                CrimeTable.NAME,
                null, // Columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null  // orderBy
        );
        return new CursorWrapper(cursor);
    }
}
