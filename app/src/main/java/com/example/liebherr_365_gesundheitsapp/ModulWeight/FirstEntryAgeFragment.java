package com.example.liebherr_365_gesundheitsapp.ModulWeight;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;

import com.anton46.stepsview.StepsView;
import com.example.liebherr_365_gesundheitsapp.R;

/**
 * Created by mpadmin on 17.05.2017.
 */

public class FirstEntryAgeFragment extends DialogFragment {
    private Context context;
    private int age;
    private final String[] labels = {"     1", "     2", "     3", "     4"};

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        // get context
        context = getActivity().getApplicationContext();

        // make dialog object
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // get the layout inflater
        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // inflate our custom layout for the dialog to a View
        View view = li.inflate(R.layout.firstentryagefragment, null);

        // get StepsView
        StepsView mStepsView = (StepsView) view.findViewById(R.id.stepsView);

        //style StepsView
        mStepsView.setLabels(labels)
                .setBarColorIndicator(Color.parseColor("#b2acac"))
                .setProgressColorIndicator(Color.parseColor("#64c5b1"))
                .setLabelColorIndicator(Color.parseColor("#64c5b1"))
                .setCompletedPosition(2)
                .drawView();

        //Intialize height numberpickerweight
        NumberPicker agenumberpicker = (NumberPicker) view.findViewById(R.id.age);

        // setOnValueChangedListener on NumberPucker integer
        agenumberpicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldValue, int newValue) {
                age = newValue;
            }
        });


        //Set interger Value 40-100
        agenumberpicker.setMinValue(15);
        agenumberpicker.setMaxValue(110);
        agenumberpicker.setBackgroundColor(Color.GRAY);
        agenumberpicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        agenumberpicker.setWrapSelectorWheel(false);

        // setOnClickListener on Button später
        Button späterbutton = (Button) view.findViewById(R.id.weiter);
        späterbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SavedSharedPrefrencesModulWeight.setAge(age);
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("age", String.valueOf(age));
                editor.apply();

                DialogFragment genderfragment = new FirstEntryGenderFragment();
                genderfragment.show(getFragmentManager(), "gender");

                //close NumberPickerFragment
                getDialog().dismiss();
            }
        });

        // inform the dialog it has a custom View
        builder.setView(view);

        return view;
    }
}