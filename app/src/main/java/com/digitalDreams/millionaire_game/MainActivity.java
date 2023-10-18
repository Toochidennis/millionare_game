package com.digitalDreams.millionaire_game;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BlendMode;
import android.graphics.Path;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public  final int SPLASH_SCREEN_DELAY=100;
    public  List<String> columnList = new ArrayList<>();
    DBHelper dbHelper;
    ImageView ddLogo;
    long logoStartTime=0;
    LinearLayout webDevContainer;
    LinearLayout mobileDevContainer;
    LinearLayout digitalMarketingContainer;
    LinearLayout dataScienceContainer;
    LinearLayout container;
    private static final long COUNTER_TIME = 5;
    public  static boolean ACTIVITY_PASSED = false;


    private long secondsRemaining;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Window window = getWindow();
        AppOpenManager appOpenAdManager;



        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.white));

        dbHelper = new DBHelper(this);

        ddLogo = findViewById(R.id.dd_logo);

//        MobileAds.initialize(this, new OnInitializationCompleteListener() {
//            @Override
//            public void onInitializationComplete(InitializationStatus initializationStatus) {
//
//            }
//
//        });

        SharedPreferences sharedPreferences = getSharedPreferences("settings",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String username = sharedPreferences.getString("username","");
        Utils.IS_DONE_INSERTING = sharedPreferences.getBoolean("IS_DONE_INSERTING",false);



        createTimer(COUNTER_TIME);
        new RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList("C5C6588E00A996967AA2085A167B0F4E","9D16E23BB90EF4BFA204300CCDCCF264"));
