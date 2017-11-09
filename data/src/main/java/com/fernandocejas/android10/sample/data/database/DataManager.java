package com.fernandocejas.android10.sample.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.fernandocejas.android10.sample.data.ALog;
import com.fernandocejas.android10.sample.data.database.xmlOps.XmlOperator;
import com.fernandocejas.android10.sample.data.entity.UserEntityNT;
import com.fernandocejas.android10.sample.domain.interactor.GetUserNTList;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Mengtao1 on 2016/12/8.
 */

public class DataManager {
    private static volatile DataManager mDataManager=null;
    private Collection<UserEntityNT> mData = null;
    private Context mContext=null;
    private DataBaseHelper mDataBaseHelper=null;
    private SQLiteDatabase mSQLiteDatabase = null;
    private XmlOperator mXmlOperator = null;
    public DataManager(Context context){
        mContext = context.getApplicationContext();
        mData = new ArrayList<>();
        mDataBaseHelper = DataBaseHelper.getInstance(mContext);
        mSQLiteDatabase = mDataBaseHelper.getWritableDatabase();
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
    public UserEntityNT UserEntityNTXml(GetUserNTList.Params params) {
        ALog.Log("UserEntityXml: "+params.getKey());
        return null;
    }

    /**
     *从xml文件中读取params对应的数据集合
     * @param params
     * @return
     */
    public Collection<UserEntityNT> UserEntityNTCollectionXml(GetUserNTList.Params params) {
        ALog.Log("UserEntityCollectionXml: "+params.getFileName());
        return mXmlOperator.UserEntityNTCollectionXml(params);
    }


    /**
     * getUserEntityCollection：查询数据表中所有数据
     * @param params
     * @return
     */
    public Collection<UserEntityNT> getUserEntityCollection(GetUserNTList.Params params){
        mData.clear();
        DbCursorWrapper cursor = queryTableData(null, null, params);
        try{
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                mData.add(cursor.getUserEntityNT());
                cursor.moveToNext();
            }
        }finally {
            cursor.close();
        }
        return mData;
    }

    /**
     * queryTableData：查询数据表中的满足条件的所有数据
     * @param whereClause
     * @param whereArgs
     * @param mParams
     * @return
     */
    public DbCursorWrapper queryTableData(String whereClause, String[] whereArgs, GetUserNTList.Params mParams) {
        if(mParams.getDataType() != GetUserNTList.Params.DataType.COLLECTION_DATA_LEVEL1 &&
                mParams.getDataType() != GetUserNTList.Params.DataType.COLLECTION_DATA_LEVEL2)return null;
        Cursor cursor = mSQLiteDatabase.query(
                mParams.getTableName(),
                null, // Columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null  // orderBy
        );
        return new DbCursorWrapper(cursor, mParams);
    }

    public UserEntityNT queryUserEntityNT(String key, GetUserNTList.Params mParams){
        DbCursorWrapper cursor = queryTableData(DbSchema.Level1TitleTable.Cols.KEY+" = ?", new String[]{key}, mParams);
        UserEntityNT mUserEntityNT=null;
        try{
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                mUserEntityNT = cursor.getUserEntityNT();
                if(mUserEntityNT.getKey().equals(key))break;
                cursor.moveToNext();
            }
        }finally {
            cursor.close();
        }
        return mUserEntityNT;
    }

    /**
     * put：将mUserEntityCollection内容保存在相应的数据表中
     * @param mUserEntityCollection
     * @param mParams
     */
    public void put(Collection<UserEntityNT> mUserEntityCollection, GetUserNTList.Params mParams){
        if(mParams.getDataType() != GetUserNTList.Params.DataType.COLLECTION_DATA_LEVEL1 &&
                mParams.getDataType() != GetUserNTList.Params.DataType.COLLECTION_DATA_LEVEL2)return;
        if(null != mUserEntityCollection && mUserEntityCollection.size() > 0){
            if(!exists(mParams))mDataBaseHelper.createTable(mParams.getTableName());
            for(UserEntityNT mUserEntityNT : mUserEntityCollection){
                put(mUserEntityNT, mParams);
            }
        }
    }

    public void put(UserEntityNT mUserEntity, GetUserNTList.Params mParams){
        String dbTableName = mParams.getTableName();
        mSQLiteDatabase.insert(dbTableName, null, getContentValues(mUserEntity, mParams));
        ALog.Log("mSQLiteDatabase.insert: "+exists(mParams));
    }

    private ContentValues getContentValues(UserEntityNT mUserEntity, GetUserNTList.Params mParams) {
        ContentValues values = new ContentValues();
        values.put(DbSchema.Level1TitleTable.Cols.KEY, mUserEntity.getKey());
        values.put(DbSchema.Level1TitleTable.Cols.ADJUNCTION, mUserEntity.getAdjunction());
        values.put(DbSchema.Level1TitleTable.Cols.PIC, mUserEntity.getPic());
        if(mParams.getDataType() == GetUserNTList.Params.DataType.COLLECTION_DATA_LEVEL1) {
            values.put(DbSchema.Level1TitleTable.Cols.NUM, mUserEntity.getNumber());
        }
        return values;
    }

    /**
     * exists：查询数据表是否存在
     * @param params
     * @return
     */
    public boolean exists(GetUserNTList.Params params){
        if(null == params)return false;
        return mDataBaseHelper.tabIsExist(params.getTableName());
    }
}
