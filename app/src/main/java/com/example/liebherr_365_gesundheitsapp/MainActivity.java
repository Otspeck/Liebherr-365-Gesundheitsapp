package com.example.liebherr_365_gesundheitsapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.NumberPicker;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class MainActivity extends AppCompatActivity {
    //public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";


    //       .-..-..---. .-..-.
    //       : :; :: .--': :: :
    //       :    :: `;  : :: :
    //       : :: :: :   : :; :
    //       :_;:_;:_;   `.__.'
    //
    // This project was developed during our project studies.
    // Wintersemster 16/17
    // Bussmann    Jan
    // Hug         Melissa
    // Otec        Marvin
    // Speer       Christopher
    // Wangler     Niklas


    private DBHelperDataSource dataSource;
    Button btnNotification;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // NOTIFICATION
        /*
        //Button wird festgelegt (saveButton)
        btnNotification = (Button) findViewById(R.id.saveButton);
        btnNotification.setOnClickListener(new View.OnClickListener() {

            //service wir gestarten mit dem klick auf den Button
            @Override
            public void onClick(View view) {
                Intent startNotificationsServiceIntent = new Intent(MainActivity.this, Notification.class);
                startService(startNotificationsServiceIntent);
            }
        });
        */


        //Intialize integer and aftkomma as numberpicker to use functions
        NumberPicker integer = (NumberPicker) findViewById(R.id.integer);
        NumberPicker afterkomma = (NumberPicker) findViewById(R.id.afterkomma);

        //Set interger Value 40-100
        integer.setMinValue(40);
        integer.setMaxValue(100);

        //Set afterkomma Value 0-9
        afterkomma.setMinValue(0);
        afterkomma.setMaxValue(9);

        //wrap@ getMinValue() || getMaxValue()
        integer.setWrapSelectorWheel(false);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return
                        super.onOptionsItemSelected(item);
        }
    }


    //function saveweight onklick @+id/saveButton
    public void saveweight(View view) {

        //Datepicker
        DatePicker date = (DatePicker) findViewById(R.id.dp);
        int dayinteger = date.getDayOfMonth();
        int monthinteger = date.getMonth();
        int yearinteger = date.getYear() - 1900;


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formateddate = sdf.format(new Date(yearinteger, monthinteger, dayinteger));


        //Numberpicker
        NumberPicker integer = (NumberPicker) findViewById(R.id.integer);
        NumberPicker afterkomma = (NumberPicker) findViewById(R.id.afterkomma);

        // get values of Numberpicker
        int integervalue = integer.getValue();
        int afterkommavalue = afterkomma.getValue();

        // call function integertofloat
        float weight = integertofloat(integervalue, afterkommavalue);
        Log.d("bmi",Float.toString(calcBmi(weight)));

        // new weightdateobject with values
        Weightdata wd = new Weightdata(weight, formateddate, calcBmi(weight));

        // new DBHelperDataSource
        dataSource = new DBHelperDataSource(this);

        Log.d("opensql", "Die Datenquelle wird geöffnet.");
        dataSource.open();

        // call function datealreadysaved and react on result
        boolean datealreadyexisting = dataSource.datealreadysaved(wd);
        Log.d("result", String.valueOf(datealreadyexisting));
        if (datealreadyexisting) {
            //call alertdialog
            alertdialog(wd);
        } else {
            dataSource.insertdata(wd);
        }

        Log.d("closesql", "Die Datenquelle wird geschlossen.");
        dataSource.close();
        Intent intent = new Intent(MainActivity.this, ListViewTable.class);
        startActivity(intent);
    }

    //alertdialog
    public void alertdialog(final Weightdata wd) {
        final Context context = this;

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setTitle("Achtung!");
        alertDialogBuilder.setMessage("Zu diesem Datum existiert schon ein Gewicht");
        alertDialogBuilder.setPositiveButton("Ändern",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //action
                        Log.d("Ausgbabe", "Ändern");
                        //call function updatedata
                        dataSource.updatedata(wd);
                        dialog.dismiss();
                    }
                });

        alertDialogBuilder.setNegativeButton("Abbruch",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int d) {
                        //action
                        dialog.dismiss();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    //function integer values -> float integervalue,afterkommavalue
    public float integertofloat(int integervalue, int afterkommavalue) {
        float result = 0;
        result += (float) integervalue;
        result += ((float) afterkommavalue / 10);
        return result;
    }

    //function monthinteger to string
    public String monthinegertostring(int dayinteger) {
        switch (dayinteger) {
            case 1:
                return "Januar";
            case 2:
                return "Februar";
            case 3:
                return "März";
            case 4:
                return "April";
            case 5:
                return "Mai";
            case 6:
                return "Juni";
            case 7:
                return "Juli";
            case 8:
                return "August";
            case 9:
                return "September";
            case 10:
                return "Oktober";
            case 11:
                return "November";
            case 12:
                return "Dezember";
        }
        return "a";
    }

    public float calcBmi(float weight) {
        SharedPreferences heightPref = PreferenceManager.getDefaultSharedPreferences(this);
        float height = Float.parseFloat(heightPref.getString("height", "180"));

        height /= 100.0;
        float bmi = weight / height / height;

        return bmi;
    }
}
