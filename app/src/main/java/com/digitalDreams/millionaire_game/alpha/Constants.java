package com.digitalDreams.millionaire_game.alpha;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.digitalDreams.millionaire_game.R;
import com.digitalDreams.millionaire_game.alpha.testing.Language;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class Constants {

    // Delay intervals for different time durations
    public static long DELAY_INTERVAL_LONG = 1000;
    public static long DELAY_INTERVAL_MEDIUM = 500L;
    public static long DELAY_INTERVAL_SHORT = 200L;

    // Strings for game outcomes
    public static String FAILED = "Failed";
    public static String PASSED = "Passed";

    // Integer values for colors
    public static int RED = 1;
    public static int GREEN = 2;
    public static int ORANGE = 0;

    // Keys for SharedPreferences
    public static String SHOULD_CONTINUE_GAME = "shouldContinueGame";
    public static String APPLICATION_DATA = "application_data";
    public static String PREF_NAME = "settings";
    public static String SHOULD_REFRESH_QUESTION = "Failure activity";
    public static String FROM_PROGRESS = "From progress";
    public static String SOUND = "Sound";

    // Suffixes and format for currency
    private static final char[] SUFFIX = {' ', 'k', 'M', 'B', 'T', 'P', 'E'};
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#0");

    private static final double[] POWERS_OF_TEN = {1e0, 1e3, 1e6, 1e9, 1e12, 1e15};

    // Maps for language, country, and texts
    private static final Map<String, Integer> languageResourceMap = new HashMap<>();
    private static final Map<String, Integer> countryResourceMap = new HashMap<>();

    // Static initialization block for maps
    static {
        languageResourceMap.put(Language.ENGLISH.getCode(), R.raw.millionaire_english);
        languageResourceMap.put(Language.ARABIC.getCode(), R.raw.millionaire_arabic);
        languageResourceMap.put(Language.FRENCH.getCode(), R.raw.millionaire_french);
        languageResourceMap.put(Language.SPANISH.getCode(), R.raw.millionaire_spanish);
        languageResourceMap.put(Language.HINDI.getCode(), R.raw.millionaire_hindi);
        languageResourceMap.put(Language.PORTUGUESE.getCode(), R.raw.millionaire_portugal);
        languageResourceMap.put(Language.URDU.getCode(), R.raw.millionaire_urdu);
        languageResourceMap.put(Language.JAPANESE.getCode(), R.raw.millionaire_japanese);
        languageResourceMap.put(Language.GERMAN.getCode(), R.raw.millionaire_german);
        languageResourceMap.put(Language.INDONESIA.getCode(), R.raw.millionaire_indonesian);
        languageResourceMap.put(Language.TURKISH.getCode(), R.raw.millionaire_turkish);
        languageResourceMap.put(Language.CHINESE.getCode(), R.raw.millionaire_chinese);
        languageResourceMap.put(Language.MALAY.getCode(), R.raw.millionaire_malay);
        languageResourceMap.put(Language.KOREAN.getCode(), R.raw.millionaire_korean);
        languageResourceMap.put(Language.VIETNAMESE.getCode(), R.raw.millionaire_vietnamese);
        languageResourceMap.put(Language.THAI.getCode(), R.raw.millionaire_thai);


        countryResourceMap.put(Language.ENGLISH.getCode(), R.raw.country_json_en);
        countryResourceMap.put(Language.ARABIC.getCode(), R.raw.country_json_ar);
        countryResourceMap.put(Language.FRENCH.getCode(), R.raw.country_json_fr);
        countryResourceMap.put(Language.SPANISH.getCode(), R.raw.country_json_es);
        countryResourceMap.put(Language.HINDI.getCode(), R.raw.country_json_hi);
        countryResourceMap.put(Language.PORTUGUESE.getCode(), R.raw.country_json_pt);
        countryResourceMap.put(Language.URDU.getCode(), R.raw.country_json_ur);
        countryResourceMap.put(Language.JAPANESE.getCode(), R.raw.country_json_ja);
        countryResourceMap.put(Language.GERMAN.getCode(), R.raw.country_json_de);
        countryResourceMap.put(Language.INDONESIA.getCode(), R.raw.country_json_in);
        countryResourceMap.put(Language.TURKISH.getCode(), R.raw.country_json_tr);
        countryResourceMap.put(Language.CHINESE.getCode(), R.raw.country_json_zh);
        countryResourceMap.put(Language.MALAY.getCode(), R.raw.country_json_ms);
        countryResourceMap.put(Language.KOREAN.getCode(), R.raw.country_json_ko);
        countryResourceMap.put(Language.VIETNAMESE.getCode(), R.raw.country_json_vi);
        countryResourceMap.put(Language.THAI.getCode(), R.raw.country_json_th);
    }

    // Getters for language and country resources
    public static Integer getLanguageResource(String languageCode) {
        return languageResourceMap.get(languageCode);
    }

    public static Integer getCountryResource(String languageCode) {
        return countryResourceMap.get(languageCode);
    }

    // Generate amounts based on game level
    public static List<Integer> generateAmount(int gameLevel) {
        int[] baseAmounts = {500, 1000, 2000, 3000, 5000, 7500, 10000, 12500, 15000,
                25000, 50000, 100000, 250000, 500000, 1000000
        };

        List<Integer> amountList = new ArrayList<>();

        for (int baseAmount : baseAmounts) {
            amountList.add(baseAmount * gameLevel);
        }

        return amountList;
    }

    // Background colors and drawable manipulation
    public static final int[] backgroundColors = {
            R.color.orange,
            R.color.redH,
            R.color.green
    };

    public static Drawable getBackgroundDrawable(int colorIndex, View itemView) {
        Drawable drawable = itemView.getBackground().mutate();
        int color = ContextCompat.getColor(itemView.getContext(), backgroundColors[colorIndex]);
        drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        return drawable;
    }

    // Format currency amount
    public static String formatCurrency(double amount) {
        return new DecimalFormat("#,###,###").format(amount);
    }

    // Format numbers in a readable format
    public static String prettyCount(int number) {
        if (number == 0 || number == 500) {
            return String.valueOf(number);
        }

        int value = (int) Math.floor(Math.log10((long) number));
        int base = value / 3;

        if (value >= 3 && base < SUFFIX.length) {
            return DECIMAL_FORMAT.format((long) number / POWERS_OF_TEN[base]) + SUFFIX[base];
        } else {
            return String.format(Locale.getDefault(), "%,d", (long) number);
        }
    }

    // Get a random suggestion string
    public static String getRandomSuggestion(Context context) {
        String[] helpList = {
                context.getResources().getString(R.string.maybe_its),
                context.getString(R.string.you_could_try),
                context.getString(R.string.i_guess_it_s)
        };

        int index = new Random().nextInt(helpList.length);

        return helpList[index];
    }

    // Get label from list based on index
    public static String getLabelFromList(Context context, int index) {
        String[] labels = {context.getResources().getString(R.string.a),
                context.getResources().getString(R.string.b),
                context.getResources().getString(R.string.c),
                context.getResources().getString(R.string.d)
        };

        return labels[index];
    }

    public static void setLocale(Activity activity) {
        String languageCode = activity.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getString("language", "");
        Locale locale = new Locale(languageCode);
        Resources resources = activity.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }

}