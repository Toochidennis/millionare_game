package com.digitalDreams.millionaire_game;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
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
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.interstitial.InterstitialAd;

public class ExitGameDialog extends Dialog {
    Context context;
    String amountWon;

    public static int MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE=100;
    File imagePath;
    public static InterstitialAd interstitialAd;
  //  AdManager adManager;

    public ExitGameDialog(@NonNull Context context,String amountWon) {
        super(context);
        this.context = context;
        this.amountWon = amountWon;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.exit_dialog_2);
        //adManager =  new AdManager(context);


        AdManager.initInterstitialAd((Activity) context);
        AdManager.initRewardedVideo((Activity) context);

        loadInterstialAd();
        SharedPreferences sharedPreferences = context.getSharedPreferences("settings",Context.MODE_PRIVATE);
        String languageCode = sharedPreferences.getString("language","en");
        int endcolor = sharedPreferences.getInt("end_color",context.getResources().getColor(R.color.purple_dark));
        int startColor = sharedPreferences.getInt("start_color",context.getResources().getColor(R.color.purple_500));
        int cardBackground = sharedPreferences.getInt("card_background",0x03045e);
        LinearLayout bg = findViewById(R.id.rootview);
        new Particles(context,bg,R.layout.image_xml,20);
        GradientDrawable gd = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[] {startColor,endcolor});

        bg.setBackgroundDrawable(gd);
        ImageView closeBtn = findViewById(R.id.close);
        closeBtn.setOnClickListener(view -> dismiss());
        RelativeLayout takeMoneyBtn = findViewById(R.id.take_money);
        RelativeLayout continueBtn = findViewById(R.id.continue_);
        takeMoneyBtn.setOnClickListener(view -> {
            Utils.darkBlueBlink(takeMoneyBtn, context);
            showInterstitial();
            if(CountDownActivity.mMediaPlayer!=null) {
                CountDownActivity.mMediaPlayer.stop();

            }

        });
        TextView amountwonText = findViewById(R.id.amount_won);
        amountwonText.setText(Utils.addCommaAndDollarSign(Integer.parseInt(amountWon)));


        continueBtn.setOnClickListener(view -> {
            Utils.darkBlueBlink(continueBtn, context);

            dismiss();
        });

        TextView descriptionTxt = findViewById(R.id.desc);
       // descriptionTxt.setText("Are you sure you want to take "+ amountWon +" and leave the game?");

        LinearLayout shareBtn = findViewById(R.id.share);
        shareBtn.setOnClickListener(view -> {
            Utils.darkBlueBlink(shareBtn, context);

            checkPermission();
        });
    }

    private void loadInterstialAd(){
        interstitialAd = AdManager.mInterstitialAd; //new InterstitialAd(context) ;
//        interstitialAd.setAdUnitId (context.getResources().getString(R.string.interstitial_adunit) ) ;
//        interstitialAd.loadAd(new AdRequest.Builder().build());
    }

    private void showInterstitial() {
        AdManager.showInterstitial((Activity) context);

        if(AdManager.mInterstitialAd != null){
            AdManager.mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                    super.onAdFailedToShowFullScreenContent(adError);
                    quite();
                }

                @Override
                public void onAdDismissedFullScreenContent() {
                    quite();
                    super.onAdDismissedFullScreenContent();
                }

                @Override
                public void onAdClicked() {
                    quite();
                    super.onAdClicked();
                }

            });


        }else{

            quite();

        }
    }

    public  void quite(){

        Intent intent = new Intent(context,PlayDetailsActivity.class);
        intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
        intent.putExtra("isShowAd",false);
        context.startActivity(intent);
        GameActivity2.gameActivity2.finish();

    }


    public void checkPermission(){
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions((Activity) context,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE);
            } else {
                // No explanation needed; request the permission

                ActivityCompat.requestPermissions((Activity) context,
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
        Uri uri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", imagePath);
        final String appPackageName = context.getPackageName(); // getPackageName() from Context or Activity object
        String url="https://play.google.com/store/apps/details?id=" + appPackageName;
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("image/*");
        String shareText = context.getResources().getString(R.string.ad_copy);
        shareText = shareText.replace("000",GameActivity2.amountWon);
        shareText=shareText.replace("111",url);
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareText);
        sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);

        context.startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }


}
