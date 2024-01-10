package com.digitalDreams.millionaire_game;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.transition.Slide;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DecimalFormat;

public class ProgressActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAnimation();
        setContentView(R.layout.activity_progress);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE);
        RelativeLayout bg = findViewById(R.id.rootview);
        SharedPreferences sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE);
        String languageCode = sharedPreferences.getString("language", "en");
        int cardBackground = sharedPreferences.getInt("card_background", 0x03045e);
        int endcolor = sharedPreferences.getInt("end_color", getResources().getColor(R.color.purple_dark));
        int startColor = sharedPreferences.getInt("start_color", getResources().getColor(R.color.purple_500));
        GradientDrawable gd = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{startColor, endcolor});

        bg.setBackgroundDrawable(gd);
        ImageView closeBtn = findViewById(R.id.close);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        Intent intent = getIntent();
        int position = intent.getIntExtra("number", 0);
        String timer = intent.getStringExtra("timer");
        if (timer.equals("true")) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finishPage();
                }
            }, 4000);
        }

        LinearLayout container = findViewById(R.id.container);
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        for (int a = 0; a < GameActivity2.moneyArr.length; a++) {
            View v = layoutInflater.inflate(R.layout.amount_item, container, false);
            TextView pText = v.findViewById(R.id.position);
            RelativeLayout r = v.findViewById(R.id.t1);
            TextView amountText = v.findViewById(R.id.amount);
            pText.setText(String.valueOf(a + 1));

            DecimalFormat formatter = new DecimalFormat("#,###,###");
            String formatted_newAmount = formatter.format(GameActivity2.moneyArr[a]);
            //editor.putString("amountWon","$"+formatted_newAmount);
            amountText.setText("$" + formatted_newAmount);
            try {
                slideInAnimation(v, a);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (a == position - 1) {
                try {
                    VectorDrawable g = (VectorDrawable) r.getBackground().mutate();
                    g.setColorFilter(ContextCompat.getColor(ProgressActivity.this, R.color.orange), PorterDuff.Mode.SRC_IN);
                    r.setBackground(g);
                } catch (Exception e) {
                    BitmapDrawable g = (BitmapDrawable) r.getBackground().mutate();
                    g.setColorFilter(ContextCompat.getColor(ProgressActivity.this, R.color.orange), PorterDuff.Mode.SRC_IN);
                    r.setBackground(g);
                }
            } else if (a < position - 1) {

                try {

                    //this code will be executed on devices running ICS or later
                    VectorDrawable g = (VectorDrawable) r.getBackground().mutate();
                    g.setColorFilter(ContextCompat.getColor(ProgressActivity.this, R.color.green), PorterDuff.Mode.SRC_IN);
                    r.setBackground(g);
                } catch (Exception e) {

//                    BitmapDrawable   g = (BitmapDrawable) r.getBackground().mutate();
//                    g.setColorFilter(ContextCompat.getColor(ProgressActivity.this,R.color.green), PorterDuff.Mode.SRC_IN);
//                    r.setBackground(g);

                }


            }
            container.addView(v);
        }
    }

    @Override
    public void onBackPressed() {
        GameActivity2.fromProgress = true;
        GameActivity2.fromProgress2 = true;
        super.onBackPressed();

    }

    public void finishPage() {
        GameActivity2.fromProgress = true;
        GameActivity2.fromProgress2 = true;
        finish();

    }

    public void setAnimation() {
        if (Build.VERSION.SDK_INT > 20) {
            Slide slide = new Slide();
            slide.setSlideEdge(Gravity.LEFT);
            slide.setDuration(800);
            slide.setInterpolator(new AccelerateDecelerateInterpolator());
            getWindow().setExitTransition(slide);
            getWindow().setEnterTransition(slide);
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        }
    }

    public void slideInAnimation(View currentItemView, long position) {
        final AnimationSet set = new AnimationSet(false);
        Animation inFromLeft = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, -1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);

        Animation animation = AnimationUtils.loadAnimation(ProgressActivity.this, R.anim.slide_in_right);

        //animation.setStartOffset(1000);


        set.addAnimation(inFromLeft);
        // set.addAnimation(fadeIn);
        animation.setDuration(300);


        animation.setStartOffset(50 * (position + 1));
        currentItemView.startAnimation(animation);
        View finalCurrentItemView = currentItemView;
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // moveRight(finalCurrentItemView,1000,10);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }


}