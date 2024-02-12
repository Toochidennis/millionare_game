package com.digitalDreams.millionaire_game;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.animation.ObjectAnimator;


public class WelcomeActivity extends AppCompatActivity {
    RelativeLayout rootView;
    ImageView loadingImageView;

    private static final int CHECK_INTERVAL = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE);
        initViews();
    }

    private void initViews() {
        rootView = findViewById(R.id.rootView);
        loadingImageView = findViewById(R.id.loadingImageView);

        SharedPreferences sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE);
        int endColor = sharedPreferences.getInt("end_color", getResources().getColor(R.color.purple_dark));
        int startColor = sharedPreferences.getInt("start_color", getResources().getColor(R.color.purple_500));

        new Particles(this, rootView, R.layout.image_xml, 20);
        GradientDrawable gradientDrawable = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{startColor, endColor});

        rootView.setBackground(gradientDrawable);

        animateImage("scaleX");
        animateImage("scaleY");

        checkBackgroundThreadStatus();

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