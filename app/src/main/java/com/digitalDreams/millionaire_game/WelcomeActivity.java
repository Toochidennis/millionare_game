package com.digitalDreams.millionaire_game;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.digitalDreams.millionaire_game.slider.AskComputer;
import com.digitalDreams.millionaire_game.slider.MinusTwo;
import com.digitalDreams.millionaire_game.slider.ResetQuestion;
import com.digitalDreams.millionaire_game.slider.TakeAPoll;

import java.util.Timer;
import java.util.TimerTask;

public class WelcomeActivity extends AppCompatActivity {
    RelativeLayout bg;
    ViewPager viewpager;
    private PageViewAdapter viewPagerAdapter;
    Timer timer;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        viewpager = findViewById(R.id.viewpager);


        SharedPreferences sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE);
        String languageCode = sharedPreferences.getString("language","en");
        int endcolor = sharedPreferences.getInt("end_color",getResources().getColor(R.color.purple_dark));
        int startColor = sharedPreferences.getInt("start_color",getResources().getColor(R.color.purple_500));
        int cardBackground = sharedPreferences.getInt("card_background",0x219ebc);
        String highscore = sharedPreferences.getString("high_score","0");
        String game_level = sharedPreferences.getString("game_level","1");
        boolean  IS_DONE_INSERTING = sharedPreferences.getBoolean("IS_DONE_INSERTING",false);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String username = sharedPreferences.getString("username","");

        if(IS_DONE_INSERTING){



            Utils.IS_DONE_INSERTING = true;
            editor.putBoolean("IS_DONE_INSERTING",true);
            editor.commit();

            startActivity(new Intent(WelcomeActivity.this,Dashboard.class));
            finish();

        }

       // setLocale(this,languageCode);

        // setting up the adapter
        viewPagerAdapter = new PageViewAdapter(getSupportFragmentManager());
        progressBar =  findViewById(R.id.progress);

        // add the fragments
        viewPagerAdapter.add(new MinusTwo(), "Page 1");
        viewPagerAdapter.add(new AskComputer(), "Page 2");
        viewPagerAdapter.add(new TakeAPoll(), "Page 2");
        viewPagerAdapter.add(new ResetQuestion(), "Page 2");
       // viewPagerAdapter.add(new Page3(), "Page 3");

        viewpager.setAdapter(viewPagerAdapter);

        bg = findViewById(R.id.rootview);

        new Particles(this,bg,R.layout.image_xml,20);
        GradientDrawable gd = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[] {startColor,endcolor});

        bg.setBackgroundDrawable(gd);
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {

                progressBar.setProgress((Utils.NUMBER_OF_INSERT*8));
                progressBar.setMax(100);
                Log.i("check--","Checkoooo");
                if(Utils.IS_DONE_INSERTING || IS_DONE_INSERTING){
                    Intent i =  new Intent(WelcomeActivity.this,Dashboard.class);
                    startActivity(i);
                    finish();
                }





                viewpager.post(new Runnable(){

                    @Override
                    public void run() {
                        viewpager.setCurrentItem((viewpager.getCurrentItem()+1)%viewPagerAdapter.getCount());
                    }
                });
            }
        };
        timer = new Timer();
        timer.schedule(timerTask, 3000, 3000);


    }
    @Override
    protected void onDestroy() {
        timer.cancel();
        super.onDestroy();
    }

}