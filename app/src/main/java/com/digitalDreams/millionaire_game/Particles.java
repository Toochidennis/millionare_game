package com.digitalDreams.millionaire_game;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Path;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.Random;


     class Particles {
        Particles(Context context, RelativeLayout rootview, int imagexml,int size) {
            for (int a = 0; a <size;a++){
               new Particle(context, rootview, imagexml);
        }

        }

         Particles(Context context, LinearLayout rootview, int imagexml, int size) {
             for (int a = 0; a < size; a++) {
                 new Particle(context, rootview, imagexml);
             }
         }

        class Particle {
            float x,y;
            int direction;
            ImageView vector;
            Long speed;
            Path path;
            int width,heigth;
            ObjectAnimator moveX;
            int finalHeight;
            int finalWidth;
            View rootview;
            Context context;
            float lastScale=1;


            public Particle(Context context, RelativeLayout rootview, int imagexml) {
                DisplayMetrics displayMetrics = new DisplayMetrics();
                ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                this.heigth = displayMetrics.heightPixels;
                this.width = displayMetrics.widthPixels;
                this.rootview = rootview;
                this.context = context;
                inflateView(context,rootview,imagexml);
                randomizePosition();
                startAnimation();

            }

            public Particle(Context context, LinearLayout rootview, int imagexml) {
                DisplayMetrics displayMetrics = new DisplayMetrics();
                ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                this.heigth = displayMetrics.heightPixels;
                this.width = displayMetrics.widthPixels;
                this.rootview = rootview;
                this.context = context;
                inflateView(context,rootview,imagexml);
                randomizePosition();
                startAnimation();

            }


            public void randomizePosition(){

                if(width==0&&heigth==0){
                    this.x=0;
                    this.y=0;
                }else {
                    this.x = new Random().nextInt((width));
                    this.y = new Random().nextInt(heigth);
                }
                this.speed = Long.valueOf((new Random().nextInt(500) + 500)*10);
                this.direction = (int) Math.round(new Random().nextInt(2));
                scaleView();

                path = new Path();
                path.moveTo(x,y);
                if(direction>0){
                    path.rQuadTo(x+250,y+0,x+40,y+250);
                }else{
                    path.rQuadTo(x-250,y+0,x-40,y+250);
                }

                moveX = ObjectAnimator.ofFloat(vector,"x","y",path );

                moveX.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        randomizePosition();
                        startAnimation();
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });


            }

            public void startAnimation(){
                moveX.setDuration(speed);
               // moveX.setStartDelay(delay);
                moveX.start();
                AlphaAnimation fadeOut=new AlphaAnimation(1,0);
                AlphaAnimation fadeIn=new AlphaAnimation(0,1);

                final AnimationSet set = new AnimationSet(false);

                set.addAnimation(fadeOut);
                set.addAnimation(fadeIn);
                fadeOut.setStartOffset(2000);
                set.setDuration(speed-2000);
                vector.startAnimation(set);

            }

            private void scaleView(){
                float scale = (float) (new Random().nextFloat()+0.5);
                ViewTreeObserver vto = vector.getViewTreeObserver();
                vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    public boolean onPreDraw() {
                        vector.getViewTreeObserver().removeOnPreDrawListener(this);
                        finalHeight = vector.getMeasuredHeight();
                        finalWidth = vector.getMeasuredWidth();
                        return true;
                    }
                });
                finalHeight= (int) (finalHeight*scale/lastScale);
                finalWidth = (int) (finalWidth*scale/lastScale);
                if(finalWidth!=0&& finalHeight!=0){

                    vector.getLayoutParams().width = finalWidth;
                    vector.getLayoutParams().height = finalHeight;
                    vector.requestLayout();
                    lastScale=scale;
                }

            }

            public void inflateView(Context context,RelativeLayout rootview,int imagelayout){
                LayoutInflater inflater = LayoutInflater.from(context);
                View view = inflater.inflate(imagelayout,rootview,false);
                rootview.addView(view);
                this.vector = (ImageView) view;
            }

            public void inflateView(Context context,LinearLayout rootview,int imagelayout){
                LayoutInflater inflater = LayoutInflater.from(context);
                View view = inflater.inflate(imagelayout,rootview,false);
                rootview.addView(view);
                this.vector = (ImageView) view;

            }

        }
}
