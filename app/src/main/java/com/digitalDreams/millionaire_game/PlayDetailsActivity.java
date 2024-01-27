package com.digitalDreams.millionaire_game;

import static com.digitalDreams.millionaire_game.GameActivity2.hasOldWinningAmount;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Base64OutputStream;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.digitalDreams.millionaire_game.alpha.activity.GameActivity3;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class PlayDetailsActivity extends AppCompatActivity {
    DBHelper dbHelper;
    File imagePath;
    RelativeLayout bg, newGameBtn;
    public static int MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE = 100;
    private String deviceId = "";
    private String modeValue = "";
    boolean isWon = false;
    boolean AdsFromFailureActivity = false;
    boolean AdsFromExitGameDialog = false;
    TextView wonTxt, usernameField, new_game_text;
    public static Activity playerDetailsActivity;

    private static final int REQUEST_CODE_SCREEN_CAPTURE = 1;
    // AdManager  adManager;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_details);


        AdManager.initInterstitialAd(this);
        AdManager.initRewardedVideo(this);

        playerDetailsActivity = this;

        //adManager =  new AdManager(this);
        RelativeLayout bg = findViewById(R.id.rootview);
//        new_game_text = findViewById(R.id.new_game_text);
//        new_game_text.setText("New Game");

        AdView mAdView;
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        boolean fromNoThanks = getIntent().getBooleanExtra("noThanks", false);
        if (fromNoThanks) {
            try {
                AdManager.showInterstitial(PlayDetailsActivity.this);
            } catch (Exception e) {
                e.printStackTrace();

            }
        }


        newGameBtn = findViewById(R.id.new_game1);
        RelativeLayout btnAnim = findViewById(R.id.btn_forAnim);
        new MyAnimation(btnAnim);

        newGameBtn.setOnClickListener(view -> {
            Utils.greenBlink(newGameBtn, getApplicationContext());
            Intent intent = new Intent(PlayDetailsActivity.this, GameActivity3.class);
            startActivity(intent);
            finish();

            // Utils.continueGame(PlayDetailsActivity.this);
        });


        SharedPreferences sharedPreferences1 = getSharedPreferences("settings", MODE_PRIVATE);
        String username = sharedPreferences1.getString("username", "");
        String oldAmountWon = sharedPreferences1.getString("amountWon", "");
        hasOldWinningAmount = sharedPreferences1.getBoolean("hasOldWinningAmount", false);
        int noOfAnsweredQuestion = sharedPreferences1.getInt("noOfAnsweredQuestion", 0);
        int nofCorrectQuestions = sharedPreferences1.getInt("noOfCorrect", 0);


        dbHelper = new DBHelper(this);

        isWon = getIntent().getBooleanExtra("isWon", false);
        AdsFromFailureActivity = getIntent().getBooleanExtra("AdsFromFailureActivity", false);
        AdsFromExitGameDialog = getIntent().getBooleanExtra("AdsFromExitGameDialog", false);
        wonTxt = findViewById(R.id.wontxt);
        usernameField = findViewById(R.id.username);

        if (username.length() > 1) {
            usernameField.setText(username.substring(0, 1).toUpperCase() + username.substring(1, username.length()));
        } else {
            usernameField.setText(getResources().getText(R.string.anonymous_user));

        }
        if (isWon) {
            wonTxt.setText(getResources().getString(R.string.won));
        }


        TextView amountWonTxt = findViewById(R.id.amount_won);
        try {
            //  boolean hasOldWinningAmount = getIntent().getBooleanExtra("hasOldWinningAmount",false);
            if (hasOldWinningAmount) {
                String formattedAmount = String.format(Locale.getDefault(), "$%s", currencyFormat(oldAmountWon));
                amountWonTxt.setText(formattedAmount);
            } else {
                amountWonTxt.setText(Utils.addCommaAndDollarSign(Integer.parseInt(GameActivity2.amountWon)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        TextView noOfQuestionsAnsweredText = findViewById(R.id.no_question_answered);
        String questionsAnsweredText;
        if (noOfAnsweredQuestion != 0) {
            questionsAnsweredText = noOfAnsweredQuestion + " " + getResources().getString(R.string.questions_answered);
            noOfQuestionsAnsweredText.setText(questionsAnsweredText);
        } else {
            questionsAnsweredText = noOfAnsweredQuestion + " " + getResources().getString(R.string.question_answered);
            noOfQuestionsAnsweredText.setText(questionsAnsweredText);
        }
        TextView accuracyTextView = findViewById(R.id.accuracy);
        float accuracy = (float) nofCorrectQuestions / (float) noOfAnsweredQuestion;

        accuracy = accuracy * 100;

        String accuracyText = Math.round(accuracy) + " " + getResources().getString(R.string.accuracy);

        accuracyTextView.setText(accuracyText);

        TextView timeTakenText = findViewById(R.id.time_taken);
        int seconds = (int) (GameActivity2.timing / 1000);
        int minutes = seconds / 60;
        if (seconds < 3600) {
            timeTakenText.setText(seconds + " sec");
        } else {
            timeTakenText.setText(minutes + " min");
        }
        if (GameActivity2.h2 != null) {
            GameActivity2.h2.removeCallbacks(GameActivity2.run);
        }
        SharedPreferences sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE);
        String languageCode = sharedPreferences.getString("language", "en");
        int endcolor = sharedPreferences.getInt("end_color", getResources().getColor(R.color.purple_dark));
        int startColor = sharedPreferences.getInt("start_color", getResources().getColor(R.color.purple_500));
        int cardBackground = sharedPreferences.getInt("card_background", 0x219ebc);
        String mode = sharedPreferences.getString("game_mode", "0");
        TextView modeTxt = findViewById(R.id.mode);
        if (mode.equals("0")) {
            modeTxt.setText("Mode: Normal");
            modeValue = "normal";
        } else {
            modeTxt.setText("Mode: Hard");
            modeValue = "hard";
        }
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE);

        GradientDrawable gd = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{startColor, endcolor});

        bg.setBackgroundDrawable(gd);
        new Particles(this, bg, R.layout.image_xml, 20);

        RelativeLayout outLine = findViewById(R.id.view_forAnim);
        new MyAnimation(outLine);

        RelativeLayout homeBtn = findViewById(R.id.home);
        LinearLayout shareBtn = findViewById(R.id.share);
        RelativeLayout newGame = findViewById(R.id.new_game);

        homeBtn.setOnClickListener(view -> {
            Utils.darkBlueBlink(homeBtn, getApplicationContext());
            Intent intent = new Intent(PlayDetailsActivity.this, Dashboard.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });

        shareBtn.setOnClickListener(view -> checkPermission());

        newGame.setOnClickListener(view -> {
            Utils.destination_activity = LeaderBoard.class;
            Utils.darkBlueBlink(newGame, getApplicationContext());
            //String json = dbHelper.buildJson();
            Intent intent = new Intent(PlayDetailsActivity.this, LeaderBoard.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });

        if (AdsFromFailureActivity) {
//            if (FailureActivity.interstitialAd.isLoaded()) {
//            FailureActivity.interstitialAd.show();
//        }
            AdManager.showInterstitial(PlayDetailsActivity.this);
        }

        if (AdsFromExitGameDialog) {
//            if (ExitGameDialog.interstitialAd.isLoaded()) {
//                ExitGameDialog.interstitialAd.show();
//            }
            AdManager.showInterstitial(PlayDetailsActivity.this);
        }


        checkScore();

        //sendScoreToSever("2000", "dking");

        deviceId = getDeviceId(this);
        Log.i("response", "device_id " + getDeviceId(this));

    }

//    private void loadInterstialAd(){
//        interstitialAd = new InterstitialAd(this) ;
//        interstitialAd.setAdUnitId (getResources().getString(R.string.interstitial_adunit) ) ;
//        interstitialAd.loadAd(new AdRequest.Builder().build());
//    }

    public static String getDeviceId(Context context) {

        String id = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return id;
    }

    public void takeScreenshot() {
        View rootView = findViewById(android.R.id.content).getRootView();

        Bitmap bitmap = Bitmap.createBitmap(
                rootView.getWidth(),
                rootView.getHeight(),
                Bitmap.Config.ARGB_8888
        );

        Canvas canvas = new Canvas(bitmap);
        rootView.draw(canvas);

        saveScreenshot(bitmap);
    }

    private void saveScreenshot(Bitmap bitmap) {
        // Use MediaStore to insert the screenshot into the MediaStore.Images.Media
        ContentResolver resolver = getContentResolver();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, "screenshot");
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
        contentValues.put(MediaStore.Images.Media.WIDTH, bitmap.getWidth());
        contentValues.put(MediaStore.Images.Media.HEIGHT, bitmap.getHeight());

        // Insert the screenshot into the MediaStore
        final Uri contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        final Uri uri = resolver.insert(contentUri, contentValues);

        try {
            if (uri != null) {
                // Open an OutputStream to the content URI
                OutputStream outputStream = resolver.openOutputStream(uri);

                if (outputStream != null) {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

                    // Close the OutputStream
                    outputStream.close();

                    // Share the screenshot
                    shareScreenshot(uri);
                }

                // Compress the Bitmap to PNG format and write it to the OutputStream
            } else {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.file_does_not_exist), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            // Handle any exceptions that might occur during the process
            e.printStackTrace();
        }
    }


    public void saveBitmap(Bitmap bitmap) {
        imagePath = new File(Environment.getExternalStorageDirectory() + "/screenshotxyz.jpg");
        FileOutputStream fos;
        try {
            if (imagePath.exists()) {
                fos = new FileOutputStream(imagePath);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos);
                fos.flush();
                fos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void shareScreenshot(Uri screenshotUri) {
        // Uri uri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", imagePath);
        // final String appPackageName = ; // getPackageName() from Context or Activity object
        String url = "https://play.google.com/store/apps/details?id=" + getPackageName();
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        sharingIntent.setType("text/plain");
        String shareText = getResources().getString(R.string.ad_copy);
        shareText = shareText.replace("000", GameActivity2.amountWon);
        shareText = shareText.replace("111", url);
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareText);
        sharingIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);

        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }


    @Override
    public void onBackPressed() {
        // initializeNotification();
//        ExitDialog dialog = new ExitDialog(this);
//        dialog.show();
//        Window window = dialog.getWindow();
//        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public void checkPermission() {
        if (ContextCompat.checkSelfPermission(PlayDetailsActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(PlayDetailsActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                ActivityCompat.requestPermissions(PlayDetailsActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE);
            } else {
                ActivityCompat.requestPermissions(PlayDetailsActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE);
            }
        } else {
            // Permission has already been granted
            /*Bitmap bitmap = takeScreenshot();
            saveBitmap(bitmap);
            shareScreenshot();*/

            takeScreenshot();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE && requestCode == RESULT_OK) {


        } else {
            Toast.makeText(PlayDetailsActivity.this, getResources().getString(R.string.permission_denied), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
               /* Bitmap bitmap = takeScreenshot();
                saveBitmap(bitmap);
                shareScreenshot();*/

                takeScreenshot();
            }
        }
    }

    public static Bitmap getScreenShot(View view) {
        View screenView = view.getRootView();
        screenView.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(screenView.getDrawingCache());
        screenView.setDrawingCacheEnabled(false);
        return bitmap;
    }

    public static void store(Bitmap bm, String fileName) {
        final String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Screenshots";
        File dir = new File(dirPath);
        if (!dir.exists())
            dir.mkdirs();
        File file = new File(dirPath, fileName);
        try {
            FileOutputStream fOut = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 85, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkScore() {
        try {
            SharedPreferences sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE);
            String highscore = sharedPreferences.getString("high_score", "0");
            String username = sharedPreferences.getString("username", "");
            String oldAmountWon = sharedPreferences.getString("amountWon", "");

            String country = sharedPreferences.getString("country", "");
            String country_flag = sharedPreferences.getString("country_flag", "");
            //Log.i("flag",country_flag);

            int h;

            try {
                h = Integer.parseInt(Utils.removeExtra(highscore));
            } catch (Exception e) {
                h = Utils.highScore;

            }

            String score = GameActivity2.amountWon;
            //score = score.substring(1);
            score = score.replace(",", "").replace("$", "");
            int s = Integer.parseInt(score);


            if (hasOldWinningAmount) {

                String amountWon = GameActivity2.amountWon.replace("$", "").replace(",", "");
                oldAmountWon = oldAmountWon.replace("$", "").replace(",", "");
                s = Integer.parseInt(amountWon) + Integer.parseInt(oldAmountWon);

            }


            if (s > h) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("high_score", String.valueOf(s));
                editor.apply();


            }
            Map<String, String> userDetails = new HashMap<>();
            userDetails.put("username", username);
            userDetails.put("country", country);
            userDetails.put("country_flag", country_flag);


            sendScoreToSever(String.valueOf(s), userDetails);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //@RequiresApi(api = Build.VERSION_CODES.M)
    private void sendScoreToSever(String score, Map<String, String> userDetails) {
        Log.i("ogabet3", String.valueOf(userDetails));

        String url = getResources().getString(R.string.base_url) + "/post_score.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
            try {
                Log.i("response111", "response== " + response);

                JSONObject obj = new JSONObject(response);
                JSONObject dailyObj = obj.getJSONObject("daily");
                String daily = dailyObj.getString("number");
                JSONObject weeklyObj = obj.getJSONObject("weekly");
                String weekly = weeklyObj.getString("number");
                String txt = "";
                String dailyMax = dailyObj.getString("max");
                String weeklyMax = weeklyObj.getString("max");

                if (daily.equals("1") && weekly.equals("1")) {

                    txt = getResources().getString(R.string.congratulations_improve_your_score_to_remain_at_the_top) + currencyFormat(dailyMax);


                } else if (daily.equals("1")) {
                    txt = getResources().getString(R.string.beat_this_weeks_highest_score) + currencyFormat(weeklyMax);

                } else if (!dailyMax.equals("null")) {
                    txt = getResources().getString(R.string.beat_today_s_highest_score_of) + currencyFormat(dailyMax);

                } else {
                    txt = getResources().getString(R.string.beat_today_s_highest_score) + currencyFormat(dailyMax);

                }


                ResultDialog r = new ResultDialog(PlayDetailsActivity.this, daily, weekly, txt);
                // r.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.blue(444)));

                // r.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.));

                if (!isFinishing()) {

                    r.show();

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }, Throwable::printStackTrace) {
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
                param.put("device_id", getDeviceId(PlayDetailsActivity.this));
                param.put("game_type", "millionaire");
                param.put("mode", modeValue);
                Log.i("praram", String.valueOf(param));
                return param;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    public String getStringFile(File f) {
        InputStream inputStream = null;
        String encodedFile = "", lastVal;
        try {
            inputStream = new FileInputStream(f.getAbsolutePath());

            byte[] buffer = new byte[10240];//specify the size to allow
            int bytesRead;
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            Base64OutputStream output64 = new Base64OutputStream(output, Base64.DEFAULT);

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                output64.write(buffer, 0, bytesRead);
            }
            output64.close();
            encodedFile = output.toString();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        lastVal = encodedFile;
        return lastVal;
    }

    private String base64String(int id) {

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), id);
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteStream);
        byte[] byteArray = byteStream.toByteArray();
        String baseString = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return baseString;

    }

    private String getAvatar() {
        SharedPreferences sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE);
        String avatar = sharedPreferences.getString("avatar", "");
        return avatar;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initializeNotification() {


//        Calendar calendar = Calendar.getInstance();
//        Date date = new Date();
//        calendar.setTime(date);
//        calendar.set(Calendar.HOUR_OF_DAY);
//
//        calendar.set(Calendar.MINUTE);
//        calendar.set(Calendar.SECOND);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);


        //makeNotificationChannel("4","noti",1);
        final int flag = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE : PendingIntent.FLAG_UPDATE_CURRENT;

        PendingIntent servicePendingIntent =
                PendingIntent.getBroadcast(PlayDetailsActivity.this, 0,
                        new Intent(PlayDetailsActivity.this, NotificationService.class), flag);
        alarmManager.cancel(servicePendingIntent);
        int delay = (60 * 2) * 1000;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, (delay), AlarmManager.INTERVAL_DAY, servicePendingIntent);
        } else {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, (delay), AlarmManager.INTERVAL_DAY, servicePendingIntent);
        }


    }

    public static String currencyFormat(String amount) {
        if (amount == null || amount.equals("null")) {
            return "0";
        }
        DecimalFormat formatter = new DecimalFormat("###,###,##0.00");
        return formatter.format(Double.parseDouble(amount));
    }


}