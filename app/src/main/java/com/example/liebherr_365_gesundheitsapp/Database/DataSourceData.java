package com.example.liebherr_365_gesundheitsapp.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DataSourceData {

    private static final String LOG_TAG = DataSourceData.class.getSimpleName();

    private SQLiteDatabase databaseData;
    private DBHelper dbHelper;
    private static final String[] COLUMNS = {
            Queries.COLUMN_MODUL, Queries.COLUMN_DATE, Queries.COLUMN_PHYSICAL_VALUES
    };

    public DataSourceData(Context context) {
        Log.d(LOG_TAG, "<DATA>Unsere DataSource erzeugt jetzt den dbHelper.<DATA>");
        dbHelper = new DBHelper(context);
    }

    public void open() {
        Log.d(LOG_TAG, "<DATA>Eine Referenz auf die Datenbank wird jetzt angefragt.<DATA>");
        databaseData = dbHelper.getWritableDatabase();
        Log.d(LOG_TAG, "<DATA>Datenbank-Referenz erhalten. Pfad zur Datenbank: " + databaseData.getPath() + "<DATA>");
    }

    public void close() {
        dbHelper.close();
        Log.d(LOG_TAG, "<DATA>Datenbank mit Hilfe des DbHelpers geschlossen.<DATA>");
    }

    public void deletedb(String modul) {
        Log.d("Modul", modul);
        databaseData.execSQL("DELETE FROM " + Queries.TABLE_DATA + " WHERE " + Queries.COLUMN_MODUL + "='" + modul + "'");
        Log.d(LOG_TAG, "<DATA>Datenbank gelöscht<DATA>");
    }

    //function insertdata into database
    public void insertdata(Data data) {
        ContentValues values = new ContentValues();

        values.put(Queries.COLUMN_MODUL, data.getModul());
        values.put(Queries.COLUMN_DATE, data.getDate());
        values.put(Queries.COLUMN_PHYSICAL_VALUES, data.getPhysicalvalues());
        values.put(Queries.COLUMN_TYPE, data.getType());

        try {
            databaseData.beginTransaction();
            databaseData.insert(Queries.TABLE_DATA, null, values);
            databaseData.setTransactionSuccessful();
        } finally {
            databaseData.endTransaction();
        }

    }

    //function deletesingledata in database
    public void deletesingledata(Data data) {
        String modul = data.getModul();
        String date = data.getDate();
        String[] values = new String[]{modul, date};
        databaseData.delete(Queries.TABLE_DATA, Queries.COLUMN_MODUL + "=? and " + Queries.COLUMN_DATE + "=?", values);
    }

    //function updatedata in database
    public void updatedata(Data data) {
        //call function deletedata
        deletesingledata(data);
        //call function insertdata
        insertdata(data);
    }

    //function cursorToWeightdata
    private Data cursorToWeightdata(Cursor cursor) {
        Data data;

        int idModul = cursor.getColumnIndex(Queries.COLUMN_MODUL);
        int idDate = cursor.getColumnIndex(Queries.COLUMN_DATE);
        int idWeight = cursor.getColumnIndex(Queries.COLUMN_PHYSICAL_VALUES);

        //int idType = cursor.getColumnIndex(Queries.COLUMN_TYPE);

        String modul = cursor.getString(idModul);
        String date = cursor.getString(idDate);
        float physicalvalues = cursor.getFloat(idWeight);

        data = new Data(modul, date, physicalvalues, "kg");

        return data;
    }

    // function getPreparedCursorForWeightList
    public Cursor getPreparedCursorForWeightList() {
        String query = "SELECT * FROM " + Queries.TABLE_DATA + " WHERE " + Queries.COLUMN_MODUL + "='ModulWeight' ORDER BY " + Queries.COLUMN_DATE + " DESC LIMIT 5";
        return databaseData.rawQuery(query, null);
    }

    //function getPreparedCursorForHistorieList
    public Cursor getPreparedCursorForHistorieList(String modulname) {
        String query = "SELECT * FROM " + Queries.TABLE_DATA + " WHERE " + Queries.COLUMN_MODUL + "='" + modulname + "'ORDER BY " + Queries.COLUMN_DATE + " DESC";
        return databaseData.rawQuery(query, null);
    }

    //function getAllDataasarray
    public Data[] getAllDataasarray(String modulname) {
        String where = Queries.COLUMN_MODUL + "='" + modulname + "'";

        Cursor cursor = databaseData.query(Queries.TABLE_DATA,
                COLUMNS, where, null, null, null, Queries.COLUMN_DATE);

        int size = cursor.getCount();
        Data[] alldata = new Data[size];

        cursor.moveToFirst();
        int counter = 0;

        while (!cursor.isAfterLast()) {
            alldata[counter] = cursorToWeightdata(cursor);
            cursor.moveToNext();
            counter++;
        }

        cursor.close();
        return alldata;
    }

    //function datealreadysaved
    public boolean datealreadysaved(Data wd) {
        boolean result = false;
        String date = wd.getDate();

        String query = "SELECT " + Queries.COLUMN_DATE + " FROM " + Queries.TABLE_DATA + " WHERE " + Queries.COLUMN_MODUL + "='" + wd.getModul() + "'";
        Cursor databaseweightresult = databaseData.rawQuery(query, null);

        int count = databaseweightresult.getCount();

        if (count == 0) {
            result = false;
        } else {
            databaseweightresult.moveToFirst();
            int iddate = databaseweightresult.getColumnIndex(Queries.COLUMN_DATE);
            String datefound = databaseweightresult.getString(iddate);
            if (date.equals(datefound)) {
                result = true;
            }
            while (databaseweightresult.moveToNext()) {
                datefound = databaseweightresult.getString(databaseweightresult.getColumnIndex(Queries.COLUMN_DATE));
                if (date.equals(datefound)) {
                    result = true;
                }
            }
        }
        return result;
    }

    //function getLatestWeight
    public float getLatestEntry(String modulname) {
        String query = "SELECT * FROM " + Queries.TABLE_DATA + " WHERE " + Queries.COLUMN_MODUL + "='" + modulname + "' ORDER BY " + Queries.COLUMN_DATE + " DESC";
        Cursor databaseweightresult = databaseData.rawQuery(query, null);

        databaseweightresult.moveToFirst();

        if (databaseweightresult.getCount() == 0) {
            return 0;
        } else {
            int ID = databaseweightresult.getColumnIndex(Queries.COLUMN_PHYSICAL_VALUES);
            float value = databaseweightresult.getFloat(ID);

            return value;
        }
    }

    // function getFirstWeight
    public String getLatestEntryDatum(String modulname) {
        String query = "SELECT * FROM " + Queries.TABLE_DATA + " WHERE " + Queries.COLUMN_MODUL + "='" + modulname + "' ORDER BY " + Queries.COLUMN_DATE + " DESC";
        Cursor databaseweightresult = databaseData.rawQuery(query, null);

        databaseweightresult.moveToFirst();

        if (databaseweightresult.getCount() == 0) {
            return null;
        } else {
            int WeightID =databaseweightresult.getColumnIndex(Queries.COLUMN_DATE);
            String latestEntryDatum =databaseweightresult.getString(WeightID);

            return latestEntryDatum;
        }
    }

    // function getValueWithDatum
    public float getValueWithDatum(String modulname, String datum) {
        String queryWhere = Queries.COLUMN_DATE + " = '" + datum + "' AND " + Queries.COLUMN_MODUL + " ='" + modulname + "'";

        Cursor cursor = databaseData.query(Queries.TABLE_DATA, COLUMNS, queryWhere, null, null, null, null);
        cursor.moveToFirst();
        if (cursor.getCount() == 0) {
            return 0;
        } else {
            int ID = cursor.getColumnIndex(Queries.COLUMN_PHYSICAL_VALUES);
            float value = cursor.getFloat(ID);

            return value;
        }
    }


    public boolean entryAlreadyExisting(String modul) {
        boolean result = false;

        // get actualdate
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.GERMANY);
        String actualdate = dateFormat.format(new java.util.Date());

        String query = "SELECT " + Queries.COLUMN_DATE + " FROM " + Queries.TABLE_DATA + " WHERE " + Queries.COLUMN_MODUL + "='" + modul + "' AND " + Queries.COLUMN_DATE + "='" + actualdate + "';";
        Cursor databaseweightresult = databaseData.rawQuery(query, null);
        if (databaseweightresult.getCount() != 0) {
            result = true;
        }
        return result;
    }
}
