package com.digitalDreams.millionaire_game;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;

import androidx.core.animation.ObjectAnimator;


public class LoadingDialog extends Dialog {

    private ImageView loadingImageView;
    private static final int CHECK_INTERVAL = 1000;


    public LoadingDialog(Context context) {
        super(context);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        setContentView(R.layout.dialog_loading);

        loadingImageView = findViewById(R.id.loadingImageView);

        animateImage("scaleX");
        animateImage("scaleY");

    }


    private void animateImage(String propertyName) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(loadingImageView, propertyName, 1f, 1.2f);
        animator.setDuration(CHECK_INTERVAL);
        animator.setRepeatMode(ObjectAnimator.REVERSE);
        animator.setRepeatCount(ObjectAnimator.INFINITE);
        animator.start();
    }

}