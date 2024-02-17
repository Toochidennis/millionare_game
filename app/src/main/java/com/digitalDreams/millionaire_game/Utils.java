package com.digitalDreams.millionaire_game;

import static com.digitalDreams.millionaire_game.FailureActivity.getDeviceId;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Utils {
    public static boolean IS_DONE_INSERTING = false;

    public static String ENGLISH_KEY = "en";
    public static String SPANISH_KEY = "es";
    public static String FRENCH_KEY = "fr";
    public static String ARABIC_KEY = "ar";
    public static String HINDI_KEY = "hi";
    public static String PORTUGUESE_KEY = "pt";
    public static String URDU_KEY = "ur";
    public static String JAPANESE_KEY = "ja";
    public static String GERMAN_KEY = "de";
    public static String INDONESIAN_KEY = "in";
    public static String TURKISH_KEY = "tr";

    public static int NUMBER_OF_INSERT = 0;
    public static Class destination_activity = Dashboard.class;
    public static int leaderboardClick = 0;
    public static int highScore = 500;
    public static String lastDatePlayed = "";
    public static String readMoreUrl = "https://weirdtrivia.com/post/";

    Utils() {

    }


    public static String addCommaToNumber(double number) {
        String pattern = "#,###,###.###";
        DecimalFormat decimalFormat = new DecimalFormat(pattern);
        return decimalFormat.format(number);

    }

    public static String addCommaToNumber(int number) {
        String pattern = "#,###,###.###";
        DecimalFormat decimalFormat = new DecimalFormat(pattern);
        return decimalFormat.format(number);

    }

    public static String addDollarSign(String number) {

        return "$" + number;
    }

    public static String addCommaAndDollarSign(double number) {

        return addDollarSign(addCommaToNumber(number));

    }


    public static boolean isOnline(Context context) {
        ConnectivityManager mgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = mgr.getActiveNetworkInfo();
        if (netInfo != null) {
            return netInfo.isConnected();
        } else {
            //No internet
            return false;
        }
    }


    public static void sendScoreToSever(Context context, String score, Map<String, String> userDetails, String modeValue) {
        String url = context.getResources().getString(R.string.post_url);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> Log.i("response", "response " + response),
                Throwable::printStackTrace) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                super.getParams();
                Map<String, String> param = new HashMap<>();
                JSONObject country_json = new JSONObject();
                try {
                    country_json.put("name", userDetails.get("country"));
                    country_json.put("url", userDetails.get("country_flag"));
                    country_json.put("id", userDetails.get("country_id"));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                param.put("score", score);
                param.put("username", userDetails.get("username"));
                param.put("country", userDetails.get("country_id"));
                param.put("country_json", country_json.toString());
                param.put("country_flag", userDetails.get("country_flag"));
                param.put("avatar", getAvatar(context));
                param.put("device_id", getDeviceId(context));
                param.put("game_type", "millionaire");
                param.put("mode", modeValue);
                Log.i("praram", String.valueOf(param));
                return param;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    public static String getAvatar(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        String avatar = sharedPreferences.getString("avatar", "");
        return avatar;
    }

    public static void saveAnonymouseUser(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", context.getResources().getString(R.string.anonymous_user));
        editor.putString("avatar", "1");
        editor.putString("country", "default");
        editor.putString("country_flag", "https://cdn.jsdelivr.net/npm/country-flag-emoji-json@2.0.0/dist/images/AX.svg");
        editor.putString("country_id", "0");
        editor.putString("game_level", "1");
        editor.putString("current_play_level", "1");
        editor.putBoolean("isFirstTime", true);

        if (username.isEmpty()) {
            editor.apply();
        }
    }

    public static String getDate() {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        return df.format(c);
    }

    public static String getTime() {
        Date currentTime = Calendar.getInstance().getTime();
        return currentTime.toString();
    }

    public static String capitalizeFirstWord(String word) {
        if (word == null || word.isEmpty()) {
            return word;
        }
        return Character.toUpperCase(word.charAt(0)) + word.substring(1);

    }

    public static void navigateToWebview(String questionId, Context context) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra("questionId", questionId);
        context.startActivity(intent);

    }

}
