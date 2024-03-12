package com.digitalDreams.millionaire_game;

import static com.digitalDreams.millionaire_game.alpha.Constants.prettyCount;
import static com.digitalDreams.millionaire_game.alpha.Constants.setLocale;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.digitalDreams.millionaire_game.alpha.AudioManager;
import com.digitalDreams.millionaire_game.alpha.testing.GameActivity4;
import com.google.android.gms.ads.AdView;

import java.util.Locale;

public class Dashboard extends AppCompatActivity {
    TextView newGameTxt, playTxt, leaderboardText, seeRankTxt, moreTxt, playTxt2, gotoYoutubeTxt;
    RelativeLayout bg;
    RelativeLayout newGameBtn, leaderBoardBtn, exitBtn, gotoYoutubeBtn, new_particle;
    ImageView settingBtn;
    String languageCode;

    AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);

    @Override
    protected void onStart() {
        super.onStart();
        AdManager.loadInterstitialAd(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);


        showInterstitialAd();

        SharedPreferences sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE);
        languageCode = sharedPreferences.getString("language", "en");
        int endColor = sharedPreferences.getInt("end_color", getResources().getColor(R.color.purple_dark));
        int startColor = sharedPreferences.getInt("start_color", getResources().getColor(R.color.purple_500));
        ///int cardBackground = sharedPreferences.getInt("card_background", 0x219ebc);
        String highScore = sharedPreferences.getString("high_score", "0");
        // String game_level = sharedPreferences.getString("game_level", "1");

        setLocale(this);

        TextView highScoreTxt = findViewById(R.id.highscore);

        try {
            String score = String.format(Locale.getDefault(), "$%s", prettyCount(Integer.parseInt(highScore)));
            highScoreTxt.setText(score);
        } catch (Exception e) {
            e.printStackTrace();
        }

        AdView mAdView;
        mAdView = findViewById(R.id.adView);
        AdManager.loadAdView(mAdView);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE);

        newGameTxt = findViewById(R.id.new_game_text);
        gotoYoutubeTxt = findViewById(R.id.goto_youtube_txt);
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
                new int[]{startColor, endColor});

        bg.setBackground(gd);

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
            sharedPreferences.edit().putBoolean("is_first_time", true).apply();
            AudioManager.greenBlink(getApplicationContext(), newGameBtn);
            Intent intent = new Intent(Dashboard.this, GameActivity4.class);
            startActivity(intent);
            finish();
        });

        gotoYoutubeBtn.setOnClickListener(view -> {
            try {
                AudioManager.darkBlueBlink(getApplicationContext(), gotoYoutubeBtn);
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/channel/UC7AAQdgwQ204aU5ztp19FKg")));
            } catch (Exception e) {
                e.printStackTrace();

            }
        });

        new_particle.setOnClickListener(view -> {
            AudioManager.darkBlueBlink(this, new_particle);
            Intent i = new Intent(Dashboard.this, WinnersActivity.class);
            startActivity(i);
        });

        leaderBoardBtn.setOnClickListener(view -> {
            Utils.destination_activity = LeaderBoard.class;
            AudioManager.darkBlueBlink(getApplicationContext(), leaderBoardBtn);
            startActivity(new Intent(Dashboard.this, LeaderBoard.class));
            //finish();
        });


        settingBtn.setOnClickListener(view -> {
            settingBtn.startAnimation(buttonClick);
            AudioManager.darkBlueBlink(this, settingBtn);
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
            AudioManager.darkBlueBlink(Dashboard.this, accountBtn);

            startActivity(intent);
        });

        RelativeLayout moreBtn = findViewById(R.id.more_games);
        moreBtn.setOnClickListener(view -> {
            AudioManager.darkBlueBlink(getApplicationContext(), moreBtn);

            Uri uri = Uri.parse("https://www.facebook.com/MillionaireGameApp");

            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(uri);
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        RelativeLayout profileBtn = findViewById(R.id.profile);
        profileBtn.setOnClickListener(view -> {
            AudioManager.darkBlueBlink(Dashboard.this, profileBtn);
            Utils.destination_activity = Dashboard.class;
            Intent intent = new Intent(Dashboard.this, UserDetails.class);
            intent.putExtra("type", "edit");
            startActivity(intent);
            finish();
        });
    }


    private void updateGameLogo() {
        ImageView gameLogo = findViewById(R.id.gameLogoImageView);
        gameLogo.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.game_logo));
    }

    private final BroadcastReceiver refreshBroadCast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            recreate();
            leaderboardText.setText(getResources().getString(R.string.leaderboard));

            updateGameLogo();
        }
    };


    @Override
    protected void onResume() {
        super.onResume();
        playTxt.setText(getResources().getString(R.string.play));
        newGameTxt.setText(getResources().getString(R.string.new_game));
        gotoYoutubeTxt.setText(getResources().getString(R.string.goto_youtube));
        seeRankTxt.setText(getResources().getString(R.string.see_your_ranking));
        moreTxt.setText(getResources().getString(R.string.more_games));
        playTxt2.setText(getResources().getString(R.string.play));
        leaderboardText.setText(getResources().getString(R.string.leaderboard));

        updateGameLogo();

        LocalBroadcastManager.getInstance(this).registerReceiver(refreshBroadCast, new IntentFilter("refresh"));

        SharedPreferences sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE);
        String languageCode = sharedPreferences.getString("language", "");

        switch (languageCode) {
            case "fr", "es", "pt", "de", "in", "tr", "ms" -> {
                playTxt.setTextSize(18);
                playTxt2.setTextSize(18);
            }
            default -> {
                playTxt2.setTextSize(32);
                playTxt.setTextSize(32);
            }
        }

        // setLocale(this);
    }

    @Override
    public void onBackPressed() {
        ExitDialog dialog = new ExitDialog(this);
        dialog.show();
        Window window = dialog.getWindow();
        assert window != null;
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }


    private void showInterstitialAd() {
        AdManager.showInterstitial(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AdManager.disposeAds();
        AudioManager.releaseMusicResources();
    }
}