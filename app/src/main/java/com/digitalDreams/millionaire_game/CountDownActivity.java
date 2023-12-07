package com.digitalDreams.millionaire_game;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.rewarded.RewardedAd;


import java.text.DecimalFormat;


public class CountDownActivity extends AppCompatActivity {
    String time = "5000";
    DBHelper dbHelper;
    public static MediaPlayer mMediaPlayer;
    public static MediaPlayer mSuccessPlayer;
    public static MediaPlayer mFailurePlayer;
    boolean hasOldWinningAmount = false;
    //  public static RewardedAd mRewardedVideoAd;
    TextView count_down_level;
    TextView amount_to_win;
    // AdManager adManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count_down);

        AdManager.initInterstitialAd(this);
        AdManager.initRewardedVideo(CountDownActivity.this);


        // mRewardedVideoAd = AdManager.rewardedAd; //MobileAds.getRewardedVideoAdInstance(this);

        loadVideoAd();


//         dbHelper = new DBHelper(this);
//         dbHelper.close();
//         String json = dbHelper.buildJson();
        hasOldWinningAmount = getIntent().getBooleanExtra("hasOldWinningAmount", false);


        SharedPreferences sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE);
        String languageCode = sharedPreferences.getString("language", "en");
        int endcolor = sharedPreferences.getInt("end_color", getResources().getColor(R.color.purple_dark));
        int startColor = sharedPreferences.getInt("start_color", getResources().getColor(R.color.purple_500));
        String game_level = sharedPreferences.getString("game_level", "1");

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE);

        RelativeLayout bg = findViewById(R.id.rootview);
        new Particles(this, bg, R.layout.image_xml, 20);
        GradientDrawable gd = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{startColor, endcolor});

        bg.setBackgroundDrawable(gd);
        resetData();

        ////// set game and level ///////////
        amount_to_win = findViewById(R.id.amount_to_win);
        count_down_level = findViewById(R.id.level);
        String pattern = "#,###,###.###";
        DecimalFormat decimalFormat = new DecimalFormat(pattern);

        int amount_to_win_int = Integer.parseInt(game_level) * 1000000;
        String level_string = "Level " + game_level;
        amount_to_win.setText("$" + decimalFormat.format(amount_to_win_int));
        count_down_level.setText(level_string);

        ////

        TextView counterText = findViewById(R.id.count_down_text);
        Intent intent = getIntent();
        String response = intent.getStringExtra("Json");
        String from = intent.getStringExtra("from");
        String courseName = intent.getStringExtra("course");
        long countdownTime = Long.parseLong(time);

        CountDownTimer timer = new CountDownTimer(countdownTime, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int sec = (int) (millisUntilFinished / 1000);
                int seconds = (sec % 3600) % 60;
                counterText.setText("" + seconds);
            }

            @Override
            public void onFinish() {
//                Intent intent = new Intent(CountDownActivity.this,GameActivity2.class);
//                intent.putExtra("Json",json);
//                if(hasOldWinningAmount){
//                    intent.putExtra("hasOldWinningAmount",true);
//                }
//                intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
//                startActivity(intent);
                GameActivity2.isStartAtFresh = false;
                finish();
            }
        };
        timer.start();
        loadSongs();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void loadSongs() {
        new Handler().post(() -> {

            try {
                mMediaPlayer = MediaPlayer.create(CountDownActivity.this, R.raw.background_sound);
                mFailurePlayer = MediaPlayer.create(CountDownActivity.this, R.raw.failure_sound2);
                mSuccessPlayer = MediaPlayer.create(CountDownActivity.this, R.raw.success_sound);
            } catch (Exception e) {
                e.printStackTrace();
            }

        });

    }

    public void loadVideoAd() {
//        // Load a reward based video ad
//        if(!mRewardedVideoAd.isLoaded()){
//            Log.i("mRewardedVideoAd","Not LOaded");
//            mRewardedVideoAd.loadAd("ca-app-pub-4696224049420135/7768937909", new AdRequest.Builder().build());
//        }else{
//            Log.i("mRewardedVideoAd","LOaded");
//
//        }


    }

    @Override
    protected void onStart() {
        loadVideoAd();
        super.onStart();
    }

    void resetData() {
        SharedPreferences sharedPref = getSharedPreferences("application_data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.apply();
    }
}