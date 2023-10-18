package com.digitalDreams.millionaire_game;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class WebviewActivity extends AppCompatActivity {
    String questionId = "";
    ImageView arrow_back;
    RelativeLayout close_container;
    ProgressBar progress_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        AdView mAdView;
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        progress_bar = findViewById(R.id.progress_bar);
        progress_bar.setVisibility(View.GONE);

        questionId = getIntent().getStringExtra("questionId");

        arrow_back = findViewById(R.id.arrow_back);
        close_container = findViewById(R.id.close_container);

        close_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.darkBlueBlink(close_container, WebviewActivity.this);
                AdManager.showInterstitial(WebviewActivity.this);
                onBackPressed();


            }
        });

        arrow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.darkBlueBlink(arrow_back, WebviewActivity.this);
                AdManager.showInterstitial(WebviewActivity.this);
                onBackPressed();


            }
        });


        // Find the WebView by its unique ID
        WebView webView = findViewById(R.id.webview);

        // loading http://www.google.com url in the WebView.
        webView.loadUrl(Utils.readMoreUrl+questionId);

        // this will enable the javascript.
        webView.getSettings().setJavaScriptEnabled(true);

        // WebViewClient allows you to handle
        // onPageFinished and override Url loading.
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Log.i("url",url);
                progress_bar.setVisibility(View.VISIBLE);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                progress_bar.setVisibility(View.VISIBLE);
                super.onPageFinished(view, url);
            }
        });


    }
}