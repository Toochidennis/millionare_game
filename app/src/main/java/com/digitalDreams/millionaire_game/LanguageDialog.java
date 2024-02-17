package com.digitalDreams.millionaire_game;

import static com.digitalDreams.millionaire_game.Utils.ARABIC_KEY;
import static com.digitalDreams.millionaire_game.Utils.ENGLISH_KEY;
import static com.digitalDreams.millionaire_game.Utils.FRENCH_KEY;
import static com.digitalDreams.millionaire_game.Utils.GERMAN_KEY;
import static com.digitalDreams.millionaire_game.Utils.HINDI_KEY;
import static com.digitalDreams.millionaire_game.Utils.INDONESIAN_KEY;
import static com.digitalDreams.millionaire_game.Utils.JAPANESE_KEY;
import static com.digitalDreams.millionaire_game.Utils.PORTUGUESE_KEY;
import static com.digitalDreams.millionaire_game.Utils.SPANISH_KEY;
import static com.digitalDreams.millionaire_game.Utils.TURKISH_KEY;
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
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.digitalDreams.millionaire_game.alpha.testing.database.AppDatabase;
import com.digitalDreams.millionaire_game.alpha.testing.database.DatabaseProvider;
import com.digitalDreams.millionaire_game.alpha.testing.database.Question;
import com.digitalDreams.millionaire_game.alpha.testing.database.QuestionDao;

import org.json.JSONArray;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.Executors;

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
        LinearLayout indonesianBtn = findViewById(R.id.indonesian_language);
        LinearLayout turkishBtn = findViewById(R.id.turkish_language);

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
        boolean isIndonesianInserted = sharedPreferences.getBoolean(INDONESIAN_KEY, false);
        boolean isTurkishInserted = sharedPreferences.getBoolean(TURKISH_KEY, false);
        gameLevel = sharedPreferences.getString("game_level", "1");

        closeBtn.setOnClickListener(view -> dismiss());

        // Define an array of language buttons along with their keys
        LinearLayout[] languageButtons = {
                englishBtn, frenchBtn, spanishBtn,
                arabicBtn, portugueseBtn, hindiBtn,
                urduBtn, germanBtn, japaneseBtn,
                indonesianBtn, turkishBtn
        };
        String[] languageKeys = {
                ENGLISH_KEY, FRENCH_KEY, SPANISH_KEY,
                ARABIC_KEY, PORTUGUESE_KEY, HINDI_KEY,
                URDU_KEY, GERMAN_KEY, JAPANESE_KEY,
                INDONESIAN_KEY, TURKISH_KEY
        };
        boolean[] isLanguageInserted = {
                isEnglishInserted, isFrenchInserted, isSpanishInserted,
                isArabicInserted, isPortugueseInserted, isHindiInserted,
                isUrduInserted, isGermanInserted, isJapaneseInserted,
                isIndonesianInserted, isTurkishInserted
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
        Log.d("response", key);

        editor.putString("language", key);

        if (!isInserted) {
            insertQuestions(key);
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
    private void insertQuestions(String languageCode) {
        loadingDialog = new LoadingDialog(unwrap(context));
        loadingDialog.setCancelable(false);
        Objects.requireNonNull(loadingDialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        loadingDialog.show();

        Executors.newSingleThreadExecutor().execute(() -> {
            initializeDatabase(getLanguageResource(languageCode), languageCode);
        });
    }

    private void initializeDatabase(int resId, String languageCode) {
        // Read the JSON file from the raw resources
        InputStream inputStream = context.getResources().openRawResource(resId);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1250];
        String json;
        try {
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                json = outputStream.toString(StandardCharsets.UTF_8);
            } else {
                json = new String(outputStream.toByteArray(), StandardCharsets.UTF_8);
            }

            // Parse JSON data into a list of Question objects
            List<Question> questions = parseJsonToQuestions(json, languageCode);

            // Insert questions into the database
            AppDatabase database = DatabaseProvider.getInstance(unwrap(context));
            QuestionDao questionDao = database.questionDao();
            questionDao.insertQuestion(questions);

            loadingDialog.dismiss();
            sendBroadcast();
        } catch (Exception e) {
            e.printStackTrace();
            loadingDialog.dismiss();
            sendBroadcast();
        } finally {
            try {
                inputStream.close();
            } catch (IOException ex) {
                ex.printStackTrace();
                loadingDialog.dismiss();
                sendBroadcast();
            }
        }
    }

    private List<Question> parseJsonToQuestions(String json, String languageCode) {
        List<Question> questions = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int a = 0; a < jsonArray.length(); a++) {

                JSONArray questionArray = jsonArray.getJSONArray(a);
                String id = String.valueOf(questionArray.getInt(0));
                String questionTitle = questionArray.getString(1);
                String level = String.valueOf(questionArray.getString(2));
                String correctAnswer = questionArray.getString(3).trim();
                String reason = questionArray.getString(4).trim();
                String optionA = questionArray.getString(5);
                String optionB = questionArray.getString(6);
                String optionC = questionArray.getString(7);
                String optionD = questionArray.getString(8);
                String language = getLanguageText(context, languageCode);
                String stageName = "GENERAL";

                Question question = new Question(id,
                        capitaliseFirstLetter(questionTitle),
                        capitaliseFirstLetter(correctAnswer),
                        new Question.Options(
                                capitaliseFirstLetter(optionA),
                                capitaliseFirstLetter(optionB),
                                capitaliseFirstLetter(optionC),
                                capitaliseFirstLetter(optionD)
                        ),
                        reason, stageName, gameLevel, level, language
                );

                questions.add(question);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return questions;
    }

    private String capitaliseFirstLetter(String word) {
        if (word == null || word.isEmpty()) {
            return word;
        }
        return Character.toUpperCase(word.charAt(0)) + word.substring(1);
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
