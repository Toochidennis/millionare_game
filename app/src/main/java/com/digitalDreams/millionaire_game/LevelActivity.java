package com.digitalDreams.millionaire_game;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.interstitial.InterstitialAd;

import org.json.JSONObject;

import java.util.ArrayList;

public class LevelActivity extends AppCompatActivity {
    RelativeLayout bg;
    ListView listView;
    LevelListAdapter levelListAdapter;
    DBHelper dbHelper;
    ArrayList arrayList;

    public static InterstitialAd interstitialAd;
    //AdManager adManager;


    @Override
    public void onBackPressed() {
        showInterstitial();
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);

       // adManager =  new AdManager(this);

        AdManager.loadInterstitialAd(this);
        AdManager.loadRewardedAd(this);



        /////////////////AD////////

        AdView mAdView;
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        loadInterstialAd();


        /////////SETTINGS//////////////


        SharedPreferences sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE);
        String languageCode = sharedPreferences.getString("language","en");
        int endcolor = sharedPreferences.getInt("end_color",getResources().getColor(R.color.purple_dark));
        int startColor = sharedPreferences.getInt("start_color",getResources().getColor(R.color.purple_500));
        int cardBackground = sharedPreferences.getInt("card_background",0x219ebc);
        String highscore = sharedPreferences.getString("high_score","0");
        String game_level = sharedPreferences.getString("game_level","1");

        bg = findViewById(R.id.rootview);
        new Particles(this,bg,R.layout.image_xml,20);
        GradientDrawable gd = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[] {startColor,endcolor});

        bg.setBackgroundDrawable(gd);

        /////////////////END OF SETTINGS///////////////

        listView = findViewById(R.id.listview);
        dbHelper = new DBHelper(this);
        arrayList = new ArrayList();

        levelListAdapter = new LevelListAdapter(this,arrayList,game_level);
        listView.setAdapter(levelListAdapter);
        //listView.setAnimation(AnimationUtils.loadAnimation(LevelActivity.this,R.anim.slide_in_right));

        levelListAdapter.notifyDataSetChanged();



        Cursor res = dbHelper.getLevels();
        ArrayList checks = new ArrayList();
        checks.clear();
        while (res.moveToNext()) {

            @SuppressLint("Range") String id = res.getString(res.getColumnIndex("ID"));
            @SuppressLint("Range") String level_name = res.getString(res.getColumnIndex("STAGE_NAME"));
            @SuppressLint("Range") String level = res.getString(res.getColumnIndex("STAGE"));
            JSONObject obj = new JSONObject();




            try{
                obj.put("level_name",level_name);
                obj.put("level",level);
//                Log.i("level_name",level_name);
//                Log.i("level",level);

                if(!checks.contains(level)){
                    checks.add(level);
                    arrayList.add(obj);
                    levelListAdapter.notifyDataSetChanged();


                }



            }catch(Exception e){}



        }




    }


    private void loadInterstialAd(){
        interstitialAd = AdManager.interstitialAd; // new InterstitialAd(LevelActivity.this) ;
//        interstitialAd.setAdUnitId (LevelActivity.this.getResources().getString(R.string.interstitial_adunit) ) ;
//        interstitialAd.loadAd(new AdRequest.Builder().build());
        AdManager.showInterstitial(LevelActivity.this);
    }

    private void showInterstitial() {
//        if (interstitialAd.isLoaded()) {
//            interstitialAd.show();
//        }else{
//            interstitialAd.setAdListener(new AdListener() {
//                public void onAdLoaded() {
//                    showInterstitial();
//                }
//            });
//        }
        AdManager.showInterstitial(LevelActivity.this);
    }




}