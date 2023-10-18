package com.digitalDreams.millionaire_game;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.Locale;

import androidx.annotation.NonNull;

class LanguageDialog extends Dialog {
    Context context;
    String languageCode="en";
    public LanguageDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.languages_dialog);

        SharedPreferences sharedPreferences = context.getSharedPreferences("settings",Context.MODE_PRIVATE);
        int endcolor = sharedPreferences.getInt("end_color",0xFF6200EE);
        int startColor = sharedPreferences.getInt("start_color",0xFFBB86FC);
        int cardBackground = sharedPreferences.getInt("card_background",0x03045e);


        ImageView closeBtn = findViewById(R.id.close);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        RelativeLayout englishBtn = findViewById(R.id.english_language);
        RelativeLayout frenchBtn = findViewById(R.id.french_language);
        englishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                languageCode="en";
                setLocale(unwrap(context),languageCode);
                dismiss();
                context.sendBroadcast(new Intent("refresh"));
                SharedPreferences sharedPreferences = context.getSharedPreferences("settings",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor =sharedPreferences.edit();
                editor.putString("language",languageCode);
                editor.apply();
            }
        });
        frenchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                languageCode="fr";
                setLocale(unwrap(context),languageCode);
                context.sendBroadcast(new Intent("refresh"));
                SharedPreferences sharedPreferences = context.getSharedPreferences("settings",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor =sharedPreferences.edit();
                editor.putString("language",languageCode);
                editor.apply();
                dismiss();

            }
        });
    }



    public static void setLocale(Activity activity, String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Resources resources = activity.getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }

    private static Activity unwrap(Context context) {
        while (!(context instanceof Activity) && context instanceof ContextWrapper) {
            context = ((ContextWrapper) context).getBaseContext();
        }

        return (Activity) context;
    }
}
