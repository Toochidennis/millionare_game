package com.digitalDreams.millionaire_game;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.interstitial.InterstitialAd;


import java.util.Locale;

public class Dashboard extends AppCompatActivity {
    //public static InterstitialAd interstitialAd;
    // public static boolean refresh =false;
    TextView newGameTxt, playTxt, leaderboardText, seeRankTxt, moreTxt, playTxt2, gotoYotubetxt;
    RelativeLayout bg;
    RelativeLayout newGameBtn, leaderBoardBtn, exitBtn, gotoYoutubeBtn, new_particle;
    ImageView settingBtn;
    //AdManager adManager;
    //DBHelper dbHelper;
    // public static RewardedVideoAd mRewardedVideoAd;
    AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);

        setContentView(R.layout.activity_dashboard);
        AdManager.initInterstitialAd(this);


        loadInterstialAd();
        //loadVideoAd();


        SharedPreferences sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE);
        String languageCode = sharedPreferences.getString("language", "en");
        int endcolor = sharedPreferences.getInt("end_color", getResources().getColor(R.color.purple_dark));
        int startColor = sharedPreferences.getInt("start_color", getResources().getColor(R.color.purple_500));
        int cardBackground = sharedPreferences.getInt("card_background", 0x219ebc);
        String highscore = sharedPreferences.getString("high_score", "0");
        String game_level = sharedPreferences.getString("game_level", "1");

        setLocale(this, languageCode);
        //setTheme();
        //dbHelper = new DBHelper(this);


        TextView highscoreTxt = findViewById(R.id.highscore);
        try {
            highscoreTxt.setText("$" + Utils.prettyCount(Integer.parseInt(highscore)));
        } catch (Exception e) {
        }
        AdView mAdView;
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE);
        newGameTxt = findViewById(R.id.new_game_text);
        gotoYotubetxt = findViewById(R.id.goto_youtube_txt);
        new_particle = findViewById(R.id.new_particle);
        playTxt = findViewById(R.id.play_text);
        playTxt2 = findViewById(R.id.play_text1);
        seeRankTxt = findViewById(R.id.see_rank_text);
        moreTxt = findViewById(R.id.more_text);
        leaderboardText = findViewById(R.id.leaderboard_text);
        bg = findViewById(R.id.rootview);
        new Particles(this, bg, R.layout.image_xml, 20);
        GradientDrawable gd = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{startColor, endcolor});

        bg.setBackgroundDrawable(gd);

        //bg.setBackgroundColor(backgroundcolor);
        newGameBtn = findViewById(R.id.new_game);
        gotoYoutubeBtn = findViewById(R.id.goto_youtube);
        RelativeLayout btnAnim = findViewById(R.id.btn_forAnim);
        new MyAnimation(btnAnim);
        leaderBoardBtn = findViewById(R.id.leaderboard);
        //exitBtn = findViewById(R.id.exit_game);
        settingBtn = findViewById(R.id.settings);
        /*newGameBtn.setBackgroundColor(cardBackground);
        leaderBoardBtn.setBackgroundColor(cardBackground);
        settingBtn.setBackgroundColor(cardBackground);*/


        newGameBtn.setOnClickListener(view -> {
            ///  newGameBtn.startAnimation(buttonClick);
            Utils.greenBlink(newGameBtn, getApplicationContext());

            Intent intent = new Intent(Dashboard.this, GameActivity2.class);
            startActivity(intent);
            finish();
        });

        gotoYoutubeBtn.setOnClickListener(view -> {
            try {
                Utils.darkBlueBlink(gotoYoutubeBtn, getApplicationContext());

                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/channel/UC7AAQdgwQ204aU5ztp19FKg")));
            } catch (Exception e) {
                e.printStackTrace();

            }
        });

        new_particle.setOnClickListener(view -> {
            try {
                MediaPlayer.create(Dashboard.this, R.raw.others).start();

            } catch (Exception e) {
                e.printStackTrace();
            }

            Intent i = new Intent(Dashboard.this, WinnersActivity.class);
            startActivity(i);

        });

        leaderBoardBtn.setOnClickListener(view -> {
            try {
                Utils.destination_activity = LeaderBoard.class;
                Utils.darkBlueBlink(leaderBoardBtn, getApplicationContext());
                MediaPlayer.create(Dashboard.this, R.raw.others).start();
            } catch (Exception e) {
                e.printStackTrace();
            }

            Intent intent = new Intent(Dashboard.this, LeaderBoard.class);
            startActivity(intent);
            finish();
        });


        settingBtn.setOnClickListener(view -> {
            settingBtn.startAnimation(buttonClick);
            try {
                MediaPlayer.create(Dashboard.this, R.raw.others).start();
            }catch (Exception e){
                e.printStackTrace();
            }

            Intent intent = new Intent(Dashboard.this, SettingActivity.class);
            startActivity(intent);

        });

        ImageView raysImg = findViewById(R.id.rays);
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);

        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setDuration(15000);
        rotateAnimation.setRepeatCount(Animation.INFINITE);

        raysImg.startAnimation(rotateAnimation);

        LinearLayout accountBtn = findViewById(R.id.account);
        accountBtn.setOnClickListener(view -> {
            Intent intent = new Intent(Dashboard.this, History.class);
            Utils.darkBlueBlink(accountBtn, Dashboard.this);

            startActivity(intent);
        });

        RelativeLayout moreBtn = findViewById(R.id.more_games);
        moreBtn.setOnClickListener(view -> {
            Utils.darkBlueBlink(moreBtn, getApplicationContext());
//                MediaPlayer.create(Dashboard.this, R.raw.others).start();
//                Utils.blink(moreBtn, R.drawable.ic_hex_2, R.drawable.ic_hexnow);
            // Uri uri = Uri.parse("https://play.google.com/store/apps/developer?id=DIGITAL+DREAMS+LIMITED");
            Uri uri = Uri.parse("https://www.facebook.com/MillionaireGameApp");

            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(uri);
                startActivity(intent);
            } catch (Exception e) {
            }
        });

        RelativeLayout profileBtn = findViewById(R.id.profile);
        profileBtn.setOnClickListener(view -> {
            Utils.darkBlueBlink(profileBtn, Dashboard.this);
            Utils.destination_activity = Dashboard.class;
            Intent intent = new Intent(Dashboard.this, UserDetails.class);
            intent.putExtra("type", "edit");
            startActivity(intent);
        });

