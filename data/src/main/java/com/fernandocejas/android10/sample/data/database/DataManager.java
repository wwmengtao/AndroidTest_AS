package com.fernandocejas.android10.sample.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.fernandocejas.android10.sample.data.ALog;
import com.fernandocejas.android10.sample.data.database.xmlOps.XmlOperator;
import com.fernandocejas.android10.sample.data.entity.UserEntityNT;
import com.fernandocejas.android10.sample.domain.interactor.GetUserListDetails;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Mengtao1 on 2016/12/8.
 */

public class DataManager {
    private static volatile DataManager mDataManager=null;
    private Collection<UserEntityNT> mData = null;
    private Context mContext=null;
    private SQLiteDatabase mSQLiteDatabase = null;
    private XmlOperator mXmlOperator = null;
    public DataManager(Context context){
        mContext = context.getApplicationContext();
        mData = new ArrayList<>();
        mSQLiteDatabase = DataBaseHelper.getInstance(mContext).getWritableDatabase();
        mXmlOperator = new XmlOperator(mContext);
    }

    public static DataManager get(Context context){
        if(null==mDataManager){
            mDataManager = new DataManager(context);
        }
        return mDataManager;
    }

    /**
     * 从xml文件中读取params对应的单个数据
     * @param params
     * @return
     */
    public UserEntityNT UserEntityXml(GetUserListDetails.Params params) {
        ALog.Log("UserEntityXml: "+params.getKey());
        return null;
    }

    /**
     *从xml文件中读取params对应的数据集合
     * @param params
     * @return
     */
    public Collection<UserEntityNT> UserEntityCollectionXml(GetUserListDetails.Params params) {
        ALog.Log("UserEntityCollectionXml: "+params.getFileName());
        return mXmlOperator.UserEntityCollectionXml(params);
    }



    public Collection<UserEntityNT> getUserEntityCollection(GetUserListDetails.Params params){
        mData.clear();
        String fileName = params.getFileName();
        DbCursorWrapper cursor = queryCrimes(fileName,null, null);
        try{
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                mData.add(cursor.getUserEntity());
                cursor.moveToNext();
            }
        }finally {
            cursor.close();
        }
        return mData;
    }

    public UserEntityNT getUserEntity(GetUserListDetails.Params params){
        String dbTableName = params.getFileName();
        String title = params.getKey();
        String columnName=null;
        DbCursorWrapper cursor = queryCrimes(dbTableName,columnName+" = ?", new String[]{title});
        UserEntityNT mUserEntity=null;
        try{
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                mUserEntity = cursor.getUserEntity();
                if(mUserEntity.getKey().equals(title))break;
                cursor.moveToNext();
            }
        }finally {
            cursor.close();
        }
        return mUserEntity;
    }

    private static ContentValues getContentValues(String []tableColumns, UserEntityNT mUserEntity) {
        if(null == tableColumns){
            return null;
        }
        ContentValues values = new ContentValues();
        for(String str:tableColumns){
            values.put(str, mUserEntity.getKey());
        }
//        values.put(CrimeTable.Cols.TITLE, crime.getTitle());
//        values.put(CrimeTable.Cols.DATE, crime.getDate().getTime());
//        values.put(CrimeTable.Cols.SOLVED, crime.isReSolved() ? 1 : 0);
//        values.put(CrimeTable.Cols.SUSPECT, crime.getSuspect());

        return values;
    }

    public void put(UserEntityNT userEntity, GetUserListDetails.Params params){
        if(params.getDataType()==GetUserListDetails.Params.DataType.SINGLE_DATA)return;
//        mSQLiteDatabase.insert(params.getFileName(), null, getContentValues(tableColumns, mUserEntity));
    }

    public void put(Collection<UserEntityNT> userEntityCollection, GetUserListDetails.Params params){
        if(null == userEntityCollection || null == params){
            return;
        }
        for(UserEntityNT mUserEntityNT : userEntityCollection){
            put(mUserEntityNT, params);
        }
    }

    public void addCrime(String dbTableName, String []tableColumns, UserEntityNT mUserEntity) {
        mSQLiteDatabase.insert(dbTableName, null, getContentValues(tableColumns, mUserEntity));
    }

    /**
     *
     * @param dbTableName 数据表名称
     * @param whereClause
     * @param whereArgs
     * @return
     */
    public DbCursorWrapper queryCrimes(String dbTableName, String whereClause, String[] whereArgs) {
        Cursor cursor = mSQLiteDatabase.query(
                dbTableName,
                null, // Columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null  // orderBy
        );
        return new DbCursorWrapper(cursor);
    }

    public boolean exists(GetUserListDetails.Params params){
        return false;
    }
}
