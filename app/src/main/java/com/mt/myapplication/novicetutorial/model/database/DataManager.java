package com.mt.myapplication.novicetutorial.model.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.mt.myapplication.novicetutorial.model.domain.User;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
        CursorWrapper cursor = queryCrimes(null, null);
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

    public User getData(UUID itemId){
        CursorWrapper cursor = queryCrimes(CrimeTable.Cols.UUID+" = ?", new String[]{itemId.toString()});
        User mCrime=null;
        try{
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                mCrime = cursor.getCrime();
                if(mCrime.getId().equals(itemId))break;
                cursor.moveToNext();
            }
        }finally {
            cursor.close();
        }
        return mCrime;
    }

    private static ContentValues getContentValues(User crime) {
        ContentValues values = new ContentValues();
        values.put(CrimeTable.Cols.UUID, crime.getId().toString());
        values.put(CrimeTable.Cols.TITLE, crime.getTitle());
        values.put(CrimeTable.Cols.DATE, crime.getDate().getTime());
        values.put(CrimeTable.Cols.SOLVED, crime.isReSolved() ? 1 : 0);
        values.put(CrimeTable.Cols.SUSPECT, crime.getSuspect());

        return values;
    }

    public void addCrime(User c) {
        mSQLiteDatabase.insert(CrimeTable.NAME, null, getContentValues(c));
    }

    public void delCrime(User c){
        mSQLiteDatabase.delete(CrimeTable.NAME, CrimeTable.Cols.UUID+" = ?", new String[]{c.getId().toString()});
    }

    public void delAllCrimes(){
        mSQLiteDatabase.delete(CrimeTable.NAME,null,null);
    }

    public void updateCrime(User c){
        mSQLiteDatabase.update(CrimeTable.NAME, getContentValues(c), CrimeTable.Cols.UUID+" = ?", new String[]{c.getId().toString()});
    }

    public CursorWrapper queryCrimes(String whereClause, String[] whereArgs) {
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

    public File getPhotoFile(User crime) {
        File externalFilesDir = mContext
                .getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        if (externalFilesDir == null) {
            return null;
        }

        return new File(externalFilesDir, crime.getPhotoFilename());
    }

    /**
     * generateDemoCrimes:自动产生一定数量的Crime演示数据存入数据库中
     */
    public void generateDemoCrimes(){
        User crime = null;
        for(int i=0; i<20; i++){
            crime = new User();
            crime.setTitle("#"+(i+1));
            crime.setSolved(0==(i%2));
            addCrime(crime);
        }
    }
}