//        WelcomeDialog welcomeDialog = new WelcomeDialog(this);
////        Window window = welcomeDialog.getWindow();
////        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//       // welcomeDialog.se
////        int width = (int)(getResources().getDisplayMetrics().widthPixels*1);
////        int height = (int)(getResources().getDisplayMetrics().heightPixels*1);
////
////        welcomeDialog.getWindow().setLayout(width, height);
//        welcomeDialog.show();
    }

    public static void setLocale(Activity activity, String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Resources resources = activity.getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }

    BroadcastReceiver refreshBroadCast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            SharedPreferences sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE);
            String languageCode = sharedPreferences.getString("language", "en");
            int endcolor = sharedPreferences.getInt("end_color", 0x230253);
            int startColor = sharedPreferences.getInt("start_color", 0xFF6200EE);
            int cardBackground = sharedPreferences.getInt("card_background", 0x03045e);
            if (SettingsDialog.soundTxt != null & SettingsDialog.soundBtn != null) {
                SettingsDialog.soundTxt.setText(getResources().getString(R.string.sound_on));
                SettingsDialog.soundBtn.setCardBackgroundColor(cardBackground);
            }
            if (SettingsDialog.gameModeTxt != null && SettingsDialog.gameModeBtn != null) {
                SettingsDialog.gameModeTxt.setText(getResources().getString(R.string.game_mode));
                SettingsDialog.gameModeBtn.setCardBackgroundColor(cardBackground);

            }
            if (SettingsDialog.themeTxt != null && SettingsDialog.selectThemeBtn != null) {
                SettingsDialog.themeTxt.setText(getResources().getString(R.string.select_a_theme));
                SettingsDialog.selectThemeBtn.setCardBackgroundColor(cardBackground);

            }
            if (SettingsDialog.vibrationTxt != null && SettingsDialog.vibrationBtn != null) {
                SettingsDialog.vibrationTxt.setText(getResources().getString(R.string.taptic));
                SettingsDialog.vibrationBtn.setCardBackgroundColor(cardBackground);

            }
            setLangauge(Dashboard.this);
            //SettingsDialog.languageBtn.setCardBackgroundColor(cardBackground);


            //newGameTxt.setText(getResources().getString(R.string.new_game));
            leaderboardText.setText(getResources().getString(R.string.leaderboard));


            setLocale(Dashboard.this, languageCode);
            //bg.setBackgroundColor(backgroundcolor);
            GradientDrawable gd = new GradientDrawable(
                    GradientDrawable.Orientation.TOP_BOTTOM,
                    new int[]{startColor, endcolor});

            bg.setBackgroundDrawable(gd);

            if (SettingActivity.bg != null) {
                String theme = sharedPreferences.getString("theme", "0");
                SettingActivity.bg.setBackgroundDrawable(gd);
                if (theme.equals("0")) {
                    SettingActivity.themeNameTxt.setText(getResources().getString(R.string.default_theme));
                } else if (theme.equals("1")) {
                    SettingActivity.themeNameTxt.setText(getResources().getString(R.string.theme_1));

                } else if (theme.equals("2")) {
                    SettingActivity.themeNameTxt.setText(getResources().getString(R.string.theme_2));
                } else if (theme.equals("3")) {
                    SettingActivity.themeNameTxt.setText(getResources().getString(R.string.theme_3));

                }
            }
            SettingActivity.soundTxt.setText(getResources().getString(R.string.sound_on));
            SettingActivity.themeTxt.setText(getResources().getString(R.string.select_a_theme));
            SettingActivity.vibrationTxt.setText(getResources().getString(R.string.taptic));
            SettingActivity.gameModeTxt.setText(getResources().getString(R.string.game_mode));
            SettingActivity.crediTxt.setText(getResources().getString(R.string.credits));
            SettingActivity.language.setText(getResources().getString(R.string.language));
            String sound = sharedPreferences.getString("sound", "1");
            if (sound.equalsIgnoreCase("1")) {
                SettingActivity.soundModeTxt.setText(getResources().getString(R.string.on));
            } else {
                SettingActivity.soundModeTxt.setText(getResources().getString(R.string.off));
            }

            String mode = sharedPreferences.getString("game_mode", "0");
            if (mode.equals("0")) {
                SettingActivity.modeText.setText(getResources().getString(R.string.simple));
            } else {
                SettingActivity.modeText.setText(getResources().getString(R.string.timed));
            }
            String vibrate = sharedPreferences.getString("vibrate", "1");
            if (vibrate.equals("0")) {
                SettingActivity.badIcon.setVisibility(View.VISIBLE);
                SettingActivity.vibrationTxt.setText(getResources().getString(R.string.off));
            } else {
                SettingActivity.badIcon.setVisibility(View.GONE);
                SettingActivity.vibrationTxt.setText(getResources().getString(R.string.on));
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        playTxt.setText(getResources().getString(R.string.play));
        newGameTxt.setText(getResources().getString(R.string.new_game));
        gotoYotubetxt.setText(getResources().getString(R.string.goto_youtube));
        seeRankTxt.setText(getResources().getString(R.string.see_your_ranking));
        moreTxt.setText(getResources().getString(R.string.more_games));
        playTxt2.setText(getResources().getString(R.string.play));
        leaderboardText.setText(getResources().getString(R.string.leaderboard));
        registerReceiver(refreshBroadCast, new IntentFilter("refresh"));

        SharedPreferences sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE);
        String languageCode = sharedPreferences.getString("language", "en");
        if (languageCode.equals("fr")) {
            playTxt.setTextSize(18);
            playTxt2.setTextSize(18);
        } else {
            playTxt2.setTextSize(32);
            playTxt.setTextSize(32);
        }
    }

    public void setLangauge(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("settings", MODE_PRIVATE);
        String languageCode = sharedPreferences.getString("language", "en");
        switch (languageCode) {
            case "en":
                SettingActivity.flagImg.setImageResource(R.drawable.united_kingdom);
                SettingActivity.languageTxt.setText(context.getResources().getString(R.string.english));
                break;
            case "fr":
                SettingActivity.flagImg.setImageResource(R.drawable.france);
                SettingActivity.languageTxt.setText(context.getResources().getString(R.string.french));

                break;
        }
    }

    @Override
    public void onBackPressed() {
        ExitDialog dialog = new ExitDialog(this);
        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    //////


    private void loadInterstialAd() {
        // interstitialAd = AdManager.mInterstitialAd; //new InterstitialAd(this) ;
        AdManager.initInterstitialAd(Dashboard.this);
//        interstitialAd.setAdUnitId (getResources().getString(R.string.interstitial_adunit) ) ;
//        interstitialAd.loadAd(new AdRequest.Builder().build());
    }

//    public  void loadVideoAd() {
//        // Load a reward based video ad
//        if(!mRewardedVideoAd.isLoaded()){
//            Log.i("mRewardedVideoAd","Not LOaded");
//            mRewardedVideoAd.loadAd("ca-app-pub-4696224049420135/7768937909", new AdRequest.Builder().build());
//        }else{
//            Log.i("mRewardedVideoAd","LOaded");
//
//        }
//
//    }


}