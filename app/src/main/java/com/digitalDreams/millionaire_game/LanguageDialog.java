package com.digitalDreams.millionaire_game;

import static com.digitalDreams.millionaire_game.Utils.IS_INSERTED_ENGLISH_KEY;
import static com.digitalDreams.millionaire_game.Utils.IS_INSERTED_SPANISH_KEY;

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
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

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

class LanguageDialog extends Dialog {

    Context context;

    String languageCode = "en";

    private LoadingDialog loadingDialog;

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
        RelativeLayout englishBtn = findViewById(R.id.english_language);
        RelativeLayout frenchBtn = findViewById(R.id.french_language);
        RelativeLayout spanishBtn = findViewById(R.id.spanish_language);

        SharedPreferences sharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        int endcolor = sharedPreferences.getInt("end_color", 0xFF6200EE);
        int startColor = sharedPreferences.getInt("start_color", 0xFFBB86FC);
        int cardBackground = sharedPreferences.getInt("card_background", 0x03045e);
        boolean isEnglishInserted = sharedPreferences.getBoolean(IS_INSERTED_ENGLISH_KEY, false);
        boolean isSpanishInserted = sharedPreferences.getBoolean(IS_INSERTED_SPANISH_KEY, false);


        closeBtn.setOnClickListener(view -> dismiss());

        englishBtn.setOnClickListener(view -> {
            languageCode = "en";
            setLocale(unwrap(context), languageCode);

            if (!isEnglishInserted) {
                loadQuestions(languageCode);
                editor.putBoolean(IS_INSERTED_ENGLISH_KEY, true);
            }

            context.sendBroadcast(new Intent("refresh"));
            editor.putString("language", languageCode);
            editor.apply();
            dismiss();
        });

        frenchBtn.setOnClickListener(view -> {
            languageCode = "fr";
            setLocale(unwrap(context), languageCode);
            context.sendBroadcast(new Intent("refresh"));
            editor.putString("language", languageCode);
            editor.apply();
            dismiss();
        });

        spanishBtn.setOnClickListener(view -> {
            languageCode = "es";
            setLocale(unwrap(context), languageCode);

            if (!isSpanishInserted) {
                loadQuestions(languageCode);
                editor.putBoolean(IS_INSERTED_SPANISH_KEY, true);
            }

            context.sendBroadcast(new Intent("refresh"));
            editor.putString("language", languageCode);
            editor.apply();
            dismiss();
        });

    }

    // ToochiDennis
    private void loadQuestions(String languageCode) {
        loadingDialog = new LoadingDialog(unwrap(context));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        loadingDialog.show();

        new Thread(() -> {
            String text;
            try {
                if (languageCode.equals("es")) {
                    text = readRawTextFile(R.raw.millionaire_es);
                } else {
                    text = readRawTextFile(R.raw.millionaire);
                }
                parseJSON(text, languageCode);
            } catch (IOException e) {
                e.printStackTrace();
                loadingDialog.dismiss();
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
        DBHelper dbHelper;
        int lent = 0;

        try {
            dbHelper = new DBHelper(context);

            JSONArray jsonArray = new JSONArray(json);

            for (int a = 0; a < jsonArray.length(); a++) {
                Log.i("index", String.valueOf(a));
                lent++;

                Utils.NUMBER_OF_INSERT++;


                if (lent == jsonArray.length()) {
                    loadingDialog.dismiss();
                }

                JSONArray question = jsonArray.getJSONArray(a);
                String id = String.valueOf(question.getInt(0)); //object.getString("id");
                String content = question.getString(1); //object.getString("content");
                String type = "qo";//object.getString("type");
                String level = String.valueOf(question.getString(2));
                String language;

                switch (languageCode) {
                    case "fr":
                        language = context.getString(R.string.french);
                        break;
                    case "es":
                        language = context.getString(R.string.spanish);
                        break;

                    default:
                        language = context.getString(R.string.english);
                        break;
                }


                String stage_name = "GENERAL";
                String stage = "1";

                String correct = question.getString(3).trim();
                String reason = question.getString(4).trim();

                JSONArray answers = new JSONArray();

                for (int j = 5; j < question.length(); j++) {
                    JSONObject obj = new JSONObject();
                    obj.put("text", question.getString(j));
                    answers.put(obj);

                }

                String answer = String.valueOf(answers);


                dbHelper.insertDetails(language, level, id, content, type, answer, correct, stage_name, stage, reason);
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
