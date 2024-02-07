package com.digitalDreams.millionaire_game;

import static com.digitalDreams.millionaire_game.Utils.ARABIC_KEY;
import static com.digitalDreams.millionaire_game.Utils.ENGLISH_KEY;
import static com.digitalDreams.millionaire_game.Utils.FRENCH_KEY;
import static com.digitalDreams.millionaire_game.Utils.GERMAN_KEY;
import static com.digitalDreams.millionaire_game.Utils.HINDI_KEY;
import static com.digitalDreams.millionaire_game.Utils.JAPANESE_KEY;
import static com.digitalDreams.millionaire_game.Utils.PORTUGUESE_KEY;
import static com.digitalDreams.millionaire_game.Utils.SPANISH_KEY;
import static com.digitalDreams.millionaire_game.Utils.URDU_KEY;
import static com.digitalDreams.millionaire_game.alpha.Constants.PREF_NAME;
import static com.digitalDreams.millionaire_game.alpha.Constants.getLanguageResource;
import static com.digitalDreams.millionaire_game.alpha.Constants.getLanguageText;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Objects;

class LanguageDialog extends Dialog {

    Context context;

    private LoadingDialog loadingDialog;
    private String gameLevel;

    public LanguageDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.languages_dialog);

        ImageView closeBtn = findViewById(R.id.close);
        LinearLayout englishBtn = findViewById(R.id.english_language);
        LinearLayout frenchBtn = findViewById(R.id.french_language);
        LinearLayout spanishBtn = findViewById(R.id.spanish_language);
        LinearLayout portugueseBtn = findViewById(R.id.portuguese_language);
        LinearLayout arabicBtn = findViewById(R.id.arabic_language);
        LinearLayout hindiBtn = findViewById(R.id.hindi_language);
        LinearLayout urduBtn = findViewById(R.id.urdu_language);
        LinearLayout germanBtn = findViewById(R.id.german_language);
        LinearLayout japaneseBtn = findViewById(R.id.japanese_language);

        SharedPreferences sharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        boolean isEnglishInserted = sharedPreferences.getBoolean(ENGLISH_KEY, false);
        boolean isSpanishInserted = sharedPreferences.getBoolean(SPANISH_KEY, false);
        boolean isFrenchInserted = sharedPreferences.getBoolean(FRENCH_KEY, false);
        boolean isArabicInserted = sharedPreferences.getBoolean(ARABIC_KEY, false);
        boolean isPortugueseInserted = sharedPreferences.getBoolean(PORTUGUESE_KEY, false);
        boolean isHindiInserted = sharedPreferences.getBoolean(HINDI_KEY, false);
        boolean isUrduInserted = sharedPreferences.getBoolean(URDU_KEY, false);
        boolean isGermanInserted = sharedPreferences.getBoolean(GERMAN_KEY, false);
        boolean isJapaneseInserted = sharedPreferences.getBoolean(JAPANESE_KEY, false);
        gameLevel = sharedPreferences.getString("game_level", "1");

        closeBtn.setOnClickListener(view -> dismiss());

        // Define an array of language buttons along with their keys
        LinearLayout[] languageButtons = {
                englishBtn, frenchBtn, spanishBtn,
                arabicBtn, portugueseBtn, hindiBtn,
                urduBtn, germanBtn, japaneseBtn
        };
        String[] languageKeys = {
                ENGLISH_KEY, FRENCH_KEY, SPANISH_KEY,
                ARABIC_KEY, PORTUGUESE_KEY, HINDI_KEY,
                URDU_KEY, GERMAN_KEY, JAPANESE_KEY
        };
        boolean[] isLanguageInserted = {
                isEnglishInserted, isFrenchInserted, isSpanishInserted,
                isArabicInserted, isPortugueseInserted, isHindiInserted,
                isUrduInserted, isGermanInserted, isJapaneseInserted
        };

        for (int i = 0; i < languageButtons.length; i++) {
            LinearLayout button = languageButtons[i];
            String languageKey = languageKeys[i];
            boolean isInserted = isLanguageInserted[i];

            button.setOnClickListener(view -> changeLanguage(languageKey, isInserted));
        }
    }

    private void changeLanguage(String key, boolean isInserted) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        setLocale(unwrap(context), key);

        editor.putString("language", key);

        if (!isInserted) {
            loadQuestions(key);
            editor.putBoolean(key, true);
        } else {
            sendBroadcast();
        }

        editor.apply();
        dismiss();
    }

    private void sendBroadcast() {
        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("refresh"));
    }

    // ToochiDennis
    private void loadQuestions(String languageCode) {
        loadingDialog = new LoadingDialog(unwrap(context));
        loadingDialog.setCancelable(false);
        Objects.requireNonNull(loadingDialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        loadingDialog.show();

        new Thread(() -> {
            try {
                String text = readRawTextFile(getLanguageResource(languageCode));
                parseJSON(text, languageCode);
            } catch (IOException e) {
                e.printStackTrace();
                loadingDialog.dismiss();
                sendBroadcast();
            }

        }).start();
    }


    private String readRawTextFile(int resId) throws IOException {
        InputStream is = context.getResources().openRawResource(resId);
        Writer writer = new StringWriter();
        char[] buffer = new char[10024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            is.close();
        }

        return writer.toString();
    }

    private void parseJSON(String json, String languageCode) {
        int lent = 0;

        try {
            DBHelper dbHelper = new DBHelper(context);

            JSONArray jsonArray = new JSONArray(json);

            for (int a = 0; a < jsonArray.length(); a++) {
                lent++;

                if (lent == jsonArray.length()) {
                    loadingDialog.dismiss();
                    sendBroadcast();
                }

                JSONArray question = jsonArray.getJSONArray(a);
                String id = String.valueOf(question.getInt(0));
                String content = question.getString(1);
                String type = "qo";
                String level = String.valueOf(question.getString(2));
                String language = getLanguageText(context, languageCode);

                String stage_name = "GENERAL";
                // String stage = "1";

                String correct = question.getString(3).trim();
                String reason = question.getString(4).trim();

                JSONArray answers = new JSONArray();

                for (int j = 5; j < question.length(); j++) {
                    JSONObject obj = new JSONObject();
                    obj.put("text", question.getString(j));
                    answers.put(obj);

                }

                String answer = String.valueOf(answers);


                dbHelper.insertDetails(language, level, id, content, type, answer, correct, stage_name, gameLevel, reason);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.i("JsonDetails", String.valueOf(lent));

    }


    public static void setLocale(Activity activity, String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Resources resources = activity.getResources();
        Configuration config = new Configuration();
        config.locale = locale;
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }

    private static Activity unwrap(Context context) {
        while (!(context instanceof Activity) && context instanceof ContextWrapper) {
            context = ((ContextWrapper) context).getBaseContext();
        }

        return (Activity) context;
    }

}
