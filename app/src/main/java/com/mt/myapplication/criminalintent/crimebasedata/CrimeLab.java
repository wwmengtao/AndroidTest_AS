package com.mt.myapplication.criminalintent.crimebasedata;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;


import com.mt.myapplication.criminalintent.database.CrimeCursorWrapper;
import com.mt.myapplication.criminalintent.database.DataBaseHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.mt.myapplication.criminalintent.database.CrimeDbSchema.CrimeTable;

/**
 * Created by Mengtao1 on 2016/12/8.
 */

public class CrimeLab {
    private static volatile CrimeLab mCrimeBank=null;
    private List<Crime> mData = null;
    private Context mContext=null;
    private SQLiteDatabase mSQLiteDatabase = null;
    public CrimeLab(Context context){
        mContext = context.getApplicationContext();
        mData = new ArrayList<Crime>();
        mSQLiteDatabase = DataBaseHelper.getInstance(mContext).getWritableDatabase();
    }

    public static CrimeLab get(Context context){
        if(null==mCrimeBank){
            mCrimeBank = new CrimeLab(context);
        }
        return mCrimeBank;
    }

    public List<Crime> getCrimes(){
        mData.clear();
        CrimeCursorWrapper cursor = queryCrimes(null, null);
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

    public Crime getCrime(UUID itemId){
        CrimeCursorWrapper cursor = queryCrimes(CrimeTable.Cols.UUID+" = ?", new String[]{itemId.toString()});
        Crime mCrime=null;
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

    private static ContentValues getContentValues(Crime crime) {
        ContentValues values = new ContentValues();
        values.put(CrimeTable.Cols.UUID, crime.getId().toString());
        values.put(CrimeTable.Cols.TITLE, crime.getTitle());
        values.put(CrimeTable.Cols.DATE, crime.getDate().getTime());
        values.put(CrimeTable.Cols.SOLVED, crime.isReSolved() ? 1 : 0);
        values.put(CrimeTable.Cols.SUSPECT, crime.getSuspect());

        return values;
    }

    public void addCrime(Crime c) {
        mSQLiteDatabase.insert(CrimeTable.NAME, null, getContentValues(c));
    }

    public void delCrime(Crime c){
        mSQLiteDatabase.delete(CrimeTable.NAME, CrimeTable.Cols.UUID+" = ?", new String[]{c.getId().toString()});
    }

    public void delAllCrimes(){
        mSQLiteDatabase.delete(CrimeTable.NAME,null,null);
    }

    public void updateCrime(Crime c){
        mSQLiteDatabase.update(CrimeTable.NAME, getContentValues(c), CrimeTable.Cols.UUID+" = ?", new String[]{c.getId().toString()});
    }

    public CrimeCursorWrapper queryCrimes(String whereClause, String[] whereArgs) {
        Cursor cursor = mSQLiteDatabase.query(
                CrimeTable.NAME,
                null, // Columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null  // orderBy
        );
        return new CrimeCursorWrapper(cursor);
    }

    public File getPhotoFile(Crime crime) {
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
        Crime crime = null;
        for(int i=0; i<20; i++){
            crime = new Crime();
            crime.setTitle("#"+(i+1));
            crime.setSolved(0==(i%2));
            addCrime(crime);
        }
    }
}
