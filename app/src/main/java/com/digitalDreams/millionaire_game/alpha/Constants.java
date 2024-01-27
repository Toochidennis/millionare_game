package com.digitalDreams.millionaire_game.alpha;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.digitalDreams.millionaire_game.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class Constants {
    public static long DELAY_INTERVAL_LONG = 1000;
    public static long DELAY_INTERVAL_MEDIUM = 500L;
    public static long DELAY_INTERVAL_SHORT = 200L;
    public static String[] labelList = {"A", "B", "C", "D", "E", "F"};
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

}
