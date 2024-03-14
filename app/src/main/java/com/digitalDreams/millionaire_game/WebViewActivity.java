package com.digitalDreams.millionaire_game;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.digitalDreams.millionaire_game.alpha.AudioManager;

public class WebViewActivity extends AppCompatActivity {
    String questionId = "";
    ImageView arrow_back;
    RelativeLayout close_container;
    ProgressBar progress_bar;

    @Override
    protected void onStart() {
        super.onStart();
        AdManager.loadInterstitialAd(this);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);


        LinearLayout adViewContainer = findViewById(R.id.adview_container);
        AdManager.loadBanner(this, adViewContainer);

        progress_bar = findViewById(R.id.progress_bar);
        progress_bar.setVisibility(View.GONE);

        questionId = getIntent().getStringExtra("questionId");

        arrow_back = findViewById(R.id.arrow_back);
        close_container = findViewById(R.id.close_container);

        close_container.setOnClickListener(view -> {
            AudioManager.darkBlueBlink(WebViewActivity.this, close_container);
            AdManager.showInterstitial(WebViewActivity.this);
            onBackPressed();
        });

        arrow_back.setOnClickListener(view -> {
            AudioManager.darkBlueBlink(WebViewActivity.this, arrow_back);
            AdManager.showInterstitial(WebViewActivity.this);
            onBackPressed();
        });

        // Find the WebView by its unique ID
        WebView webView = findViewById(R.id.webview);

        // loading http://www.google.com url in the WebView.
        webView.loadUrl(Utils.readMoreUrl + questionId);

        // this will enable the javascript.
        webView.getSettings().setJavaScriptEnabled(true);

        // WebViewClient allows you to handle
        // onPageFinished and override Url loading.
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Log.i("url", url);
                progress_bar.setVisibility(View.VISIBLE);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                progress_bar.setVisibility(View.GONE);
                super.onPageFinished(view, url);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AudioManager.releaseMusicResources();
        AdManager.disposeAds();
    }
}