package com.digitalDreams.millionaire_game;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.GradientDrawable;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class WinnersActivity extends AppCompatActivity {
    File imagePath;
    //ImageView shareBTN, backBTN;
    RelativeLayout bg,newGameBtn;
    RelativeLayout imageRoot;
    TextView usernameTXT;
    TextView amountWonTXT;
    String amountWon;
    ImageView imageView;
    LinearLayout share_layout;
    public static int MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winners);

        ImageView raysImg = findViewById(R.id.rays);
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);

        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setDuration(15000);
        rotateAnimation.setRepeatCount(Animation.INFINITE);

        raysImg.startAnimation(rotateAnimation);


        ///////////////PLAYSOUND//////////
        playBackgroundSound();

        //////////////SET AVATAR/////////////////////
        imageView = findViewById(R.id.king);
        //setAvatar(imageView,getAvatar());


        share_layout = findViewById(R.id.share_layout);

        usernameTXT = findViewById(R.id.username);
        amountWonTXT = findViewById(R.id.amount_won);

        newGameBtn = findViewById(R.id.new_game);

        share_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermission();

            }
        });

        newGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GameActivity2.hasOldWinningAmount = true;
//                Intent intent = new Intent(WinnersActivity.this,GameActivity2.class);
//                intent.putExtra("hasOldWinningAmount",true);
//                startActivity(intent);
//                finish();
                GameActivity2.hasOldWinningAmount = true;
                Utils.continueGame(WinnersActivity.this);
            }
        });

        RelativeLayout btnAnim = findViewById(R.id.btn_forAnim);
        new MyAnimation(btnAnim);



        SharedPreferences sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE);
        String languageCode = sharedPreferences.getString("language","en");
        int endcolor = sharedPreferences.getInt("end_color",getResources().getColor(R.color.purple_dark));
        int startColor = sharedPreferences.getInt("start_color",getResources().getColor(R.color.purple_500));
        int cardBackground = sharedPreferences.getInt("card_background",0x219ebc);
        String highscore = sharedPreferences.getString("high_score","0");
        String username = sharedPreferences.getString("username","");
        amountWon = sharedPreferences.getString("amountWon","1000000");
        String game_level = sharedPreferences.getString("game_level","1");

        /////////////Save level//////

        SharedPreferences.Editor editor = sharedPreferences.edit();
        int game_level_int = Integer.parseInt(game_level);

        int  next_level = game_level_int+1;

        editor.putString("game_level",String.valueOf(next_level));
        editor.putString("high_score",amountWon);
        editor.apply();


        ///////////////////////////

       try{
           usernameTXT.setText(username.substring(0,1).toUpperCase()+username.substring(1,username.length()));
           amountWonTXT.setText(Utils.addDollarSign(Utils.addCommaToNumber(Integer.parseInt(amountWon))));
       }catch (Exception e){}
        bg = findViewById(R.id.rootview);
        imageRoot = findViewById(R.id.imageroot);
        new Particles(this,bg,R.layout.image_xml,20);
        GradientDrawable gd = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[] {startColor,endcolor});

        bg.setBackgroundDrawable(gd);

        //share = findViewById(R.id.share);
        //next =  findViewById(R.id.next);
       /// home = findViewById(R.id.home);


       // new NewParticle(this, R.layout.single_star,imageRoot,100,5000);



//        shareBTN = findViewById(R.id.shareBTN);
//        backBTN = findViewById(R.id.backBTN);
//
//        share.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(WinnersActivity.this,PlayDetailsActivity.class);
//                intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
//                intent.putExtra("isWon",true);
//                intent.putExtra("isShowAd",false);
//                startActivity(intent);
//                finish();
//            }
//        });
////
//        home.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(WinnersActivity.this, Dashboard.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//                finish();
//
//            }
//        });
    }

    private void playBackgroundSound(){
        try{
            if(GameActivity2.mWinning_sound!=null) {
                CountDownActivity.mMediaPlayer.stop();

                GameActivity2.mWinning_sound.setAudioStreamType(AudioManager.STREAM_MUSIC);
                GameActivity2.mWinning_sound.setLooping(true);
                GameActivity2.mWinning_sound.start();
            }
        }catch (Exception e){}
    }

    private String setAvatar(ImageView imageView,String avatar){
        //SharedPreferences sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE);
        //String avatar = sharedPreferences.getString("avatar","");
        switch (avatar){
            case "1":
                imageView.setImageResource(R.drawable.avatar1);
                break;
            case "2":
                imageView.setImageResource(R.drawable.avatar2);
                break;
            case "3":
                imageView.setImageResource(R.drawable.avatar3);
                break;
            case "4":
                imageView.setImageResource(R.drawable.avatar4);
                break;
        }
        return avatar;
    }

    private String getAvatar(){
        SharedPreferences sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE);
        String avatar = sharedPreferences.getString("avatar","");
        return avatar;
    }

    public void checkPermission() {
        if (ContextCompat.checkSelfPermission(WinnersActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(WinnersActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                ActivityCompat.requestPermissions(WinnersActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE);
            } else {
                ActivityCompat.requestPermissions(WinnersActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE);
            }
        } else {
            // Permission has already been granted
            Bitmap bitmap = takeScreenshot();
            saveBitmap(bitmap);
            shareIt();
        }
    }

    public void saveBitmap(Bitmap bitmap) {
        imagePath = new File(Environment.getExternalStorageDirectory() + "/screenshotxyz.jpg");
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(imagePath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public Bitmap takeScreenshot() {
        View rootView = findViewById(android.R.id.content).getRootView();
        rootView.setDrawingCacheEnabled(true);
        return rootView.getDrawingCache();
    }

    private void shareIt() {
        Uri uri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", imagePath);
        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
        String url = "https://play.google.com/store/apps/details?id=" + appPackageName;
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        sharingIntent.setType("image/jpeg");
        String shareText = getResources().getString(R.string.ad_copy);
        shareText = shareText.replace("000", amountWon);
        shareText = shareText.replace("111", url);
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareText);
        sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);

        startActivity(Intent.createChooser(sharingIntent, "Share via"));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE) {
            if (resultCode == RESULT_OK) {
                Bitmap bitmap = takeScreenshot();
                saveBitmap(bitmap);
                shareIt();
            } else {
                Toast.makeText(WinnersActivity.this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Bitmap bitmap = takeScreenshot();
                saveBitmap(bitmap);
                shareIt();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(GameActivity2.mWinning_sound!=null) {
            GameActivity2.mWinning_sound.pause();

        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if(GameActivity2.mWinning_sound!=null) {
            GameActivity2.mWinning_sound.start();

        }
    }
}