package com.digitalDreams.millionaire_game;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.LeaderBoardVH> {
    private Context context;
    private List<PlayerObject> list;

    public LeaderboardAdapter(Context context, List<PlayerObject> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public LeaderBoardVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view;
        view = layoutInflater.inflate(R.layout.leader_score_item, parent, false);
        return new LeaderBoardVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LeaderBoardVH holder, int position) {
        PlayerObject obj = list.get(position);
        int newPosition = Integer.parseInt(obj.getPosition()) + 1;

        String rank = String.valueOf(newPosition);

        //String rank = obj.getPosition();
        String playerName = obj.getPlayerName();
        String highscore = obj.getHighscore();
        String country = obj.getCountry();
        String country_flag = obj.getCountry_flag();
        if (!country_flag.isEmpty() && country_flag.length() > 4) {
            try {
                SVGLoader.fetchSvg(context, country_flag, holder.flag_img);
            } catch (Exception e) {
                e.printStackTrace();
            }

            holder.flag_img.setVisibility(View.VISIBLE);

            holder.flag_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    gotToCountry(country, country_flag);
                }
            });

        }

        /////
        holder.countryTxt.setText(Html.fromHtml("<u>" + country + "</u>"));
        holder.countryTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                gotToCountry(country, country_flag);

            }
        });

        ///holder.flag_img.setImageResource();

        holder.positionTXT.setText(rank);
        holder.playerNameTxt.setText(playerName);
        holder.playerScoreTxt.setText(highscore);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class LeaderBoardVH extends RecyclerView.ViewHolder {
        TextView positionTXT, playerNameTxt, playerScoreTxt, countryTxt;
        ImageView flag_img;

        public LeaderBoardVH(@NonNull View itemView) {
            super(itemView);
            positionTXT = itemView.findViewById(R.id.position);
            playerNameTxt = itemView.findViewById(R.id.player_name);
            playerScoreTxt = itemView.findViewById(R.id.highscore);
            countryTxt = itemView.findViewById(R.id.country);
            flag_img = itemView.findViewById(R.id.flag_img);
            flag_img.setVisibility(View.GONE);
        }
    }

    public void gotToCountry(String country_name, String country_flag) {

        Intent intent = new Intent(context, CountryLeaderBoard.class);
        ///Utils.darkBlueBlink(convertView,getApplicationContext());


// /                  Map<String,String> map = new HashMap<>();
///                   map.put("country",country);
///                   map.put("country_flag",country_flag);
        //intent.putExtra("country_map", (Serializable) map);
        intent.putExtra("country", country_name);
        intent.putExtra("country_flag", country_flag);
        context.startActivity(intent);
    }


}
