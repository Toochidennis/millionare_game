package com.digitalDreams.millionaire_game;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

class ExitDialog extends Dialog {
    Activity context;
    public ExitDialog(@NonNull Activity context) {
        super(context);
        this.context = context;
    }
//    AdView mAdView;
//
//    @Override
//    protected void onStart() {
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);
//
//        super.onStart();
//    }

    RelativeLayout relativeLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.exit_dialog_layout);
        RelativeLayout yesBtn = findViewById(R.id.yes);
        RelativeLayout noBtn = findViewById(R.id.no);
        RelativeLayout rootview=findViewById(R.id.rootview);
        SharedPreferences sharedPreferences = context.getSharedPreferences("settings",Context.MODE_PRIVATE);
        int endcolor = sharedPreferences.getInt("end_color",context.getResources().getColor(R.color.purple_dark));
        int startColor = sharedPreferences.getInt("start_color",context.getResources().getColor(R.color.purple_500));
        GradientDrawable gd = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[] {startColor,endcolor});

        rootview.setBackgroundDrawable(gd);


       // mAdView = findViewById(R.id.adView);
       // AdRequest adRequest = new AdRequest.Builder().build();
       // mAdView.loadAd(adRequest);
        //mAdView.setAdSize(new AdSize(AdSize.FULL_WIDTH, 50));
        relativeLayout = findViewById(R.id.close_dialog);

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //context.finish();
                if( CountDownActivity.mMediaPlayer != null){
                   try{
                       CountDownActivity.mMediaPlayer.stop();
                       CountDownActivity.mMediaPlayer.release();
                   }catch (Exception e){}

                }

//                Intent a = new Intent(Intent.ACTION_MAIN);
//                a.addCategory(Intent.CATEGORY_HOME);
//                a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(a);

                int pid = android.os.Process.myPid();
                android.os.Process.killProcess(pid);
                getOwnerActivity().finish();
                System.exit(0);

            }
        });

        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

             try{
                if(Utils.isOnline(context)){
                    context.startActivity(
                            new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("https://play.google.com/store/apps/details?id=com.digitalDreams.millionaire_game")));
             }else{

                    System.exit(0);


                }
             }catch (Exception e){

             }
            }
        });
    }
}
