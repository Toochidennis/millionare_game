package com.digitalDreams.millionaire_game;

import static com.digitalDreams.millionaire_game.alpha.Constants.PREF_NAME;
import static com.digitalDreams.millionaire_game.alpha.Constants.getLanguageResource;

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
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.digitalDreams.millionaire_game.alpha.testing.Language;
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
    //  private String gameLevel;

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
        LinearLayout chineseBtn = findViewById(R.id.chinese_language);
        LinearLayout koreanBtn = findViewById(R.id.korean_language);
        LinearLayout malayBtn = findViewById(R.id.malay_language);
        LinearLayout thaiBtn = findViewById(R.id.thai_language);
        LinearLayout vietnameseBtn = findViewById(R.id.vietnamese_language);


        SharedPreferences sharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);

        closeBtn.setOnClickListener(view -> dismiss());

        // Define an array of language buttons along with their keys
        LinearLayout[] languageButtons = {
                englishBtn, frenchBtn, spanishBtn, arabicBtn, portugueseBtn, hindiBtn, urduBtn,
                germanBtn, japaneseBtn, indonesianBtn, turkishBtn, chineseBtn, malayBtn, thaiBtn,
                vietnameseBtn, koreanBtn
        };

        String[] languageKeys = {
                Language.ENGLISH.getCode(), Language.FRENCH.getCode(), Language.SPANISH.getCode(),
                Language.ARABIC.getCode(), Language.PORTUGUESE.getCode(), Language.HINDI.getCode(),
                Language.URDU.getCode(), Language.GERMAN.getCode(), Language.JAPANESE.getCode(),
                Language.INDONESIA.getCode(), Language.TURKISH.getCode(), Language.CHINESE.getCode(),
                Language.MALAY.getCode(), Language.THAI.getCode(), Language.VIETNAMESE.getCode(),
                Language.KOREAN.getCode()
        };

        for (int i = 0; i < languageButtons.length; i++) {
            LinearLayout button = languageButtons[i];
            String languageKey = languageKeys[i];
            boolean isLanguageInserted = sharedPreferences.getBoolean(languageKeys[i], false);

            button.setOnClickListener(view -> changeLanguage(languageKey, isLanguageInserted));
        }
    }

    private void changeLanguage(String key, boolean isInserted) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        setLocale(unwrap(context), key);

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

        Executors.newSingleThreadExecutor().execute(() ->
                initializeDatabase(getLanguageResource(languageCode))
        );
    }

    private void initializeDatabase(int resId) {
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
            List<Question> questions = parseJsonToQuestions(json);

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

    private List<Question> parseJsonToQuestions(String json) {
        List<Question> questions = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int a = 0; a < jsonArray.length(); a++) {
                JSONArray questionArray = jsonArray.getJSONArray(a);
                if (questionArray.length() >= 9) { // Ensure the array has at least 9 elements
                    String id = String.valueOf(questionArray.optInt(0)); // Use optInt to handle null values
                    String questionTitle = questionArray.optString(1, ""); // Provide default value for empty strings
                    String level = String.valueOf(questionArray.optInt(2));
                    String correctAnswer = questionArray.optString(3, "").trim();
                    String reason = questionArray.optString(4, "").trim();
                    String optionA = questionArray.optString(5, "");
                    String optionB = questionArray.optString(6, "");
                    String optionC = questionArray.optString(7, "");
                    String optionD = questionArray.optString(8, "");
                    String language = context.getString(R.string.language_);

                    Question question = new Question(id,
                            capitaliseFirstLetter(questionTitle),
                            capitaliseFirstLetter(correctAnswer),
                            new Question.Options(
                                    capitaliseFirstLetter(optionA),
                                    capitaliseFirstLetter(optionB),
                                    capitaliseFirstLetter(optionC),
                                    capitaliseFirstLetter(optionD)
                            ),
                            reason, "GENERAL", "1", level, language
                    );

                    questions.add(question);
                }
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