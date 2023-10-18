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
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public  class CountryJsonAdapter  extends RecyclerView.Adapter<CountryJsonAdapter.CountryAdapterVH> {
    ArrayList<JSONObject> jsonObjects;
    Context context;


    public CountryJsonAdapter( Context context,ArrayList<JSONObject> jsonObjects) {
        this.jsonObjects = jsonObjects;
        this.context = context;
    }

    @NonNull
    @Override
    public CountryAdapterVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view;
        view = layoutInflater.inflate(R.layout.leader_score_item, parent, false);
        return new CountryJsonAdapter.CountryAdapterVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CountryAdapterVH holder, int position) {

        try {

            JSONObject object = jsonObjects.get(position);
            String score = object.getString("score");
            String country_name = object.getString("country");
            String country_json_string = object.getString("country_json");
            JSONObject country_json_object = new JSONObject(country_json_string.replaceAll("[\\\\]{1}[\"]{1}", "\""));
            String country_flag = country_json_object.getString("url");
            if (!country_flag.isEmpty()) {
                SVGLoader.fetchSvg(context, country_flag, holder.flag_img);

                //Picasso.get().load(country_flag).into(holder.flag_img);
                holder.flag_img.setVisibility(View.VISIBLE);
                //holder.flag_img.setImageResource(R.drawable.bad);

            }

            /////
            holder.countryTxt.setText(country_name);

            ///holder.flag_img.setImageResource();

            holder.positionTXT.setText(String.valueOf(position+1));
            holder.playerNameTxt.setText(country_name);
            holder.playerScoreTxt.setText(score);
        }catch (Exception e){
            Log.i("ogaboss","AnyErro");
            e.printStackTrace();
        }

    }


    @Override
    public int getItemCount() {
        return jsonObjects.size();
    }

    class CountryAdapterVH extends RecyclerView.ViewHolder {
        TextView positionTXT, playerNameTxt, playerScoreTxt, countryTxt;
        ImageView flag_img;

        public CountryAdapterVH(@NonNull View itemView) {
            super(itemView);
            positionTXT = itemView.findViewById(R.id.position);
            playerNameTxt = itemView.findViewById(R.id.player_name);
            playerScoreTxt = itemView.findViewById(R.id.highscore);
            countryTxt = itemView.findViewById(R.id.country);
            countryTxt.setVisibility(View.GONE);
            flag_img = itemView.findViewById(R.id.flag_img);
            flag_img.setVisibility(View.GONE);
        }
    }
}
