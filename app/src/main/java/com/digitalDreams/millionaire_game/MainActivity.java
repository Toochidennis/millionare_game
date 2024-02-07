package com.digitalDreams.millionaire_game;

import static com.digitalDreams.millionaire_game.alpha.Constants.getLanguageResource;
import static com.digitalDreams.millionaire_game.alpha.Constants.getLanguageText;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Path;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.ads.RequestConfiguration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    public final int SPLASH_SCREEN_DELAY = 100;
    public List<String> columnList = new ArrayList<>();
    DBHelper dbHelper;
    ImageView ddLogo;
    long logoStartTime = 0;
    LinearLayout webDevContainer;
    LinearLayout mobileDevContainer;
    LinearLayout digitalMarketingContainer;
    LinearLayout dataScienceContainer;
    LinearLayout container;
    private static final long COUNTER_TIME = 5;
    public static boolean ACTIVITY_PASSED = false;
    private String languageCode;

    TextView trainTxt, webDevTxt, mobileDevTxt, digitalTxt, dataScienceTxt;

    private long secondsRemaining;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Window window = getWindow();
   //     AppOpenManager appOpenAdManager;


        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.white));

        dbHelper = new DBHelper(this);

        ddLogo = findViewById(R.id.dd_logo);


        SharedPreferences sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
       // String username = sharedPreferences.getString("username", "");
        Utils.IS_DONE_INSERTING = sharedPreferences.getBoolean("IS_DONE_INSERTING", false);
        languageCode = sharedPreferences.getString("language", "");

        webDevContainer = findViewById(R.id.web_development);
        mobileDevContainer = findViewById(R.id.mobile_development);
        digitalMarketingContainer = findViewById(R.id.digital_marketing);
        dataScienceContainer = findViewById(R.id.data_science);
        container = findViewById(R.id.container);
        trainTxt = findViewById(R.id.train_text);
        webDevTxt = findViewById(R.id.web_dev_txt);
        mobileDevTxt = findViewById(R.id.mobile_txt);
        digitalTxt = findViewById(R.id.digital_marketing_txt);
        dataScienceTxt = findViewById(R.id.data_science_txt);

        updateTextViews();

        createTimer(COUNTER_TIME);

        new RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList("C5C6588E00A996967AA2085A167B0F4E", "9D16E23BB90EF4BFA204300CCDCCF264"));

        loadQuestionJson(editor);


        AlphaAnimation fadeIn = new AlphaAnimation(0, 1);
        final AnimationSet set = new AnimationSet(false);

        set.addAnimation(fadeIn);
        set.setDuration(1500);
        set.setStartOffset(100);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        int yValue = height - 500;
        Path path = new Path();
        path.moveTo(0, height);
        ObjectAnimator moveX = ObjectAnimator.ofFloat(ddLogo, "x", "y", path);
        moveX.setDuration(0);
        moveX.start();

        ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(ddLogo, "alpha", 1);
        ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(ddLogo, "translationY", 0);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(objectAnimator1, objectAnimator2);
        animatorSet.setDuration(2000);
        animatorSet.setInterpolator(new DecelerateInterpolator());
        animatorSet.start();

