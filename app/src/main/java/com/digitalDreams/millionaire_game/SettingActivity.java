package com.digitalDreams.millionaire_game;

import static com.digitalDreams.millionaire_game.alpha.Constants.setLocale;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.digitalDreams.millionaire_game.alpha.AudioManager;

public class SettingActivity extends AppCompatActivity {

    public RelativeLayout bg;
    public TextView themeNameTxt;
    public ImageView flagImg;
    public TextView languageTxt;
    public TextView soundTxt, themeTxt, gameModeTxt, vibrationTxt, creditTxt, language, soundModeTxt, settingsTxt;
    public TextView modeText;
    public ImageView badIcon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLocale(this);
        setContentView(R.layout.activity_setting);

        SharedPreferences sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE);
        int endColor = sharedPreferences.getInt("end_color", getResources().getColor(R.color.purple_dark));
        int startColor = sharedPreferences.getInt("start_color", getResources().getColor(R.color.purple_500));
        String theme = sharedPreferences.getString("theme", getResources().getString(R.string.default_theme));
        //setTheme();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE);

        bg = findViewById(R.id.rootview);
        new Particles(this, bg, R.layout.image_xml, 20);
        GradientDrawable gradientDrawable = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{startColor, endColor});

        bg.setBackground(gradientDrawable);

        RelativeLayout closeBtn = findViewById(R.id.close_container);

        closeBtn.setOnClickListener(view -> {
            AudioManager.darkBlueBlink(this, closeBtn);
            onBackPressed();
        });

        RelativeLayout soundBtn = findViewById(R.id.sound);
        RelativeLayout languageBtn = findViewById(R.id.language);
        RelativeLayout themeBtn = findViewById(R.id.select_theme);
        RelativeLayout gameModeBtn = findViewById(R.id.game_mode);
        RelativeLayout vibrationBtn = findViewById(R.id.vibration);
        RelativeLayout creditBtn = findViewById(R.id.credits);
        flagImg = findViewById(R.id.flag);
        languageTxt = findViewById(R.id.language_value);
        themeNameTxt = findViewById(R.id.theme_name);
        themeNameTxt.setText(theme);
        soundTxt = findViewById(R.id.sound_txt);
        themeTxt = findViewById(R.id.select_theme_txt);
        gameModeTxt = findViewById(R.id.game_mode_txt);
        vibrationTxt = findViewById(R.id.vibration_txt);
        creditTxt = findViewById(R.id.credit_txt);
        language = findViewById(R.id.language_txt);
        settingsTxt = findViewById(R.id.settingsTextView);

        String sound = sharedPreferences.getString("sound", "1");

        languageTxt.setText(getString(R.string.language_));
        flagImg.setImageResource(R.drawable.country_flag);

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

        soundBtn.setOnClickListener(view -> {
            AudioManager.darkBlueBlink(this, soundBtn);
            String sound1 = sharedPreferences.getString("sound", "1");
            SharedPreferences.Editor editor = sharedPreferences.edit();
            if ((sound1.equals("1"))) {
                editor.putString("sound", "0");
            } else if (sound1.equals("0")) {
                editor.putString("sound", "1");
            }
            editor.apply();
            sound1 = sharedPreferences.getString("sound", "1");

            if (sound1.equalsIgnoreCase("1")) {
                soundModeTxt.setText(getResources().getString(R.string.on));
            } else {
                soundModeTxt.setText(getResources().getString(R.string.off));

            }
            if (sound1.equals("0")) {
                soundIcon.setImageResource(R.drawable.ic_baseline_volume_off_24);
            } else {
                soundIcon.setImageResource(R.drawable.ic_baseline_volume_up_24);
            }
        });

        languageBtn.setOnClickListener(view -> {
            AudioManager.darkBlueBlink(this, languageBtn);
            LanguageDialog dialog = new LanguageDialog(SettingActivity.this);
            dialog.show();
            Window window = dialog.getWindow();
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        });

        switch (theme) {
            case "0" -> themeNameTxt.setText(getResources().getString(R.string.default_theme));
            case "1" -> themeNameTxt.setText(getResources().getString(R.string.theme_1));
            case "2" -> themeNameTxt.setText(getResources().getString(R.string.theme_2));
            case "3" -> themeNameTxt.setText(getResources().getString(R.string.theme_3));
        }

        themeBtn.setOnClickListener(view -> {
            AudioManager.darkBlueBlink(this, themeBtn);
            ThemeDialog dialog = new ThemeDialog(SettingActivity.this);
            dialog.show();
            Window window = dialog.getWindow();
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        });

        String mode = sharedPreferences.getString("game_mode", "0");
        modeText = findViewById(R.id.game_mode_value);

        if (mode.equals("0")) {
            modeText.setText(getResources().getString(R.string.simple));
        } else {
            modeText.setText(getResources().getString(R.string.timed));
        }

        gameModeBtn.setOnClickListener(view -> {
            AudioManager.darkBlueBlink(this, gameModeBtn);
            String mode1 = sharedPreferences.getString("game_mode", "0");
            SharedPreferences.Editor editor = sharedPreferences.edit();

            if (mode1.equals("0")) {
                editor.putString("game_mode", "1");
            } else if (mode1.equals("1")) {
                editor.putString("game_mode", "0");
            }
            editor.apply();
            mode1 = sharedPreferences.getString("game_mode", "0");

            if (mode1.equals("0")) {
                modeText.setText(getResources().getString(R.string.simple));
            } else {
                modeText.setText(getResources().getString(R.string.timed));
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

        vibrationBtn.setOnClickListener(view -> {
            AudioManager.darkBlueBlink(this, vibrationBtn);
            String vibrate1 = sharedPreferences.getString("vibrate", "1");
            SharedPreferences.Editor editor = sharedPreferences.edit();

            if ((vibrate1.equals("1"))) {
                editor.putString("vibrate", "0");
            } else if (vibrate1.equals("0")) {
                editor.putString("vibrate", "1");
            }
            editor.apply();

            vibrate1 = sharedPreferences.getString("vibrate", "1");
            ImageView badIcon = findViewById(R.id.bad);
            if (vibrate1.equals("0")) {
                badIcon.setVisibility(View.VISIBLE);
                vibrationTxt.setText(getResources().getString(R.string.off));
            } else {
                badIcon.setVisibility(View.GONE);
                vibrationTxt.setText(getResources().getString(R.string.on));
            }
        });

        creditBtn.setOnClickListener(view -> {
            AudioManager.darkBlueBlink(this, creditBtn);
            CreditDialog dialog = new CreditDialog(SettingActivity.this);
            dialog.show();
            Window window = dialog.getWindow();
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        });

        TextView versionTxt = findViewById(R.id.version);
        String version = "";

        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        String versionText = "v" + version;
        versionTxt.setText(versionText);
    }


    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            recreate();

            SharedPreferences sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE);
            int endColor = sharedPreferences.getInt("end_color", 0x230253);
            int startColor = sharedPreferences.getInt("start_color", 0xFF6200EE);
            int cardBackground = sharedPreferences.getInt("card_background", 0x03045e);

            if (SettingsDialog.soundTxt != null & SettingsDialog.soundBtn != null) {
                SettingsDialog.soundTxt.setText(getResources().getString(R.string.sound_on));
                SettingsDialog.soundBtn.setCardBackgroundColor(cardBackground);
            }
            if (SettingsDialog.gameModeTxt != null && SettingsDialog.gameModeBtn != null) {
                SettingsDialog.gameModeTxt.setText(getResources().getString(R.string.game_mode));
                SettingsDialog.gameModeBtn.setCardBackgroundColor(cardBackground);

            }
            if (SettingsDialog.themeTxt != null && SettingsDialog.selectThemeBtn != null) {
                SettingsDialog.themeTxt.setText(getResources().getString(R.string.select_a_theme));
                SettingsDialog.selectThemeBtn.setCardBackgroundColor(cardBackground);

            }
            if (SettingsDialog.vibrationTxt != null && SettingsDialog.vibrationBtn != null) {
                SettingsDialog.vibrationTxt.setText(getResources().getString(R.string.taptic));
                SettingsDialog.vibrationBtn.setCardBackgroundColor(cardBackground);
            }


            GradientDrawable gradientDrawable = new GradientDrawable(
                    GradientDrawable.Orientation.TOP_BOTTOM,
                    new int[]{startColor, endColor});

            bg.setBackground(gradientDrawable);

            if (bg != null) {
                String theme = sharedPreferences.getString("theme", "0");
                bg.setBackground(gradientDrawable);
                switch (theme) {
                    case "0" ->
                            themeNameTxt.setText(getResources().getString(R.string.default_theme));
                    case "1" -> themeNameTxt.setText(getResources().getString(R.string.theme_1));
                    case "2" -> themeNameTxt.setText(getResources().getString(R.string.theme_2));
                    case "3" -> themeNameTxt.setText(getResources().getString(R.string.theme_3));
                }
            }

            soundTxt.setText(getResources().getString(R.string.sound_on));
            themeTxt.setText(getResources().getString(R.string.select_a_theme));
            vibrationTxt.setText(getResources().getString(R.string.taptic));
            gameModeTxt.setText(getResources().getString(R.string.game_mode));
            creditTxt.setText(getResources().getString(R.string.credits));
            language.setText(getResources().getString(R.string.language));
            settingsTxt.setText(getResources().getString(R.string.settings));


            String sound = sharedPreferences.getString("sound", "1");

            if (sound.equalsIgnoreCase("1")) {
                soundModeTxt.setText(getResources().getString(R.string.on));
            } else {
                soundModeTxt.setText(getResources().getString(R.string.off));
            }

            String mode = sharedPreferences.getString("game_mode", "0");
            if (mode.equals("0")) {
                modeText.setText(getResources().getString(R.string.simple));
            } else {
                modeText.setText(getResources().getString(R.string.timed));
            }
            String vibrate = sharedPreferences.getString("vibrate", "1");
            if (vibrate.equals("0")) {
                badIcon.setVisibility(View.VISIBLE);
                vibrationTxt.setText(getResources().getString(R.string.off));
            } else {
                badIcon.setVisibility(View.GONE);
                vibrationTxt.setText(getResources().getString(R.string.on));
            }
        }
    };


    @Override
    protected void onResume() {
        super.onResume();

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter("refresh"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AudioManager.releaseMusicResources();
    }
}