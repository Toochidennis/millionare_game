package com.digitalDreams.millionaire_game;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.VectorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.PublicKey;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GameActivity2 extends AppCompatActivity {

    // public static MediaPlayer mMediaPlayer;
    // public static MediaPlayer mSuccessPlayer;
    //  public static MediaPlayer mFailurePlayer;


    static boolean active = false;


    String time_1;

    public static MediaPlayer mWinning_sound;
    String countdownTime = "60000";
    boolean timeOn = false;
    Integer grading = 1;
    Integer correctGrade = 0;


    long time;
    String course;
    String year;
    String json, courseId, levelId, examId;
    Integer number = 1;
    View prevView;
    View nextView;
    public View current;
    String[] numbs = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    CountDownTimer cTimer = null;
    AlertDialog dialog = null;
    String exam_type_id;
    Boolean exam_on = false;
    public static Boolean checkNext = false;
    boolean resetQuestion = false;
    String from;
    String[] content;
    String answr = "", accessLevel;
    public static int ab = 0;
    public static int qPosition = -1;
    private String type;
    public static int userScore, totalScore, lifeline = 3, level = 1;
    TextView levelTxt, scoreTxt, lifelineTxt, progressTxt;
    public static boolean continueGame = false;
    public static boolean isStartAtFresh = false;
    boolean continueSound;

    int totalQuestionCount = 0;
    int failCount = 0;
    int progress = 0;
    public static Integer[] moneyArr = {};
    //={500,1000,2000,3000,5000,7500,10000,12500,15000,25000,
    //50000,100000,250000,500000,1000000};
    int p = 0;
    int p1 = 3;
    boolean _2question = true;
    boolean askFriend = true;
    boolean vote = true;
    boolean skip = true;
    String[] answerDescriptionArr = {"I dont know. Choose", "Maybe it's ", "Don't really know, Go for "};
    float[] answerPercentages = {70, 80, 65};
    public static String amountWon = "0";
    public static boolean hasOldWinningAmount = false;
    String option2, option1;
    String sound, vibrate;
    DBHelper dbHelper;
    public static int noOfQuestionAnswered;
    public static int noOfCorrectAnswer;
    public static int noOfPagesPassed;
    public static long timing;
    public static Handler h2;
    public static Runnable run;
    File imagePath;
    public static int MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE = 100;
    //public static RewardedAd mRewardedVideoAd;
    //boolean hasOldWinningAmount = false;
    //public static InterstitialAd interstitialAd;
    public static boolean fromProgress = true;
    public static boolean fromProgress2 = false;
    boolean removeOptions = false;
    boolean hasRefreshed = false;
    RelativeLayout progressBtn, exitBtn, cancel_button;
    LinearLayout guid_layout;
    TextView opt_dec_1, opt_dec_2, opt_dec_3, opt_dec_4;
    int number_of_failure = 0;
    LinearLayout lifeGuardContainers = null;
    TextView amountWonTxt;
    //AdManager adManager;
    public static Activity gameActivity2;
    JSONArray allQuestion = new JSONArray();
    JSONObject singleQuestion = new JSONObject();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            countDown();
        }

        playStong();

        try {
            DateFormat df = new SimpleDateFormat("EEE, d MMM, HH:mm", Locale.getDefault());
            time_1 = df.format(Calendar.getInstance().getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }

        setContentView(R.layout.activity_exam_game2);
        fromProgress = getIntent().getBooleanExtra("fromProgress", false);
        gameActivity2 = this;
        Utils.lastDatePlayed = time_1;


        hasOldWinningAmount = getIntent().getBooleanExtra("hasOldWinningAmount", false);
        noOfQuestionAnswered = 0;
        noOfCorrectAnswer = 0;
        timing = 0;
        amountWon = "0";
        noOfPagesPassed = 0;

        dbHelper = new DBHelper(this);


        AdManager.initInterstitialAd(this);
        AdManager.initRewardedVideo(this);
        //mRewardedVideoAd = AdManager.rewardedAd; //MobileAds.getRewardedVideoAdInstance(this);
        loadVideoAd();
        loadInterstialAd();

        //Log.i("hasOldWinningAmount", String.valueOf(hasOldWinningAmount));


        AdView mAdView;
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE);
        RelativeLayout bg = findViewById(R.id.rootview);
        SharedPreferences sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE);
        String languageCode = sharedPreferences.getString("language", "en");
        int endcolor = sharedPreferences.getInt("end_color", getResources().getColor(R.color.purple_dark));
        int startColor = sharedPreferences.getInt("start_color", getResources().getColor(R.color.purple_500));
        int cardBackground = sharedPreferences.getInt("card_background", 0x03045e);
        String oldAmountWon = sharedPreferences.getString("amountWon", "");
        boolean isFirstTime = sharedPreferences.getBoolean("isFirstTime", false);
        String game_level = sharedPreferences.getString("game_level", "1");
        int game_level_int = Integer.parseInt(game_level);

        moneyArr = new Integer[]{500 * (game_level_int), 1000 * (game_level_int), 2000 * (game_level_int),
                3000 * (game_level_int), 5000 * (game_level_int), 7500 * (game_level_int), 10000 * (game_level_int),
                12500 * (game_level_int), 15000 * (game_level_int), 25000 * (game_level_int),
                50000 * (game_level_int), 100000 * (game_level_int), 250000 * (game_level_int), 500000 * (game_level_int),
                1000000 * (game_level_int)};


        ///new Particles(this,bg,R.layout.image_xml,30);
        GradientDrawable gd = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{startColor, endcolor});

        bg.setBackgroundDrawable(gd);
        sound = sharedPreferences.getString("sound", "1");
        vibrate = sharedPreferences.getString("vibrate", "1");


        progressBtn = findViewById(R.id.progressBtn);
        exitBtn = findViewById(R.id.exitBtn);
        amountWonTxt = findViewById(R.id.amount_won);


        progressBtn.setOnClickListener(view -> {
            Intent intent = new Intent(GameActivity2.this, ProgressActivity.class);
            intent.putExtra("number", p1);
            intent.putExtra("timer", "false");
            if (Build.VERSION.SDK_INT > 20) {
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(GameActivity2.this);
                startActivity(intent, options.toBundle());
            } else {
                startActivity(intent);
            }
        });

        exitBtn.setOnClickListener(view -> {
            Utils.darkBlueBlink(exitBtn, this);

            ExitGameDialog dialog = new ExitGameDialog(GameActivity2.this, amountWon);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            Window window = dialog.getWindow();
            assert window != null;
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);


        });


        Bundle bundle = null;

        bundle = this.getIntent().getExtras();
        //course = bundle.getString("course");

        if (savedInstanceState != null) {

            // noOfCorrectAnswer = savedInstanceState.getInt("noOfCorrectAnswer", 0);
            // number = savedInstanceState.getInt("number", 1);
            Log.i("instantstate", String.valueOf(readNoOfCorrectAnswer()));


            noOfCorrectAnswer = readNoOfCorrectAnswer();
            LinearLayout parent = findViewById(R.id.displayExam);
            int i = parent.indexOfChild(current);
            nextView = parent.getChildAt(i);
            // amountWon = oldAmountWon;
            //setAmountWon();
            readSavedData();
            noOfPagesPassed = noOfCorrectAnswer;
            Log.i("instantstate", String.valueOf(noOfPagesPassed));

            // next2(current, nextView);
            json = sharedPreferences.getString("saved_json", "");

        } else {
            json = dbHelper.buildJson(); //bundle.getString("Json");
            //from = bundle.getString("from");

            sharedPreferences.edit().putString("saved_json", json).apply();
        }

        json = Html.fromHtml(json).toString();
        startDisplay(json);


//        ImageView video_ad_Icon2 = current.findViewById(R.id.video_ad2);
//        video_ad_Icon2.setVisibility(View.VISIBLE);
////        video_ad_Icon2.setImageResource(R.drawable.videoicon);
////        video_ad_Icon2.setColorFilter(R.color.green);
////
////        video_ad_Icon2.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        video_ad_Icon2.setImageResource(R.drawable.vicon2);
        cancel_button = findViewById(R.id.cancel_button);
        guid_layout = findViewById(R.id.guid_layout);

        //////////GUIDE LAYOUT HIDE AND SHOW/////
