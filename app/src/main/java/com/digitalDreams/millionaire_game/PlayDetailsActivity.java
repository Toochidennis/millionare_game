package com.digitalDreams.millionaire_game;

import static com.digitalDreams.millionaire_game.GameActivity2.hasOldWinningAmount;
import static com.digitalDreams.millionaire_game.alpha.Constants.formatCurrency;

import android.Manifest;
import android.annotation.SuppressLint;
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
    TextView wonTxt, usernameField;
    public static Activity playerDetailsActivity;



    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_details);


        AdManager.initInterstitialAd(this);
        AdManager.initRewardedVideo(this);

        playerDetailsActivity = this;

        RelativeLayout bg = findViewById(R.id.rootview);


        AdView mAdView;
        mAdView = findViewById(R.id.adView);
        @SuppressLint("VisibleForTests") AdRequest adRequest = new AdRequest.Builder().build();
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
        });


        SharedPreferences sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");
        String newAmountWon = sharedPreferences.getString("amountWon", "0");
        hasOldWinningAmount = sharedPreferences.getBoolean("hasOldWinningAmount", false);
        int noOfAnsweredQuestion = sharedPreferences.getInt("noOfAnsweredQuestion", 0);
        int nofCorrectQuestions = sharedPreferences.getInt("noOfCorrect", 0);
        int totalAmountWon = sharedPreferences.getInt("totalAmountWon", 0);
        int endColor = sharedPreferences.getInt("end_color", getResources().getColor(R.color.purple_dark));
        int startColor = sharedPreferences.getInt("start_color", getResources().getColor(R.color.purple_500));
        String mode = sharedPreferences.getString("game_mode", "0");
        String duration = sharedPreferences.getString("duration", "0");

        dbHelper = new DBHelper(this);

        isWon = getIntent().getBooleanExtra("isWon", false);
        AdsFromFailureActivity = getIntent().getBooleanExtra("AdsFromFailureActivity", false);
        AdsFromExitGameDialog = getIntent().getBooleanExtra("AdsFromExitGameDialog", false);
        wonTxt = findViewById(R.id.wontxt);
        usernameField = findViewById(R.id.username);

        if (username.length() > 1) {
            String user = Character.toUpperCase(username.charAt(0)) + username.substring(1).trim();
            usernameField.setText(user);
        } else {
            usernameField.setText(getResources().getText(R.string.anonymous_user));

        }

        if (isWon) {
            wonTxt.setText(getResources().getString(R.string.won));
        }

        TextView amountWonTxt = findViewById(R.id.amount_won);

        try {
            int parsedNewAmount = Integer.parseInt(newAmountWon);
            totalAmountWon += parsedNewAmount;
            String formattedAmount = String.format(Locale.getDefault(), "$%s", formatCurrency(totalAmountWon));
            amountWonTxt.setText(formattedAmount);
        } catch (Exception e) {
            e.printStackTrace();
        }

        TextView noOfQuestionsAnsweredText = findViewById(R.id.no_question_answered);
        TextView accuracyTextView = findViewById(R.id.accuracy);
        TextView timeTakenText = findViewById(R.id.time_taken);

        String questionsAnsweredText;
        if (noOfAnsweredQuestion != 0) {
            questionsAnsweredText = noOfAnsweredQuestion + " " + getResources().getString(R.string.questions_answered);
            noOfQuestionsAnsweredText.setText(questionsAnsweredText);
        } else {
            questionsAnsweredText = noOfAnsweredQuestion + " " + getResources().getString(R.string.question_answered);
            noOfQuestionsAnsweredText.setText(questionsAnsweredText);
        }

        float accuracy = (float) nofCorrectQuestions / (float) noOfAnsweredQuestion;
        accuracy = accuracy * 100;
        String accuracyText = Math.round(accuracy) + " " + getResources().getString(R.string.accuracy);
        accuracyTextView.setText(accuracyText);

        if (duration.equals("0")) {
            timeTakenText.setText(getResources().getString(R.string._0_second));
        } else {
            timeTakenText.setText(duration);
        }

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

        GradientDrawable gradientDrawable = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{startColor, endColor});

        bg.setBackground(gradientDrawable);
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

        shareBtn.setOnClickListener(view -> {
            checkPermission();
            shareBtn.setClickable(false);
        });

        newGame.setOnClickListener(view -> {
            Utils.destination_activity = LeaderBoard.class;
            Utils.darkBlueBlink(newGame, getApplicationContext());
            Intent intent = new Intent(PlayDetailsActivity.this, LeaderBoard.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });

        if (AdsFromFailureActivity) {
            AdManager.showInterstitial(PlayDetailsActivity.this);
        }

        if (AdsFromExitGameDialog) {

            AdManager.showInterstitial(PlayDetailsActivity.this);
        }


        checkScore();


        deviceId = getDeviceId(this);

    }


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


    private void shareScreenshot(Uri screenshotUri) {
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


            takeScreenshot();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE && requestCode == RESULT_OK) {
            takeScreenshot();
        } else {
            Toast.makeText(PlayDetailsActivity.this, getResources().getString(R.string.permission_denied), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takeScreenshot();
            }
        }
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


    private void checkScore() {
        try {
            SharedPreferences sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE);
            String highScore = sharedPreferences.getString("high_score", "0");
            String username = sharedPreferences.getString("username", "");
            String newAmountWon = sharedPreferences.getString("amountWon", "0");
            int totalAmountWon = sharedPreferences.getInt("totalAmountWon", 0);

            String country = sharedPreferences.getString("country", "");
            String country_flag = sharedPreferences.getString("country_flag", "");

            int parsedHighScore = Integer.parseInt(highScore);

            int parsedNewAmount = Integer.parseInt(newAmountWon);
            totalAmountWon += parsedNewAmount;

            if (totalAmountWon > parsedHighScore) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("high_score", String.valueOf(totalAmountWon));
                editor.apply();
            }

            Map<String, String> userDetails = new HashMap<>();
            userDetails.put("username", username);
            userDetails.put("country", country);
            userDetails.put("country_flag", country_flag);


            // sendScoreToSever(String.valueOf(s), userDetails);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //@RequiresApi(api = Build.VERSION_CODES.M)
    private void sendScoreToSever(String score, Map<String, String> userDetails) {
        String url = getResources().getString(R.string.base_url) + "/post_score.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
            try {
                Log.i("response111", "response== " + response);

                JSONObject obj = new JSONObject(response);
                JSONObject dailyObj = obj.getJSONObject("daily");
                String daily = dailyObj.getString("number");
                JSONObject weeklyObj = obj.getJSONObject("weekly");
                String weekly = weeklyObj.getString("number");
                String txt;
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


    private String getAvatar() {
        SharedPreferences sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE);
        String avatar = sharedPreferences.getString("avatar", "");
        return avatar;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initializeNotification() {
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