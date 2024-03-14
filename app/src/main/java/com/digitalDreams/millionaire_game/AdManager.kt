package com.digitalDreams.millionaire_game

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import com.google.ads.mediation.admob.AdMobAdapter
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object AdManager {

    @JvmField
    var interstitialAd: InterstitialAd? = null

    @JvmField
    var rewardedAd: RewardedAd? = null

    @JvmStatic
    fun initializeAds(context: Context) {
        MobileAds.initialize(context) {
            Log.d("AdManager", "Ads initialized successfully")
        }
    }

    @JvmStatic
    fun loadInterstitialAd(activity: Activity) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val adRequest = AdRequest.Builder().build()
                val interstitialAdLoadCallback = object : InterstitialAdLoadCallback() {
                    override fun onAdLoaded(ad: InterstitialAd) {
                        interstitialAd = ad
                        Log.d("AdManager", "Interstitial ad loaded successfully")
                    }

                    override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                        Log.d("AdManager", "Interstitial ad failed to load: ${loadAdError.message}")
                    }
                }
                InterstitialAd.load(
                    activity,
                    activity.getString(R.string.interstitial_ad_unit_id),
                    adRequest,
                    interstitialAdLoadCallback
                )
            } catch (e: Exception) {
                Log.e("AdManager", "Failed to load interstitial ad: ${e.message}")
            }
        }
    }

    @JvmStatic
    fun showInterstitial(activity: Activity) {
        interstitialAd?.show(activity)
            ?: Log.d("AdManager", "Interstitial ad is not ready")
    }

    @JvmStatic
    fun loadRewardedAd(activity: Activity) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val adRequest = AdRequest.Builder().build()
                val rewardedAdLoadCallback = object : RewardedAdLoadCallback() {
                    override fun onAdLoaded(ad: RewardedAd) {
                        rewardedAd = ad
                        Log.d("AdManager", "Rewarded ad loaded successfully")
                    }

                    override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                        Log.d("AdManager", "Rewarded ad failed to load: ${loadAdError.message}")
                    }
                }
                RewardedAd.load(
                    activity,
                    activity.getString(R.string.rewarded_ad_unit_id),
                    adRequest,
                    rewardedAdLoadCallback
                )
            } catch (e: Exception) {
                Log.e("AdManager", "Failed to load rewarded ad: ${e.message}")
            }
        }
    }

    @JvmStatic
    fun loadBanner(activity: Activity, adViewContainer: LinearLayout) {
        val adView = AdView(activity)
        adView.setAdSize(adSize(activity, adViewContainer))
        adView.adUnitId = activity.getString(R.string.banner_ad_unit)

        val extras = Bundle().apply {
           putString( "collapsible", "bottom")
        }
        Log.d("response", "$extras")
        val adRequest = AdRequest.Builder()
            .addNetworkExtrasBundle(AdMobAdapter::class.java, extras)
            .build()

        adViewContainer.addView(adView)
        adView.loadAd(adRequest)
    }

    // Determine the screen width (less decorations) to use for the ad width.
    // If the ad hasn't been laid out, default to the full screen width.
    private fun adSize(activity: Activity, adViewContainer: View): AdSize {
        val display = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            activity.display
        } else {
            @Suppress("DEPRECATION")
            activity.windowManager.defaultDisplay
        }
        val outMetrics = DisplayMetrics()
        @Suppress("DEPRECATION")
        display?.getMetrics(outMetrics)

        val density = outMetrics.density

        var adWidthPixels = adViewContainer.width.toFloat()
        if (adWidthPixels == 0f) {
            adWidthPixels = outMetrics.widthPixels.toFloat()
        }

        val adWidth = (adWidthPixels / density).toInt()
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, adWidth)
    }

    @JvmStatic
    fun showRewardedAd(activity: Activity) {
        rewardedAd?.show(activity) { rewardItem ->
            Log.d("AdManager", "User earned reward: ${rewardItem.amount} ${rewardItem.type}")
        } ?: Log.d("AdManager", "Rewarded ad is not ready")
    }

    @JvmStatic
    fun disposeAds() {
        interstitialAd?.fullScreenContentCallback = null
        interstitialAd = null
        rewardedAd?.fullScreenContentCallback = null
        rewardedAd = null
    }
}