//        if(isFirstTime) {
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                   // guid_layout.setVisibility(View.VISIBLE);
//                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                            editor.putBoolean("isFirstTime",false);
//                            editor.commit();
//
//                }
//            }, 10000);
//
//        }

        cancel_button.setOnClickListener(view -> guid_layout.setVisibility(View.GONE));

    }


    public void startDisplay(String result) {
        try {
            JSONArray jsonArray = new JSONArray(result);
            for (int b = 0; b < jsonArray.length(); b++) {
                JSONObject p = jsonArray.getJSONObject(b);
                setUpDisplay(p, b);
            }
            takeTest();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setUpDisplay(JSONObject p, int a) throws JSONException {

        int c = 1;
        try {
            JSONArray exm = p.getJSONArray("q");
            // render2("0", exm, p,c);
        } catch (JSONException e) {
            e.printStackTrace();
            JSONObject exm = p.getJSONObject("q");
            //render2("0", exm, p,c);
            allQuestion = exm.getJSONArray("0");
        }

        Log.i("render2", allQuestion.toString());
        setQuestion(0);

    }


    public void setQuestion(int index) {
        Log.i("render2", String.valueOf(index));
        LinearLayout lay = findViewById(R.id.displayExam);
        lay.removeAllViews();

        try {

            JSONObject js = allQuestion.getJSONObject(index);
            singleQuestion = js;

            if (js != null) {
                String gid = js.optString("id");
                String ty = js.getString("type");
                Log.i("render2", String.valueOf(js.optString("correct")));
                Log.i("render2", String.valueOf(singleQuestion));
                switch (ty) {
                    case "section":
                        break;
                    case "qp":
                        break;
                    case "qo":
                        qo(lay, js, index);
                        break;
                    case "af":
                        af(lay, js, false, index);
                        break;

                }
                // render2(gid, b, j,d);
            }
        } catch (JSONException e) {
        }

    }


    public void render2(String a, JSONArray b, JSONObject j, int d) throws JSONException {
        LinearLayout lay = findViewById(R.id.displayExam);

        try {
            if (b.getJSONArray(0) == null) return;
            JSONArray g = null;
            try {
                g = b.optJSONArray(Integer.parseInt(a));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            if (b == null || g == null) return;
            if (g != null) {

                for (int i = 0; i < g.length(); i++) {
                    JSONObject js = g.getJSONObject(i);
                    if (js != null) {
                        String gid = js.optString("id");
                        String ty = js.getString("type");
                        switch (ty) {
                            case "section":
                                break;
                            case "qp":
                                break;
                            case "qo":
                                qo(lay, js, d);
                                break;
                            case "af":
                                af(lay, js, false, d);
                                break;

                        }
                        render2(gid, b, j, d);
                    }

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void render2(String a, JSONObject b, JSONObject j, int d) throws JSONException {
        LinearLayout lay = findViewById(R.id.displayExam);


        try {
            if (b.getJSONArray(a) == null) return;
            JSONArray g = b.getJSONArray(a);
            if (b == null || g == null) return;
            if (g != null) {
                for (int i = 0; i < g.length(); i++) {

                    JSONObject js = g.getJSONObject(i);
                    if (js != null) {
                        String gid = js.getString("id");
                        String ty = js.getString("type");

                        switch (ty) {
                            case "section":
                                break;
                            case "qp":
                                break;
                            case "qo":
                                qo(lay, js, d);
                                break;
                            case "af":
                                af(lay, js, false, d);
                                break;

                        }
                        render2(gid, b, j, d);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void takeTest() {
        setTime();
        RelativeLayout r = findViewById(R.id.nav);
        r.setVisibility(View.GONE);
        LinearLayout parent = findViewById(R.id.displayExam);
        current = parent.getChildAt(parent.indexOfChild(current) + 1);

        current.setVisibility(View.VISIBLE);
        SharedPreferences sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE);
        String mode = sharedPreferences.getString("game_mode", "0");
        if (mode.equals("1")) {
            calcTime(countdownTime);
        } else {
            RelativeLayout timerContainer = findViewById(R.id.timer_container);
            timerContainer.setVisibility(View.GONE);
        }

        exam_on = true;
    }

    private void setTime() {
        h2 = new Handler();
        run = new Runnable() {

            @Override
            public void run() {
                long millis = System.currentTimeMillis();
                int seconds = (int) (millis / 1000);
                int minutes = seconds / 60;
                seconds = seconds % 60;
                timing = timing + 1000;

                h2.postDelayed(this, 1000);
            }
        };
        h2.postDelayed(run, 1000);

    }


    public void qo(LinearLayout a, JSONObject b, int qNo) {
        try {
            String questionId = b.getString("id");
            String cleaned = b.getString("content");
            String cleaned1 = Html.fromHtml(cleaned).toString();
            String tag = b.optString("tag");
            String content = cleaned1;
            try {
                content = content.substring(0, 1).toUpperCase() + content.substring(1);
            } catch (StringIndexOutOfBoundsException e) {
                //e.printStackTrace();
            }
            LayoutInflater inflater = LayoutInflater.from(GameActivity2.this);
            View v;
            try {

                v = inflater.inflate(R.layout.qo_1, a, false);
                Log.i("errorrr", "========11");
            } catch (Exception e) {
                e.printStackTrace();
                Log.i("errorrr", "========");

                v = inflater.inflate(R.layout.qo_1, a, false);
            }

            TextView questionNumber = v.findViewById(R.id.question_progress);
            int currentPage = noOfPagesPassed + 1;
            questionNumber.setText(currentPage + " / 15");


            TextView txt = v.findViewById(R.id.qo_text);
            txt.setText(content);
            CardView cardView = v.findViewById(R.id.card);
            //RelativeLayout progress_container = v.findViewById(R.id.progress_container);
            RelativeLayout _question = v.findViewById(R.id._questions);
            RelativeLayout askaFriend = v.findViewById(R.id.ask_friend);
            RelativeLayout voting = v.findViewById(R.id.voting);
            RelativeLayout refresh = v.findViewById(R.id.skip);
            LinearLayout options_container = v.findViewById(R.id.options_container);
            ////////////////////
            opt_dec_1 = v.findViewById(R.id.opt_dec_1);
            opt_dec_2 = v.findViewById(R.id.opt_dec_2);
            opt_dec_3 = v.findViewById(R.id.opt_dec_3);
            opt_dec_4 = v.findViewById(R.id.opt_dec_4);

            ////////////////////

            v.setVisibility(View.GONE);
            String qImage = b.optString("question_image");
            // ImageView questionImage = v.findViewById(R.id.question_pic);
//            if(!qImage.isEmpty()) {
//                questionImage.setVisibility(View.VISIBLE);
//            }

            v.setTag("qo");
            a.addView(v);
            Log.i("resetQuestion", String.valueOf(resetQuestion));


            number++;

            grading++;
            p++;


            //////////////FEDANIMATION/////


            fedInAnimation(_question, 1000);
            fedInAnimation(opt_dec_1, 1000);
            fedInAnimation(askaFriend, 1200);
            fedInAnimation(opt_dec_2, 1200);
            fedInAnimation(voting, 1400);
            fedInAnimation(opt_dec_3, 1400);

            fedInAnimation(refresh, 1600);
            fedInAnimation(opt_dec_4, 1600);
            //fedInAnimation(progress_container,500);
            fedInAnimation(cardView, 500);
            fedInAnimation(txt, 550);
            fedInAnimation(progressBtn, 500);
            fedInAnimation(exitBtn, 500);
            fedInAnimation(questionNumber, 500);
            fedInAnimation(options_container, 1000);
            //////////////////////////////////////
            af((LinearLayout) v, b, true, number);
        } catch (JSONException e) {

        }
    }


    public void af(LinearLayout a, JSONObject b, boolean visible, int number) {

        lifeGuardContainers = a;

        float scale = getResources().getDisplayMetrics().density;
        int dp = (int) (16 * scale + 0.5f);
        try {
            if (!b.optString("answer").equals(null) && !b.optString("answer").isEmpty()) {

                String cleaned = b.optString("answer");
                String correct = b.optString("correct").trim();

                String content1;
                View v;
                if (visible) {
                    v = LayoutInflater.from(this).inflate(R.layout.optionsview, a, false);
                } else {
                    v = LayoutInflater.from(this).inflate(R.layout.card, a, false);
                }
                content1 = cleaned.replace("&nbsp;", "");
                content = content1.split("\\|\\|");


                final LinearLayout q = v.findViewById(R.id.qd);
                TextView ans = v.findViewById(R.id.correct_ans);

                final TextView your_ans = v.findViewById(R.id.your_ans);

                final String correct1 = Html.fromHtml(correct).toString();
                if (content1.contains("||")) {
                    content = content1.split("\\|\\|");
                    renderQO(content, correct1, q, ans, a);
                } else {
                    JSONArray jsonArray = new JSONArray(content1);
                    renderQO(jsonArray, correct1, q, ans, a);
                }
                if (!visible) {
                    LinearLayout a2 = v.findViewById(R.id.caard);
                    View v2 = LayoutInflater.from(this).inflate(R.layout.optionsview, a2, false);
                    v.setVisibility(View.GONE);
                    v.setTag("af");
                    TextView numbering = v.findViewById(R.id.numbering);
                    numbering.setText("QUESTION " + number);
                    number++;
                    grading++;
                    a2.addView(v2);
                    a.addView(v);
                } else {
                    LinearLayout a3 = a.findViewById(R.id.card_lin);
                    a3.addView(v);
                }


                RelativeLayout hideQuestionBtn = a.findViewById(R.id._questions);
                RelativeLayout askFriendBtn = a.findViewById(R.id.ask_friend);
                RelativeLayout votingBtn = a.findViewById(R.id.voting);
                RelativeLayout skipBtn = a.findViewById(R.id.skip);
                ImageView refresh = a.findViewById(R.id.refresh);
                RelativeLayout answerContainer = a.findViewById(R.id.ask_answer_container);
                LinearLayout votingContainer = a.findViewById(R.id.voting_container);
                TextView answerOption = a.findViewById(R.id.option);
                TextView answerDesc = a.findViewById(R.id.answer_desc);
                String desc = answerDescriptionArr[new Random().nextInt(answerDescriptionArr.length)];
                answerDesc.setText(desc);
                hideQuestionBtn.setOnClickListener(view -> {
//                        ImageView video_ad1_Icon = a.findViewById(R.id.video_ad1);
//                      if(removeOptions && fromProgress2){
//                          showInterstitial();
//                          removeOptions = false;
//                         // fromProgress2 = false;
//                          video_ad1_Icon.setVisibility(View.GONE);
//                         // hideQuestion(q,ans.getText().toString());
//                      }else{
//
//                          removeOptions = true;
//                         // fromProgress2 = false;
//
//                      }

                    hideQuestion(q, ans.getText().toString());
                    _2question = false;
                    ImageView badIcon = a.findViewById(R.id.bad1);
                    badIcon.setVisibility(View.VISIBLE);

                    hideQuestionBtn.setClickable(false);
                });

                askFriendBtn.setOnClickListener(view -> {
                    askFriend = false;
                    votingContainer.setVisibility(View.GONE);
                    ImageView badIcon = a.findViewById(R.id.bad2);
                    badIcon.setVisibility(View.VISIBLE);
                    answerContainer.setVisibility(View.VISIBLE);
                    String answer = ans.getText().toString().trim();
                    String correctOption = checkCorrectOptions(q, answer);
                    answerOption.setText(correctOption);
                    askFriendBtn.setClickable(false);

                });

                votingBtn.setOnClickListener(view -> {
                    answerContainer.setVisibility(View.GONE);
                    votingContainer.setVisibility(View.VISIBLE);
                    ImageView badIcon = a.findViewById(R.id.bad3);
                    badIcon.setVisibility(View.VISIBLE);
                    vote = false;
                    String answer = ans.getText().toString().trim();
                    String correctOption = checkCorrectOptions(q, answer);
                    setVotingGraph(a, correctOption);
                    votingBtn.setClickable(false);
                });

                skipBtn.setOnClickListener(view -> {

                    ImageView video_ad_Icon2 = a.findViewById(R.id.video_ad2);

                    AdManager.showInterstitial(GameActivity2.this);
//                        if(hasRefreshed && fromProgress2){
//                            showInterstitial();
//                            hasRefreshed = false;
//                            //fromProgress2 = false;
//                            video_ad_Icon2.setVisibility(View.GONE);
//                            // hideQuestion(q,ans.getText().toString());
//                        }else{
//
//                            hasRefreshed = true;
//                            //fromProgress2 = false;
//
//                        }

                    showInterstitial();


                    enableOptions(current.findViewById(R.id.qd));
                    //skip=false;
                    answerContainer.setVisibility(View.GONE);
                    votingContainer.setVisibility(View.GONE);
                    //showOptions(q);
                    // ImageView badIcon = a.findViewById(R.id.bad4);
                    // badIcon.setVisibility(View.VISIBLE);
                    refreshQuestion();
                    // skipBtn.setClickable(false);
                    rotateView(refresh, video_ad_Icon2, skipBtn);
                });


                for (int c = 0; c < q.getChildCount(); c++) {
                    final RelativeLayout r = (RelativeLayout) q.getChildAt(c);
                    final TextView k = q.getChildAt(c).findViewById(R.id.opt);
                    k.setTag(number);
                    int finalC = c;
                    r.setOnClickListener(v1 -> {
                        r.setEnabled(false);
                        disableOptions(q);
                        p1 = noOfPagesPassed + 2; //(int) k.getTag();
                        Log.i("render2== AF no of", String.valueOf(noOfPagesPassed));
                        Log.i("render2== AF", String.valueOf(p1));
                        checkNext = false;
                        if (cTimer != null) {
                            cTimer.cancel();
                        }
                        your_ans.setText(k.getText().toString());
                        Drawable gd = r.getBackground().mutate();
                        //LayerDrawable gd = new LayerDrawable(new Drawable[] { r.getBackground().mutate() });
                        gd.setColorFilter(ContextCompat.getColor(GameActivity2.this, R.color.orange), PorterDuff.Mode.SRC_IN);
                        r.setBackground(gd);
                        final RelativeLayout[] relativeLayout = {(RelativeLayout) q.getChildAt(finalC)};
                        final Drawable[] vectorDrawable = {gd};
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    for (int c1 = 0; c1 < q.getChildCount(); c1++) {


                                        TextView k1 = q.getChildAt(c1).findViewById(R.id.opt);


                                        if (k1.getText().toString().trim().equals(ans.getText().toString().trim())) {
                                            relativeLayout[0] = (RelativeLayout) q.getChildAt(c1);
                                            vectorDrawable[0] = relativeLayout[0].getBackground().mutate();

                                            if (number_of_failure >= 1) {
                                                vectorDrawable[0].setColorFilter(ContextCompat.getColor(GameActivity2.this, R.color.green), PorterDuff.Mode.SRC_IN);
                                                relativeLayout[0].setBackground(vectorDrawable[0]);
                                            }

                                        }

                                    }
                                    if (!k.getText().toString().trim().equals(ans.getText().toString().trim())) {
                                        VectorDrawable gd = (VectorDrawable) r.getBackground().mutate();
                                        gd.setColorFilter(ContextCompat.getColor(GameActivity2.this, R.color.redH), PorterDuff.Mode.SRC_IN);
                                        r.setBackground(gd);
                                        failCount++;
                                    }
                                    int questionId = 0;
                                    try {
                                        questionId = b.getInt("id");
                                        // Log.i("checking", String.valueOf(questionId));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    int currentProgress = noOfPagesPassed + 2;
                                    Log.i("renderpp", String.valueOf(noOfPagesPassed));
                                    checkAnswer(k.getText().toString(), ans.getText().toString(), currentProgress, vectorDrawable[0], relativeLayout[0], questionId);


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }, 1000);


                    });

                }
            }
        } catch (Exception e) {
            finish();
            e.printStackTrace();
            Toast.makeText(this, "Exam Cannot Be Loaded at this Time!!", Toast.LENGTH_LONG).show();
        }
    }

    public void wonder() {
        final ImageButton jl = findViewById(R.id.fwd);
        Timer buttonTimer = new Timer();
        buttonTimer.schedule(new TimerTask() {

            @Override
            public void run() {
                runOnUiThread(() -> {
                    if (!checkNext) {
                        //jl.performClick();
                        try {
                            next(jl);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        /*Intent intent = new Intent(ExamGameActivity.this,GameEnd.class);
                        intent.putExtra("level",String.valueOf(level));
                        intent.putExtra("score",String.valueOf(userScore));
                        startActivity(intent);
                        finish();*/
                    }
                });
            }
        }, 1000);
    }

    public void calcTime(String a) {

        time = Long.parseLong(countdownTime);
        startTimer();
    }


    public void next(View view) throws JSONException {
        checkNext = true;
        resetQuestion = false;
        LinearLayout parent = findViewById(R.id.displayExam);
        int i = parent.indexOfChild(current);
        setQuestion(noOfPagesPassed);


        RelativeLayout r = findViewById(R.id.nav);


        ImageButton f = findViewById(R.id.fwd);
        ImageButton p = findViewById(R.id.prev);

        nextView = parent.getChildAt(i);


        updateLifelines(nextView);
        if (parent.getChildAt(i + 2) == null)
            //f.setVisibility(View.GONE);
            if (nextView != null) {
                current.setVisibility(view.GONE);
                current = nextView;
                nextView.setVisibility(view.VISIBLE);
                if (nextView.getTag() != null) {
                    if (nextView.getTag().equals("intro")) {
                        r.setVisibility(View.GONE);
                    } else {
                        r.setVisibility(View.VISIBLE);
                    }
                }
            }
        p.setVisibility(View.GONE);
        f.setVisibility(View.GONE);
        r.setVisibility(View.GONE);
        ab = ab + 1;
        qPosition = ab;
    }

    public void resetQuestion(View current_old, View nextView) {
        checkNext = true;
        resetQuestion = true;
        LinearLayout parent = findViewById(R.id.displayExam);
        int i = parent.indexOfChild(current_old);
        try {
            setQuestion(noOfPagesPassed);
        } catch (Exception e) {
        }


        RelativeLayout r = findViewById(R.id.nav);


        ImageButton f = findViewById(R.id.fwd);
        ImageButton p = findViewById(R.id.prev);
        nextView = parent.getChildAt(i);

        updateLifelines(nextView);
        if (parent.getChildAt(i + 2) == null) f.setVisibility(View.GONE);
        if (nextView != null) {
            current_old.setVisibility(View.GONE);
            current = nextView;
            nextView.setVisibility(View.VISIBLE);
            if (nextView.getTag() != null) {
                if (nextView.getTag().equals("intro")) {
                    r.setVisibility(View.GONE);
                } else {
                    r.setVisibility(View.VISIBLE);
                }
            }
        }
        p.setVisibility(View.GONE);
        f.setVisibility(View.GONE);
        r.setVisibility(View.GONE);
        ab = ab + 1;
        qPosition = ab;
    }

    public void next2(View current_old, View nextView) {
        checkNext = true;
        resetQuestion = false;
        LinearLayout parent = findViewById(R.id.displayExam);
        int i = parent.indexOfChild(current_old);
        try {
            setQuestion(noOfPagesPassed);
        } catch (Exception e) {
        }


        RelativeLayout r = findViewById(R.id.nav);


        ImageButton f = findViewById(R.id.fwd);
        ImageButton p = findViewById(R.id.prev);
        nextView = parent.getChildAt(i);

        // updateLifelines(nextView);
/*        if(parent.getChildAt(i+2)==null)f.setVisibility(View.GONE);
        if(nextView !=null) {
            current_old.setVisibility(View.GONE);
            current = nextView;
            nextView.setVisibility(View.VISIBLE);
            if(nextView.getTag()!=null) {
                if (nextView.getTag().equals("intro")) {
                    r.setVisibility(View.GONE);
                } else {
                    r.setVisibility(View.VISIBLE);
                }
            }
        }*/
        p.setVisibility(View.GONE);
        f.setVisibility(View.GONE);
        r.setVisibility(View.GONE);
        ab = ab + 1;
        qPosition = ab;
    }


    void startTimer() {
        cTimer = new CountDownTimer(time, 1000) {
            TextView m = findViewById(R.id.time);

            public void onTick(long millisUntilFinished) {
                int sec = (int) (millisUntilFinished / 1000);
                int seconds = (sec % 3600) % 60;
                m.setText("" + seconds);
            }

            public void onFinish() {
                m.setText("00");
                cTimer.cancel();
                timeOut();
            }
        };
        cTimer.start();
        timeOn = true;
    }

    private void renderQO(JSONArray jsonArray, String correct1, LinearLayout q, TextView ans, LinearLayout a) {
        int t = 0;

        try {
            int duration = 300;
            long startTimev = 100;
            for (int j = 0; j < jsonArray.length(); j++) {
                JSONObject jsonObject = jsonArray.getJSONObject(j);
                String text = jsonObject.getString("text");
                String image = jsonObject.optString("filename");


                String d = null, cl = null;
                Character cl1 = null;
                if (correct1.length() > 1) {
                    d = correct1.substring(1).trim();
                    cl = correct1.substring(0, 1);
                    cl1 = correct1.charAt(1);
                }
                if (cl != null && cl.equalsIgnoreCase("A") && cl1 != null && Character.isDigit(cl1) || cl != null && cl.equalsIgnoreCase("R") && cl1 != null && Character.isDigit(cl1)) {
                    String t_st = Character.toString(correct1.charAt(1));
                    t = Integer.parseInt(t_st) - 1;


                } else {
                    if (text.trim().equalsIgnoreCase(correct1.trim())) {
                        t = j;
                    }
                }


                String str = text;
                try {
                    str = str.substring(0, 1).toUpperCase() + str.substring(1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                str = str.replace("\n", "");


                View vi = LayoutInflater.from(this).inflate(R.layout.options_layout, a, false);
                Animation animation = AnimationUtils.loadAnimation(getBaseContext(), R.anim.slide_in_right);
                // animation.setStartOffset(0);
                animation.setStartOffset(((j + 1) * 200) + 1000);

                vi.startAnimation(animation);
                RelativeLayout card = vi.findViewById(R.id.view_cont);

                TextView k = vi.findViewById(R.id.opt);
                TextView optionTxt = vi.findViewById(R.id.alpha_opt);
                optionTxt.setText(numbs[j]);

                k.setText(str);


                if (j == t) {
                    ans.setText(str);

                }

                q.addView(vi);

                //card.startAnimation(inFromLeftAnimation(duration,j*startTimev));


            }
        } catch (NumberFormatException | JSONException e) {//finish();
            e.printStackTrace();
            //Toast.makeText(this, "Exam Cannot Be Loaded at this Time!!", Toast.LENGTH_LONG).show();
        }
    }

    private void renderQO(String[] arr, String correct1, LinearLayout q, TextView ans, LinearLayout a) {


        String corr = null;
        int t = 0;

        try {

            int duration = 300;
            long startTimev = 100;
            for (int j = 0; j < arr.length; j++) {

                String d = null, cl = null;
                Character cl1 = null;
                if (correct1.length() > 1) {
                    d = correct1.substring(1).trim();
                    cl = correct1.substring(0, 1);
                    cl1 = correct1.charAt(1);
                }
                if (cl != null && cl.equalsIgnoreCase("A") && cl1 != null && Character.isDigit(cl1) || cl != null && cl.equalsIgnoreCase("R") && cl1 != null && Character.isDigit(cl1)) {
                    String t_st = Character.toString(correct1.charAt(1));
                    t = Integer.parseInt(t_st) - 1;


                } else {
                    if (arr[j].trim().equalsIgnoreCase(correct1.trim())) {
                        t = j;
                    }
                }
                                /*String t_st = Character.toString(correct1.charAt(1));
                                try {
                                    t = Integer.parseInt(t_st) - 1;
                                }catch (NumberFormatException e){
                                    e.printStackTrace();
                                    if (content[j].trim().equalsIgnoreCase(correct1.trim())) {
                                    t=j;
                                    }
                                }*/


                String str = arr[j];
                try {
                    str = str.substring(0, 1).toUpperCase() + str.substring(1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                str = str.replace("\n", "");


                View vi = LayoutInflater.from(this).inflate(R.layout.options_layout, a, false);
                RelativeLayout card = vi.findViewById(R.id.view_cont);

                TextView k = vi.findViewById(R.id.opt);
                TextView optionTxt = vi.findViewById(R.id.alpha_opt);
                optionTxt.setText(numbs[j]);

                k.setText(str);
                if (j == t) {
                    ans.setText(str);
                }

                q.addView(vi);

                //card.startAnimation(inFromLeftAnimation(duration,j*startTimev));


            }
        } catch (NumberFormatException e) {//finish();
            e.printStackTrace();
            //Toast.makeText(this, "Exam Cannot Be Loaded at this Time!!", Toast.LENGTH_LONG).show();
        }
    }


    private Animation inFromLeftAnimation(int duration, Long startTime) {
        Animation inFromLeft = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, -1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        inFromLeft.setDuration(duration);
        inFromLeft.setStartOffset(startTime);
        inFromLeft.setInterpolator(new AccelerateInterpolator());
        return inFromLeft;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return false;
    }

    private void checkAnswer(
            String answer,
            String correct,
            int number1,
            Drawable vectorDrawable,
            RelativeLayout relativeLayout,
            int questionID
    ) {

        loadVideoAd();

        continueSound = true;
        String amountWon_main = GameActivity2.amountWon.replace("$", "").replace(",", "");

        if (answer.trim().equals(correct.trim())) {
            number_of_failure = 0;
            noOfQuestionAnswered++;
            noOfPagesPassed++;

            vectorDrawable.setColorFilter(ContextCompat.getColor(GameActivity2.this, R.color.green), PorterDuff.Mode.SRC_IN);
            relativeLayout.setBackground(vectorDrawable);
            if (vibrate.equals("1")) {
                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                }
            }
            if (sound.equals("1")) {
                playSuccessSound();
            }
            noOfCorrectAnswer++;
            saveNoOfCorrectAnswer();
            amountWon = String.valueOf(moneyArr[p1 - 2]);
            String amountWon1 = GameActivity2.amountWon.replace("$", "").replace(",", "");


            saveHistory(questionID, answer, correct, amountWon1, true);
//            CorrectAnswerDialog correctAnswerDialog = new  CorrectAnswerDialog();
//            correctAnswerDialog.show(  getSupportFragmentManager(),
//                    "Customer details Bottom sheet");
            final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
            bottomSheetDialog.setContentView(R.layout.correct_answer_dialog);

            try {
                TextView alpha_opt = bottomSheetDialog.findViewById(R.id.alpha_opt);
                TextView opt = bottomSheetDialog.findViewById(R.id.opt);
                TextView reason = bottomSheetDialog.findViewById(R.id.reason);

                assert opt != null;
                opt.setText(Utils.capitalizeFirstWord(singleQuestion.getString("correct")));
                JSONArray opt_answer = new JSONArray(singleQuestion.getString("answer"));
                int position = getOptionIndex(opt_answer, singleQuestion.getString("correct"));
                alpha_opt.setText(numbs[position]);

                reason.setText(Utils.capitalizeFirstWord(singleQuestion.getString("reason")));
                RelativeLayout readMore = bottomSheetDialog.findViewById(R.id.read_more);
                assert readMore != null;
                readMore.setOnClickListener(view -> Utils.navigateToWebview(String.valueOf(questionID), GameActivity2.this));
//

            } catch (Exception e) {
                e.printStackTrace();

            }
            bottomSheetDialog.show();


            //bottomSheetDialog.


//            final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
//
//            bottomSheetDialog.setContentView(R.layout.correct_answer_dialog);
            bottomSheetDialog.setOnDismissListener(dialogInterface -> {

                bottomSheetDialog.dismiss();


                /////

                new Handler().postDelayed(() -> {
                    if (noOfCorrectAnswer >= 15) {
                        loadSongs();
                        SharedPreferences sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE);
                        int current_play_level_int = Integer.parseInt(sharedPreferences.getString("current_play_level", "1"));
                        int newGameLevel = current_play_level_int + 1;
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        /// editor.putString("game_level",String.valueOf(newGameLevel));
                        editor.putString("current_play_level", String.valueOf(newGameLevel));

                        if (hasOldWinningAmount) {
                            String amountWon = GameActivity2.amountWon.replace("$", "").replace(",", "");
                            String oldAmountWon = sharedPreferences.getString("amountWon", "0").replace("$", "").replace(",", "");
                            int parsedAmountWon;
                            int parsedOldAmount;

                            try {

                                if (amountWon.isEmpty()) {
                                    parsedAmountWon = 0;
                                } else {
                                    parsedAmountWon = Integer.parseInt(amountWon);
                                }

                                if (oldAmountWon.isEmpty()) {
                                    parsedOldAmount = 0;
                                } else {
                                    parsedOldAmount = Integer.parseInt(oldAmountWon);
                                }

                                int newAmount = parsedAmountWon + parsedOldAmount;
                                DecimalFormat formatter = new DecimalFormat("#,###,###");
                                String formatted_newAmount = formatter.format(newAmount);
                                editor.putString("amountWon", formatted_newAmount);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            editor.putString("amountWon", amountWon);
                        }
                        editor.apply();

                        Intent intent = new Intent(GameActivity2.this, WinnersActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("isWon", true);
                        intent.putExtra("isShowAd", false);
                        startActivity(intent);
                        //finish();
                    } else {
                        Intent intent = new Intent(GameActivity2.this, ProgressActivity.class);
                        intent.putExtra("number", number1);
                        intent.putExtra("timer", "true");
                        if (Build.VERSION.SDK_INT > 20) {
                            ActivityOptions options =
                                    ActivityOptions.makeSceneTransitionAnimation(GameActivity2.this);
                            startActivity(intent);
                        } else {
                            startActivity(intent);
                        }
                        wonder();
                    }
                }, 1000);

                //bottomSheetDialog.show();


                /////

            });


            RelativeLayout close_dialog = bottomSheetDialog.findViewById(R.id.close_dialog);
            assert close_dialog != null;
            close_dialog.setOnClickListener(view -> {
                close_dialog.setEnabled(false);

                bottomSheetDialog.dismiss();


            });


        } else {
            saveHistory(questionID, answer, correct, amountWon_main, false);
            if (sound.equals("1")) {
                playFailureSound();
            }

            if (vibrate.equals("1")) {
                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                }
            }

            if (number_of_failure < 1) {
                //////First Failure//////
                number_of_failure++;
                WrongAnswerDialog wrongAnswerDialog = new WrongAnswerDialog(GameActivity2.this, CountDownActivity.mMediaPlayer);

                wrongAnswerDialog.show();
                wrongAnswerDialog.setCancelable(false);
                RelativeLayout giveUp = wrongAnswerDialog.findViewById(R.id.give_up);
                RelativeLayout play_again = wrongAnswerDialog.findViewById(R.id.play_again);
                ImageView cancel_icon = wrongAnswerDialog.findViewById(R.id.cancel_icon);

                cancel_icon.setOnClickListener(view -> {

                    cancel_icon.setEnabled(false);
                    giveUp.performClick();
                });

                play_again.setOnClickListener(view -> {
                    if (Utils.isOnline(GameActivity2.this)) {
                        wrongAnswerDialog.showRewarededAdWithListener();

                    } else {

                        giveUp.performClick();

                    }
                });

                giveUp.setOnClickListener(view -> {
                    wrongAnswerDialog.dismiss();

                    final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(GameActivity2.this);
                    bottomSheetDialog.setContentView(R.layout.correct_answer_dialog);

                    try {
                        TextView alpha_opt = bottomSheetDialog.findViewById(R.id.alpha_opt);
                        TextView opt = bottomSheetDialog.findViewById(R.id.opt);
                        assert opt != null;
                        opt.setText(Utils.capitalizeFirstWord(singleQuestion.getString("correct")));
                        TextView reason = bottomSheetDialog.findViewById(R.id.reason);
                        assert reason != null;
                        reason.setText(Utils.capitalizeFirstWord(singleQuestion.getString("reason")));
                        JSONArray opt_answer = new JSONArray(singleQuestion.getString("answer"));
                        int position = getOptionIndex(opt_answer, singleQuestion.getString("correct"));
                        alpha_opt.setText(numbs[position]);
                        RelativeLayout readMore = bottomSheetDialog.findViewById(R.id.read_more);
                        assert readMore != null;
                        readMore.setOnClickListener(view1 -> Utils.navigateToWebview(String.valueOf(questionID), GameActivity2.this));
//

                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                    bottomSheetDialog.show();
                    RelativeLayout close_dialog = bottomSheetDialog.findViewById(R.id.close_dialog);
                    assert close_dialog != null;
                    close_dialog.setOnClickListener(view12 -> {
                        close_dialog.setEnabled(false);

                        bottomSheetDialog.dismiss();

                    });
                    bottomSheetDialog.setOnDismissListener(dialogInterface -> {


                        bottomSheetDialog.dismiss();

//
                        if (CountDownActivity.mMediaPlayer != null) {
                            CountDownActivity.mMediaPlayer.pause();
                        }


                        Intent intent = new Intent(GameActivity2.this, FailureActivity.class);
                        startActivity(intent);


                    });

                });


            } else {
                /////Total Failure///

                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(GameActivity2.this);
                bottomSheetDialog.setContentView(R.layout.correct_answer_dialog);

                try {
                    TextView alpha_opt = bottomSheetDialog.findViewById(R.id.alpha_opt);
                    TextView opt = bottomSheetDialog.findViewById(R.id.opt);
                    assert opt != null;
                    opt.setText(Utils.capitalizeFirstWord(singleQuestion.getString("correct")));
                    TextView reason = bottomSheetDialog.findViewById(R.id.reason);
                    JSONArray opt_answer = new JSONArray(singleQuestion.getString("answer"));
                    reason.setText(Utils.capitalizeFirstWord(singleQuestion.getString("reason")));
                    int position = getOptionIndex(opt_answer, singleQuestion.getString("correct"));
                    alpha_opt.setText(numbs[position]);
                    RelativeLayout readMore = bottomSheetDialog.findViewById(R.id.read_more);
                    assert readMore != null;
                    readMore.setOnClickListener(view -> Utils.navigateToWebview(String.valueOf(questionID), GameActivity2.this));
//

                } catch (Exception e) {
                    e.printStackTrace();

                }
                bottomSheetDialog.show();
                RelativeLayout close_dialog = bottomSheetDialog.findViewById(R.id.close_dialog);
                assert close_dialog != null;
                close_dialog.setOnClickListener(view -> {
                    close_dialog.setEnabled(false);
                    bottomSheetDialog.dismiss();

                });
                bottomSheetDialog.setOnDismissListener(dialogInterface -> new Handler().postDelayed(() -> {

                    Intent intent = new Intent(GameActivity2.this, FailureActivity.class);
                    if (hasOldWinningAmount) {
                        intent.putExtra("hasOldWinningAmount", hasOldWinningAmount);
                    }
                    if (CountDownActivity.mMediaPlayer != null) {
                        CountDownActivity.mMediaPlayer.pause();
                    }
                    startActivity(intent);
                }, 500));

            }


        }

    }

    private void playSuccessSound() {
        try {
            if (CountDownActivity.mSuccessPlayer != null) {
                CountDownActivity.mSuccessPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                CountDownActivity.mSuccessPlayer.setLooping(false);
                CountDownActivity.mSuccessPlayer.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void playFailureSound() {
        //CountDownActivity.mFailurePlayer  = MediaPlayer.create(this, R.raw.failure_sound);
        try {
            if (CountDownActivity.mFailurePlayer != null) {
                CountDownActivity.mFailurePlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                CountDownActivity.mFailurePlayer.setLooping(false);
                CountDownActivity.mFailurePlayer.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void playBackgroundSound() {
        try {
            if (CountDownActivity.mMediaPlayer != null) {
                CountDownActivity.mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                CountDownActivity.mMediaPlayer.setLooping(true);
                CountDownActivity.mMediaPlayer.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        try {
            loadInterstialAd();
            loadVideoAd();


            Log.i("obidati", String.valueOf(noOfPagesPassed));

//        ImageView video_ad_Icon1 = current.findViewById(R.id.video_ad1);
            //    ImageView video_ad_Icon2 = current.findViewById(R.id.video_ad2);
            if (removeOptions && fromProgress2) {
                /// video_ad_Icon1.setVisibility(View.VISIBLE);

            }
            if (hasRefreshed && fromProgress2) {
                //  video_ad_Icon2.setVisibility(View.VISIBLE);

            }


            Log.i("Loadeddd", String.valueOf(fromProgress));
            if (fromProgress) {
                fromProgress = false;
                //hideLifeGuard();
                //number_of_failure = 0;
                LinearLayout parent = findViewById(R.id.displayExam);
                int i = parent.indexOfChild(current);
                nextView = parent.getChildAt(i);

                RelativeLayout answerContainer = parent.findViewById(R.id.ask_answer_container);
                answerContainer.setVisibility(View.GONE);


                if (noOfCorrectAnswer <= 14) {
                    try {
                        updateLifelines(nextView);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            } else {

                // showInterstitial();
                fromProgress = true;

            }

            continueSound = false;

            if (continueGame) {
                hideLifeGuard();
                number_of_failure = 0;

                if (cTimer != null) {
                    cTimer.cancel();
                    countdownTime = "60000";
                    startTimer();
                }

                enableOptions(current.findViewById(R.id.qd));
                //AllowOptions(findViewById(R.id.qd));
//            ImageView badIcon = lay.findViewById(R.id.bad4);
//            badIcon.setVisibility(View.VISIBLE);

                // refreshQuestion();

                // setQuestion(noOfPagesPassed);

                Log.i("continue", "continue");

                refreshQuestion();
                // wonder();
                //next2();
                continueGame = false;

            } else {
                Log.i("continue", "dicontinue");

            }
            if (sound.equals("1")) {

                playBackgroundSound();
            }

            setAmountWon();


//        if(mRewardedVideoAd==null) {
//            showRewardedVideo();
//        }else {
//            mRewardedVideoAd.show();
//        }

            Log.i("obidati", String.valueOf(noOfPagesPassed));


            if (isStartAtFresh) {
                countDown();

                isStartAtFresh = false;
                json = dbHelper.buildJson();
                json = Html.fromHtml(json).toString();
                noOfPagesPassed = 0;
                _2question = true;
                askFriend = true;
                vote = true;
                startDisplay(json);
                playStong();
                playBackgroundSound();
                number_of_failure = 0;
                noOfCorrectAnswer = 0;


                SharedPreferences sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE);

                String game_level = sharedPreferences.getString("game_level", "1");
                int game_level_int = Integer.parseInt(game_level);

                moneyArr = new Integer[]{500 * (game_level_int), 1000 * (game_level_int), 2000 * (game_level_int),
                        3000 * (game_level_int), 5000 * (game_level_int), 7500 * (game_level_int), 10000 * (game_level_int),
                        12500 * (game_level_int), 15000 * (game_level_int), 25000 * (game_level_int),
                        50000 * (game_level_int), 100000 * (game_level_int), 250000 * (game_level_int), 500000 * (game_level_int),
                        1000000 * (game_level_int)};


                //   gameStateSharedPreference.edit().putBoolean("isStartGame", isStartAtFresh).apply();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void countDown() {
        Intent intent = new Intent(GameActivity2.this, CountDownActivity.class);

        startActivity(intent);
    }

    public void showRewardedVideo() {

//        if (mRewardedVideoAd.isLoaded()) {
//            if(CountDownActivity.mMediaPlayer!=null) {
//                CountDownActivity.mMediaPlayer.stop();
//            }
//            mRewardedVideoAd.show();
//
//        }
        AdManager.showRewardAd(GameActivity2.this);
    }


    @Override
    protected void onPause() {
        super.onPause();
        //mMediaPlayer.stop();
        //mMediaPlayer.release();
        if (isFinishing()) {
            if (cTimer != null) {
                cTimer.cancel();
            }
        }

        if (!continueSound && CountDownActivity.mMediaPlayer != null) {
            CountDownActivity.mMediaPlayer.pause();
        }

    }


    @Override
    public void onBackPressed() {
//        ExitDialog dialog = new ExitDialog(this);
//        dialog.show();
//        Window window = dialog.getWindow();
//        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    private void hideQuestion(View v, String correct) {
        int p = 0;
        List<Integer> optNumberList = new ArrayList<>();
        LinearLayout q = v.findViewById(R.id.qd);
        int optionsSize = q.getChildCount();
        for (int c = 0; c < optionsSize; c++) {
            final RelativeLayout r = (RelativeLayout) q.getChildAt(c);
            TextView k = r.findViewById(R.id.opt);
            if (k.getText().toString().trim().equals(correct.trim())) {
                p = c;
            }
            //    Animation aniFade = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.faddout);
//            r.startAnimation(aniFade);
            r.setVisibility(View.INVISIBLE);
            optNumberList.add(c);
        }

        optNumberList.remove(Integer.valueOf(p));
        int[] integers = new int[optNumberList.size()];
        for (int i = 0; i < integers.length; i++) {
            integers[i] = optNumberList.get(i);
        }

        int position = getRandom(integers);
        show2question(p, position, v);

    }

    public static int getRandom(int[] array) {
        int rnd = new Random().nextInt(array.length);
        return array[rnd];
    }

    private void show2question(int a, int b, View v) {
        LinearLayout q = v.findViewById(R.id.qd);
        final RelativeLayout r1 = (RelativeLayout) q.getChildAt(a);
        final RelativeLayout r2 = (RelativeLayout) q.getChildAt(b);
        TextView t1 = r1.findViewById(R.id.alpha_opt);
        TextView t2 = r2.findViewById(R.id.alpha_opt);
        option1 = t1.getText().toString().trim();
        option2 = t2.getText().toString().trim();
        try {
            option1 = option1.substring(0, 1);
            option2 = option2.substring(0, 1);
        } catch (StringIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        r1.setVisibility(View.VISIBLE);
        r2.setVisibility(View.VISIBLE);
    }

    private void updateLifelines(View view) {
        if (!_2question) {
            ImageView badIcon = view.findViewById(R.id.bad1);
            badIcon.setVisibility(View.VISIBLE);
            RelativeLayout hideQuestionBtn = view.findViewById(R.id._questions);
            hideQuestionBtn.setClickable(false);
        }

        if (!askFriend) {
            ImageView badIcon = view.findViewById(R.id.bad2);
            badIcon.setVisibility(View.VISIBLE);
            RelativeLayout askFriendBtn = view.findViewById(R.id.ask_friend);
            askFriendBtn.setClickable(false);
        }

        if (!vote) {
            ImageView badIcon = view.findViewById(R.id.bad3);
            badIcon.setVisibility(View.VISIBLE);
            RelativeLayout votingBtn = view.findViewById(R.id.voting);
            votingBtn.setClickable(false);
        }
//        if(!skip){
//            ImageView badIcon = view.findViewById(R.id.bad4);
//            badIcon.setVisibility(View.VISIBLE);
//            RelativeLayout votingBtn = view.findViewById(R.id.skip);
//            votingBtn.setClickable(false);
//        }
    }

    private void setVotingGraph(View v, String option) {
        option = option.trim();
        LinearLayout progressA, progressATotal, progressB, progressBTotal, progressC, progressCTotal, progressD, progressDtotal;
        TextView progressTextA, progressTextB, progressTextC, progressTextD;
        progressA = v.findViewById(R.id.progressA);
        progressATotal = v.findViewById(R.id.progress_totalA);
        progressB = v.findViewById(R.id.progressB);
        progressBTotal = v.findViewById(R.id.progress_totalB);
        progressC = v.findViewById(R.id.progressC);
        progressCTotal = v.findViewById(R.id.progress_totalC);
        progressD = v.findViewById(R.id.progressD);
        progressDtotal = v.findViewById(R.id.progress_totalD);
        progressTextA = v.findViewById(R.id.progress1);
        progressTextB = v.findViewById(R.id.progress2);
        progressTextC = v.findViewById(R.id.progress3);
        progressTextD = v.findViewById(R.id.progress4);

        if (option.equalsIgnoreCase("A")) {
            float progressWeigth = answerPercentages[new Random().nextInt(answerPercentages.length)];
            float remainingWeigth = 100 - progressWeigth;
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, remainingWeigth);
            progressA.setLayoutParams(layoutParams);
            LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, progressWeigth);
            progressATotal.setLayoutParams(layoutParams1);
            progressTextA.setText(progressWeigth + "%");
            LinearLayout[] l = {progressC, progressB, progressD};
            LinearLayout[] l1 = {progressCTotal, progressBTotal, progressDtotal};
            TextView[] t = {progressTextC, progressTextB, progressTextD};
            setUpNonAnswerBars(progressWeigth, l, l1, t);
        } else if (option.equalsIgnoreCase("B")) {
            float progressWeigth = answerPercentages[new Random().nextInt(answerPercentages.length)];
            float remainingWeigth = 100 - progressWeigth;
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, remainingWeigth);
            progressB.setLayoutParams(layoutParams);
            LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, progressWeigth);
            progressBTotal.setLayoutParams(layoutParams1);
            progressTextB.setText(progressWeigth + "%");
            LinearLayout[] l = {progressA, progressC, progressD};
            LinearLayout[] l1 = {progressATotal, progressCTotal, progressDtotal};
            TextView[] t = {progressTextA, progressTextC, progressTextD};
            setUpNonAnswerBars(progressWeigth, l, l1, t);

        } else if (option.equalsIgnoreCase("C")) {
            float progressWeigth = answerPercentages[new Random().nextInt(answerPercentages.length)];
            float remainingWeigth = 100 - progressWeigth;
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, remainingWeigth);
            progressC.setLayoutParams(layoutParams);
            LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, progressWeigth);
            progressCTotal.setLayoutParams(layoutParams1);
            progressTextC.setText(progressWeigth + "%");
            float[] weightsPercents;

            LinearLayout[] l = {progressA, progressB, progressD};
            LinearLayout[] l1 = {progressATotal, progressBTotal, progressDtotal};
            TextView[] t = {progressTextA, progressTextB, progressTextD};
            setUpNonAnswerBars(progressWeigth, l, l1, t);
        } else if (option.equalsIgnoreCase("D")) {
            float progressWeigth = answerPercentages[new Random().nextInt(answerPercentages.length)];
            float remainingWeigth = 100 - progressWeigth;
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, remainingWeigth);
            progressD.setLayoutParams(layoutParams);
            LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, progressWeigth);
            progressDtotal.setLayoutParams(layoutParams1);
            progressTextD.setText(progressWeigth + "%");
            LinearLayout[] l = {progressA, progressB, progressC};
            LinearLayout[] l1 = {progressATotal, progressBTotal, progressCTotal};
            TextView[] t = {progressTextA, progressTextB, progressTextC};
            setUpNonAnswerBars(progressWeigth, l, l1, t);

        }

        displayOrHideBar(option1, option2);
    }

    private void setUpNonAnswerBars(float progressWeigth, LinearLayout[] l, LinearLayout[] l1, TextView[] t) {
        float[] weightsPercents;
        if (progressWeigth == 80) {
            weightsPercents = new float[]{5, 11, 4};
            for (int b = 0; b < l.length; b++) {
                LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 100 - weightsPercents[b]);
                l[b].setLayoutParams(layoutParams3);
                LinearLayout.LayoutParams layoutParams4 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, weightsPercents[b]);
                l1[b].setLayoutParams(layoutParams4);
                t[b].setText(weightsPercents[b] + "%");
                if (option2 != null && option1 != null) {
                    t[b].setText(5 + 11 + 4 + "%");
                }

            }

        } else if (progressWeigth == 70) {
            weightsPercents = new float[]{25, 4, 1};
            for (int b = 0; b < l.length; b++) {
                LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 100 - weightsPercents[b]);
                l[b].setLayoutParams(layoutParams3);
                LinearLayout.LayoutParams layoutParams4 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, weightsPercents[b]);
                l1[b].setLayoutParams(layoutParams4);
                t[b].setText(weightsPercents[b] + "%");
                if (option2 != null && option1 != null) {
                    t[b].setText(25 + 4 + 1 + "%");

                }

            }

        } else if (progressWeigth == 65) {
            weightsPercents = new float[]{10, 16, 9};
            for (int b = 0; b < l.length; b++) {
                LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 100 - weightsPercents[b]);
                l[b].setLayoutParams(layoutParams3);
                LinearLayout.LayoutParams layoutParams4 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, weightsPercents[b]);
                l1[b].setLayoutParams(layoutParams4);
                t[b].setText(weightsPercents[b] + "%");
                if (option2 != null && option1 != null) {
                    t[b].setText(10 + 16 + 9 + "%");
                }
            }
        }
    }


    private void disableOptions(LinearLayout q) {
        if (number_of_failure >= 1) {
            for (int c = 0; c < q.getChildCount(); c++) {
                final RelativeLayout r = (RelativeLayout) q.getChildAt(c);
                r.setClickable(false);
                Log.i("Cliiiii", "Cliiiii22222222222" + String.valueOf(c));
            }
        }
    }

    private void enableOptions(LinearLayout q) {
        Log.i("Cliiiii", "Cliiiii");
        for (int c = 0; c < q.getChildCount(); c++) {

            final RelativeLayout r = (RelativeLayout) q.getChildAt(c);

            try {
                VectorDrawable gd = (VectorDrawable) r.getBackground().mutate();
                gd.setColorFilter(ContextCompat.getColor(GameActivity2.this, R.color.basic_color), PorterDuff.Mode.SRC_IN);
                r.setBackground(gd);
            } catch (Exception e) {
                BitmapDrawable g = (BitmapDrawable) r.getBackground().mutate();
                g.setColorFilter(ContextCompat.getColor(GameActivity2.this, R.color.basic_color), PorterDuff.Mode.SRC_IN);
                r.setBackground(g);
            }
            // r.setBackgroundColor(getResources().getColor(R.color.green));
            r.setVisibility(View.VISIBLE);
            r.setActivated(true);
            r.setClickable(true);
            Log.i("Cliiiii", "Cliiiii" + String.valueOf(c));
        }
    }

    private void hideOptions(LinearLayout q) {
        Log.i("Cliiiii", "Cliiiii");
        for (int c = 0; c < q.getChildCount(); c++) {

            final RelativeLayout r = (RelativeLayout) q.getChildAt(c);
            VectorDrawable gd = (VectorDrawable) r.getBackground().mutate();
            gd.setColorFilter(ContextCompat.getColor(GameActivity2.this, R.color.basic_color), PorterDuff.Mode.SRC_IN);
            r.setBackground(gd);
            Animation animation = AnimationUtils.loadAnimation(getBaseContext(), R.anim.slide_in_left);
            // animation.setStartOffset(0);
            animation.setStartOffset((c + 1) * 200);
            // r.setBackgroundColor(getResources().getColor(R.color.green));

            r.setAnimation(animation);
            // r.setVisibility(View.GONE);
//            r.setActivated(true);
//            r.setClickable(true);
//            Log.i("Cliiiii","Cliiiii"+String.valueOf(c));
        }
    }

    private void showOptions2(LinearLayout q) {
        Log.i("Cliiiii", "Cliiiii");
        for (int c = 0; c < q.getChildCount(); c++) {

            final RelativeLayout r = (RelativeLayout) q.getChildAt(c);

            try {
                VectorDrawable gd = (VectorDrawable) r.getBackground().mutate();
                gd.setColorFilter(ContextCompat.getColor(GameActivity2.this, R.color.basic_color), PorterDuff.Mode.SRC_IN);
                r.setBackground(gd);
            } catch (Exception e) {
                BitmapDrawable g = (BitmapDrawable) r.getBackground().mutate();
                g.setColorFilter(ContextCompat.getColor(GameActivity2.this, R.color.basic_color), PorterDuff.Mode.SRC_IN);
                r.setBackground(g);
            }

            Animation animation = AnimationUtils.loadAnimation(getBaseContext(), R.anim.slide_in_right);
            // animation.setStartOffset(0);
            animation.setStartOffset((c + 1) * 200);
            // r.setBackgroundColor(getResources().getColor(R.color.green));
            r.setVisibility(View.VISIBLE);
            r.setAnimation(animation);
//            r.setActivated(true);
//            r.setClickable(true);
//            Log.i("Cliiiii","Cliiiii"+String.valueOf(c));
        }
    }


    private String checkCorrectOptions(LinearLayout q, String option) {
        String correctOption = "";
        for (int c = 0; c < q.getChildCount(); c++) {
            final RelativeLayout r = (RelativeLayout) q.getChildAt(c);
            final TextView k = q.getChildAt(c).findViewById(R.id.opt);
            TextView optionTxt = q.getChildAt(c).findViewById(R.id.alpha_opt);
            if (k.getText().toString().trim().equals(option.trim())) {
                correctOption = optionTxt.getText().toString();
            }
        }
        return correctOption;
    }

    private void showOptions(LinearLayout q) {
        for (int c = 0; c < q.getChildCount(); c++) {
            final RelativeLayout r = (RelativeLayout) q.getChildAt(c);
            r.setVisibility(View.VISIBLE);
        }
    }

    private void timeOut() {

//        disableOptions(findViewById(R.id.qd));
//        TextView correctAns = findViewById(R.id.correct_ans);
//        LinearLayout q = findViewById(R.id.qd);
//        for(int a=0;a<q.getChildCount();a++){
//            RelativeLayout ansContainer = (RelativeLayout) q.getChildAt(a);
//            TextView answer = ansContainer.findViewById(R.id.opt);
//            if(correctAns.getText().toString().equalsIgnoreCase(answer.getText().toString())){
//                ansContainer.setBackgroundColor(getResources().getColor(R.color.green));
//                answer.setTextColor(getResources().getColor(R.color.white));
//            }
//        }

        try {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    FailDialog dialog;
                    dialog = new FailDialog(GameActivity2.this, amountWon);
                    dialog.setCancelable(false);
                    dialog.setCanceledOnTouchOutside(false);
                    if (!isFinishing()) {
                        // dialog.show();
                        final ImageButton jl = findViewById(R.id.fwd);

                        WrongAnswerDialog wrongAnswerDialog = new WrongAnswerDialog(GameActivity2.this, CountDownActivity.mMediaPlayer);

                        wrongAnswerDialog.show();
                        wrongAnswerDialog.setCancelable(false);
                        RelativeLayout giveUp = wrongAnswerDialog.findViewById(R.id.give_up);
                        RelativeLayout play_again = wrongAnswerDialog.findViewById(R.id.play_again);
                        ImageView cancel_icon = wrongAnswerDialog.findViewById(R.id.cancel_icon);

                        cancel_icon.setOnClickListener(view -> {

                            cancel_icon.setEnabled(false);
                            giveUp.performClick();
                        });

                        play_again.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (Utils.isOnline(GameActivity2.this)) {
                                    wrongAnswerDialog.showRewarededAdWithListener();
                                    wrongAnswerDialog.mRewardedVideoAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                        @Override
                                        public void onAdDismissedFullScreenContent() {
                                            super.onAdDismissedFullScreenContent();
                                            setTime();
                                        }

                                    });

                                } else {

                                    giveUp.performClick();

                                }
                            }
                        });

                        giveUp.setOnClickListener(view -> {
                            wrongAnswerDialog.dismiss();

                            final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(GameActivity2.this);
                            bottomSheetDialog.setContentView(R.layout.correct_answer_dialog);

                            try {
                                TextView alpha_opt = bottomSheetDialog.findViewById(R.id.alpha_opt);
                                TextView opt = bottomSheetDialog.findViewById(R.id.opt);
                                assert opt != null;
                                opt.setText(Utils.capitalizeFirstWord(singleQuestion.getString("correct")));
                                TextView reason = bottomSheetDialog.findViewById(R.id.reason);
                                int questionID = singleQuestion.getInt("id");
                                reason.setText(Utils.capitalizeFirstWord(singleQuestion.getString("reason")));
                                JSONArray opt_answer = new JSONArray(singleQuestion.getString("answer"));
                                int position = getOptionIndex(opt_answer, singleQuestion.getString("correct"));
                                alpha_opt.setText(numbs[position]);
                                RelativeLayout readMore = bottomSheetDialog.findViewById(R.id.read_more);
                                readMore.setOnClickListener(view1 -> Utils.navigateToWebview(String.valueOf(questionID), GameActivity2.this));
//

                            } catch (Exception e) {
                                e.printStackTrace();

                            }
                            bottomSheetDialog.show();
                            RelativeLayout close_dialog = bottomSheetDialog.findViewById(R.id.close_dialog);
                            assert close_dialog != null;
                            close_dialog.setOnClickListener(view12 -> {
                                close_dialog.setEnabled(false);

                                bottomSheetDialog.dismiss();

                            });
                            bottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialogInterface) {


                                    bottomSheetDialog.dismiss();

//
                                    if (CountDownActivity.mMediaPlayer != null) {
                                        CountDownActivity.mMediaPlayer.pause();
                                    }


                                    Intent intent = new Intent(GameActivity2.this, FailureActivity.class);
                                    startActivity(intent);


                                }
                            });

                        });


                    }
                    Window window = dialog.getWindow();
                    window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                }
            }, 1000);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void displayOrHideBar(String option1, String option2) {
        int position1 = 0;
        int position2 = 0;
        if (option1 != null && option2 != null) {
            switch (option1.trim().toLowerCase()) {
                case "a":
                    position1 = 0;
                    break;
                case "b":
                    position1 = 1;
                    break;
                case "c":
                    position1 = 2;
                    break;
                case "d":
                    position1 = 3;
                    break;
            }

            switch (option2.trim().toLowerCase()) {
                case "a":
                    position2 = 0;
                    break;
                case "b":
                    position2 = 1;
                    break;
                case "c":
                    position2 = 2;
                    break;
                case "d":
                    position2 = 3;
                    break;
            }

            LinearLayout votingContainer = findViewById(R.id.voting_container);
            for (int a = 0; a < votingContainer.getChildCount(); a++) {
                LinearLayout barContainer = (LinearLayout) votingContainer.getChildAt(a);
                if (option1 != null && option2 != null) {
                    if (position1 == a || position2 == a) {
                        barContainer.setVisibility(View.VISIBLE);
                    } else {
                        barContainer.setVisibility(View.INVISIBLE);
                    }
                }
            }

        }

    }


    private void refreshQuestion() {

        Log.i("obidati", String.valueOf(noOfPagesPassed));
        resetQuestion = true;
        final LinearLayout q1 = current.findViewById(R.id.qd);
        ///hideOptions(q1);
        showOptions2(q1);

        Cursor res = dbHelper.getQuestionByLevel2(String.valueOf(p1));
        res.moveToNext();
        @SuppressLint("Range") String id = res.getString(res.getColumnIndex("ID"));
        @SuppressLint("Range") String language = res.getString(res.getColumnIndex("LANGUAGE"));
        @SuppressLint("Range") String question = res.getString(res.getColumnIndex("QUESTION"));
        @SuppressLint("Range") String answer = res.getString(res.getColumnIndex("ANSWER"));
        @SuppressLint("Range") String type = res.getString(res.getColumnIndex("TYPE"));
        @SuppressLint("Range") String correct = res.getString(res.getColumnIndex("CORRECT"));
        @SuppressLint("Range") String reason = res.getString(res.getColumnIndex("REASON"));


        try {
            JSONObject contentObj = new JSONObject();
            contentObj.put("id", id);
            contentObj.put("parent", "0");
            contentObj.put("content", question);
            contentObj.put("title", "");
            contentObj.put("type", type);
            contentObj.put("answer", answer);
            contentObj.put("correct", correct);
            contentObj.put("question_image", "");
            contentObj.put("reason", reason);
            allQuestion.put(noOfPagesPassed, contentObj);
            Log.i("render2", "replace");
            Log.i("render2", String.valueOf(noOfPagesPassed));
            Log.i("render2", String.valueOf(contentObj));
            Log.i("render2", String.valueOf(allQuestion));

            //setQuestion(noOfPagesPassed);
            resetQuestion(current, current);
        } catch (Exception e) {
            e.printStackTrace();
        }

//        TextView questionTxt=current.findViewById(R.id.qo_text);
//        try {
//            question = question.substring(0,1).toUpperCase()+""+question.substring(1);
//        }catch (StringIndexOutOfBoundsException e){
//            e.printStackTrace();
//        }
//        questionTxt.setText(question);
//        TextView correctAnsText=current.findViewById(R.id.correct_ans);
//
//
//        final LinearLayout q = current.findViewById(R.id.qd);
//        try {
//            JSONArray answerArr = new JSONArray(answer);
//            for (int c = 0; c < q.getChildCount(); c++) {
//                JSONObject optObj = answerArr.getJSONObject(c);
//
//
//
//                String opt = optObj.getString("text");
//                opt = opt.substring(0,1).toUpperCase()+""+opt.substring(1)+" ";
//                TextView k = q.getChildAt(c).findViewById(R.id.opt);
//                k.setText(opt);
//
//                if(opt.equalsIgnoreCase(correct)){
//                    correctAnsText.setText(opt);
//                }
//            }
//        }catch (JSONException e){
//            e.printStackTrace();
//        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE) {
            if (resultCode == RESULT_OK) {
                Bitmap bitmap = takeScreenshot();
                saveBitmap(bitmap);
                shareIt();
            } else {
                Toast.makeText(GameActivity2.this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE) {

            if (grantResults.length >= 1) {

                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Bitmap bitmap = takeScreenshot();
                    saveBitmap(bitmap);
                    shareIt();
                } else {
                    ///
                }
            }
        }
    }

    public Bitmap takeScreenshot() {
        View rootView = findViewById(android.R.id.content).getRootView();
        rootView.setDrawingCacheEnabled(true);
        return rootView.getDrawingCache();
    }

    public void saveBitmap(Bitmap bitmap) {
        imagePath = new File(Environment.getExternalStorageDirectory() + "/screenshot.png");
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(imagePath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void shareIt() {
        //Uri uri = Uri.fromFile(imagePath);
        Uri uri = FileProvider.getUriForFile(this, getPackageName() + ".provider", imagePath);
        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
        String url = "https://play.google.com/store/apps/details?id=" + appPackageName;
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("image/*");
        String shareText = getResources().getString(R.string.ad_copy);
        shareText = shareText.replace("000", GameActivity2.amountWon);
        shareText = shareText.replace("111", url);
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareText);
        sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);

        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    private void setAmountWon() {
        String amountWon_ = amountWon.replace("$", "");
        saveNoOfCorrectAnswer();

        try {
            if (amountWon_.isEmpty()) {
                amountWonTxt.setText("$0");
            } else {
                switch (Integer.parseInt(amountWon_)) {
                    case 0:
                        amountWonTxt.setText("$0");
                        break;
                    case 500:
                        amountWonTxt.setText("$500");
                        break;

                    default:
                        amountWonTxt.setText("$" + prettyCount(Integer.parseInt(amountWon)).replace(".0", ""));
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void loadVideoAd() {
        // Load a reward based video ad
//        if(!mRewardedVideoAd.isLoaded() && mRewardedVideoAd !=null){
//            Log.i("mRewardedVideoAd","Not LOaded");
//            mRewardedVideoAd.loadAd("ca-app-pub-4696224049420135/7768937909", new AdRequest.Builder()
//                    .addTestDevice("9D16E23BB90EF4BFA204300CCDCCF264").build());
//
//
//        }else{
//            Log.i("mRewardedVideoAd","LOaded");
//
//        }
        AdManager.initRewardedVideo(GameActivity2.this);


    }

    private void loadSongs() {
        try {
            new Handler().post(() -> mWinning_sound = MediaPlayer.create(GameActivity2.this, R.raw.winning_sound));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void loadInterstialAd() {

//        interstitialAd = new InterstitialAd(this) ;
//        interstitialAd.setAdUnitId (getResources().getString(R.string.interstitial_adunit) ) ;
//        //String deviceId = md5(android_id).toUpperCase();
//        interstitialAd.loadAd(new AdRequest.Builder().addTestDevice("9D16E23BB90EF4BFA204300CCDCCF264").build());
//        //mInterstitialAd.loadAd(adRequest);


        AdManager.initInterstitialAd(GameActivity2.this);


    }


    private void showInterstitial() {
//        Log.i("checkInt",String.valueOf(interstitialAd.isLoaded()));
//        if (interstitialAd.isLoaded()) {
//
//            interstitialAd.show();
//        }else{
//            interstitialAd.setAdListener(new AdListener() {
//                public void onAdLoaded() {
//                    loadInterstialAd();
//                }
//            });
//        }


        AdManager.initInterstitialAd(GameActivity2.this);
    }


    private void SlideInLeft(View view) {
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


    }


    public void rotateView(ImageView refreshIcon, View videoIcon, View refreshBTN) {
        boolean isShowing = false;
        RotateAnimation rotate = new RotateAnimation(0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(1500);
        rotate.setInterpolator(new LinearInterpolator());
        refreshBTN.setClickable(false);
        rotate.setRepeatCount(Animation.INFINITE);
        refreshIcon.startAnimation(rotate);
        videoIcon.setVisibility(View.GONE);

//    if(videoIcon.isShown()){
//        isShowing = true;
//
//
//    }
        //refreshBTN.getBackground().setAlpha(10);
        refreshBTN.setAlpha(.4f);


        boolean finalIsShowing = isShowing;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshIcon.clearAnimation();
                // rotate.cancel();
                refreshBTN.setClickable(true);
                refreshBTN.setAlpha(1f);
                videoIcon.setVisibility(View.VISIBLE);

                //
//            if(finalIsShowing){
//                videoIcon.setVisibility(View.VISIBLE);
//                refreshIcon.setColorFilter(getResources().getColor(R.color.dark_grey1));
//
//            }


            }
        }, 60000);

    }

    @Override
    protected void onStart() {
        active = true;
        loadVideoAd();
        loadInterstialAd();
        super.onStart();
    }

    public void fedInAnimation(View view, long delay) {
        Animation fadeIn = new AlphaAnimation(0, 1);
        //  fadeIn.setInterpolator(new D()); //add this
        fadeIn.setDuration(800);
        fadeIn.setStartOffset(delay);
//
//        Animation fadeOut = new AlphaAnimation(1, 0);
//        fadeOut.setInterpolator(new AccelerateInterpolator()); //and this
//        fadeOut.setStartOffset(1000);
//        fadeOut.setDuration(2000);

        AnimationSet animation = new AnimationSet(false); //change to false
        animation.addAnimation(fadeIn);
        //animation.addAnimation(fadeOut);
        view.setAnimation(animation);
    }

    public String prettyCount(Integer number) {
        char[] suffix = {' ', 'k', 'M', 'B', 'T', 'P', 'E'};
        long numValue = number.longValue();
        int value = (int) Math.floor(Math.log10(numValue));
        int base = value / 3;
        if (value >= 3 && base < suffix.length) {
            return new DecimalFormat("#0.0").format(numValue / Math.pow(10, base * 3)) + suffix[base];
        } else {
            return new DecimalFormat("#,##0").format(numValue);
        }
    }

    void hideLifeGuard() {
        // if(lifeGuardContainers != null){

        RelativeLayout answerContainer = current.findViewById(R.id.ask_answer_container);
        LinearLayout votingContainer = current.findViewById(R.id.voting_container);
        answerContainer.setVisibility(View.GONE);
        votingContainer.setVisibility(View.GONE);

        //  }
    }

    void saveNoOfCorrectAnswer() {
        SharedPreferences sharedPref = getSharedPreferences("application_data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("noOfCorrectAnswer", noOfCorrectAnswer);
        editor.putBoolean("_2question", _2question);
        editor.putBoolean("askFriend", askFriend);
        editor.putBoolean("vote", vote);
        editor.putString("amountWon", amountWon);
        editor.apply();
        //Log.i("instantstate", String.valueOf(readNoOfCorrectAnswer()));
    }

    int readNoOfCorrectAnswer() {
        SharedPreferences sharedPref = getSharedPreferences("application_data", Context.MODE_PRIVATE);

        return sharedPref.getInt("noOfCorrectAnswer", 0);

    }

    public void readSavedData() {
        SharedPreferences sharedPref = getSharedPreferences("application_data", Context.MODE_PRIVATE);

        _2question = sharedPref.getBoolean("_2question", false);
        askFriend = sharedPref.getBoolean("askFriend", false);
        vote = sharedPref.getBoolean("vote", false);
        amountWon = sharedPref.getString("amountWon", "0");


    }

    public void saveHistory(int questionId, String answer, String correctAnswer, String high_score, boolean is_correct) {
        Log.i("checking", String.valueOf(questionId));

        dbHelper.saveHistory(String.valueOf(questionId), answer, correctAnswer, time_1, time_1, high_score, is_correct);

    }

    public int getOptionIndex(JSONArray jsonArray, String correctAswer) {
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                // text.getString("text").trim().equals(correctAnswer.trim())
                if (object.getString("text").trim().equals(correctAswer.trim())) {
                    return i;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return 3;
    }

    @Override
    public void onStop() {
        super.onStop();
        active = false;
    }

    public void playStong() {
        try {

            CountDownActivity.mMediaPlayer = MediaPlayer.create(GameActivity2.this, R.raw.background_sound);
            CountDownActivity.mFailurePlayer = MediaPlayer.create(GameActivity2.this, R.raw.failure_sound2);
            CountDownActivity.mSuccessPlayer = MediaPlayer.create(GameActivity2.this, R.raw.success_sound);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}