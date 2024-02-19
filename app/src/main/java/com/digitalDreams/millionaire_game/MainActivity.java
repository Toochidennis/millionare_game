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
import android.os.Build;
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

import com.digitalDreams.millionaire_game.alpha.testing.database.AppDatabase;
import com.digitalDreams.millionaire_game.alpha.testing.database.DatabaseProvider;
import com.digitalDreams.millionaire_game.alpha.testing.database.Question;
import com.digitalDreams.millionaire_game.alpha.testing.database.QuestionDao;
import com.google.android.gms.ads.RequestConfiguration;

import org.json.JSONArray;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    ImageView ddLogo;
    //  long logoStartTime = 0;
    LinearLayout webDevContainer;
    LinearLayout mobileDevContainer;
    LinearLayout digitalMarketingContainer;
    LinearLayout dataScienceContainer;
    LinearLayout container;
    private static final long COUNTER_TIME = 5;
    //public static boolean ACTIVITY_PASSED = false;
    private String languageCode;

    private QuestionDao questionDao;

    TextView trainTxt, webDevTxt, mobileDevTxt, digitalTxt, dataScienceTxt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Window window = getWindow();
        //     AppOpenManager appOpenAdManager;


        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.white));

        //dbHelper = new DBHelper(this);
        AppDatabase database = DatabaseProvider.getInstance(this);
        questionDao = database.questionDao();

        SharedPreferences sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // String username = sharedPreferences.getString("username", "");
        Utils.IS_DONE_INSERTING = sharedPreferences.getBoolean("IS_DONE_INSERTING", false);
        languageCode = sharedPreferences.getString("language", "");

        webDevContainer = findViewById(R.id.web_development);
        ddLogo = findViewById(R.id.dd_logo);
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

        createTimer();

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

        // int yValue = height - 500;
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
            if (questionDao.getQuestionSize() == 0) {
                Utils.IS_DONE_INSERTING = false;

                initializeDatabase();
                editor.putBoolean(languageCode, true);
                editor.apply();
            } else {
                Utils.IS_DONE_INSERTING = true;
                editor.putBoolean("IS_DONE_INSERTING", true);
                editor.apply();
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


    private void initializeDatabase() {
        // Read the JSON file from the raw resources
        int resId = getLanguageResource(languageCode);
        InputStream inputStream = getResources().openRawResource(resId);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1250];
        String json;

        try {
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                json = outputStream.toString(StandardCharsets.UTF_8);
            } else {
                json = new String(outputStream.toByteArray(), StandardCharsets.UTF_8);
            }

            // Parse JSON data into a list of Question objects
            List<Question> questions = parseJsonToQuestions(json);

            // Insert questions into the database
            questionDao.insertQuestion(questions);

            Utils.IS_DONE_INSERTING = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }

    private List<Question> parseJsonToQuestions(String json) {
        List<Question> questions = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int a = 0; a < jsonArray.length(); a++) {
                JSONArray questionArray = jsonArray.getJSONArray(a);
                if (questionArray.length() >= 9) { // Ensure the array has at least 9 elements
                    String id = String.valueOf(questionArray.optInt(0)); // Use optInt to handle null values
                    String questionTitle = questionArray.optString(1, ""); // Provide default value for empty strings
                    String level = String.valueOf(questionArray.optInt(2));
                    String correctAnswer = questionArray.optString(3, "").trim();
                    String reason = questionArray.optString(4, "").trim();
                    String optionA = questionArray.optString(5, "");
                    String optionB = questionArray.optString(6, "");
                    String optionC = questionArray.optString(7, "");
                    String optionD = questionArray.optString(8, "");
                    String language = getLanguageText(this, languageCode);

                    Question question = new Question(id,
                            capitaliseFirstLetter(questionTitle),
                            capitaliseFirstLetter(correctAnswer),
                            new Question.Options(
                                    capitaliseFirstLetter(optionA),
                                    capitaliseFirstLetter(optionB),
                                    capitaliseFirstLetter(optionC),
                                    capitaliseFirstLetter(optionD)
                            ),
                            reason, "GENERAL", "1", level, language
                    );

                    questions.add(question);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return questions;
    }


    private String capitaliseFirstLetter(String word) {
        if (word == null || word.isEmpty()) {
            return word;
        }
        return Character.toUpperCase(word.charAt(0)) + word.substring(1);
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


    private void createTimer() {
        CountDownTimer countDownTimer =
                new CountDownTimer(MainActivity.COUNTER_TIME * 1000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
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
                        ((MyApplication) application).showAdIfAvailable(
                                MainActivity.this,
                                () -> startDashboardActivity()
                        );

                    }
                };
        countDownTimer.start();
    }

    /**
     * Start the Dashboard.
     */
    public void startDashboardActivity() {
        Executors.newSingleThreadExecutor().execute(() -> {
            if (questionDao.getQuestionSize() > 0) {
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

                    startActivity(new Intent(MainActivity.this, Dashboard.class));
                    finish();
                }
            }
        });
    }
}