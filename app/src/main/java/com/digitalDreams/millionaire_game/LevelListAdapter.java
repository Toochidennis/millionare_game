package com.digitalDreams.millionaire_game;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONObject;

import java.util.ArrayList;

public class LevelListAdapter extends ArrayAdapter<JSONObject> {
    int i = 0;
    String game_level;
    ArrayList<JSONObject> arrayList;
    Context context;
    public LevelListAdapter(@NonNull Context context, ArrayList<JSONObject> arrayList,String game_level) {
        super(context,0, arrayList);
        this.game_level = game_level;
        this.arrayList = arrayList;
        this.context =context;
       // super(context,0, arrayList);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View currentItemView = convertView;
        if (currentItemView == null) {
            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.single_level_layout, parent, false);
        }

            JSONObject currentJSONObject = getItem(position);
            Log.i("currentJSONObject", String.valueOf(currentJSONObject));
            TextView levelTXT = currentItemView.findViewById(R.id.level);
            TextView subTitleTXT = currentItemView.findViewById(R.id.sub_title);
            LinearLayout play_container = currentItemView.findViewById(R.id.play_container);
            ImageView imageView = currentItemView.findViewById(R.id.imageview);
            RelativeLayout btnAnim = currentItemView.findViewById(R.id.btn_forAnim);
            try{

          ///  levelTXT.setText("Stage "+currentJSONObject.getString("level"));
                levelTXT.setText(currentJSONObject.getString("level_name"));
            subTitleTXT.setText(currentJSONObject.getString("level_name"));
            int user_level_int = Integer.parseInt(game_level);
            int leve_int = Integer.parseInt(currentJSONObject.getString("level"));


            if(user_level_int > leve_int){
                btnAnim.setVisibility(View.GONE);
                play_container.setBackgroundTintList(context.getResources().getColorStateList(R.color.orange));
                imageView.setImageResource(R.drawable.ic_baseline_play);
            }else if(user_level_int == leve_int){
                btnAnim.setVisibility(View.VISIBLE);
                new MyAnimation(btnAnim);

                play_container.setBackgroundTintList(context.getResources().getColorStateList(R.color.green));
                imageView.setImageResource(R.drawable.ic_baseline_play);

            }else{
                play_container.setBackgroundTintList(context.getResources().getColorStateList(R.color.dark_grey));
                imageView.setImageResource(R.drawable.ic_baseline_play);
            }



                slideInAnoimation(currentItemView,position);


                currentItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //if(user_level_int >= leve_int){
                        SharedPreferences sharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("current_play_level",String.valueOf(leve_int));
                        editor.apply();


                        Intent intent = new Intent(context,CountDownActivity.class);
                        context.startActivity(intent);
                       // context.finish();



                   // }

                }
            });



        }catch (Exception e){
            e.printStackTrace();
            Log.i("isssue", String.valueOf(e.toString()));

        }
        return currentItemView;
    }

    public  void slideInAnoimation(View currentItemView, long position){
        final AnimationSet set = new AnimationSet(false);
        Animation inFromLeft = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, -1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);

        Animation animation = AnimationUtils.loadAnimation( context, R.anim.slide_in_right);

        //animation.setStartOffset(1000);



        set.addAnimation(inFromLeft);
        // set.addAnimation(fadeIn);
        animation.setDuration(400);


        animation.setStartOffset(200*(position+1));
        currentItemView.startAnimation(animation);
        View finalCurrentItemView = currentItemView;
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                moveRight(finalCurrentItemView,1000,10);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    public  void moveRight(View currentItemView,long duration,float moveX){

        currentItemView.animate()
                .translationX(moveX)
                .translationY(0)
                //.setStartDelay(delay)
                .setInterpolator(new DecelerateInterpolator())
                .setDuration(duration).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                moveLeft(currentItemView,duration,-moveX);

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

    }
    public  void moveLeft(View currentItemView,long duration,float moveX){
        currentItemView.animate()
                .translationX(moveX)
                .translationY(0)
                //.setStartDelay(delay)
                .setInterpolator(new DecelerateInterpolator())
                .setDuration(duration).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                moveRight(currentItemView,duration,moveX);

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }
}
