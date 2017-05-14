package com.example.liebherr_365_gesundheitsapp.ModulWeight;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.liebherr_365_gesundheitsapp.Database.Data;
import com.example.liebherr_365_gesundheitsapp.Database.DataSourceData;
import com.example.liebherr_365_gesundheitsapp.R;

public class SingleDataRecord extends DialogFragment {
    Context context;
    private DataSourceData dataSourceData;

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {

        // get value from bundle
        Bundle bundle = this.getArguments();
        final String bundledatum = bundle.getString("date");

        // get context
        context = getActivity().getApplicationContext();

        // make dialog object
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // get the layout inflater
        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // inflate our custom layout for the dialog to a View
        final View view = li.inflate(R.layout.singledatarecord, null);

        // inform the dialog it has a custom View
        builder.setView(view);

        // setOnClickListener on Button update
        Button buttonupdate = (Button) view.findViewById(R.id.update);
        buttonupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // new DBHelperDataSource
                dataSourceData = new DataSourceData(context);
                dataSourceData.open();

                //call function deletedb
                String ModulWeight = "ModulWeight";
                dataSourceData.deletedb(ModulWeight);

                Log.d("closesql", "<DATA>Die Datenquelle wird geschlossen.<DATA>");
                dataSourceData.close();

                //close NumberPickerModulWeight
                getDialog().dismiss();
            }
        });

        // setOnClickListener on Button delete
        Button buttondelete = (Button) view.findViewById(R.id.delete);
        buttondelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // declare String datum
                String datum = formateDatum(bundledatum);

                // new DBHelperDataSource
                dataSourceData = new DataSourceData(context);
                dataSourceData.open();

                // declare String ModulWeight
                String ModulWeightString = "ModulWeight";

                Data data = new Data(ModulWeightString, datum);

                //call function deletesingledata
                dataSourceData.deletesingledata(data);

                ModulWeight.adapter.changeCursor(dataSourceData.getPreparedCursorForWeightList());
                ModulWeight.changeButtons();

                Log.d("closesql", "<DATA>Die Datenquelle wird geschlossen.<DATA>");
                dataSourceData.close();

                //close NumberPickerModulWeight

                getDialog().dismiss();


            }
        });
        return view;
    }

    // funtion formateDatum
    private String formateDatum(String datum) {
        String day = datum.substring(0, 2);
        String month = datum.substring(3, 5);
        String year = datum.substring(6, 10);
        datum = year + "-" + month + "-" + day;
        return datum;
    }
}