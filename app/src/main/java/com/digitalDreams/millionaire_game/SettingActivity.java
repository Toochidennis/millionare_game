package com.digitalDreams.millionaire_game;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SettingActivity extends AppCompatActivity {
    public static RelativeLayout bg;
    public static TextView themeNameTxt;
    public static ImageView flagImg;
    public static TextView languageTxt;
    public static TextView soundTxt, themeTxt, gameModeTxt, vibrationTxt, crediTxt, language, soundModeTxt;
    public static TextView modeText;
    public static ImageView badIcon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        SharedPreferences sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE);
        String languageCode = sharedPreferences.getString("language", "en");
        int endcolor = sharedPreferences.getInt("end_color", getResources().getColor(R.color.purple_dark));
        int startColor = sharedPreferences.getInt("start_color", getResources().getColor(R.color.purple_500));
        int cardBackground = sharedPreferences.getInt("card_background", 0x219ebc);
        String theme = sharedPreferences.getString("theme", "Default");
        //setTheme();


        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE);

        bg = findViewById(R.id.rootview);
        new Particles(this, bg, R.layout.image_xml, 20);
        GradientDrawable gd = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{startColor, endcolor});

        bg.setBackgroundDrawable(gd);

        RelativeLayout closeBtn = findViewById(R.id.close_container);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.darkBlueBlink(closeBtn, getApplicationContext());

                onBackPressed();
            }
        });

        RelativeLayout soundBtn = findViewById(R.id.sound);
        RelativeLayout languageBtn = findViewById(R.id.language);
        RelativeLayout themeBtn = findViewById(R.id.select_theme);
        RelativeLayout gameModeBtn = findViewById(R.id.game_mode);
        RelativeLayout vibrationBtn = findViewById(R.id.vibration);
        RelativeLayout creditBtn = findViewById(R.id.credits);
        flagImg = findViewById(R.id.flag);
        languageTxt = findViewById(R.id.language_value);
        new Dashboard().setLanguage(SettingActivity.this);
        themeNameTxt = findViewById(R.id.theme_name);
        themeNameTxt.setText(theme);
        soundTxt = findViewById(R.id.sound_txt);
        themeTxt = findViewById(R.id.select_theme_txt);
        gameModeTxt = findViewById(R.id.game_mode_txt);
        vibrationTxt = findViewById(R.id.vibration_txt);
        crediTxt = findViewById(R.id.credit_txt);
        language = findViewById(R.id.language_txt);

        String sound = sharedPreferences.getString("sound", "1");

        soundModeTxt = findViewById(R.id.sound_value);
        if (sound.equalsIgnoreCase("1")) {
            soundModeTxt.setText(getResources().getString(R.string.on));
        } else {
            soundModeTxt.setText(getResources().getString(R.string.off));

        }
        ImageView soundIcon = findViewById(R.id.sound_img);
        if (sound.equals("0")) {
            soundIcon.setImageResource(R.drawable.ic_baseline_volume_off_24);
        } else {
            soundIcon.setImageResource(R.drawable.ic_baseline_volume_up_24);
        }
        soundBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.darkBlueBlink(soundBtn, getApplicationContext());
                String sound = sharedPreferences.getString("sound", "1");
                SharedPreferences.Editor editor = sharedPreferences.edit();
                if ((sound.equals("1"))) {
                    editor.putString("sound", "0");
                } else if (sound.equals("0")) {
                    editor.putString("sound", "1");
                }
                editor.apply();
                sound = sharedPreferences.getString("sound", "1");
                if (sound.equalsIgnoreCase("1")) {
                    soundModeTxt.setText(getResources().getString(R.string.on));
                } else {
                    soundModeTxt.setText(getResources().getString(R.string.off));

                }
                if (sound.equals("0")) {
                    soundIcon.setImageResource(R.drawable.ic_baseline_volume_off_24);
                } else {
                    soundIcon.setImageResource(R.drawable.ic_baseline_volume_up_24);
                }
            }
        });
        languageBtn.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                Utils.darkBlueBlink(languageBtn, getApplicationContext());
                LanguageDialog dialog = new LanguageDialog(SettingActivity.this);
                dialog.show();
                Window window = dialog.getWindow();
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
        });

        if (theme.equals("0")) {
            SettingActivity.themeNameTxt.setText(getResources().getString(R.string.default_theme));
        } else if (theme.equals("1")) {
            SettingActivity.themeNameTxt.setText(getResources().getString(R.string.theme_1));

        } else if (theme.equals("2")) {
            SettingActivity.themeNameTxt.setText(getResources().getString(R.string.theme_2));
        } else if (theme.equals("3")) {
            SettingActivity.themeNameTxt.setText(getResources().getString(R.string.theme_3));
        }


        themeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.darkBlueBlink(themeBtn, getApplicationContext());
                ThemeDialog dialog = new ThemeDialog(SettingActivity.this);
                dialog.show();
                Window window = dialog.getWindow();
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
        });
        String mode = sharedPreferences.getString("game_mode", "0");
        modeText = findViewById(R.id.game_mode_value);
        if (mode.equals("0")) {
            modeText.setText(getResources().getString(R.string.simple));
        } else {
            modeText.setText(getResources().getString(R.string.timed));
        }
        gameModeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.darkBlueBlink(gameModeBtn, getApplicationContext());
                String mode = sharedPreferences.getString("game_mode", "0");
                SharedPreferences.Editor editor = sharedPreferences.edit();
                if (mode.equals("0")) {
                    editor.putString("game_mode", "1");
                } else if (mode.equals("1")) {
                    editor.putString("game_mode", "0");

                }
                editor.apply();
                mode = sharedPreferences.getString("game_mode", "0");
                if (mode.equals("0")) {
                    modeText.setText(getResources().getString(R.string.simple));
                } else {
                    modeText.setText(getResources().getString(R.string.timed));
                }

            }
        });
        vibrationTxt = findViewById(R.id.vibration_value);
        String vibrate = sharedPreferences.getString("vibrate", "1");
        badIcon = findViewById(R.id.bad);
        if (vibrate.equals("0")) {
            badIcon.setVisibility(View.VISIBLE);
            vibrationTxt.setText(getResources().getString(R.string.off));
        } else {
            badIcon.setVisibility(View.GONE);
            vibrationTxt.setText(getResources().getString(R.string.on));
        }

        vibrationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.darkBlueBlink(vibrationBtn, getApplicationContext());
                String vibrate = sharedPreferences.getString("vibrate", "1");
                SharedPreferences.Editor editor = sharedPreferences.edit();
                if ((vibrate.equals("1"))) {
                    editor.putString("vibrate", "0");
                } else if (vibrate.equals("0")) {
                    editor.putString("vibrate", "1");
                }
                editor.apply();
                vibrate = sharedPreferences.getString("vibrate", "1");
                if (vibrate.equals("0")) {
                    ImageView badIcon = findViewById(R.id.bad);
                    badIcon.setVisibility(View.VISIBLE);
                    vibrationTxt.setText(getResources().getString(R.string.off));
                } else {
                    ImageView badIcon = findViewById(R.id.bad);
                    badIcon.setVisibility(View.GONE);
                    vibrationTxt.setText(getResources().getString(R.string.on));
                }
            }
        });
        creditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.darkBlueBlink(creditBtn, getApplicationContext());
                CreditDialog dialog = new CreditDialog(SettingActivity.this);
                dialog.show();
                Window window = dialog.getWindow();
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
        });

        TextView versionTxt = findViewById(R.id.version);
        String version = "";
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        versionTxt.setText("v" + version);
    }
}