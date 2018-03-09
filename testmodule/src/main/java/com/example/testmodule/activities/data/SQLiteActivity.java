package com.example.testmodule.activities.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.testmodule.BaseActivity;
import com.example.testmodule.R;
import com.example.testmodule.dataops.DataBaseHelper;
import com.fernandocejas.android10.sample.data.ALog;
import com.fernandocejas.android10.sample.data.database.DataManager;
import com.fernandocejas.android10.sample.data.database.DbSchema;
import com.fernandocejas.android10.sample.data.database.DbSchema.DbCursorWrapper;
import com.fernandocejas.android10.sample.data.database.xmlOps.XmlOperator;
import com.fernandocejas.android10.sample.data.entity.UserEntityNT;
import com.fernandocejas.android10.sample.domain.interactor.GetUserNTList;

import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SQLiteActivity extends BaseActivity {
    private static final String TAG = "SQLiteActivity";
    @BindView(R.id.tv)    TextView tv;
    private DataManager mDataManager;
    private DataBaseHelper mDataBaseHelper;
    private SQLiteDatabase mSQLiteDatabase;
    private List<UserEntityNT> userEntityList;
    private GetUserNTList.Params params;
    private String fileName = "xmlfiles.xml";
    private String tableName = "xmlfiles";
    private XmlOperator mXmlOperator;
    private Collection<UserEntityNT> mUserEntityNTCollection = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlite);
        mUnbinder = ButterKnife.bind(this);
        mDataManager = DataManager.get(this.getApplicationContext());
//        userEntityList = (List<UserEntityNT>)mDataManager.getUserEntityCollection(params);
        mDataBaseHelper = DataBaseHelper.getInstance(this.getApplicationContext());
        mSQLiteDatabase = mDataBaseHelper.getWritableDatabase();
        mXmlOperator = new XmlOperator(this.getApplicationContext());
        params = new GetUserNTList.Params(GetUserNTList.Params.DataType.COLLECTION_DATA_LEVEL1, fileName, null);
    }

    protected void onResume(){
        super.onResume();
        mUserEntityNTCollection = mXmlOperator.UserEntityNTCollectionXml(params);
//        mXmlOperator.visitCollection(mUserEntityNTCollection);
    }

    @OnClick(R.id.btn1)
    public void onClick1(View view){
        boolean tableExist = mDataBaseHelper.tabIsExist(tableName);
        ALog.Log("tableExist: "+tableExist);
        tv.setText("tableExist: "+tableExist);
    }

    @OnClick(R.id.btn2)
    public void onClick2(View view){
        mDataBaseHelper.createTable(tableName);
    }

    @OnClick(R.id.btn3)
    public void onClick3(View view){
        mDataBaseHelper.deleteTable(tableName);
    }

    @OnClick(R.id.btn4)
    public void onClick4(View view){
        saveCollection(mUserEntityNTCollection, params);
    }

    @OnClick(R.id.btn5)
    public void onClick5(View view){
        UserEntityNT mUserEntityNT = queryUserEntityNT("applist.xml", params);
        if(null != mUserEntityNT){
            ALog.Log("queryUserEntityNT");
            ALog.Log("getPic: "+mUserEntityNT.getPic());
        }
    }

    @OnClick(R.id.btn6)
    public void onClick6(View view){
        mDataBaseHelper.getAllTables();
    }

    @OnClick(R.id.btn7)
    public void onClick7(View view){
        mDataBaseHelper.dropTables();
    }

    public void saveCollection(Collection<UserEntityNT> mUserEntityCollection, GetUserNTList.Params mParams){
        if(mParams.getDataType() != GetUserNTList.Params.DataType.COLLECTION_DATA_LEVEL1 &&
                mParams.getDataType() != GetUserNTList.Params.DataType.COLLECTION_DATA_LEVEL2)return;
        if(null != mUserEntityCollection && mUserEntityCollection.size() > 0){
            for(UserEntityNT mUserEntityNT : mUserEntityCollection){
                saveData(mUserEntityNT, mParams);
            }
        }
    }

    public void saveData(UserEntityNT mUserEntity, GetUserNTList.Params mParams) {
        String dbTableName = mParams.getTableName();
        mSQLiteDatabase.insert(dbTableName, null, getContentValues(mUserEntity, mParams));
    }

    private ContentValues getContentValues(UserEntityNT mUserEntity, GetUserNTList.Params mParams) {
        ALog.Log("getContentValues");
        ContentValues values = new ContentValues();
        values.put(DbSchema.Level1TitleTable.Cols.KEY, mUserEntity.getKey());
        values.put(DbSchema.Level1TitleTable.Cols.ADJ, mUserEntity.getAdjunction());
        values.put(DbSchema.Level1TitleTable.Cols.PIC, mUserEntity.getPic());
        if(mParams.getDataType() == GetUserNTList.Params.DataType.COLLECTION_DATA_LEVEL1) {
            values.put(DbSchema.Level1TitleTable.Cols.NUM, mUserEntity.getNumber());
        }
        return values;
    }

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

}