package com.example.liebherr_365_gesundheitsapp.ModulDrink;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.liebherr_365_gesundheitsapp.Database.Queries;
import com.example.liebherr_365_gesundheitsapp.R;

class CursorAdapterDrink extends CursorAdapter {

    CursorAdapterDrink(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.historie_list, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView textViewdatum = (TextView) view.findViewById(R.id.datum);
        TextView textViewweight = (TextView) view.findViewById(R.id.data);

        //set text datum
        String datum = cursor.getString(getCursor().getColumnIndexOrThrow(Queries.getColumnDate()));
        datum = formateDatum(datum);
        textViewdatum.setText(datum);

        //set text glasses
        String glasses = cursor.getString(cursor.getColumnIndexOrThrow(Queries.getColumnPhysicalValues()));

        // add type
        if (glasses.equals("1")) {
            glasses += " Glas";
        } else {
            glasses += " Gläser";
        }

        textViewweight.setText(glasses);
    }

    // funtion formateDatum
    private String formateDatum(String datum) {
        String year = datum.substring(0, 4);
        String month = datum.substring(5, 7);
        String day = datum.substring(8, 10);
        datum = day + "." + month + "." + year;
        return datum;
    }
}
