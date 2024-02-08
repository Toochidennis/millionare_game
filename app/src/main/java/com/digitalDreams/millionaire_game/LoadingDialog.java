package com.digitalDreams.millionaire_game;

import static com.digitalDreams.millionaire_game.alpha.Constants.DELAY_INTERVAL_LONG;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;

import androidx.core.animation.ObjectAnimator;

import java.util.Objects;


public class LoadingDialog extends Dialog {

    private ImageView loadingImageView;


    public LoadingDialog(Context context) {
        super(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        setContentView(R.layout.dialog_loading);

        loadingImageView = findViewById(R.id.loadingImageView);

        animateImage("scaleX");
        animateImage("scaleY");
    }


    private void animateImage(String propertyName) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(loadingImageView, propertyName, 1f, 1.2f);
        animator.setDuration(DELAY_INTERVAL_LONG);
        animator.setRepeatMode(ObjectAnimator.REVERSE);
        animator.setRepeatCount(ObjectAnimator.INFINITE);
        animator.start();
    }

}