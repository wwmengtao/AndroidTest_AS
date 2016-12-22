package com.mt.androidtest_as.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mt.androidtest_as.ALog;
import com.mt.androidtest_as.database.ATCursorWrapper;
import com.mt.androidtest_as.database.DataBaseHelper;
import com.mt.androidtest_as.database.ATDbSchema.ATDataTable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mengtao1 on 2016/12/22.
 */

public class DataBank {
    private static Context mContext = null;
    private static volatile DataBank mDataBank = null;
    private SQLiteDatabase mSQLiteDatabase = null;
    private List<BaseData> mData = null;

    private DataBank(Context context){
        mContext = context.getApplicationContext();
        mSQLiteDatabase = DataBaseHelper.getInstance(mContext).getWritableDatabase();
        mData = new ArrayList<BaseData>();
    }

    public static DataBank get(Context context){
        if(null == mDataBank){
            synchronized (DataBank.class){
                if(null == mDataBank){
                    mDataBank = new DataBank(context);
                }
            }
        }
        return mDataBank;
    }

    /**
     * create a database
     * @param count
     */
    public void generateData(int count){
        if(count<1)return;
        BaseData mBaseData = null;
        for(int i=0;i<count;i++){
            mBaseData = new BaseData();
            mBaseData.setTitle("item: "+i);
            addData(mBaseData);
        }
    }


    public void addData(BaseData b) {
        mSQLiteDatabase.insert(ATDataTable.NAME, null, getContentValues(b));
    }

    private static ContentValues getContentValues(BaseData b) {
        ContentValues values = new ContentValues();
        values.put(ATDataTable.Cols.UUID, b.getID().toString());
        values.put(ATDataTable.Cols.TITLE, b.getTitle());
        return values;
    }

    public List<BaseData> getData(){
        mData.clear();
        ATCursorWrapper cursor = queryData(null, null);
        try{
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                mData.add(cursor.getData());
                ALog.Log("mData.add:"+cursor.getData().getTitle());
                cursor.moveToNext();
            }
        }finally {
            cursor.close();
        }
        return mData;
    }

    public ATCursorWrapper queryData(String whereClause, String[] whereArgs) {
        Cursor cursor = mSQLiteDatabase.query(
                ATDataTable.NAME,
                null, // Columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null  // orderBy
        );
        return new ATCursorWrapper(cursor);
    }

    public void delCrime(BaseData b){
        mSQLiteDatabase.delete(ATDataTable.NAME, ATDataTable.Cols.UUID+" = ?", new String[]{b.getID().toString()});
    }

    public void clearDataBase(){
        mSQLiteDatabase.delete(ATDataTable.NAME,null,null);
    }

}
