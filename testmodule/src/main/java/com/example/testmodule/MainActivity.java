package com.example.testmodule;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fernandocejas.android10.sample.data.ALog;
import com.fernandocejas.android10.sample.data.database.DataBaseHelper;
import com.fernandocejas.android10.sample.data.database.DataManager;
import com.fernandocejas.android10.sample.data.entity.UserEntityNT;
import com.fernandocejas.android10.sample.domain.interactor.GetUserListDetails;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.btn1)    Button btn1;
    @BindView(R.id.btn2)    Button btn2;
    @BindView(R.id.btn3)    Button btn3;

    @BindView(R.id.tv)    TextView tv;

    private Unbinder mUnbinder;
    private DataManager mDataManager;
    private DataBaseHelper mDataBaseHelper;
    private SQLiteDatabase mSQLiteDatabase;
    private List<UserEntityNT> userEntityList;
    private GetUserListDetails.Params params;
    private String tableName = "xmlfiles";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mUnbinder = ButterKnife.bind(this);
        params = new GetUserListDetails.Params(GetUserListDetails.Params.DataType.COLLECTION_DATA_LEVEL1, tableName, "");
        mDataManager = DataManager.get(this.getApplicationContext());
//        userEntityList = (List<UserEntityNT>)mDataManager.getUserEntityCollection(params);
        mDataBaseHelper = DataBaseHelper.getInstance(this.getApplicationContext());
        mSQLiteDatabase = mDataBaseHelper.getWritableDatabase();
    }

    protected void onDestroy(){
        super.onDestroy();
        mUnbinder.unbind();
    }

    @OnClick(R.id.btn1)
    public void onClick1(View view){
        boolean tableExist = mDataBaseHelper.tabIsExist(tableName);
        ALog.Log("tableExist: "+tableExist);
        tv.setText(""+tableExist);

    }

    @OnClick(R.id.btn2)
    public void onClick2(View view){
        mDataBaseHelper.createTable(tableName);
    }

    @OnClick(R.id.btn3)
    public void onClick3(View view){
        boolean tableExist = mDataBaseHelper.tabIsExist(tableName);
        if(tableExist)mDataBaseHelper.deleteTable(tableName);
    }
}
