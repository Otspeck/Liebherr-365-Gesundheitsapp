package com.example.liebherr_365_gesundheitsapp;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

public class DBHelperDataSource {

    private static final String LOG_TAG = DBHelperDataSource.class.getSimpleName();

    private SQLiteDatabase database;
    private DBHelper dbHelper;

    public DBHelperDataSource(Context context) {
        Log.d(LOG_TAG, "Unsere DataSource erzeugt jetzt den dbHelper.");
        dbHelper = new DBHelper(context);
    }

    public void open() {
        Log.d(LOG_TAG, "Eine Referenz auf die Datenbank wird jetzt angefragt.");
        database = dbHelper.getWritableDatabase();
        Log.d(LOG_TAG, "Datenbank-Referenz erhalten. Pfad zur Datenbank: " + database.getPath());
    }

    public void close() {
        dbHelper.close();
        Log.d(LOG_TAG, "Datenbank mit Hilfe des DbHelpers geschlossen.");
    }


    //function getData to see what have been saved
    public String getData() {
        database = dbHelper.getReadableDatabase();
        String s;

        Cursor c = database.rawQuery("SELECT * FROM " + weightquery.getDbName(), null);
        c.moveToFirst();
        s = "Gewicht: ";
        s += c.getString(c.getColumnIndex(weightquery.getColumnWeight()));
        s += " // Datum: ";
        s += c.getString(c.getColumnIndex(weightquery.getColumnDate()));

        dbHelper.close();
        return s;
    }

    //function insert data into database
    public void insertdata(weightdata wd) {
        ContentValues values = new ContentValues();
        values.put(weightquery.getColumnWeight(), wd.getWeight());
        values.put(weightquery.getColumnDate(), wd.getDate());
        values.put(weightquery.getColumnBmi(), wd.getBmi());
        database.insert(weightquery.getDbName(), null, values);
    }

   /* public boolean datealreadysaved(weightdata wd) {
        String date = wd.getDate();
        boolean result;

        //Todo:  Function fertigstellen ( Marvin )
        Cursor cursor = database.query(weightquery.getDbName(),weightquery.getColumnDate(),null,null,null,null);

        int
        while(cursor.moveToNext()) {
            if (cursor.getString()== wd.getDate()) ; //add the item
        }

        return true;
    }*/
}
