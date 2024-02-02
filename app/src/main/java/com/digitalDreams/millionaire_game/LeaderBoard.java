package com.digitalDreams.millionaire_game;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LeaderBoard extends AppCompatActivity {
    public static int MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE = 100;
    RecyclerView recyclerView;
    List<PlayerObject> list;
    LinearLayout firstContainer, secondContainer, thirdContainer, fourthCountainer;
    TextView name1, name2, name3, amount1, amount2, amount3;
    TextView country_name1, country_name2, country_name3;
    ImageView img1, img2, img3;
    ImageView flag1, flag2, flag3;
    LeaderboardAdapter adapter;
    LinearLayout allBtn, weekBtn, dailyBtn, container, countryBtn;
    LayoutInflater inflater;
    View view, view2, view3, view4;
    String json1, json2, json3, json4;
    LinearLayout emptyLayout;
    TextView emptyText;
    ProgressBar progressBar;
    //public static InterstitialAd interstitialAd;
    RelativeLayout share_container;
    LinearLayout country_layout_container;

    private boolean mIsBackVisible = false;
    private View mCardBackLayout;
    File imagePath;
    String username;
    //ListView country_json;
    ArrayList<JSONObject> jsonObjects;
    CountryJsonAdapter countryJsonAdapter;
    RecyclerView countryListView;
    LayoutInflater layoutInflater;

    // AdManager adManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);
        // adManager = new AdManager(this);
        AdManager.loadRewardedAd(this);
        AdManager.loadInterstitialAd(this);


        /////////////
        jsonObjects = new ArrayList<>();
        layoutInflater = LayoutInflater.from(this);


        /////////////////AD////////


        AdView mAdView;
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        loadInterstialAd();


        share_container = findViewById(R.id.share_container);
        allBtn = findViewById(R.id.all);
        weekBtn = findViewById(R.id.week);
        dailyBtn = findViewById(R.id.daily);

        countryBtn = findViewById(R.id.country);

        container = findViewById(R.id.container);
        emptyLayout = findViewById(R.id.empty_state);
        emptyText = findViewById(R.id.empty_text);
        progressBar = findViewById(R.id.progress);
        //container.removeAllViews();
        //country_json = findViewById(R.id.country_json);
        /////////

        inflater = LayoutInflater.from(this);
        view = inflater.inflate(R.layout.all_time_layout, container, false);
        container.addView(view);

        list = new ArrayList<>();
        setView(view);
        allBtn.setOnClickListener(view -> {
            Utils.darkBlueBlink(allBtn, getApplicationContext());
            list.clear();
            emptyText.setVisibility(View.GONE);
            selector(0);
            container.removeAllViews();
            view = inflater.inflate(R.layout.all_time_layout, container, false);
            container.addView(view);
            setView(view);
            if (json1 != null) {
                try {
                    parseJSON(json1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        weekBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.darkBlueBlink(weekBtn, getApplicationContext());
                list.clear();
                emptyText.setVisibility(View.GONE);
                container.removeAllViews();
                selector(1);
                view2 = inflater.inflate(R.layout.week_layout, container, false);
                container.addView(view2);
                setView(view2);
                if (json2 != null) {
                    try {
                        parseJSON(json2);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        getWeeklyLeaderBoard();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        dailyBtn.setOnClickListener(view -> {
            Utils.darkBlueBlink(dailyBtn, getApplicationContext());
            list.clear();
            emptyText.setVisibility(View.GONE);
            selector(2);
            container.removeAllViews();
            view3 = inflater.inflate(R.layout.daily_layout, container, false);
            container.addView(view3);
            setView(view3);
            if (json3 != null) {
                try {
                    parseJSON(json3);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    getDialyLeaderBoard();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        countryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.darkBlueBlink(countryBtn, getApplicationContext());

                list.clear();
                jsonObjects.clear();
                emptyText.setVisibility(View.GONE);
                selector(3);
                container.removeAllViews();
                country_layout_container.removeAllViews();
                view4 = inflater.inflate(R.layout.country_leader_board, container, false);
                container.addView(view4);
                setView(view4);
                if (json4 != null) {
                    try {
                        country_layout_container.removeAllViews();
                        solveCountryJson(json4);
                        parseJSON(json4);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    country_layout_container.removeAllViews();
                    try {
                        getCountryLeaderBoard();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        share_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermission();
            }
        });


        SharedPreferences sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE);
        String languageCode = sharedPreferences.getString("language", "en");
        int endColor = sharedPreferences.getInt("end_color", getResources().getColor(R.color.purple_dark));
        int startColor = sharedPreferences.getInt("start_color", getResources().getColor(R.color.purple_500));
        int cardBackground = sharedPreferences.getInt("card_background", 0x219ebc);
        username = sharedPreferences.getString("username", "");
        moveToSignUp(username);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE);

        RelativeLayout bg = findViewById(R.id.rootview);
        new Particles(this, bg, R.layout.image_xml, 20);
        GradientDrawable gd = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{startColor, endColor});

        bg.setBackgroundDrawable(gd);

        mCardBackLayout = findViewById(R.id.avatar1);
        //loadAnimation(mCardBackLayout);
        loadAnimations(mCardBackLayout);
        RelativeLayout closeBtn = findViewById(R.id.close_container);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.darkBlueBlink(closeBtn, getApplicationContext());
                showInterstitial();


            }
        });


        String highscore = sharedPreferences.getString("high_score", "0");
        String username = sharedPreferences.getString("username", "");

        try {
            getAllLeaderBoard();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void loadAnimations(View view) {
        AnimatorSet flip;
        flip = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.out_animator);
        flip.setTarget(view);
        flip.setDuration(5000);
        flip.start();
        flip.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                loadAnimations(view);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }


    private void getAllLeaderBoard() {
        String url = getResources().getString(R.string.base_url) + "/get_leaderboard.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("response", "response " + response);
                progressBar.setVisibility(View.GONE);
                if (response != null) {
                    try {
                        adapter.notifyDataSetChanged();
                        list.clear();
                        json1 = response;
                        parseJSON(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                super.getParams();
                Map<String, String> param = new HashMap<>();
                param.put("game_type", "millionaire");
                param.put("period", "today");
                return param;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void getWeeklyLeaderBoard() {
        emptyLayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        String url = getResources().getString(R.string.base_url) + "/get_leaderboard.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("responseweek", "response " + response);
                progressBar.setVisibility(View.GONE);
                if (response != null) {
                    try {
                        list.clear();
                        adapter.notifyDataSetChanged();
                        json2 = response;
                        parseJSON(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                super.getParams();
                Map<String, String> param = new HashMap<>();
                param.put("game_type", "millionaire");
                param.put("period", "week");
                return param;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void getDialyLeaderBoard() {
        emptyLayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        String url = getResources().getString(R.string.base_url) + "/get_leaderboard.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("response099", "response " + response);
                progressBar.setVisibility(View.GONE);
                if (response != null) {
                    try {
                        list.clear();

                        json3 = response;
                        parseJSON(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                super.getParams();
                Map<String, String> param = new HashMap<>();
                param.put("game_type", "millionaire");
                param.put("period", "daily");
                return param;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    private void getCountryLeaderBoard() {
        emptyLayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        String url = getResources().getString(R.string.base_url) + "/get_leaderboard.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("response_country", "response " + response);
                progressBar.setVisibility(View.GONE);
                solveCountryJson(response);
                if (response != null) {
                    try {
                        list.clear();

                        json4 = response;
                        JSONObject object = new JSONObject(response);
                        solveCountryJson(response);


                        Log.i("confirm", json4);

                        // parseJSON(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                super.getParams();
                Map<String, String> param = new HashMap<>();
                param.put("game_type", "millionaire");
                param.put("country", "1");
                return param;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    private void parseJSON(String response) throws JSONException {

        JSONArray jsonArray = new JSONArray(response);

        for (int a = 0; a < jsonArray.length(); a++) {
            JSONObject obj = jsonArray.getJSONObject(a);
            String score = obj.getString("score");
            String username = obj.getString("username");
            String country = ""; //= obj.getString("country");
            String country_json = obj.getString("country_json");
            String country_flag = "";
            if (country_json.length() > 10) {

                JSONObject country_json_object = new JSONObject(country_json.replaceAll("[\\\\]{1}[\"]{1}", "\""));

                country = country_json_object.getString("name");
                country_flag = country_json_object.getString("url").replace("\\", "");
                Log.i("efi", country_flag + " " + username + " " + country);

            }
//            Log.i("ok" +
//                    "iii", obj.toString());
            if (username.length() > 13) {
                username = username.substring(0, 13) + "...";
            }
            String avatar = obj.getString("avatar");
//            username = username.substring(0,1).toUpperCase()+""+username.substring(1);
            String pattern = "#,###,###.###";
            DecimalFormat decimalFormat = new DecimalFormat(pattern);
            if (country_flag.length() > 10) {
                // Log.i("J9999",username+"  "+country_flag);
            }

            if (a == 0) {
                firstContainer.setVisibility(View.VISIBLE);
                name1.setText(username);
                getAvatar(img1, avatar);
                amount1.setText("$" + decimalFormat.format(Integer.parseInt(score)));

                country_name2.setVisibility(View.VISIBLE);
                country_name2.setText(country);
                if (!country_flag.isEmpty()) {
                    loadFlag(country_flag, flag2);
                    // SVGLoader.fetchSvg(LeaderBoard.this,country_flag,flag2);
                    flag2.setVisibility(View.VISIBLE);

                }
            } else if (a == 1) {
                secondContainer.setVisibility(View.VISIBLE);
                name2.setText(username);
                getAvatar(img2, avatar);
                amount2.setText("$" + decimalFormat.format(Integer.parseInt(score)));
                country_name1.setText(country);
                country_name1.setVisibility(View.VISIBLE);

                if (!country_flag.isEmpty()) {
                    // SVGLoader.fetchSvg(LeaderBoard.this,country_flag,flag1);
                    loadFlag(country_flag, flag1);
                    flag1.setVisibility(View.VISIBLE);
                }

            } else if (a == 2) {
                thirdContainer.setVisibility(View.VISIBLE);
                name3.setText(username);
                getAvatar(img3, avatar);
                amount3.setText("$" + decimalFormat.format(Integer.parseInt(score)));
                country_name3.setText(country);
                country_name3.setVisibility(View.VISIBLE);
                if (!country_flag.isEmpty()) {
                    loadFlag(country_flag, flag3);
                    //SVGLoader.fetchSvg(LeaderBoard.this,country_flag,flag3);
                    flag3.setVisibility(View.VISIBLE);
                }
            } else {

                PlayerObject obj1 = new PlayerObject();
                obj1.setPlayerName(username);
                obj1.setHighscore("$" + decimalFormat.format(Integer.parseInt(score)));
                obj1.setPosition("" + a);
                obj1.setCountry(country);
                obj1.setCountry_flag(country_flag);

                list.add(obj1);
            }
        }
        adapter.notifyDataSetChanged();
        if (jsonArray.length() == 0 || jsonArray == null) {
            emptyText.setVisibility(View.VISIBLE);
        } else if (jsonArray != null && jsonArray.length() > 0) {

        } else {
            emptyLayout.setVisibility(View.GONE);
        }

    }

    private String getAvatar(ImageView imageView, String avatar) {
        //SharedPreferences sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE);
        //String avatar = sharedPreferences.getString("avatar","");
        switch (avatar) {
            case "1":
                imageView.setImageResource(R.drawable.avatar1);
                break;
            case "2":
                imageView.setImageResource(R.drawable.avatar2);
                break;
            case "3":
                imageView.setImageResource(R.drawable.avatar3);
                break;
            case "4":
                imageView.setImageResource(R.drawable.avatar4);
                break;
        }
        return avatar;
    }

    @SuppressLint("ResourceAsColor")
    private void setView(View view) {

        Log.i("iiii999", "Obi");


        country_layout_container = view.findViewById(R.id.country_layout_container);


        img1 = view.findViewById(R.id.img1);
        img2 = view.findViewById(R.id.img2);
        img3 = view.findViewById(R.id.img3);

        country_name1 = view.findViewById(R.id.country_name1);
        country_name2 = view.findViewById(R.id.country_name2);
        country_name3 = view.findViewById(R.id.country_name3);


        country_name3.setVisibility(View.GONE);

        country_name1.setVisibility(View.GONE);
        country_name2.setVisibility(View.GONE);


        flag1 = view.findViewById(R.id.flag1);
        flag2 = view.findViewById(R.id.flag2);
        flag3 = view.findViewById(R.id.flag3);


        flag1.setVisibility(View.GONE);
        flag2.setVisibility(View.GONE);
        flag3.setVisibility(View.GONE);


        firstContainer = view.findViewById(R.id.first);
        secondContainer = view.findViewById(R.id.second);
        thirdContainer = view.findViewById(R.id.third);
        name1 = view.findViewById(R.id.name1);
        name2 = view.findViewById(R.id.name2);
        name3 = view.findViewById(R.id.name3);

        amount1 = view.findViewById(R.id.amount_won_1);
        amount2 = view.findViewById(R.id.amount_won_2);
        amount3 = view.findViewById(R.id.amount_won_3);
        recyclerView = view.findViewById(R.id.recyclerview);


        adapter = new LeaderboardAdapter(this, list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        ///////
//        countryJsonAdapter = new CountryJsonAdapter(this,jsonObjects);
//        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this);
//        countryListView = view.findViewById(R.id.country_listview);
//        countryListView.setLayoutManager(layoutManager2);
//        countryListView.setHasFixedSize(true);
//        countryListView.setAdapter(countryJsonAdapter);
    }

    private void deSelector() {

        LinearLayout[] arrCon = {allBtn, weekBtn, dailyBtn, countryBtn};

        for (LinearLayout layout : arrCon) {
            layout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        }
    }

    private void selector(int b) {
        LinearLayout[] arrCon = {allBtn, weekBtn, dailyBtn, countryBtn};
        deSelector();
        for (int a = 0; a < arrCon.length; a++) {
            if (a == b) {
                LinearLayout layout = arrCon[a];
                layout.setBackgroundColor(getResources().getColor(R.color.basic_color));
            }
        }
    }


    private void loadInterstialAd() {
        //interstitialAd = AdManager.mInterstitialAd; //new InterstitialAd(LeaderBoard.this) ;
//        interstitialAd.setAdUnitId (LeaderBoard.this.getResources().getString(R.string.interstitial_adunit) ) ;
//        interstitialAd.loadAd(new AdRequest.Builder().build());
        AdManager.loadInterstitialAd(LeaderBoard.this);
    }

    private void showInterstitial() {
//        if (interstitialAd.isLoaded()) {
//            interstitialAd.show();
//        }else{
//            interstitialAd.setAdListener(new AdListener() {
//                public void onAdLoaded() {
//                    showInterstitial();
//                }
//            });
//        }
        AdManager.showInterstitial(LeaderBoard.this);
        if (AdManager.interstitialAd != null) {
            AdManager.interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                    Intent i = new Intent(LeaderBoard.this, Dashboard.class);
                    startActivity(i);
                    finish();
                    super.onAdFailedToShowFullScreenContent(adError);
                }

                @Override
                public void onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent();
                    Intent i = new Intent(LeaderBoard.this, Dashboard.class);
                    startActivity(i);
                    finish();
                }
            });

        } else {
            Intent i = new Intent(LeaderBoard.this, Dashboard.class);
            startActivity(i);
            finish();
        }
    }

    public void checkPermission() {
        if (ContextCompat.checkSelfPermission(LeaderBoard.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(LeaderBoard.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                ActivityCompat.requestPermissions(LeaderBoard.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE);
            } else {
                ActivityCompat.requestPermissions(LeaderBoard.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE);
            }
        } else {
            // Permission has already been granted
            Bitmap bitmap = takeScreenshot();
            saveBitmap(bitmap);
            shareIt();
        }
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
                Toast.makeText(LeaderBoard.this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Bitmap bitmap = takeScreenshot();
                saveBitmap(bitmap);
                shareIt();
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

    public void saveBitmap(Bitmap bitmap) {
        imagePath = new File(Environment.getExternalStorageDirectory() + "/screenshotxyz.jpg");
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(imagePath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void shareIt() {
        Uri uri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", imagePath);
        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
        String url = "https://play.google.com/store/apps/details?id=" + appPackageName;
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        sharingIntent.setType("image/jpeg");
        String shareText = getResources().getString(R.string.ad_copy2);
        shareText = shareText.replace("usernamevariable", username);
        shareText = shareText.replace("111", url);
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareText);
        sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);

        startActivity(Intent.createChooser(sharingIntent, "Share via"));

    }

    public Bitmap takeScreenshot() {
        View rootView = findViewById(android.R.id.content).getRootView();
        rootView.setDrawingCacheEnabled(true);
        return rootView.getDrawingCache();
    }


    public void solveCountryJson(String json) {
        Log.i("king", "oooooo");

        try {
            JSONArray jsonArray = new JSONArray(json);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                jsonObjects.add(obj);
                Log.i("jsonnnnnn" + String.valueOf(i), String.valueOf(obj));


            }


            //countryJsonAdapter.notifyDataSetChanged();

            inflateCountryViews(jsonObjects);


        } catch (Exception e) {
            e.printStackTrace();

        }


    }


    public void inflateCountryViews(ArrayList jsonObjects) {
        country_layout_container.removeAllViews();
        for (int i = 0; i < jsonObjects.size(); i++) {

            View convertView = layoutInflater.inflate(R.layout.leader_score_item, country_layout_container, false);


            //convertView = LayoutInflater.from(context).inflate(R.layout.leader_score_item, parent, false);

            TextView substring = convertView.findViewById(R.id.highscore);
            TextView player_name = convertView.findViewById(R.id.player_name);
            TextView country = convertView.findViewById(R.id.country);
            country.setVisibility(View.GONE);
            ImageView flag = convertView.findViewById(R.id.flag_img);
            TextView positionTXT = convertView.findViewById(R.id.position);
            positionTXT.setText(String.valueOf(i + 1));

            try {
                JSONObject object = (JSONObject) jsonObjects.get(i);
                String score = object.getString("score");
                String country_name = object.getString("country");
                String country_json_string = object.getString("country_json");
                JSONObject country_json_object = new JSONObject(country_json_string.replaceAll("[\\\\]{1}[\"]{1}", "\""));
                String country_flag = country_json_object.getString("url");

                if (!country_flag.isEmpty()) {
                    // SVGLoader.fetchSvg(LeaderBoard.this, country_flag, flag);
                    loadFlag(country_flag, flag);
                } else {
                    flag.setVisibility(View.GONE);

                }

                String pattern = "#,###,###.###";
                DecimalFormat decimalFormat = new DecimalFormat(pattern);


                Log.i("ooooopp111", String.valueOf(object));

                player_name.setText(country_name);
                substring.setText("$" + decimalFormat.format(Integer.parseInt(score)));
                country.setText(country_name);


                //Value {"name":"Ascension Island","url":""} at country_json of type java.lang.String cannot be converted to JSONObject

                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(LeaderBoard.this, CountryLeaderBoard.class);
                        Utils.darkBlueBlink(convertView, getApplicationContext());


//                   Map<String,String> map = new HashMap<>();
//                   map.put("country",country);
//                   map.put("country_flag",country_flag);
                        //intent.putExtra("country_map", (Serializable) map);
                        intent.putExtra("country", country_name);
                        intent.putExtra("country_flag", country_flag);
                        startActivity(intent);
                    }
                });


            } catch (Exception e) {
                /// Log.i("ooooopp",e.toString());
                e.printStackTrace();

            }

            country_layout_container.addView(convertView);

        }


    }

    public void moveToSignUp(String username) {
        Utils.destination_activity = LeaderBoard.class;


//        if(username.equals(getResources().getString(R.string.anonymous_user))){
//            Utils.leaderboardClick = Utils.leaderboardClick+1;
//           if( Utils.leaderboardClick < 2) {
//               Intent i = new Intent(LeaderBoard.this, UserDetails.class);
//               startActivity(i);
//               finish();
//           }else{
//
//               Utils.leaderboardClick = 0;
//
//           }
//        }


    }

    @Override
    public void onBackPressed() {

        Intent i = new Intent(LeaderBoard.this, Dashboard.class);
        startActivity(i);

        super.onBackPressed();
    }

    public void loadFlag(String url, ImageView imageView) {
        try {
            SVGLoader.fetchSvg(LeaderBoard.this, url, imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}