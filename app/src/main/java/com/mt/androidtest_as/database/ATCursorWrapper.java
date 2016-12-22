package com.mt.androidtest_as.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.mt.androidtest_as.data.BaseData;
import com.mt.androidtest_as.database.ATDbSchema.ATDataTable;


import java.util.Date;
import java.util.UUID;

public class ATCursorWrapper extends CursorWrapper {
    public ATCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public BaseData getData() {
        String uuidString = getString(getColumnIndex(ATDataTable.Cols.UUID));
        String title = getString(getColumnIndex(ATDataTable.Cols.TITLE));
        BaseData mBaseData = new BaseData(UUID.fromString(uuidString));
        mBaseData.setTitle(title);
        return mBaseData;
    }
}
