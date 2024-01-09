package com.digitalDreams.millionaire_game;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.animation.ObjectAnimator;


public class WelcomeActivity extends AppCompatActivity {
    RelativeLayout rootView;
    ImageView loadingImageView;
    //  Timer timer;
    // ProgressBar progressBar;

    private static final int CHECK_INTERVAL = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        initViews();
    }

    private void initViews() {
        rootView = findViewById(R.id.rootView);
        loadingImageView = findViewById(R.id.loadingImageView);

        SharedPreferences sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE);
        String languageCode = sharedPreferences.getString("language", "en");
        int endColor = sharedPreferences.getInt("end_color", getResources().getColor(R.color.purple_dark));
        int startColor = sharedPreferences.getInt("start_color", getResources().getColor(R.color.purple_500));
        int cardBackground = sharedPreferences.getInt("card_background", 0x219ebc);
        String highScore = sharedPreferences.getString("high_score", "0");
        String gameLevel = sharedPreferences.getString("game_level", "1");
        boolean IS_DONE_INSERTING = sharedPreferences.getBoolean("IS_DONE_INSERTING", false);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String username = sharedPreferences.getString("username", "");

 /*       if (IS_DONE_INSERTING) {
            Utils.IS_DONE_INSERTING = true;
            editor.putBoolean("IS_DONE_INSERTING", true);
            editor.commit();

            startActivity(new Intent(WelcomeActivity.this, Dashboard.class));
            finish();
        }*/


        new Particles(this, rootView, R.layout.image_xml, 20);
        GradientDrawable gd = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{startColor, endColor});

        // bg.setBackgroundDrawable(gd);
        rootView.setBackground(gd);

        animateImage("scaleX");
        animateImage("scaleY");

        checkBackgroundThreadStatus();


/*        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if (Utils.IS_DONE_INSERTING || IS_DONE_INSERTING) {
                    Intent i = new Intent(WelcomeActivity.this, Dashboard.class);
                    startActivity(i);
                    finish();
                }
            }
        };

        new Timer().schedule(timerTask, 3000, 3000);*/
    }

    // New code added for animating logo
    private void animateImage(String propertyName) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(loadingImageView, propertyName, 1f, 1.2f);
        animator.setDuration(1000);
        animator.setRepeatMode(ObjectAnimator.REVERSE);
        animator.setRepeatCount(ObjectAnimator.INFINITE);
        animator.start();
    }

    // New code added
    private void checkBackgroundThreadStatus() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Utils.IS_DONE_INSERTING) {
                    startActivity(new Intent(WelcomeActivity.this, Dashboard.class));
                    finish();
                } else {
                    handler.postDelayed(this, CHECK_INTERVAL);
                }
            }
        }, CHECK_INTERVAL);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}