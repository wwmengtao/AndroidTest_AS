package com.fernandocejas.android10.sample.data.database;

import android.database.Cursor;

import com.fernandocejas.android10.sample.data.entity.UserEntityNT;


public class DbCursorWrapper extends android.database.CursorWrapper {
    public DbCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public UserEntityNT getUserEntity() {
//        String uuidString = getString(getColumnIndex(FirstLevelTitleTable.Cols.UUID));
//        String title = getString(getColumnIndex(FirstLevelTitleTable.Cols.TITLE));
//        long date = getLong(getColumnIndex(FirstLevelTitleTable.Cols.DATE));
//        int isSolved = getInt(getColumnIndex(FirstLevelTitleTable.Cols.SOLVED));
//        String suspect = getString(getColumnIndex(FirstLevelTitleTable.Cols.SUSPECT));

//        User crime = new User(1);
//        crime.setTitle(title);
//        crime.setDate(new Date(date));
//        crime.setSolved(isSolved != 0);
//        crime.setSuspect(suspect);

        return null;
    }
}
