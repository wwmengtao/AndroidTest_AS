package com.fernandocejas.android10.sample.data.database;

import android.database.Cursor;

import com.fernandocejas.android10.sample.data.entity.UserEntityNT;
import com.fernandocejas.android10.sample.domain.interactor.GetUserListDetails;


public class DbCursorWrapper extends android.database.CursorWrapper {
    private GetUserListDetails.Params mParams;
    public DbCursorWrapper(Cursor cursor, GetUserListDetails.Params mParams) {
        super(cursor);
        this.mParams = mParams;
    }

    public UserEntityNT getUserEntityNT() {
        boolean haveNum = mParams.getDataType() == GetUserListDetails.Params.DataType.COLLECTION_DATA_LEVEL1;
        String key = getString(getColumnIndex(DbSchema.Level1TitleTable.Cols.KEY));
        String adj = getString(getColumnIndex(DbSchema.Level1TitleTable.Cols.ADJUNCTION));
        String pic = getString(getColumnIndex(DbSchema.Level1TitleTable.Cols.PIC));
        int num = -1;
        if(haveNum)num = getInt(getColumnIndex(DbSchema.Level1TitleTable.Cols.NUM));

        UserEntityNT mUserEntityNT = new UserEntityNT(key);
        mUserEntityNT.setAdjunction(adj);
        mUserEntityNT.setPic(pic);
        if(haveNum)mUserEntityNT.setNumber(num);
        return mUserEntityNT;
    }
}
