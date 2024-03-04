package com.digitalDreams.millionaire_game;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.digitalDreams.millionaire_game.alpha.AudioManager;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class HistoryDetails extends AppCompatActivity {
    RelativeLayout bg;
    DBHelper dbHelper;
    HistoryDetailsAdapter historyAdapter;
    RecyclerView recyclerview;
    ImageView arrow_back;
    RelativeLayout close_container;
    //ListView listView;
    // TextView high_score;
    String date_played;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        SharedPreferences sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE);
        String highscore = sharedPreferences.getString("high_score", "0");
        String regex = "[^0-9]";
        String cleanedHighScore = highscore.replaceAll(regex, "");

        AdManager.loadInterstitialAd(this);
        date_played = getIntent().getStringExtra("date_played");

        AdView mAdView;
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        dbHelper = new DBHelper(this);
        recyclerview = findViewById(R.id.recyclerview);
        JSONArray jsonArray = dbHelper.buildHistoriesByDateTime(date_played);
        historyAdapter = new HistoryDetailsAdapter(this, jsonArray, Utils.addCommaAndDollarSign(Double.parseDouble(cleanedHighScore)));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setHasFixedSize(true);
        recyclerview.setAdapter(historyAdapter);

        arrow_back = findViewById(R.id.arrow_back);
        close_container = findViewById(R.id.close_container);
        //  high_score = findViewById(R.id.high_score);

        ArrayList arrayList = dbHelper.buildHistories2();
        ArrayList dates = (ArrayList) arrayList.get(0);
        JSONObject jsonObject = (JSONObject) arrayList.get(1);

//        HistoryContainerAdapter historyContainerAdapter = new HistoryContainerAdapter(this,dates,jsonObject);
//        listView.setAdapter(historyContainerAdapter);


        close_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AudioManager.darkBlueBlink(HistoryDetails.this, close_container);
                AdManager.showInterstitial(HistoryDetails.this);
                onBackPressed();


            }
        });

        arrow_back.setOnClickListener(view -> {
            AudioManager.darkBlueBlink(HistoryDetails.this, arrow_back);
            AdManager.showInterstitial(HistoryDetails.this);
            onBackPressed();

        });

        String languageCode = sharedPreferences.getString("language", "en");
        int endcolor = sharedPreferences.getInt("end_color", getResources().getColor(R.color.purple_dark));
        int startColor = sharedPreferences.getInt("start_color", getResources().getColor(R.color.purple_500));
        int cardBackground = sharedPreferences.getInt("card_background", 0x219ebc);
        String game_level = sharedPreferences.getString("game_level", "1");

        // high_score.setText(AudioManager.addCommaAndDollarSign(Double.parseDouble(highscore)));


        bg = findViewById(R.id.rootview);
        new Particles(this, bg, R.layout.image_xml, 20);
        GradientDrawable gd = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{startColor, endcolor});

        bg.setBackgroundDrawable(gd);

        dbHelper.buildHistories2();


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AudioManager.releaseMusicResources();
        AdManager.disposeAds();
    }
}