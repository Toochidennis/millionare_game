package com.digitalDreams.millionaire_game;

import static com.digitalDreams.millionaire_game.alpha.AudioManager.playBackgroundMusic;
import static com.digitalDreams.millionaire_game.alpha.AudioManager.stopBackgroundMusic;
import static com.digitalDreams.millionaire_game.alpha.Constants.APPLICATION_DATA;
import static com.digitalDreams.millionaire_game.alpha.Constants.SHOULD_CONTINUE_GAME;
import static com.digitalDreams.millionaire_game.alpha.Constants.SHOULD_REFRESH_QUESTION;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.digitalDreams.millionaire_game.alpha.AudioManager;
import com.digitalDreams.millionaire_game.alpha.activity.GameActivity3;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FailureActivity extends AppCompatActivity {
    long time;
    String countdownTime = "9000";

    String modeValue = "";
    TextView replay_level;
    int animationCount = 0;
    RelativeLayout hex;
    TextView failureTxt;
    TextView noThankBtn;
    RelativeLayout continueBtn;
    RelativeLayout btn_forAnim;
    CountDownTimer countDownTimer;
    boolean hasOldWinningAmount = false;

    RelativeLayout new_games;
    //  AdManager adManager;
    String username = "";


    @Override
    protected void onStart() {
        super.onStart();
        AdManager.loadInterstitialAd(this);
        AdManager.loadRewardedAd(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_failure);

        replay_level = findViewById(R.id.replay);
        hex = findViewById(R.id.hex);
        failureTxt = findViewById(R.id.failureTxt);
        noThankBtn = findViewById(R.id.no_thanks);
        continueBtn = findViewById(R.id.continue_game);
        btn_forAnim = findViewById(R.id.btn_forAnim);
        new_games = findViewById(R.id.new_games);
        new_games.setVisibility(View.GONE);

        continueBtn.setVisibility(View.GONE);
        failureTxt.setVisibility(View.GONE);

        noThankBtn.setVisibility(View.GONE);
        btn_forAnim.setVisibility(View.GONE);

        new MyAnimation(hex);

        animateWebContainer(replay_level);

        Animation aniFade = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadding);
        continueBtn.startAnimation(aniFade);
        btn_forAnim.startAnimation(aniFade);


        AdView mAdView;
        mAdView = findViewById(R.id.adView);
        @SuppressLint("VisibleForTests") AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        showInterstitialAd();

        SharedPreferences sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE);
        int endColor = sharedPreferences.getInt("end_color", getResources().getColor(R.color.purple_dark));
        int startColor = sharedPreferences.getInt("start_color", getResources().getColor(R.color.purple_500));
        hasOldWinningAmount = sharedPreferences.getBoolean("hasOldWinningAmount", false);
        username = sharedPreferences.getString("username", "");

        String mode = sharedPreferences.getString("game_mode", "0");
        //TextView modeTxt = findViewById(R.id.mode);
        if (mode.equals("0")) {
            //  modeTxt.setText("Mode: Normal");
            modeValue = "normal";
        } else {
            // modeTxt.setText("Mode: Hard");
            modeValue = "hard";
        }

        checkScore();

        new MyAnimation(btn_forAnim);
        LinearLayout rootView = findViewById(R.id.rootview);

        new Particles(this, rootView, R.layout.image_xml, 20);
        GradientDrawable gd = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{startColor, endColor});

        rootView.setBackground(gd);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE);


        new_games.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AudioManager.darkBlueBlink(getApplicationContext(),new_games);

                new_games.setClickable(false);

                stopBackgroundMusic();

                AdManager.showInterstitial(FailureActivity.this);

                if (AdManager.interstitialAd != null) {
                    AdManager.interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                        @Override
                        public void onAdClicked() {
                            // Called when a click is recorded for an ad.
                            //Log.d("Admob", "Ad was clicked.");
                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                            restartGame();
                            super.onAdFailedToShowFullScreenContent(adError);
                        }

                        @Override
                        public void onAdDismissedFullScreenContent() {
                            restartGame();
                            super.onAdDismissedFullScreenContent();
                        }
                    });

                } else {
                    restartGame();
                }
            }
        });


        noThankBtn.setOnClickListener(view -> {
            Utils.destination_activity = PlayDetailsActivity.class;
            username = sharedPreferences.getString("username", "");
            noThankBtn.setClickable(false);

            stopBackgroundMusic();

            finishGameActivity();

            Intent intent = new Intent(FailureActivity.this, PlayDetailsActivity.class);

            if (hasOldWinningAmount) {
                intent.putExtra("hasOldWinningAmount", true);
            }

            intent.putExtra("noThanks", true);
            startActivity(intent);
            finish();
        });

        TextView countDownTxt = findViewById(R.id.count_down_text);
        time = Long.parseLong(countdownTime);

        countDownTimer = new CountDownTimer(time, 1000) {
            @Override
            public void onTick(long l) {
                int sec = (int) (l / 1000);
                int seconds = (sec % 3600) % 60;
                countDownTxt.setText(String.valueOf(seconds));
            }

            @Override
            public void onFinish() {
            }
        };

        countDownTimer.start();

        continueBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                AudioManager.greenBlink(FailureActivity.this, continueBtn);

                if (!isNetworkConnected()) {
                    startActivity(new Intent(FailureActivity.this, PlayDetailsActivity.class));
                    finish();
                }

                continueBtn.setClickable(false);

                AdManager.showRewardedAd(FailureActivity.this);

                try {
                    if (AdManager.rewardedAd != null) {
                        AdManager.rewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdClicked() {
                                super.onAdClicked();
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                finishGameActivity();
                                startActivity(new Intent(FailureActivity.this, PlayDetailsActivity.class));
                                finish();
                                super.onAdFailedToShowFullScreenContent(adError);
                            }

                            @Override
                            public void onAdDismissedFullScreenContent() {
                                finish();
                                playBackgroundMusic(FailureActivity.this);
                                updateSharedPreference(true);
                                super.onAdDismissedFullScreenContent();
                            }
                        });
                    } else {
                        restartGame();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    backToGameActivity();
                }
            }
        });

    }

    private void finishGameActivity() {
        if (GameActivity3.gameActivity != null) {
            GameActivity3.gameActivity.finish();
        }
    }

    private void restartGame() {
        updateSharedPreference(false);
        startActivity(new Intent(FailureActivity.this, CountDownActivity.class));
        finish();
    }


    private void animateWebContainer(View view) {
        AlphaAnimation fadeIn = new AlphaAnimation(0, 1);
        final AnimationSet set = new AnimationSet(false);
        Animation inFromLeft = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, -1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);

        set.addAnimation(inFromLeft);
        set.addAnimation(fadeIn);
        set.setDuration(1000);
        set.setStartOffset(100);
        view.startAnimation(set);

        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                animationCount++;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (animationCount == 1) {
                    failureTxt.setVisibility(View.VISIBLE);
                    animateWebContainer(failureTxt);
                } else if (animationCount == 2) {
                    continueBtn.setVisibility(View.VISIBLE);
                    btn_forAnim.setVisibility(View.VISIBLE);
                    animateWebContainer(btn_forAnim);
                    animateWebContainer(continueBtn);
                } else if (animationCount == 4) {
                    new_games.setVisibility(View.VISIBLE);
                    animateMobileContainer(new_games);
                } else if (animationCount == 5) {
                    noThankBtn.setVisibility(View.VISIBLE);
                    // animateWebContainer(noThankBtn);
                }

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void animateMobileContainer(View view) {
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
        fadeIn.setDuration(2000);

        view.startAnimation(fadeIn);
        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                animationCount++;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                noThankBtn.setVisibility(View.VISIBLE);
                animateWebContainer(noThankBtn);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AdManager.disposeAds();
        AudioManager.releaseMusicResources();
    }

    private void showInterstitialAd() {
        AdManager.showInterstitial(FailureActivity.this);

        if (AdManager.interstitialAd != null) {
            AdManager.interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    animateWebContainer(replay_level);
                }

            });
        }
    }

    private void checkScore() {
        SharedPreferences sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE);
        String highScore = sharedPreferences.getString("high_score", "0");
        String username = sharedPreferences.getString("username", "");
        String country = sharedPreferences.getString("country", "");
        String country_flag = sharedPreferences.getString("country_flag", "");
        String newAmountWon = sharedPreferences.getString("amountWon", "0");
        int totalAmountWon = sharedPreferences.getInt("totalAmountWon", 0);
        boolean isFinishLevel = sharedPreferences.getBoolean("isFinishLevel", false);

        try {
            int parsedHighScore = Integer.parseInt(highScore);
            int parsedNewAmount = Integer.parseInt(newAmountWon);
            int totalAmount2;

            if (isFinishLevel) {
                totalAmount2 = totalAmountWon + parsedNewAmount;
            } else {
                totalAmount2 = parsedNewAmount;
            }

            if (totalAmount2 > parsedHighScore) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("high_score", String.valueOf(totalAmount2));
                editor.apply();
            }

            Map<String, String> userDetails = new HashMap<>();
            userDetails.put("username", username);
            userDetails.put("country", country);
            userDetails.put("country_flag", country_flag);

            // sendScoreToSever(String.valueOf(totalAmount2), userDetails);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendScoreToSever(String score, Map<String, String> userDetails) {
        String url = getResources().getString(R.string.base_url) + "/post_score.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response ->
                Log.i("response", "response " + response), new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                super.getParams();
                Map<String, String> param = new HashMap<>();
                JSONObject country_json = new JSONObject();
                try {
                    country_json.put("name", userDetails.get("country"));
                    country_json.put("url", userDetails.get("country_flag"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                param.put("score", score);
                param.put("username", userDetails.get("username"));
                param.put("country", userDetails.get("country"));
                param.put("country_json", country_json.toString());
                param.put("country_flag", userDetails.get("country_flag"));
                param.put("avatar", getAvatar());
                param.put("device_id", getDeviceId(FailureActivity.this));
                param.put("game_type", "millionaire");
                param.put("mode", modeValue);
                return param;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private String getAvatar() {
        SharedPreferences sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE);
        return sharedPreferences.getString("avatar", "");
    }

    public static String getDeviceId(Context context) {
    /*    String id = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return id;*/
        try {
            return AdvertisingIdClient.getAdvertisingIdInfo(context).getId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return UUID.randomUUID().toString();
    }


    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }


    public void backToGameActivity() {
        updateSharedPreference(true);
        finish();
    }

    private void updateSharedPreference(boolean continueGame) {
        SharedPreferences sharedPreferences = getSharedPreferences(APPLICATION_DATA, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(SHOULD_CONTINUE_GAME, continueGame);
        editor.putBoolean(SHOULD_REFRESH_QUESTION, true);
        editor.apply();
    }

}