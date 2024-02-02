package com.digitalDreams.millionaire_game;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

public class AdManager {

    public static InterstitialAd interstitialAd;
    public static RewardedAd rewardedAd;

    public static void initializeAds(Context context) {
        MobileAds.initialize(context, initializationStatus -> Log.d("AdManager", "Ads initialized successfully"));
    }

    public static void loadInterstitialAd(Activity activity) {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(activity, activity.getString(R.string.interstitial_ad_unit_id), adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        AdManager.interstitialAd = interstitialAd;
                        Log.d("AdManager", "Interstitial ad loaded successfully");
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        Log.d("AdManager", "Interstitial ad failed to load: " + loadAdError.getMessage());
                    }
                });
    }

    public static void showInterstitial(Activity activity) {
        if (interstitialAd != null) {
            interstitialAd.show(activity);
        } else {
            Log.d("AdManager", "Interstitial ad is not ready");
        }
    }

    public static void loadRewardedAd(Activity activity) {
        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedAd.load(activity, activity.getString(R.string.rewarded_ad_unit_id), adRequest,
                new RewardedAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull RewardedAd ad) {
                        rewardedAd = ad;
                        Log.d("AdManager", "Rewarded ad loaded successfully");
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        Log.d("AdManager", "Rewarded ad failed to load: " + loadAdError.getMessage());
                    }
                });
    }

    public static void showRewardedAd(Activity activity) {
        if (rewardedAd != null) {
            rewardedAd.show(activity, rewardItem -> Log.d("AdManager", "User earned reward: " + rewardItem.getAmount() + " " + rewardItem.getType()));
        } else {
            Log.d("AdManager", "Rewarded ad is not ready");
        }
    }

    public static void disposeAds() {
        if (interstitialAd != null) {
            interstitialAd.setFullScreenContentCallback(null);
            interstitialAd = null;
        }
        if (rewardedAd != null) {
            rewardedAd.setFullScreenContentCallback(null);
            rewardedAd = null;
        }
    }
}
