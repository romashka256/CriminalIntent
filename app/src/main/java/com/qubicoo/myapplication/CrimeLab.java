package com.qubicoo.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import database.CrimeBaseHelper;
import database.CrimeCursorWrapper;
import database.CrimeDbSchema.CrimeTable;

// Singleton to store one instance of the Crime Lab
public class CrimeLab {

    private SQLiteDatabase mDataBase;
    private Context mContext;
    private ArrayList<Crime> mCrimes;

    private static CrimeLab sCrimeLab;
    private Context mAppContext;

    public void addCrime(Crime c) {

        ContentValues values = getContentValues(c);
        mDataBase.insert(CrimeTable.NAME,null,values);
    }

    public void deleteCrime(UUID id) {
        Log.e("tag", "will be delete");
        mDataBase.delete(CrimeTable.NAME,CrimeTable.Cols.UUID + " = ?",new String[] {id.toString()});
    }

    // Constructor
    private CrimeLab(Context appContext) {
        mContext = appContext.getApplicationContext();

        mDataBase = new CrimeBaseHelper(mContext).getWritableDatabase();

		/* For now, auto-populate the list
        for(int i = 0; i < 100; i++){
			Crime c = new Crime();
			c.setTitle("Crime #" + i);
			c.setSolved(i % 2 == 0);
			mCrimes.add(c);
		}
		*/
    }

    // Create Singleton
    public static CrimeLab get(Context c) {
        // If the singleton doesn't exist yet, create it.
        if (sCrimeLab == null) {
            // Don't assume that context 'c' will always be what you expect!  Be
            //    extra safe and call the method to make sure.
            sCrimeLab = new CrimeLab(c.getApplicationContext());
        }

        return sCrimeLab;
    }
    private static ContentValues getContentValues(Crime crime){
        ContentValues values = new ContentValues();
        values.put(CrimeTable.Cols.UUID,crime.getId().toString());
        values.put(CrimeTable.Cols.TITLE,crime.getTitle());
        values.put(CrimeTable.Cols.DATE,crime.getDate().getTime());
        values.put(CrimeTable.Cols.SOLVED,crime.isSolved() ? 1 : 0);
        values.put(CrimeTable.Cols.SUSPECT,crime.getSuspect());
        return values;
    }

    private CrimeCursorWrapper queryCrimes(String whereClause, String[] whereArgs){
        Cursor cursor = mDataBase.query(
                CrimeTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null);
        return new CrimeCursorWrapper(cursor);
    }

    // Return entire list of crimes
    public List<Crime> getCrimes() {
        List<Crime> crimes = new ArrayList<>();

        CrimeCursorWrapper cursor = queryCrimes(null, null);
        try{
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                crimes.add(cursor.getCrime());
                cursor.moveToNext();
            }
        }finally {
            cursor.close();
        }
        return crimes;
    }

    // Return only a specific crime
    public Crime getCrime(UUID id) {

        CrimeCursorWrapper cursor = queryCrimes(CrimeTable.Cols.UUID + " = ?",
                new String[] {id.toString()}
        );
        try{
            if(cursor.getCount() == 0){
                return null;
            }
            cursor.moveToFirst();
            return cursor.getCrime();
        } finally {
            cursor.close();
        }
    }

    public int size() {
        return mCrimes.size();
    }


    public void updateCrime(Crime crime){
        String uuidString = crime.getId().toString();
        ContentValues values = getContentValues(crime);
        mDataBase.update(CrimeTable.NAME,values,CrimeTable.Cols.UUID + " = ?", new String[]{uuidString });
    }

}
