package com.digitalDreams.millionaire_game;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

class ThemeDialog extends Dialog {
    Context context;
    public ThemeDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.theme_layout_dialog);


        SharedPreferences sharedPreferences = context.getSharedPreferences("settings",Context.MODE_PRIVATE);
        int endcolor = sharedPreferences.getInt("end_color",0xFF6200EE);
        int startColor = sharedPreferences.getInt("start_color",0xFFBB86FC);        int cardBackground = sharedPreferences.getInt("card_background",0x03045e);
        String theme = sharedPreferences.getString("theme","Default");
        RelativeLayout rootview = findViewById(R.id.rootview);
        //rootview.setBackgroundColor(endcolor);
        ImageView closeBtn = findViewById(R.id.close);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();

            }
        });

        ImageView theme1 = findViewById(R.id.theme_1);
        ImageView theme2 = findViewById(R.id.theme_2);
        ImageView theme3 = findViewById(R.id.theme_3);
        ImageView defaultTheme = findViewById(R.id.default_theme);

        defaultTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = context.getSharedPreferences("settings",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor =sharedPreferences.edit();
                editor.putInt("end_color",getContext().getResources().getColor(R.color.purple_dark));
                editor.putInt("start_color",getContext().getResources().getColor(R.color.purple_500));
                editor.putInt("card_background",getContext().getResources().getColor(R.color.spearmint));
                editor.putString("theme","0");
                editor.apply();
                context.sendBroadcast(new Intent("refresh"));
                dismiss();
            }
        });

        theme1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = context.getSharedPreferences("settings",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor =sharedPreferences.edit();
                editor.putInt("end_color",getContext().getResources().getColor(R.color.hot_pink_dark));
                editor.putInt("start_color",getContext().getResources().getColor(R.color.hot_pink));
                editor.putInt("card_background",getContext().getResources().getColor(R.color.spearmint));
                editor.putString("theme","1");
                editor.apply();
                context.sendBroadcast(new Intent("refresh"));
                dismiss();
            }
        });
        theme2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = context.getSharedPreferences("settings",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor =sharedPreferences.edit();
                editor.putInt("end_color",getContext().getResources().getColor(R.color.dark_blue_dark));
                editor.putInt("start_color",getContext().getResources().getColor(R.color.dark_blue));
                editor.putInt("card_background",getContext().getResources().getColor(R.color.baby_blue));
                editor.putString("theme","2");
                editor.apply();
                dismiss();
                context.sendBroadcast(new Intent("refresh"));
            }
        });
        theme3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = context.getSharedPreferences("settings",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor =sharedPreferences.edit();
                editor.putInt("end_color",getContext().getResources().getColor(R.color.blue_gray));
                editor.putInt("start_color",getContext().getResources().getColor(R.color.blue_gray));
                editor.putInt("card_background",getContext().getResources().getColor(R.color.rose_quartz));
                editor.putString("theme","3");
                editor.apply();
                dismiss();
                context.sendBroadcast(new Intent("refresh"));
            }
        });
    }
}
