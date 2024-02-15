package com.digitalDreams.millionaire_game.alpha.activity;

import static com.digitalDreams.millionaire_game.alpha.Constants.FROM_PROGRESS;
import static com.digitalDreams.millionaire_game.alpha.Constants.PREF_NAME;
import static com.digitalDreams.millionaire_game.alpha.Constants.SOUND;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.RelativeLayout;

import com.digitalDreams.millionaire_game.R;
import com.digitalDreams.millionaire_game.alpha.Constants;
import com.digitalDreams.millionaire_game.alpha.adapters.ProgressAdapter;

import java.util.List;

public class ProgressActivity2 extends AppCompatActivity {

    private RelativeLayout exitButton, rootView;
    private RecyclerView progressRecyclerView;

    private List<Integer> previousAmountList;

    private SharedPreferences sharedPreferences;
    private boolean shouldUseTimer = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE);

        animateRoot();
        setContentView(R.layout.activity_progress2);

        initViews();
    }

    private void initViews() {
        exitButton = findViewById(R.id.exit_container);
        progressRecyclerView = findViewById(R.id.progress_recyclerView);
        rootView = findViewById(R.id.rootView);

        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        Intent intent = getIntent();
        previousAmountList = intent.getIntegerArrayListExtra("amount_won");
        shouldUseTimer = intent.getBooleanExtra("should_use_timer", false);

        System.out.println(previousAmountList);

        initDisplay();
    }

    private void initDisplay() {
        setRootViewBackgroundColor();
        setUpAmountAdapter();

        exitButton.setOnClickListener(exit -> onBackPressed());

        if (shouldUseTimer) {
            new Handler().postDelayed(this::finish, 4000);
        }
    }

    private void setRootViewBackgroundColor() {
        int endColor = sharedPreferences.getInt("end_color", getResources().getColor(R.color.purple_dark));
        int startColor = sharedPreferences.getInt("start_color", getResources().getColor(R.color.purple_500));

        RelativeLayout rootView = findViewById(R.id.rootView);

        GradientDrawable gradientDrawable =
                new GradientDrawable(
                        GradientDrawable.Orientation.TOP_BOTTOM,
                        new int[]{startColor, endColor}
                );

        rootView.setBackground(gradientDrawable);
    }

    private void setUpAmountAdapter() {
        String gameLevel = sharedPreferences.getString("game_level", "1");
        int level = Integer.parseInt(gameLevel);

        List<Integer> amountList = Constants.generateAmount(level);

        ProgressAdapter progressAdapter = new ProgressAdapter(amountList, previousAmountList);

        progressRecyclerView.hasFixedSize();
        progressRecyclerView.setAdapter(progressAdapter);
    }


    private void animateRoot() {
        Slide slide = new Slide();
        slide.setSlideEdge(Gravity.START);
        slide.setDuration(800);
        slide.setInterpolator(new AccelerateDecelerateInterpolator());
        getWindow().setExitTransition(slide);
        getWindow().setEnterTransition(slide);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @Override
    protected void onPause() {
        super.onPause();
        updateSoundState();
    }

    @Override
    protected void onStop() {
        super.onStop();
        updateSoundState();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        updateSoundState();
    }

    private void updateSoundState() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(SOUND, false);
        editor.putBoolean(FROM_PROGRESS, true);
        editor.apply();
    }
}