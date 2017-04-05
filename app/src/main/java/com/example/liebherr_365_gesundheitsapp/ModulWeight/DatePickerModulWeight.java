package com.example.liebherr_365_gesundheitsapp.ModulWeight;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.liebherr_365_gesundheitsapp.R;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class DatePickerModulWeight extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the current date as the default date in the picker
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY);
        String actualdate = dateFormat.format(new java.util.Date());

        //convert datestrings to int
        int day = Integer.parseInt(actualdate.substring(0, 2));
        int month = Integer.parseInt(actualdate.substring(3, 5)) - 1;
        int year = Integer.parseInt(actualdate.substring(6));

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    //function onDateSet
    public void onDateSet(DatePicker view, int year, int month, int day) {
        // exclude years smaller then 2016
        if (year < 2016) {
            // create new WrongDatumFragment
            DialogFragment WrongDatumFragment = new WrongDatumFragment();

            // open WrongDatumFragment
            WrongDatumFragment.show(getFragmentManager(), "wrongDatum");
            getDialog().dismiss();
        } else {
            // create new NumberPickerModulWeight
            DialogFragment NumberPickerModulWeight = new NumberPickerModulWeight();

            // create bundle and fill with values
            Bundle bundle = new Bundle();
            bundle.putInt("day", day);
            bundle.putInt("month", month);
            bundle.putInt("year", year);

            // setArguments to NumberPickerModulWeight
            NumberPickerModulWeight.setArguments(bundle);

            // open NumberPickerModulWeight
            NumberPickerModulWeight.show(getFragmentManager(), "numberPicker");
        }
    }
}