//        int yValue1 = height - 400;

        new Handler().postDelayed(() -> {
            container.setVisibility(View.VISIBLE);
            trainTxt.setVisibility(View.VISIBLE);
            webDevContainer.setVisibility(View.VISIBLE);
            mobileDevContainer.setVisibility(View.VISIBLE);
            dataScienceContainer.setVisibility(View.VISIBLE);
            digitalMarketingContainer.setVisibility(View.VISIBLE);
            animateWebContainer();
            animateMobileContainer();
            animateDigitalContainer();
            animateDataScienceContainer();
            trainTxt.startAnimation(set);
        }, 2000);

    }

    private void loadQuestionJson(SharedPreferences.Editor editor) {
        new Thread(() -> {
            try {
                dbHelper = new DBHelper(this);

                if (dbHelper.getQuestionSize() == 0) {
                    Utils.IS_DONE_INSERTING = false;

                    String text = readRawTextFile(getLanguageResource(languageCode));
                    parseJSON(text);

                    editor.putBoolean(languageCode, true);
                    editor.apply();
                } else {
                    Utils.IS_DONE_INSERTING = true;
                    editor.putBoolean("IS_DONE_INSERTING", true);
                    editor.apply();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void updateTextViews() {
        setLocale(this, languageCode);

        trainTxt.setText(getResources().getString(R.string.we_train_you_on));
        webDevTxt.setText(getResources().getString(R.string.web_development));
        mobileDevTxt.setText(getResources().getString(R.string.mobile_development));
        digitalTxt.setText(getResources().getString(R.string.digital_marketing));
        dataScienceTxt.setText(getResources().getString(R.string.data_science));
    }

    public static void setLocale(Activity activity, String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Resources resources = activity.getResources();
        Configuration config = resources.getConfiguration();
        config.locale = locale;
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }


    private String readRawTextFile(int resId) throws IOException {
        InputStream is = getResources().openRawResource(resId);
        Writer writer = new StringWriter();
        char[] buffer = new char[10024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            is.close();
        }

        return writer.toString();
    }


    private void parseJSON(String json) {
        int lent = 0;
        try {
            JSONArray jsonArray = new JSONArray(json);

            for (int a = 0; a < jsonArray.length(); a++) {
                Log.i("index", String.valueOf(a));
                lent++;

                Utils.NUMBER_OF_INSERT++;


                if (lent == jsonArray.length()) {
                    Utils.IS_DONE_INSERTING = true;
                }


                JSONArray question = jsonArray.getJSONArray(a);
                String id = String.valueOf(question.getInt(0));
                String content = question.getString(1);
                String type = "qo";
                String level = String.valueOf(question.getString(2));
                String language = getLanguageText(this, languageCode);

                String stage_name = "GENERAL";
                String stage = "1";

                String correct = question.getString(3).trim();
                String reason = question.getString(4).trim();

                JSONArray answers = new JSONArray();

                for (int j = 5; j < question.length(); j++) {
                    JSONObject obj = new JSONObject();
                    obj.put("text", question.getString(j));
                    answers.put(obj);
                }

                String answer = String.valueOf(answers);

                dbHelper.insertDetails(language, level, id, content, type, answer, correct, stage_name, stage, reason);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("JsonDetails", String.valueOf(lent));

    }


    private void animateWebContainer() {
        AlphaAnimation fadeIn = new AlphaAnimation(0, 1);
        final AnimationSet set = new AnimationSet(false);
        Animation inFromLeft = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, -1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        set.addAnimation(inFromLeft);
        set.addAnimation(fadeIn);
        set.setDuration(1500);
        set.setStartOffset(100);
        webDevContainer.startAnimation(set);
    }

    private void animateMobileContainer() {
        AlphaAnimation fadeIn = new AlphaAnimation(0, 1);
        final AnimationSet set = new AnimationSet(false);
        Animation inFromRight = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);

        set.addAnimation(inFromRight);
        set.addAnimation(fadeIn);
        set.setDuration(1500);
        set.setStartOffset(100);
        mobileDevContainer.startAnimation(set);
    }

    private void animateDigitalContainer() {
        AlphaAnimation fadeIn = new AlphaAnimation(0, 1);
        final AnimationSet set = new AnimationSet(false);
        Animation inFromLeft = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, -1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        set.addAnimation(inFromLeft);
        set.addAnimation(fadeIn);
        set.setDuration(1500);
        set.setStartOffset(100);
        digitalMarketingContainer.startAnimation(set);
    }

    private void animateDataScienceContainer() {
        AlphaAnimation fadeIn = new AlphaAnimation(0, 1);
        final AnimationSet set = new AnimationSet(false);
        Animation inFromRight = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);

        set.addAnimation(inFromRight);
        set.addAnimation(fadeIn);
        set.setDuration(1500);
        set.setStartOffset(100);
        dataScienceContainer.startAnimation(set);
    }


    private void createTimer(long seconds) {
        //final TextView counterTextView = findViewById(R.id.timer);

        CountDownTimer countDownTimer =
                new CountDownTimer(seconds * 1000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        // secondsRemaining = ((millisUntilFinished / 1000) + 1);
                        // counterTextView.setText("App is done loading in: " + secondsRemaining);
                    }

                    @Override
                    public void onFinish() {
                        secondsRemaining = 0;
                        //counterTextView.setText("Done.");

                        Application application = getApplication();

                        // If the application is not an instance of MyApplication, log an error message and
                        // start the MainActivity without showing the app open ad.
                        if (!(application instanceof MyApplication)) {
                            // Log.e(LOG_TAG, "Failed to cast application to MyApplication.");
                            startDashboardActivity();
                            return;
                        }

                        // Show the app open ad.
                        ((MyApplication) application)
                                .showAdIfAvailable(
                                        MainActivity.this,
                                        new MyApplication.OnShowAdCompleteListener() {
                                            @Override
                                            public void onShowAdComplete() {
                                                startDashboardActivity();
                                            }
                                        });
                    }
                };
        countDownTimer.start();
    }

    /**
     * Start the Dashboard.
     */
    public void startDashboardActivity() {
        if (dbHelper.getQuestionSize() > 0) {
            new Handler().postDelayed(() -> {
                SharedPreferences sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE);
                String username = sharedPreferences.getString("username", "");
                if (username.isEmpty()) {
                    Intent intent = new Intent(MainActivity.this, UserDetails.class);
                    startActivity(intent);
                    finish();
                } else {
                    SharedPreferences.Editor editor = sharedPreferences.edit();


                    Utils.IS_DONE_INSERTING = true;
                    editor.putBoolean("IS_DONE_INSERTING", true);
                    editor.apply();


                    Intent intent = new Intent(MainActivity.this, Dashboard.class);
                    startActivity(intent);
                    finish();
                }
            }, SPLASH_SCREEN_DELAY);
        }
    }

}