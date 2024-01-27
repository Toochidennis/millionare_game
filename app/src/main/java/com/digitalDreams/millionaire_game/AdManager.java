package com.digitalDreams.millionaire_game;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.gms.ads.rewarded.ServerSideVerificationOptions;

public class AdManager {
    public static InterstitialAd mInterstitialAd;
    public static RewardedAd rewardedAd;
    //Context context;
    //Activity activity;
    //Activity activity;

//    AdManager(Context context){
//        this.context = context;
//       // this.activity = activity;
//        activity = (Activity) context;
//        initInterstitialAd();
//        initRewardedVideo();
//
//
//    }


    public static void initInterstitialAd(Activity context) {
        // if(mInterstitialAd == null) {
        MobileAds.initialize(context, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(context, context.getResources().getString(R.string.interstitial_adunit), adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        Log.i("InterstitialAdd", "onAdLoaded");
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.d("InterstitialAdd", loadAdError.toString());
                        if (mInterstitialAd == null) {
                            mInterstitialAd = null;
                        }
                    }
                });

        // }
    }


    public static void showInterstitial(Activity context) {
        try {
            if (mInterstitialAd != null) {
                mInterstitialAd.show(context);
            } else {
                initInterstitialAd(context);
                Log.d("TAG", "The interstitial ad wasn't ready yet.");
            }

        } catch (Exception e) {

        }
    }

    public static void initRewardedVideo(Activity context) {
        // if(rewardedAd == null) {
        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedAd.load(context, "ca-app-pub-4696224049420135/7768937909",
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error.
                        Log.d("Admob", loadAdError.toString());
                        if (rewardedAd == null) {
                            rewardedAd = null;

                        }

                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd ad) {
                        rewardedAd = ad;
                        Log.d("rewardedAd", "Ad was loaded.");
                    }
                });

        // }


//    rewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
//        @Override
//        public void onAdClicked() {
//            // Called when a click is recorded for an ad.
//            Log.d("REwarded", "Ad was clicked.");
//        }
//
//        @Override
//        public void onAdDismissedFullScreenContent() {
//            // Called when ad is dismissed.
//            // Set the ad reference to null so you don't show the ad a second time.
//            Log.d("REwarded", "Ad dismissed fullscreen content.");
//            rewardedAd = null;
//        }
//
//        @Override
//        public void onAdFailedToShowFullScreenContent(AdError adError) {
//            // Called when ad fails to show.
//            Log.e("REwarded", "Ad failed to show fullscreen content.");
//            rewardedAd = null;
//        }
//
//        @Override
//        public void onAdImpression() {
//            // Called when an impression is recorded for an ad.
//            Log.d("REwarded", "Ad recorded an impression.");
//        }
//
//        @Override
//        public void onAdShowedFullScreenContent() {
//            // Called when ad is shown.
//            Log.d("REwarded", "Ad showed fullscreen content.");
//        }
//    });


    }

    public static void showRewardAd(Activity context) {
        try {
            if (rewardedAd != null) {
                //Activity activityContext = MainActivity.this;

                rewardedAd.show(context, rewardItem -> {
                    // Handle the reward.
                    Log.d("rewarded", "The user earned the reward.");
                    int rewardAmount = rewardItem.getAmount();
                    String rewardType = rewardItem.getType();
                });
            } else {
                Log.d("rewarded", "The rewarded ad wasn't ready yet.");
                initRewardedVideo(context);
            }
        } catch (Exception e) {

        }
    }


}
