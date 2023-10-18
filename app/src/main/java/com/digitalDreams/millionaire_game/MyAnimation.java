package com.digitalDreams.millionaire_game;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;

public class                                                                                                                                                                                              MyAnimation {
    View view;
    int finalHeight;
    int finalWidth;
    float lastScale=1;

    public MyAnimation( View view) {
        this.view = view;
        startAnimation();
    }


    private void startAnimation(){
        ObjectAnimator scaleUpX = ObjectAnimator.ofFloat(view, "scaleX", 1.4f);
        ObjectAnimator scaleUpY = ObjectAnimator.ofFloat(view, "scaleY", 1.5f);
        scaleUpX.setDuration(1500);
        scaleUpY.setDuration(1500);

        AnimatorSet scaleUp = new AnimatorSet();
        scaleUp.play(scaleUpX).with(scaleUpY);

        scaleUp.start();
        scaleUp.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                scaleDown();
                startAnimation();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        AlphaAnimation fadeOut=new AlphaAnimation(1,0);
        //AlphaAnimation fadeIn=new AlphaAnimation(0,1);

        final AnimationSet set = new AnimationSet(false);
        //set.addAnimation(fadeIn);
        set.addAnimation(fadeOut);
        set.setDuration(1500);
        view.startAnimation(set);
    }

    private void scaleDown(){
        ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(view, "scaleX", 0.8f);
        ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(view, "scaleY", 0.8f);
        scaleDownX.setDuration(0);
        scaleDownY.setDuration(0);

        AnimatorSet scaleDown = new AnimatorSet();
        scaleDown.play(scaleDownX).with(scaleDownY);

        scaleDown.start();
    }
}
