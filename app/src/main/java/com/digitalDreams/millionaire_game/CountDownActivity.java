package com.digitalDreams.millionaire_game;

import static com.digitalDreams.millionaire_game.alpha.AudioManager.playBackgroundMusic;
import static com.digitalDreams.millionaire_game.alpha.Constants.APPLICATION_DATA;
import static com.digitalDreams.millionaire_game.alpha.Constants.DELAY_INTERVAL_LONG;
import static com.digitalDreams.millionaire_game.alpha.Constants.PREF_NAME;
import static com.digitalDreams.millionaire_game.alpha.Constants.SHOULD_CONTINUE_GAME;
import static com.digitalDreams.millionaire_game.alpha.Constants.SOUND;
import static com.digitalDreams.millionaire_game.alpha.Constants.formatCurrency;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;


public class CountDownActivity extends AppCompatActivity {
    public static MediaPlayer mMediaPlayer;
    public static MediaPlayer mSuccessPlayer;
    public static MediaPlayer mFailurePlayer;
    TextView count_down_level;
    TextView amount_to_win;
    // AdManager adManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count_down);

        AdManager.loadInterstitialAd(this);
        AdManager.loadRewardedAd(CountDownActivity.this);

        RelativeLayout relativeLayout = findViewById(R.id.rootview);
        amount_to_win = findViewById(R.id.amount_to_win);
        count_down_level = findViewById(R.id.level);
        TextView counterText = findViewById(R.id.count_down_text);


        boolean fromWinners = getIntent().getBooleanExtra("fromWinners", false);

        isFromWinnersActivity(fromWinners);

        SharedPreferences sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE);
        int endColor = sharedPreferences.getInt("end_color", getResources().getColor(R.color.purple_dark));
        int startColor = sharedPreferences.getInt("start_color", getResources().getColor(R.color.purple_500));
        String game_level = sharedPreferences.getString("game_level", "1");
        String sound = sharedPreferences.getString("sound", "1");


        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE);

        new Particles(this, relativeLayout, R.layout.image_xml, 20);

        GradientDrawable gradientDrawable = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{startColor, endColor}
        );

        relativeLayout.setBackground(gradientDrawable);

        resetData();

        ////// set game and level ///////////
        try {
            double amountToWinInt = Integer.parseInt(game_level) * 1000000;
            String levelText = getString(R.string.count_down_level) + " " + game_level;
            String amountToWinText = String.format(Locale.getDefault(), "$%s", formatCurrency(amountToWinInt));

            amount_to_win.setText(amountToWinText);
            count_down_level.setText(levelText);
        } catch (Exception e) {
            e.printStackTrace();
        }


        long countdownTime = DELAY_INTERVAL_LONG * 5;

        CountDownTimer timer = new CountDownTimer(countdownTime, DELAY_INTERVAL_LONG) {
            @Override
            public void onTick(long millisUntilFinished) {
                int sec = (int) (millisUntilFinished / DELAY_INTERVAL_LONG);
                int seconds = (sec % 3600) % 60;
                counterText.setText(String.valueOf(seconds));
            }

            @Override
            public void onFinish() {
                //  GameActivity2.isStartAtFresh= false;
                finish();
                updateSoundState();
                playBackgroundMusic(CountDownActivity.this);
            }
        };

        timer.start();
        //loadSongs();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    void resetData() {
        SharedPreferences sharedPref = getSharedPreferences(APPLICATION_DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.putBoolean(SHOULD_CONTINUE_GAME, false);
        editor.apply();
    }

    private void updateSoundState() {
        SharedPreferences sharedPref = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(SOUND, false);
        editor.apply();
    }

    private void isFromWinnersActivity(boolean isWon) {
        SharedPreferences sharedPref = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("amountWon", "0");
        editor.putBoolean("isFinishLevel", isWon);
        editor.apply();
    }
}