//        appOpenAdManager = new AppOpenManager(this);
//        appOpenAdManager.fetchAd();

        //Log.i("response","t "+text);
        new Thread(new Runnable(){
            @Override
            public void run(){
                String text = null;
                try {
                    if(dbHelper.getQuestionSize()==0) {
                        Utils.IS_DONE_INSERTING = false;
                        //  saveAnonymouseUser();
                        text = readRawTextFile(R.raw.millionaire);
                        parseJSON(text);
                    }else{
                        Utils.IS_DONE_INSERTING = true;
                        editor.putBoolean("IS_DONE_INSERTING",true);
                        editor.commit();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();


        webDevContainer = findViewById(R.id.web_development);
        mobileDevContainer = findViewById(R.id.mobile_development);
        digitalMarketingContainer = findViewById(R.id.digital_marketing);
        dataScienceContainer = findViewById(R.id.data_science);
        container = findViewById(R.id.container);
        TextView trainTxt = findViewById(R.id.train_text);


        //Animation infromBottom = inFromBottomAnimation(1500,logoStartTime);
        //ddLogo.startAnimation(infromBottom);

        AlphaAnimation fadeIn=new AlphaAnimation(0,1);
        final AnimationSet set = new AnimationSet(false);

        set.addAnimation(fadeIn);
        set.setDuration(1500);
        set.setStartOffset(100);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        Log.i("response","heigth "+height);

        int yValue = height - 500;
        Path path = new Path();
        path.moveTo(0,height);
        ObjectAnimator moveX = ObjectAnimator.ofFloat(ddLogo, "x", "y",path );
        moveX.setDuration(0);
        moveX.start();

        ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(ddLogo,"alpha", 1);
        ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(ddLogo, "translationY", 0);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(objectAnimator1, objectAnimator2);
        animatorSet.setDuration(2000);
        animatorSet.setInterpolator(new DecelerateInterpolator());
        animatorSet.start();
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        int yValue1 = height - 400;
        /*ObjectAnimator objectAnimator4 = ObjectAnimator.ofFloat(container,"alpha", 1);
        ObjectAnimator objectAnimator3 = ObjectAnimator.ofFloat(container, "translationY", 1000);
        AnimatorSet animatorSet1 = new AnimatorSet();
        animatorSet1.playTogether(objectAnimator3, objectAnimator4);
        animatorSet1.setDuration(2000);
        animatorSet1.setInterpolator(new DecelerateInterpolator());
        animatorSet1.start();

        animatorSet1.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                container.setVisibility(View.VISIBLE);
                trainTxt.setVisibility(View.VISIBLE);
                webDevContainer.setVisibility(View.VISIBLE);
                mobileDevContainer.setVisibility(View.VISIBLE);
                dataScienceContainer.setVisibility(View.VISIBLE);
                digitalMarketingContainer.setVisibility(View.VISIBLE);
                animateWebContainer();
                animateMobileContainer();
                animateDigtalContainer();
                animateDataScienceContainer();
                trainTxt.startAnimation(set);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });*/

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                container.setVisibility(View.VISIBLE);
                trainTxt.setVisibility(View.VISIBLE);
                webDevContainer.setVisibility(View.VISIBLE);
                mobileDevContainer.setVisibility(View.VISIBLE);
                dataScienceContainer.setVisibility(View.VISIBLE);
                digitalMarketingContainer.setVisibility(View.VISIBLE);
                animateWebContainer();
                animateMobileContainer();
                animateDigtalContainer();
                animateDataScienceContainer();
                trainTxt.startAnimation(set);
            }
        },2000);

    }

    private String readRawTextFile( int resId) throws IOException {
        InputStream is = getResources().openRawResource(resId);
        Writer writer = new StringWriter();
        char[] buffer = new char[10024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            is.close();
        }

        String jsonString = writer.toString();
        return jsonString;
    }

    private void createTable(String json){
        try {
            JSONObject jsonObject = new JSONObject(json);
            Iterator<String> iterator = jsonObject.keys();
            while (iterator.hasNext()){
                String key = iterator.next();
                if(!columnList.contains(key)) {
                    columnList.add(key);
                }
                JSONObject obj1 = jsonObject.getJSONObject(key);
                Iterator<String> iterator1 = obj1.keys();
                while (iterator1.hasNext()){
                    String key1 = iterator1.next();
                    if(!columnList.contains(key1)) {
                        columnList.add(key1);
                    }
                    JSONArray jsonArray = obj1.getJSONArray(key1);
                    for(int a=0;a<jsonArray.length();a++){
                        JSONObject obj2 = jsonArray.getJSONObject(a);
                        Iterator<String> iterator2 = obj2.keys();
                        while (iterator2.hasNext()){
                            String key2 = iterator2.next();
                            if(!columnList.contains(key2)) {
                                columnList.add(key2);
                            }
                            //Log.i("response",""+key2+" "+obj2.getString(key2));
                            obj2.getString(key2);
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }finally {
            //dbHelper = new DBHelper(this)
            //insertDataToTable(json);

        }

    }

    private void parseJSON(String json){
        int lent =0;
        try {
            JSONArray jsonArray = new JSONArray(json);






//                            String key = iter4.next();
//                            Log.i("kkkkkkkkkkkkkkkkkkkkk",String.valueOf(key));
//                            JSONArray jsonArray = json_each_level.getJSONArray(key);
//
//                            Log.i("tables", String.valueOf(jsonArray));
//                            //   Log.i("tables", String.valueOf(jsonArray));
//                            Log.i("stage_name", String.valueOf(levels));
//                            Log.i("stage", String.valueOf(each_level_key));
            for (int a = 0; a < jsonArray.length(); a++) {
                Log.i("index", String.valueOf(a));
                lent++;

                Utils.NUMBER_OF_INSERT ++;


                if(lent == jsonArray.length()){
                    Utils.IS_DONE_INSERTING = true;
                }

//                                JSONObject object = jsonArray.getJSONObject(a);
//                                String id = object.getString("id");
//                                String content = object.getString("content");
//                                String type = object.getString("type");
//                                String answer = object.getString("answer");
//                                String correct = object.getString("correct");
//                                String title = object.getString("title");
//                                String questionImage = object.getString("question_image");

                JSONArray question = jsonArray.getJSONArray(a);
                String id = String.valueOf(question.getInt(0)); //object.getString("id");
                String content = question.getString(1); //object.getString("content");
                String type = "qo";//object.getString("type");
                String level = String.valueOf(question.getString(2));
                String language = "GENERAL";


                String stage_name = "GENERAL";
                String stage = "1";

                String correct = question.getString(3).trim();//object.getString("correct");
                String reason = question.getString(4).trim();
//                                String title = object.getString("title");
//                                String questionImage = object.getString("question_image");

                JSONArray answers = new JSONArray();

                for(int j = 5; j < question.length(); j++){
                    JSONObject obj = new JSONObject();
                    obj.put("text", question.getString(j));
                    answers.put(obj);

                }

                String answer = String.valueOf(answers);





                dbHelper.insertDetails(language,level,id,content,type,answer,correct,stage_name,stage, reason);
            }




//                Iterator<String> iterator = obj1.keys();
//                while (iterator.hasNext()) {
//                    String key = iterator.next();
//                    JSONArray jsonArray = obj1.getJSONArray(key);
//                    for (int a = 0; a < jsonArray.length(); a++) {
//                        JSONObject object = jsonArray.getJSONObject(a);
//                        String id = object.getString("id");
//                        String content = object.getString("content");
//                        String type = object.getString("type");
//                        String answer = object.getString("answer");
//                        String correct = object.getString("correct");
//                        String title = object.getString("title");
//                        String questionImage = object.getString("question_image");
//                       // dbHelper.insertDetails(k,key,id,content,type,answer,correct);
//                    }
//                }



        }catch (JSONException e){
            e.printStackTrace();
        }
        Log.i("JsonDetails", String.valueOf(lent));

//        if(!username.isEmpty()) {
//            Intent intent = new Intent(MainActivity.this, Dashboard.class);
//            startActivity(intent);
//            finish();
//        }else {
//            Intent intent = new Intent(MainActivity.this, UserDetails.class);
//            startActivity(intent);
//            finish();
//        }

    }

    private void insertDataToTable(String json){
        try {
            JSONObject jsonObject = new JSONObject(json);
            Iterator<String> iterator = jsonObject.keys();
            while (iterator.hasNext()){
                List<String> dataList = new ArrayList<>();
                String key = iterator.next();
                dataList.add(key);
                JSONObject obj1 = jsonObject.getJSONObject(key);
                Iterator<String> iterator1 = obj1.keys();
                while (iterator1.hasNext()){
                    String key1 = iterator1.next();
                    dataList.add(key1);
                    JSONArray jsonArray = obj1.getJSONArray(key1);
                    for(int a=0;a<jsonArray.length();a++){
                        JSONObject obj2 = jsonArray.getJSONObject(a);
                        Iterator<String> iterator2 = obj2.keys();
                        while (iterator2.hasNext()){
                            String key2 = iterator2.next();
                            Log.i("response",""+key2+" "+obj2.getString(key2));
                            dataList.add(obj2.getString(key2));

                            //String id = obj2.getString(columnList.get())
                            //dbHelper.insertDetails(columnList,dataList);
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
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

    private Animation inFromRightAnimation(int duration, Long startTime) {
        Animation inFromRigth = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        inFromRigth.setDuration(duration);
        inFromRigth.setStartOffset(startTime);
        inFromRigth.setInterpolator(new AccelerateInterpolator());
        return inFromRigth;
    }
    private Animation inFromBottomAnimation(int duration,Long startTime) {
        Animation inFromLeft = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, +1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        inFromLeft.setDuration(duration);
        inFromLeft.setStartOffset(startTime);
        inFromLeft.setInterpolator(new AccelerateInterpolator());
        return inFromLeft;
    }

    private void animateWebContainer(){
        AlphaAnimation fadeIn=new AlphaAnimation(0,1);
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

    private void animateMobileContainer(){
        AlphaAnimation fadeIn=new AlphaAnimation(0,1);
        final AnimationSet set = new AnimationSet(false);
        Animation inFromRigth = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);

        set.addAnimation(inFromRigth);
        set.addAnimation(fadeIn);
        set.setDuration(1500);
        set.setStartOffset(100);
        mobileDevContainer.startAnimation(set);
    }

    private void animateDigtalContainer(){
        AlphaAnimation fadeIn=new AlphaAnimation(0,1);
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

    private void animateDataScienceContainer(){
        AlphaAnimation fadeIn=new AlphaAnimation(0,1);
        final AnimationSet set = new AnimationSet(false);
        Animation inFromRigth = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);

        set.addAnimation(inFromRigth);
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

    /** Start the Dashboard. */
    public void startDashboardActivity() {
        if(dbHelper.getQuestionSize()>0){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    SharedPreferences sharedPreferences = getSharedPreferences("settings",MODE_PRIVATE);
                    String username = sharedPreferences.getString("username","");
                    if(username.isEmpty()) {
                        Intent intent = new Intent(MainActivity.this, UserDetails.class);
                        startActivity(intent);
                        finish();
                    }else {
                        SharedPreferences.Editor editor = sharedPreferences.edit();


                        Utils.IS_DONE_INSERTING = true;
                        editor.putBoolean("IS_DONE_INSERTING",true);
                        editor.commit();




                        Intent intent = new Intent(MainActivity.this, Dashboard.class);
                        startActivity(intent);
                        finish();
                    }
                }
            },SPLASH_SCREEN_DELAY);
        }
    }


}