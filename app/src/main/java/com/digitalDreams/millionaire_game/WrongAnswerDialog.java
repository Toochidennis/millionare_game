package com.digitalDreams.millionaire_game;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;


import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.rewarded.RewardedAd;


public class WrongAnswerDialog extends Dialog {
    Context context;
    RewardedAd mRewardedVideoAd;
    MediaPlayer mMediaPlayer;
    ImageView cancel_icon;
   // AdManager adManager;

    public WrongAnswerDialog(@NonNull Context context,

                             MediaPlayer mMediaPlayer) {

        super(context);
        this.context = context;

        this.mMediaPlayer = mMediaPlayer;
        AdManager.initInterstitialAd((Activity)context);
        AdManager.initRewardedVideo((Activity) context);


    }

    RelativeLayout continue_btn,close_dialog;
    RelativeLayout give_up;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wrong_answer_layout);
        continue_btn = findViewById(R.id.play_again);
        give_up = findViewById(R.id.give_up);
        close_dialog =findViewById(R.id.close_dialog);
        cancel_icon =  findViewById(R.id.cancel_icon);
        close_dialog.setVisibility(View.GONE);





        SharedPreferences sharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        String languageCode = sharedPreferences.getString("language","en");
        int endcolor = sharedPreferences.getInt("end_color",context.getResources().getColor(R.color.purple_dark));
        int startColor = sharedPreferences.getInt("start_color",context.getResources().getColor(R.color.purple_500));
        String game_level = sharedPreferences.getString("game_level","1");

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE);

        RelativeLayout bg = findViewById(R.id.rootview);

        GradientDrawable gd = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[] {startColor,endcolor});

        bg.setBackgroundDrawable(gd);

//        cancel_icon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dismiss();
//
//                if (mMediaPlayer != null) {
//                    mMediaPlayer.pause();
//                }
//
//
//
//                Intent intent = new Intent(getContext(), FailureActivity.class);
//                context.startActivity(intent);
//
//            }
//        });

        close_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();

                if (mMediaPlayer != null) {
                    mMediaPlayer.pause();
                }



                Intent intent = new Intent(getContext(), FailureActivity.class);
                context.startActivity(intent);

            }
        });

//        continue_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//
//
//            }
//        });




    }

    public   void  showRewarededAdWithListener(){

        Utils.greenBlink(continue_btn, context);
        if (CountDownActivity. mMediaPlayer != null) {
            CountDownActivity. mMediaPlayer.pause();
        }

        if(Utils.isOnline(context)) {

            // if (mRewardedVideoAd != null) {

            AdManager.showRewardAd((Activity) context);
            try{
                AdManager.rewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdClicked() {
                        // Called when a click is recorded for an ad.
                        // Log.d(TAG, "Ad was clicked.");
                    }

                    @Override
                    public void onAdDismissedFullScreenContent() {
                        // Called when ad is dismissed.
                        // Set the ad reference to null so you don't show the ad a second time.
                        //Log.d(TAG, "Ad dismissed fullscreen content.");
                        //rewardedAd = null;
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                        // Called when ad fails to show.
                        //Log.e(TAG, "Ad failed to show fullscreen content.");
                        // rewardedAd = null;
                        Toast.makeText(context,"Error: Loading video Ad failed, Please connect to the internet",Toast.LENGTH_LONG).show();

                        dismiss();

                        Intent intent = new Intent(getContext(), FailureActivity.class);
                        context.startActivity(intent);
                    }

                    @Override
                    public void onAdImpression() {
                        // Called when an impression is recorded for an ad.
                        //Log.d(TAG, "Ad recorded an impression.");
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        // Called when ad is shown.
                        // Log.d(TAG, "Ad showed fullscreen content.");
                    }
                });
            }catch (Exception e){
                e.printStackTrace();

                AdManager.showInterstitial((Activity) context);
                if(AdManager.mInterstitialAd != null){
                    AdManager.mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                        @Override
                        public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                            super.onAdFailedToShowFullScreenContent(adError);
                        }
                    });
                }else{
                    dismiss();
                    Intent i = new Intent(context,FailureActivity.class);
                    context.startActivity(i);

                }


            }
            // }
            dismiss();
        }

        else {
            Toast.makeText(context,"Connect to internet and try again",Toast.LENGTH_LONG).show();
            dismiss();
            Intent intent = new Intent(getContext(), FailureActivity.class);
            context.startActivity(intent);


        }

    }

}
