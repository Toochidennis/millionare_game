package com.digitalDreams.millionaire_game.alpha;

import static com.digitalDreams.millionaire_game.Utils.ARABIC_KEY;
import static com.digitalDreams.millionaire_game.Utils.ENGLISH_KEY;
import static com.digitalDreams.millionaire_game.Utils.FRENCH_KEY;
import static com.digitalDreams.millionaire_game.Utils.HINDI_KEY;
import static com.digitalDreams.millionaire_game.Utils.PORTUGUESE_KEY;
import static com.digitalDreams.millionaire_game.Utils.SPANISH_KEY;
import static com.digitalDreams.millionaire_game.Utils.URDU_KEY;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.digitalDreams.millionaire_game.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class Constants {
    public static long DELAY_INTERVAL_LONG = 1000;
    public static long DELAY_INTERVAL_MEDIUM = 500L;
    public static long DELAY_INTERVAL_SHORT = 200L;
    public static String FAILED = "Failed";
    public static String PASSED = "Passed";
    public static int RED = 1;
    public static int GREEN = 2;
    public static int ORANGE = 0;
    public static String SHOULD_CONTINUE_GAME = "shouldContinueGame";
    public static String APPLICATION_DATA = "application_data";
    public static String PREF_NAME = "settings";
    public static String SHOULD_REFRESH_QUESTION = "Failure activity";
    public static String FROM_PROGRESS = "From progress";
    public static String SOUND = "Sound";
    private static final char[] SUFFIX = {' ', 'k', 'M', 'B', 'T', 'P', 'E'};
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#0");

    private static final double[] POWERS_OF_TEN = {1e0, 1e3, 1e6, 1e9, 1e12, 1e15};

    private static final Map<String, Integer> languageResourceMap = new HashMap<>();
    private static final Map<String, String> languageTexts = new HashMap<>();
    private static final Map<String, Integer> countryResourceMap = new HashMap<>();

    static {
        languageResourceMap.put(ENGLISH_KEY, R.raw.millionaire);
        languageResourceMap.put(ARABIC_KEY, R.raw.millionare_ar);
        languageResourceMap.put(FRENCH_KEY, R.raw.millionaire_fr);
        languageResourceMap.put(SPANISH_KEY, R.raw.millionaire_es);
        languageResourceMap.put(HINDI_KEY, R.raw.millionare_hi);
        languageResourceMap.put(PORTUGUESE_KEY, R.raw.millionaire_pt);
        languageResourceMap.put(URDU_KEY, R.raw.millionare_ur);

        countryResourceMap.put(ENGLISH_KEY, R.raw.country_json_en);
        countryResourceMap.put(ARABIC_KEY, R.raw.country_json_ar);
        countryResourceMap.put(FRENCH_KEY, R.raw.country_json_fr);
        countryResourceMap.put(SPANISH_KEY, R.raw.country_json_es);
        countryResourceMap.put(HINDI_KEY, R.raw.country_json_hi);
        countryResourceMap.put(PORTUGUESE_KEY, R.raw.country_json_pt);
        countryResourceMap.put(URDU_KEY, R.raw.country_json_ur);

        languageTexts.put(ENGLISH_KEY, null);
        languageTexts.put(ARABIC_KEY, null);
        languageTexts.put(FRENCH_KEY, null);
        languageTexts.put(SPANISH_KEY, null);
        languageTexts.put(HINDI_KEY, null);
        languageTexts.put(PORTUGUESE_KEY, null);
        languageTexts.put(URDU_KEY, null);
    }

    public static Integer getLanguageResource(String languageCode) {
        return languageResourceMap.get(languageCode);
    }

    public static Integer getCountryResource(String languageCode) {
        return countryResourceMap.get(languageCode);
    }

    public static String getLanguageText(Context context, String languageCode) {
        // Fetch language text from the map
        String text = languageTexts.get(languageCode);

        // If text is not fetched yet, fetch it from resources
        if (text == null) {
            text = context.getResources().getString(getResourceIdByLanguageCode(languageCode));
            // Store the fetched text in the map for later retrieval
            languageTexts.put(languageCode, text);
        }

        return text;
    }

    private static int getResourceIdByLanguageCode(String languageCode) {
        // Get resource ID based on language code
        switch (languageCode) {
            case "ar":
                return R.string.arabic;
            case "fr":
                return R.string.french;
            case "es":
                return R.string.spanish;
            case "hi":
                return R.string.hindi;
            case "pt":
                return R.string.portuguese;
            case "ur":
                return R.string.urdu;
            default:
                return R.string.english;
        }
    }


    public static List<Integer> generateAmount(int gameLevel) {
        int[] baseAmounts = {500, 1000, 2000, 3000, 5000, 7500, 10000, 12500, 15000,
                25000, 50000, 100000, 250000, 500000, 1000000};

        List<Integer> amountList = new ArrayList<>();

        for (int baseAmount : baseAmounts) {
            amountList.add(baseAmount * gameLevel);
        }

        return amountList;
    }

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

    public static String formatCurrency(double amount) {
        return new DecimalFormat("#,###,###").format(amount);
    }

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

    public static String getRandomSuggestion(Context context) {
        String[] helpList = {
                context.getResources().getString(R.string.maybe_its),
                context.getString(R.string.you_could_try),
                context.getString(R.string.i_guess_it_s)
        };

        int index = new Random().nextInt(helpList.length);

        return helpList[index];
    }

    public static String getLabelFromList(Context context, int index) {
        String[] labels = {context.getResources().getString(R.string.a),
                context.getResources().getString(R.string.b),
                context.getResources().getString(R.string.c),
                context.getResources().getString(R.string.d)
        };

        return labels[index];
    }

}
