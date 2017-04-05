package com.example.liebherr_365_gesundheitsapp.ModulWeight;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;


import com.example.liebherr_365_gesundheitsapp.Database.DBHelperDataSourceData;
import com.example.liebherr_365_gesundheitsapp.Database.Data;
import com.example.liebherr_365_gesundheitsapp.R;

import java.sql.Date;
import java.text.SimpleDateFormat;

/**
 * Created by mpadmin on 12.01.2017.
 */

public class NumberPickerModulWeight extends DialogFragment {
    Context context;
    private DBHelperDataSourceData dataSourceData;
    int day;
    int month;
    int year;
    int integervalue;
    int afterkommavalue = 0;

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {

        // get values from bundle
        Bundle bundle = this.getArguments();
        day = bundle.getInt("day", 0);
        month = bundle.getInt("month", 0);
        year = bundle.getInt("year", 0);

        // get context
        context = getActivity().getApplicationContext();

        // make dialog object
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // get the layout inflater
        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // inflate our custom layout for the dialog to a View
        View view = li.inflate(R.layout.numberpicker, null);

        //Intialize integer and aftkomma as numberpicker to use functions
        NumberPicker integer = (NumberPicker) view.findViewById(R.id.integer);
        NumberPicker afterkomma = (NumberPicker) view.findViewById(R.id.afterkomma);

        // setOnValueChangedListener on NumberPucker integer
        integer.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldValue, int newValue) {
                integervalue = newValue;
            }
        });

        // setOnValueChangedListener on NumberPucker integer
        afterkomma.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldValue, int newValue) {
                afterkommavalue = newValue;
            }
        });

        //Set interger Value 40-100
        integer.setMinValue(40);
        integer.setMaxValue(150);
        integer.setBackgroundColor(Color.GRAY);
        integer.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        //get latestweight and set picker
        dataSourceData = new DBHelperDataSourceData(context);
        dataSourceData.open();
        int lastentry = dataSourceData.getLatestEntry(getString(R.string.modulweight));
        if (lastentry != 0) {
            integer.setValue(lastentry);
            integervalue = lastentry;
        } else {
            integer.setValue((int) SavedSharedPrefrencesModulWeight.getWeightGoal());
            integervalue = (int) SavedSharedPrefrencesModulWeight.getWeightGoal();
        }
        dataSourceData.close();

        //Set afterkomma Value 0-9
        afterkomma.setMinValue(0);
        afterkomma.setMaxValue(9);
        afterkomma.setBackgroundColor(Color.GRAY);
        afterkomma.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        //wrap@ getMinValue() || getMaxValue()
        integer.setWrapSelectorWheel(false);

        // inform the dialog it has a custom View
        builder.setView(view);

        // setOnClickListener on Button save
        Button button = (Button) view.findViewById(R.id.save);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // formate date
                year = year - 1900;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String formateddate = sdf.format(new Date(year, month, day));

                // call function integertofloat
                float weight = integertofloat(integervalue, afterkommavalue);

                // type declaration
                String type = "kg";

                // modul declaration
                String modulweight = "ModulWeight";

                // new weightdateobject with values
                Data wd = new Data(modulweight, formateddate, weight, type);


                // new DBHelperDataSource
                dataSourceData = new DBHelperDataSourceData(context);
                dataSourceData.open();
                // call function datealreadysaved and react on result
                boolean datealreadyexisting = dataSourceData.datealreadysaved(wd);
                dataSourceData.close();

                if (datealreadyexisting) {
                    // date already extisting?

                    // create new ChangeDataFragment
                    DialogFragment ChangeDataFragment = new ChangeDataFragment();

                    // create bundle and fill with values
                    Bundle bundle = new Bundle();
                    bundle.putString("modul", modulweight);
                    bundle.putInt("day", day);
                    bundle.putInt("month", month);
                    bundle.putInt("year", year);
                    bundle.putFloat("weight", weight);
                    bundle.putString("type", type);

                    // setArguments to NumberPickerModulWeight
                    ChangeDataFragment.setArguments(bundle);

                    // open ChangeDataFragment
                    ChangeDataFragment.show(getFragmentManager(), "changeData");
                    getDialog().dismiss();
                } else {

                    // new DBHelperDataSource
                    dataSourceData = new DBHelperDataSourceData(context);
                    dataSourceData.open();
                    if (dataSourceData.getFirstWeight("ModulWeight") != 0) {
                        dataSourceData.insertdata(wd);
                    } else {
                        // separate the first value in the database
                        dataSourceData.insertdata(wd);

                        // bind textweightdiffernce to TextView
                        TextView textweightdifference = (TextView) getActivity().findViewById(R.id.weightdifference);

                        // bind textweightstart to TextView
                        TextView textweightstart = (TextView) getActivity().findViewById(R.id.firstweight);

                        // set text textweightstart
                        textweightstart.setText(String.valueOf(weight));

                        // call function calculateweightdifference
                        String weightdifferncestring = calculateweightdifference(weight);

                        // set text textweightdifference
                        textweightdifference.setText(weightdifferncestring);
                    }
                    ModulWeight.adapter.changeCursor(dataSourceData.getPreparedCursorForWeightList());

                    //call function activatebuttons
                    activatebuttons();

                    Log.d("closesql", "<DATA>Die Datenquelle wird geschlossen.<DATA>");
                    dataSourceData.close();

                    //close NumberPickerFragment
                    getDialog().dismiss();
                }
            }
        });
        return view;
    }

    //function integer values -> float integervalue,afterkommavalue
    public float integertofloat(int integervalue, int afterkommavalue) {
        float result = 0;
        result += (float) integervalue;
        result += ((float) afterkommavalue / 10);
        return result;
    }

    //function calculateweightdifference
    public String calculateweightdifference(float weight) {
        String weightdifferncestring;
        float weightdiffernce;
        float weightgoal = ModulWeight.getWeightGoal();

        if (weight == weightgoal) {
            weightdifferncestring = "0.0 kg";
        } else if (weight < weightgoal) {
            weightdiffernce = weightgoal - weight;
            //call function roundfloat
            weightdiffernce = roundfloat(weightdiffernce);
            weightdifferncestring = "+ " + String.valueOf(weightdiffernce) + " kg";
        } else {
            weightdiffernce = weight - weightgoal;
            //call function roundfloat
            weightdiffernce = roundfloat(weightdiffernce);
            weightdifferncestring = "- " + String.valueOf(weightdiffernce) + " kg";
        }
        return weightdifferncestring;
    }

    // function roundfloat
    public float roundfloat(float inputfloat) {
        float roundedfloat = 0;
        inputfloat += 0.05;
        inputfloat = (int) (inputfloat * 10);
        roundedfloat = inputfloat / 10;
        return roundedfloat;
    }

    public void activatebuttons() {
        // bind diagrammbutton to Button
        Button diagrammbutton = (Button) getActivity().findViewById(R.id.viewgraph);

        // bind deletebutton to Button
        Button deletebutton = (Button) getActivity().findViewById(R.id.deleteButton);

        // set deletebutton enabled and change opacity, color
        diagrammbutton.setEnabled(true);
        diagrammbutton.getBackground().setAlpha(255);
        diagrammbutton.setTextColor(getResources().getColor(R.color.colorPrimary));

        // set deletebutton enabled and change opacity, color
        deletebutton.setEnabled(true);
        deletebutton.getBackground().setAlpha(255);
        deletebutton.setTextColor(getResources().getColor(R.color.colorPrimary));
    }
}