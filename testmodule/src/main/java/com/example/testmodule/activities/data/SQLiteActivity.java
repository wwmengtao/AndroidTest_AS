package com.example.testmodule.activities.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.androidcommon.database.DbTransaction;
import com.example.testmodule.BaseActivity;
import com.example.testmodule.R;
import com.example.testmodule.dataops.DataBaseHelper;
import com.fernandocejas.android10.sample.data.ALog;
import com.fernandocejas.android10.sample.data.database.DataManager;
import com.fernandocejas.android10.sample.data.database.DbSchema;
import com.fernandocejas.android10.sample.data.database.DbSchema.DbCursorWrapper;
import com.fernandocejas.android10.sample.data.database.xmlOps.XmlOperator;
import com.fernandocejas.android10.sample.data.entity.UserEntityNT;

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
    }

    @OnClick(R.id.btn5)
    public void onClick5(View view){

    }

    @OnClick(R.id.btn6)
    public void onClick6(View view){
        mDataBaseHelper.getAllTables();
    }

    @OnClick(R.id.btn7)
    public void onClick7(View view){
        mDataBaseHelper.dropTables();
    }

}