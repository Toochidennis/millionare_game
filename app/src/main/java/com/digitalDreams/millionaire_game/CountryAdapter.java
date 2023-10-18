package com.digitalDreams.millionaire_game;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import java.util.ArrayList;

public class CountryAdapter extends ArrayAdapter {
    ArrayList<String> countries;
    ArrayList<String> flags;
    Context context;

    public CountryAdapter(@NonNull Context context, ArrayList<String> countries, ArrayList<String> flags) {
        super(context, 0, countries);
        this.countries =countries;
        this.flags = flags;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        return initView(position, convertView, parent);
        // return super.getView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable
            View convertView, @NonNull ViewGroup parent)
    {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView,
                          ViewGroup parent)
    {
        // It is used to set our custom view.
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.single_country, parent, false);
        }
       // ImageView flag = convertView.findViewById(R.id.flag);
        TextView name = convertView.findViewById(R.id.name);
        name.setText(countries.get(position));
      //  SVGLoader.fetchSvg(context, flags.get(position), flag);



        return convertView;
    }


}
