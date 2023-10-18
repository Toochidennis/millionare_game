package com.digitalDreams.millionaire_game;

import static android.icu.text.Transliterator.REVERSE;
import static android.view.animation.Animation.INFINITE;

import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Path;
import android.transition.Slide;
import android.transition.Transition;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.core.animation.AccelerateDecelerateInterpolator;
import androidx.core.animation.Animator;
import androidx.core.animation.AnimatorSet;
import androidx.core.animation.ObjectAnimator;

import java.util.ArrayList;
import java.util.Random;

public class NewParticle {
    int width;
    int heigth;
    ImageView vectorImage;
   // View rootview;
    Context context;
    double startScale = 0.001;
    double endScale = 1;
    RelativeLayout rootView;
    int imageView;
    int x;
    int y;
    int totalParticles;
    int durationBound;
   // double maxDuration;


    NewParticle(Context context, int imageView,RelativeLayout rootview,
                int totalParticles,int durationBound){
        this.context = context;
        //this.vectorImage1 = imageView;
        this.rootView = rootview;
        this.imageView = imageView ;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        this.heigth = displayMetrics.heightPixels;
        this.width = displayMetrics.widthPixels;
        this.totalParticles = totalParticles;
        this.durationBound = durationBound;




        generateViews(totalParticles);



    }
    public void generateViews(double totalParticles){
        for(int x = 0; x <= totalParticles; x++ ){
            inflateView(context,rootView,imageView);

        }

    }

public void inflateView(Context context, RelativeLayout rootview, int imagelayout){
    LayoutInflater inflater = LayoutInflater.from(context);
    View view = inflater.inflate(imagelayout,rootview,false);
    rootview.addView(view);
    this.vectorImage = (ImageView) view;



    startAnimation(vectorImage);


}

    public void startAnimation(ImageView currentImage){
        ////////SET COLOR////
        //R.drawable.star.;
        int[] colors = {R.color.white,R.color.white,R.color.orange,R.color.white, R.color.color1};
        int colorPosition = (int) (Math.random() * colors.length);

        currentImage.setColorFilter(context.getResources().getColor(colors[colorPosition])); ;
        ///////////
        this.x = new Random().nextInt((width));
        this.y = new Random().nextInt(heigth);
        int duration = (int) Math.round(new Random().nextInt(durationBound));;
        Path  path = new Path();
        path.moveTo(x,y);


        ObjectAnimator animator = ObjectAnimator.ofFloat(currentImage,
                "x","y",path);
        animator.setDuration(duration);
        /////OPACITY///////////
        ObjectAnimator.ofFloat(currentImage, View.ALPHA, 0, (float) 0.5).setDuration(duration).start();

        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(@NonNull Animator animation) {

            }

            @Override
            public void onAnimationEnd(@NonNull Animator animation) {
                startAnimation(currentImage);

            }

            @Override
            public void onAnimationCancel(@NonNull Animator animation) {

            }

            @Override
            public void onAnimationRepeat(@NonNull Animator animation) {

            }
        });

        ///////////////////////SCALE//////////////////
        Animation anim = new ScaleAnimation(
                (float) startScale ,  (float) endScale, // Start and end values for the X axis scaling
                (float)startScale,  (float)endScale, // Start and end values for the Y axis scaling
                Animation.RELATIVE_TO_SELF, 0f, // Pivot point of X scaling
                Animation.RELATIVE_TO_SELF, 1f); // Pivot point of Y scaling
        anim.setFillAfter(true); // Needed to keep the result of the animation
        anim.setDuration(duration);
       // anim.setRepeatCount(INFINITE);
        currentImage.startAnimation(anim);

        animator.setDuration(duration).start();





    }

}
