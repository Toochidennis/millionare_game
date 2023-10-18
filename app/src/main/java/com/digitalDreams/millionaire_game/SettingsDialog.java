package com.digitalDreams.millionaire_game;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

class SettingsDialog extends Dialog {
    private Context context;
    public static TextView soundTxt,languageTxt,themeTxt,gameModeTxt,vibrationTxt;
    public static ImageView languageFlag;
    public static CardView rootView;
    public static CardView languageBtn,selectThemeBtn,soundBtn,gameModeBtn,vibrationBtn;
    public SettingsDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.settings_dialog);
        soundTxt = findViewById(R.id.sound);
        languageTxt = findViewById(R.id.language);
        themeTxt = findViewById(R.id.theme);
        gameModeTxt = findViewById(R.id.game_mode);
        vibrationTxt = findViewById(R.id.vibration);
        languageFlag = findViewById(R.id.lang_flag);
        rootView = findViewById(R.id.rootview);
        soundBtn = findViewById(R.id.sound_);
        vibrationBtn = findViewById(R.id.vibration_);
        gameModeBtn = findViewById(R.id.game_mode_);
        languageBtn = findViewById(R.id.change_language);
        selectThemeBtn =findViewById(R.id.select_theme);
        SharedPreferences sharedPreferences = context.getSharedPreferences("settings",Context.MODE_PRIVATE);
        int endcolor = sharedPreferences.getInt("end_color",0xFF6200EE);
        int startColor = sharedPreferences.getInt("start_color",0xFFBB86FC);        int cardBackground = sharedPreferences.getInt("card_background",0x03045e);
        soundBtn.setCardBackgroundColor(cardBackground);
        languageBtn.setCardBackgroundColor(cardBackground);
        vibrationBtn.setCardBackgroundColor(cardBackground);
        gameModeBtn.setCardBackgroundColor(cardBackground);
        selectThemeBtn.setCardBackgroundColor(cardBackground);
        rootView.setCardBackgroundColor(endcolor);
        new Dashboard().setLangauge(context);
        languageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LanguageDialog dialog = new LanguageDialog(getContext());
                dialog.show();
                Window window = dialog.getWindow();
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
        });
        ImageView closeBtn = findViewById(R.id.close);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        selectThemeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ThemeDialog dialog = new ThemeDialog(context);
                dialog.show();
                Window window = dialog.getWindow();
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
        });
        String mode = sharedPreferences.getString("game_mode",context.getResources().getString(R.string.simple));

        TextView modeText = findViewById(R.id.mode_setting);
        modeText.setText(mode);
        gameModeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mode = sharedPreferences.getString("game_mode",context.getResources().getString(R.string.simple));
                SharedPreferences.Editor editor = sharedPreferences.edit();

                if(mode.equals("simple")){
                    editor.putString("game_mode","timed");
                }else if(mode.equals("timed")){
                    editor.putString("game_mode","simple");

                }
                editor.apply();
                mode = sharedPreferences.getString("game_mode",context.getResources().getString(R.string.simple));
                modeText.setText(mode);
            }
        });
        String sound = sharedPreferences.getString("sound","on");

        TextView soundModeTxt = findViewById(R.id.sound);
        soundModeTxt.setText("Sound "+sound);
        ImageView soundIcon = findViewById(R.id.sound_img);
        if(sound.equals("off")){
            soundIcon.setImageResource(R.drawable.ic_baseline_volume_off_24);
        }else{
            soundIcon.setImageResource(R.drawable.ic_baseline_volume_up_24);
        }
        soundBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sound = sharedPreferences.getString("sound","on");
                SharedPreferences.Editor editor = sharedPreferences.edit();
                if((sound.equals("on"))){
                    editor.putString("sound","off");
                }else if(sound.equals("off")){
                    editor.putString("sound","on");
                }
                editor.apply();
                sound = sharedPreferences.getString("sound","on");
                soundModeTxt.setText("Sound "+sound);
                if(sound.equals("off")){
                    soundIcon.setImageResource(R.drawable.ic_baseline_volume_off_24);
                }else{
                    soundIcon.setImageResource(R.drawable.ic_baseline_volume_up_24);
                }

            }
        });

        ImageView vibrationIcon = findViewById(R.id.vibration_icon);
        TextView vibrationTxt = findViewById(R.id.vibration);
        vibrationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String vibrate = sharedPreferences.getString("vibrate","on");
                SharedPreferences.Editor editor = sharedPreferences.edit();
                if((vibrate.equals("on"))){
                    editor.putString("vibrate","off");
                }else if(vibrate.equals("off")){
                    editor.putString("vibrate","on");
                }
                editor.apply();
                vibrate = sharedPreferences.getString("vibrate","on");
                soundModeTxt.setText("Vibration "+vibrate);
                if(vibrate.equals("off")){
                    ImageView badIcon = findViewById(R.id.bad);
                    badIcon.setVisibility(View.VISIBLE);
                    vibrationTxt.setText("Vibration "+vibrate);
                }else{
                    ImageView badIcon = findViewById(R.id.bad);
                    badIcon.setVisibility(View.GONE);
                    vibrationTxt.setText("Vibration "+vibrate);
                }
            }
        });
    }


}
