package com.digitalDreams.millionaire_game;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class trash extends ArrayAdapter {
    ArrayList<JSONObject> jsonObjects;
    Context context;
    public trash(@NonNull Context context, ArrayList jsonObjects) {
        super(context, 0,jsonObjects);
        this.jsonObjects = jsonObjects;
        this.context = context;




    }


    @Override
    public int getCount() {
        Log.i("Sizzzeee",String.valueOf(jsonObjects.size()));
        return jsonObjects.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.leader_score_item, parent, false);

        TextView substring = convertView.findViewById(R.id.highscore);
        TextView player_name = convertView.findViewById(R.id.player_name);
        TextView country = convertView.findViewById(R.id.country);
        ImageView flag = convertView.findViewById(R.id.flag_img);

        try {
            JSONObject object = jsonObjects.get(position);
            String score = object.getString("score");
            String country_name = object.getString("country");
            String country_json_string = object.getString("country_json");
            JSONObject  country_json_object =  new JSONObject(country_json_string.replaceAll("[\\\\]{1}[\"]{1}","\""));
            String country_flag = country_json_object.getString("url");

            if(!country_flag.isEmpty())
                SVGLoader.fetchSvg(context,country_flag,flag);


            Log.i("ooooopp111",String.valueOf(object));

            player_name.setText(country_name);
            substring.setText("$"+score);
            country.setText(country_name);


            //Value {"name":"Ascension Island","url":""} at country_json of type java.lang.String cannot be converted to JSONObject

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context,CountryLeaderBoard.class);
//                   Map<String,String> map = new HashMap<>();
//                   map.put("country",country);
//                   map.put("country_flag",country_flag);
                    //intent.putExtra("country_map", (Serializable) map);
                    intent.putExtra("country",country_name);
                    context.startActivity(intent);
                }
            });


        }catch (Exception e){
            Log.i("ooooopp",e.toString());
            e.printStackTrace();

        }

        return convertView;
    }
}

