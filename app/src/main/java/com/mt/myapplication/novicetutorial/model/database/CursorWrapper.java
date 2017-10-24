package com.mt.myapplication.novicetutorial.model.database;

import android.database.Cursor;

import com.fernandocejas.android10.sample.domain.User;
import com.mt.myapplication.criminalintent.database.CrimeDbSchema.CrimeTable;

import java.util.Date;
import java.util.UUID;

public class CursorWrapper extends android.database.CursorWrapper {
    public CursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public User getCrime() {
        String uuidString = getString(getColumnIndex(CrimeTable.Cols.UUID));
        String title = getString(getColumnIndex(CrimeTable.Cols.TITLE));
        long date = getLong(getColumnIndex(CrimeTable.Cols.DATE));
        int isSolved = getInt(getColumnIndex(CrimeTable.Cols.SOLVED));
        String suspect = getString(getColumnIndex(CrimeTable.Cols.SUSPECT));

        User crime = new User(1);
//        crime.setTitle(title);
//        crime.setDate(new Date(date));
//        crime.setSolved(isSolved != 0);
//        crime.setSuspect(suspect);

        return crime;
    }
}
