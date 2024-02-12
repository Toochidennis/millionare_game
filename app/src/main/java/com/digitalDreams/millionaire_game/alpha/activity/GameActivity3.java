package com.digitalDreams.millionaire_game.alpha.activity;

import static com.digitalDreams.millionaire_game.AdManager.disposeAds;
import static com.digitalDreams.millionaire_game.Utils.ARABIC_KEY;
import static com.digitalDreams.millionaire_game.alpha.AudioManager.pauseBackgroundMusic;
import static com.digitalDreams.millionaire_game.alpha.AudioManager.playBackgroundMusic;
import static com.digitalDreams.millionaire_game.alpha.AudioManager.playFailureSound;
import static com.digitalDreams.millionaire_game.alpha.AudioManager.playSuccessSound;
import static com.digitalDreams.millionaire_game.alpha.AudioManager.releaseMusicResources;
import static com.digitalDreams.millionaire_game.alpha.AudioManager.stopBackgroundMusic;
import static com.digitalDreams.millionaire_game.alpha.Constants.APPLICATION_DATA;
import static com.digitalDreams.millionaire_game.alpha.Constants.DELAY_INTERVAL_LONG;
import static com.digitalDreams.millionaire_game.alpha.Constants.DELAY_INTERVAL_MEDIUM;
import static com.digitalDreams.millionaire_game.alpha.Constants.FAILED;
import static com.digitalDreams.millionaire_game.alpha.Constants.FROM_PROGRESS;
import static com.digitalDreams.millionaire_game.alpha.Constants.GREEN;
import static com.digitalDreams.millionaire_game.alpha.Constants.ORANGE;
import static com.digitalDreams.millionaire_game.alpha.Constants.PASSED;
import static com.digitalDreams.millionaire_game.alpha.Constants.PREF_NAME;
import static com.digitalDreams.millionaire_game.alpha.Constants.RED;
import static com.digitalDreams.millionaire_game.alpha.Constants.SHOULD_CONTINUE_GAME;
import static com.digitalDreams.millionaire_game.alpha.Constants.SHOULD_REFRESH_QUESTION;
import static com.digitalDreams.millionaire_game.alpha.Constants.SOUND;
import static com.digitalDreams.millionaire_game.alpha.Constants.generateAmount;
import static com.digitalDreams.millionaire_game.alpha.Constants.getBackgroundDrawable;
import static com.digitalDreams.millionaire_game.alpha.Constants.getLabelFromList;
import static com.digitalDreams.millionaire_game.alpha.Constants.getRandomSuggestion;
import static com.digitalDreams.millionaire_game.alpha.Constants.prettyCount;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.digitalDreams.millionaire_game.AdManager;
import com.digitalDreams.millionaire_game.CountDownActivity;
import com.digitalDreams.millionaire_game.DBHelper;
import com.digitalDreams.millionaire_game.ExitGameDialog;
import com.digitalDreams.millionaire_game.FailureActivity;
import com.digitalDreams.millionaire_game.R;
import com.digitalDreams.millionaire_game.Utils;
import com.digitalDreams.millionaire_game.WinnersActivity;
import com.digitalDreams.millionaire_game.WrongAnswerDialog;
import com.digitalDreams.millionaire_game.alpha.AudioManager;
import com.digitalDreams.millionaire_game.alpha.ExplanationBottomSheetDialog;
import com.digitalDreams.millionaire_game.alpha.adapters.OnOptionsClickListener;
import com.digitalDreams.millionaire_game.alpha.adapters.OptionsAdapter;
import com.digitalDreams.millionaire_game.alpha.models.OptionsModel;
import com.digitalDreams.millionaire_game.alpha.models.QuestionModel;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

/**
 * GameActivity3 - Quiz Game Activity
 * <p>
 * Description:
 * GameActivity3 is an Android activity class that implements a quiz game functionality.
 * This activity allows users to answer multiple-choice questions and progress through
 * the game by selecting correct answers. The activity includes features such as displaying
 * questions, handling user selections, managing game flow, and providing feedback to users.
 * <p>
 * <p>
 * Functionality:
 * - Display questions retrieved from a database.
 * - Allow users to select options to answer questions.
 * - Check the correctness of selected answers and provide feedback.
 * - Manage game flow by progressing to the next question or displaying explanations/failure dialogs.
 * - Update the amount won by the user based on correct answers.
 * - Control background music, sound effects, and timer during the game.
 * <p>
 * <p>
 * Key Components:
 * - Views: Initialize and manage UI components such as buttons, text views, and recycler views.
 * - User Interaction: Handle user selections of options and process selected answers.
 * - Question Data: Retrieve questions from a database and prepare them for display.
 * - Dialogs: Display explanations for correct answers and failure dialogs for incorrect answers.
 * - Game Flow: Control the progression of the game and update the amount won by the user.
 * <p>
 * <p>
 * Usage:
 * GameActivity3 is used as the main activity for the quiz game module within the application.
 * It can be launched from other activities or components using intents.
 * <p>
 * <p>
 * Notes:
 * - This activity is part of a larger application and may interact with other modules or components.
 * - Additional features and enhancements can be implemented based on project requirements and user feedback.
 * <p>
 * <p>
 * Author: [ToochiDennis]
 * Version: 6.2.4
 * Date: [9th Feb, 2024]
 */

public class GameActivity3 extends AppCompatActivity implements OnOptionsClickListener {


    private RelativeLayout exitButton, timerContainer, amountContainer;
    private TextView questionTextView, amountWonTextView, questionProgressTextView, countdownTextView;

    RelativeLayout minus2QuestionsButton, askComputerButton, takeAPollButton, resetQuestionButton;
    RelativeLayout askComputerContainer;
    TextView suggestionTextView, optionTextView;
    CardView questionCardView;
    TextView lifeLineDescriptionTextView1, lifeLineDescriptionTextView2, lifeLineDescriptionTextView3, lifeLineDescriptionTextView4;
    LinearLayout votingContainer;
    ProgressBar progressBarA, progressBarB, progressBarC, progressBarD;
    TextView textViewA, textViewB, textViewC, textViewD;
    ImageView minus2QuestionsImageView, askComputerImageView, takeAPollImageView, refreshImageView, refreshVideoImageView;
    private RecyclerView optionsRecyclerView;
    AdView adView;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private DBHelper dbHelper;
    private QuestionModel questionModel;
    private JSONArray questionJSONArray;
    private OptionsAdapter optionsAdapter;

    private int questionIndex = 0;
    private int numberOfFailure = 0;
    private int numberOfPassed = 0;
    private int numberOfAnswered = 0;
    private boolean optionsClickable = true;
    private boolean hasAskedComputer = false;
    private boolean hasTakenPoll = false;
    private boolean hasMinus2Question = false;
    private List<Integer> amountList;
    private List<Integer> amountWonList = new ArrayList<>();
    private int amountWonText;
    private String selectedAnswer;
    private long startTimeMillis;
    private CountDownTimer countDownTimer = null;

    @SuppressLint("StaticFieldLeak")
    public static Activity gameActivity;


    @Override
    protected void onStart() {
        super.onStart();
        initializeAds();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gameActivity = this;

        if (savedInstanceState == null) {
            initializeGame();
        } else {
            reInitializeGame();
        }

    }

    /**
     * Initializes the game by setting up views, background color, preparing questions, and handling view clicks.
     */
    private void initializeGame() {
        startCountDownActivity();
        initializeViews();
        setRootViewBackgroundColor();
        loadQuestions();
        handleViewsClick();

        startTimeMillis = System.currentTimeMillis();
    }


    private void reInitializeGame() {
        initializeViews();
        setRootViewBackgroundColor();
        handleViewsClick();

        String gameLevel = sharedPreferences.getString("game_level", "1");
        int level = Integer.parseInt(gameLevel);
        amountList = generateAmount(level);

        //enableLifeLines();
        restoreSavedProgress();
    }

    /**
     * Initializes UI components by finding and assigning views from the layout XML file.
     */
    private void initializeViews() {
        setContentView(R.layout.activity_game3);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE);

        exitButton = findViewById(R.id.exitBtn);
        timerContainer = findViewById(R.id.timer_container);
        countdownTextView = findViewById(R.id.time);
        amountContainer = findViewById(R.id.amount_container);
        amountWonTextView = findViewById(R.id.amount_won_text);
        questionTextView = findViewById(R.id.question_text);
        questionCardView = findViewById(R.id.question_card_view);
        minus2QuestionsButton = findViewById(R.id.minus_two_questions);
        askComputerButton = findViewById(R.id.ask_computer);
        takeAPollButton = findViewById(R.id.take_a_poll);
        resetQuestionButton = findViewById(R.id.reset_question);
        minus2QuestionsImageView = findViewById(R.id.bad1);
        askComputerImageView = findViewById(R.id.bad2);
        takeAPollImageView = findViewById(R.id.bad3);
        askComputerContainer = findViewById(R.id.ask_answer_container);
        suggestionTextView = findViewById(R.id.suggestion_text);
        optionTextView = findViewById(R.id.option_text);
        votingContainer = findViewById(R.id.voting_container);
        progressBarA = findViewById(R.id.progress_bar1);
        progressBarB = findViewById(R.id.progress_bar2);
        progressBarC = findViewById(R.id.progress_bar3);
        progressBarD = findViewById(R.id.progress_bar4);
        textViewA = findViewById(R.id.progress_text1);
        textViewB = findViewById(R.id.progress_text2);
        textViewC = findViewById(R.id.progress_text3);
        textViewD = findViewById(R.id.progress_text4);
        lifeLineDescriptionTextView1 = findViewById(R.id.option_description_1);
        lifeLineDescriptionTextView2 = findViewById(R.id.option_description_2);
        lifeLineDescriptionTextView3 = findViewById(R.id.option_description_3);
        lifeLineDescriptionTextView4 = findViewById(R.id.option_description_4);
        refreshImageView = findViewById(R.id.refresh_imageview);
        refreshVideoImageView = findViewById(R.id.video_imageview);
        optionsRecyclerView = findViewById(R.id.options_recyclerView);
        questionProgressTextView = findViewById(R.id.question_progress);
        adView = findViewById(R.id.adView);

        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        dbHelper = new DBHelper(this);

        String gameLevel = sharedPreferences.getString("game_level", "1");
        int level = Integer.parseInt(gameLevel);
        amountList = generateAmount(level);
    }

    /**
     * Handles user interactions such as button clicks.
     */
    private void handleViewsClick() {
        exitButton.setOnClickListener(v -> super.onBackPressed());

        minus2QuestionsButton.setOnClickListener(minus -> hideTwoQuestions());

        resetQuestionButton.setOnClickListener(reset -> skipQuestion());

        askComputerButton.setOnClickListener(ask -> askComputer());

        takeAPollButton.setOnClickListener(poll -> takeAPoll());

        exitButton.setOnClickListener(exit -> showExitDialog());

        amountContainer.setOnClickListener(viewAmount ->
                startActivity(new Intent(this, ProgressActivity2.class)
                        .putIntegerArrayListExtra("amount_won", new ArrayList<>(amountWonList))
                        .putExtra("should_use_timer", false)));

        adView.loadAd(createAdRequest());
    }

    private void showQuestion() {
        updateAmountWon();
        parseQuestionJSONArray(questionIndex);
        animateViews();
        startCountdownTimerIfGameModeIsTimed();
        saveGameProgress();
    }

    /**
     * Prepares questions for the game from a JSON array.
     */
    private void loadQuestions() {
        try {
            String questionsJson = dbHelper.buildJson();

            if (questionsJson != null) {
                JSONArray jsonArray = new JSONArray(questionsJson);
                JSONObject jsonObject = jsonArray.getJSONObject(0).getJSONObject("q");
                questionJSONArray = jsonObject.getJSONArray("0");

                showQuestion();
            } else {
                showToast();
                Log.d("TAG", "recreated");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseQuestionJSONArray(int index) {
        try {
            List<OptionsModel> optionsList = new ArrayList<>();

            JSONObject questionObject = questionJSONArray.getJSONObject(index);
            String questionId = questionObject.getString("id");
            String question = capitaliseFirstLetter(questionObject.getString("content")).trim();
            String correct = capitaliseFirstLetter(questionObject.getString("correct")).trim();
            String reason = capitaliseFirstLetter(questionObject.getString("reason")).trim();
            String options = questionObject.getString("answer");

            JSONArray optionsArray = new JSONArray(options);

            for (int i = 0; i < optionsArray.length(); i++) {
                JSONObject optionsObject = optionsArray.getJSONObject(i);
                String option = optionsObject.getString("text");
                optionsList.add(new OptionsModel(String.valueOf(i), capitaliseFirstLetter(option).trim()));
            }

            questionModel = new QuestionModel(questionId, question, correct, reason, optionsList);

            processQuestion();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String capitaliseFirstLetter(String word) {
        if (word == null || word.isEmpty()) {
            return word;
        }
        return Character.toUpperCase(word.charAt(0)) + word.substring(1);
    }

    private void setRootViewBackgroundColor() {
        int endColor = sharedPreferences.getInt("end_color", getResources().getColor(R.color.purple_dark));
        int startColor = sharedPreferences.getInt("start_color", getResources().getColor(R.color.purple_500));

        RelativeLayout rootView = findViewById(R.id.rootView);

        GradientDrawable gradientDrawable =
                new GradientDrawable(
                        GradientDrawable.Orientation.TOP_BOTTOM,
                        new int[]{startColor, endColor}
                );

        rootView.setBackground(gradientDrawable);
    }

    private void processQuestion() {
        questionTextView.setText(questionModel.getQuestionText());
        inflateOptions();
    }

    private void inflateOptions() {
        optionsAdapter = new OptionsAdapter(questionModel.getOptions(), this);
        optionsRecyclerView.hasFixedSize();
        optionsRecyclerView.setAdapter(optionsAdapter);
    }

    /**
     * Handles user clicks on options.
     *
     * @param position The position of the clicked option.
     * @param itemView The view representing the clicked option.
     */
    @Override
    public void onOptionClick(int position, View itemView) {
        if (optionsClickable) {
            itemView.setBackground(getBackgroundDrawable(ORANGE, itemView));
            itemView.postDelayed(() -> handleUserSelection(position, itemView), DELAY_INTERVAL_LONG);
            optionsClickable = false;
        }
    }

    /**
     * Handles user interactions such as button clicks.
     */

    private void handleUserSelection(int position, View itemView) {
        selectedAnswer = questionModel.getOptions().get(position).getOptionText().trim();
        String correctAnswer = questionModel.getCorrectText().trim();

        if (selectedAnswer.equalsIgnoreCase(correctAnswer)) {
            itemView.setBackground(getBackgroundDrawable(GREEN, itemView));

            playSuccessSound(this);

            showExplanationDialog(PASSED);
        } else {
            itemView.setBackground(getBackgroundDrawable(RED, itemView));

            playFailureSound(this);

            showExplanationDialog(FAILED);
        }
    }

    private void vibrate() {
        if (vibrationStatus().equals("1")) {
            Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            if (vibrator != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(DELAY_INTERVAL_MEDIUM, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    vibrator.vibrate(DELAY_INTERVAL_MEDIUM);
                }
            }
        }
    }

    private void showExplanationDialog(String from) {
        vibrate();
        cancelTimer();

        ExplanationBottomSheetDialog explanationBottomSheetDialog;

        if (Objects.equals(from, PASSED)) {
            explanationBottomSheetDialog = new ExplanationBottomSheetDialog(this, questionModel);
            explanationBottomSheetDialog.show();

            explanationBottomSheetDialog.setOnDismissListener(dialog -> {
                dialog.dismiss();

                new Handler().postDelayed(this::startProgressActivity, DELAY_INTERVAL_LONG);
            });

            saveHistory(
                    questionModel.getQuestionId(),
                    selectedAnswer,
                    questionModel.getCorrectText().trim(),
                    String.valueOf(amountWonText),
                    true
            );

        } else {
            explanationBottomSheetDialog = new ExplanationBottomSheetDialog(this, questionModel);
            startFailureActivities(explanationBottomSheetDialog);

            saveHistory(
                    questionModel.getQuestionId(),
                    selectedAnswer,
                    questionModel.getCorrectText().trim(),
                    String.valueOf(amountWonText),
                    false
            );
        }
    }

    private void startProgressActivity() {
        amountWonText = amountList.get(numberOfPassed);
        amountWonList.add(amountWonText);

        if (numberOfPassed == 14) {
            saveNewAmountWon();
            startWinnersActivity();
        } else {
            updateRefreshQuestionState(false);
            updateShouldContinueGame(true);
            updateProgressState(true);

            startActivity(new Intent(this, ProgressActivity2.class)
                    .putIntegerArrayListExtra("amount_won", new ArrayList<>(amountWonList))
                    .putExtra("should_use_timer", true));

            questionIndex++;
            numberOfPassed++;
            numberOfAnswered++;

            saveNewAmountWon();
        }
        optionsClickable = true;
        updateMusicState(true);
    }

    private void startFailureActivities(ExplanationBottomSheetDialog explanationBottomSheetDialog) {
        if (numberOfFailure < 1) {
            showFailureDialog();
            numberOfFailure++;
        } else {
            pauseBackgroundMusic();

            explanationBottomSheetDialog.show();

            explanationBottomSheetDialog.setOnDismissListener(dialog -> {
                dialog.dismiss();
                numberOfFailure = 0;
                startActivity(new Intent(this, FailureActivity.class));
            });

            updateRefreshQuestionState(true);
            updateShouldContinueGame(false);
        }

        optionsClickable = true;

        updateProgressState(false);
        clearSavedProgress();
    }

    private void startWinnersActivity() {
        Intent intent = new Intent(this, WinnersActivity.class);
        intent.putExtra("isWon", true);
        intent.putExtra("isShowAd", false);
        startActivity(intent);

        saveTotalAmountWon(amountWonText);
    }

    private void showFailureDialog() {
        pauseBackgroundMusic();
        WrongAnswerDialog wrongAnswerDialog = new WrongAnswerDialog(this, questionModel);
        wrongAnswerDialog.setCancelable(false);
        wrongAnswerDialog.show();
    }

    private void updateAmountWon() {
        String formattedAmount = String.format(Locale.getDefault(), "$%s", prettyCount(amountWonText));
        amountWonTextView.setText(formattedAmount);
        updateQuestionProgress();
    }

    private void updateQuestionProgress() {
        String progress = String.format(Locale.getDefault(), "%d/15", questionIndex + 1);
        questionProgressTextView.setText(progress);
    }

    private void saveNewAmountWon() {
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString("amountWon", String.valueOf(amountWonText));
        editor.putBoolean("hasOldWinningAmount", true);
        editor.putInt("noOfCorrect", numberOfPassed);
        editor.putInt("noOfAnswered", numberOfAnswered);
        editor.apply();
    }

    private void saveTotalAmountWon(int totalAmountWon) {
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putInt("totalAmountWon", totalAmountWon);
        editor.apply();
    }

    private boolean shouldContinueGame() {
        sharedPreferences = getSharedPreferences(APPLICATION_DATA, MODE_PRIVATE);
        return sharedPreferences.getBoolean(SHOULD_CONTINUE_GAME, true);
    }

    private boolean isShouldRefreshQuestion() {
        sharedPreferences = getSharedPreferences(APPLICATION_DATA, MODE_PRIVATE);
        return sharedPreferences.getBoolean(SHOULD_REFRESH_QUESTION, false);
    }

    private boolean isFromProgress() {
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        return sharedPreferences.getBoolean(FROM_PROGRESS, false);
    }

    /**
     * private String soundStatus() {
     * sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
     * return sharedPreferences.getString("sound", "1");
     * }
     */

    private String vibrationStatus() {
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        return sharedPreferences.getString("vibrate", "1");
    }

    private String gameMode() {
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        return sharedPreferences.getString("game_mode", "0");
    }

    private void updateProgressState(boolean progressState) {
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putBoolean(FROM_PROGRESS, progressState);
        editor.apply();
    }

    private boolean shouldPlayMusic() {
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        return sharedPreferences.getBoolean(SOUND, false);
    }

    private void updateMusicState(boolean soundState) {
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putBoolean(SOUND, soundState);
        editor.apply();
    }

    private void updateRefreshQuestionState(boolean refreshState) {
        sharedPreferences = getSharedPreferences(APPLICATION_DATA, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putBoolean(SHOULD_REFRESH_QUESTION, refreshState);
        editor.apply();
    }

    private void updateShouldContinueGame(boolean savedState) {
        sharedPreferences = getSharedPreferences(APPLICATION_DATA, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putBoolean(SHOULD_CONTINUE_GAME, savedState);
        editor.apply();
    }

    private void startCountDownActivity() {
        startActivity(new Intent(this, CountDownActivity.class));
    }

    private void initializeAds() {
        AdManager.loadInterstitialAd(this);
        AdManager.loadRewardedAd(this);
    }

    private void skipQuestion() {
        if (Utils.isOnline(this)) {
            try {
                AdManager.showRewardedAd(this);

                AdManager.rewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                        showToast();
                        optionsClickable = true;
                    }

                    @Override
                    public void onAdDismissedFullScreenContent() {
                        updateRefreshQuestionState(true);
                        updateShouldContinueGame(true);
                        updateProgressState(false);
                        optionsClickable = true;
                    }
                });

            } catch (Exception e) {
                showToast();
            }
        } else {
            showToast();
        }
    }

    private void hideTwoQuestions() {
        String correctAnswer = questionModel.getCorrectText();
        optionsAdapter.hideRandomOptions(correctAnswer);
        minus2QuestionsImageView.setVisibility(View.VISIBLE);
        minus2QuestionsButton.setClickable(false);
        hasMinus2Question = true;

        updateSuggestionAndPollVisibility();
    }

    private void askComputer() {
        askComputerContainer.setVisibility(View.VISIBLE);
        votingContainer.setVisibility(View.GONE);
        askComputerButton.setClickable(false);
        askComputerImageView.setVisibility(View.VISIBLE);
        hasAskedComputer = true;

        String suggestion = getRandomSuggestion(this);
        String label = getCorrectLabel();
        suggestionTextView.setText(suggestion);
        optionTextView.setText(label);
    }

    private void takeAPoll() {
        votingContainer.setVisibility(View.VISIBLE);
        takeAPollButton.setClickable(false);
        askComputerContainer.setVisibility(View.GONE);
        takeAPollImageView.setVisibility(View.VISIBLE);
        hasTakenPoll = true;

        int[] correctOptions = {60, 65, 75};
        String label = getCorrectLabel();
        int index = new Random().nextInt(correctOptions.length);
        int progress = correctOptions[index];

        updateProgressBar(label, progress);
    }

    private void updateProgressBar(String correctLabel, int correctProgress) {
        int remainingPercentage = 100 - correctProgress;
        int incorrectProgress = remainingPercentage / 3;

        ProgressBar[] progressBars = {progressBarA, progressBarB, progressBarC, progressBarD};
        TextView[] textViews = {textViewA, textViewB, textViewC, textViewD};

        // Detect the language being used (Latin or Arabic)
        boolean isArabic = isArabic();

        // Determine the starting character based on the language
        char startingChar = isArabic ? 'أ' : 'A';

        for (int i = 0; i < progressBars.length; i++) {
            // Compare characters dynamically
            animateProgressBar(
                    progressBars[i],
                    textViews[i],
                    (correctLabel.equals(String.valueOf((char) (startingChar + i)))) ? correctProgress : incorrectProgress
            );
        }
    }

    private boolean isArabic() {
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        String languageCode = sharedPreferences.getString("language", "");
        return languageCode.equals(ARABIC_KEY);
    }

    /**
     * Animates progress bars and text views based on user actions.
     *
     * @param progressBar The progress bar to be animated.
     * @param textView    The text view associated with the progress bar.
     * @param progress    The progress value to be animated.
     */
    private void animateProgressBar(ProgressBar progressBar, TextView textView, int progress) {
        ObjectAnimator anim = ObjectAnimator.ofInt(progressBar, "progress", progress);
        anim.setDuration(DELAY_INTERVAL_LONG);
        anim.start();

        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, progress);
        valueAnimator.setDuration(DELAY_INTERVAL_LONG);
        valueAnimator.addUpdateListener(animation -> textView.setText(String.format(Locale.getDefault(), "%s%%", animation.getAnimatedValue())));
        valueAnimator.start();
    }

    private void updateSuggestionAndPollVisibility() {
        if (hasTakenPoll) {
            votingContainer.setVisibility(View.GONE);
        }

        if (hasAskedComputer) {
            askComputerContainer.setVisibility(View.GONE);
        }
    }

    private void enableLifeLines() {
        minus2QuestionsButton.setClickable(true);
        minus2QuestionsImageView.setVisibility(View.GONE);
        askComputerButton.setClickable(true);
        askComputerImageView.setVisibility(View.GONE);
        takeAPollButton.setClickable(true);
        takeAPollImageView.setVisibility(View.GONE);

        amountWonList.clear();
        numberOfFailure = 0;
        numberOfPassed = 0;
        questionIndex = 0;
        amountWonText = 0;
    }

    private void disableLifeLines() {
        if (hasTakenPoll) {
            takeAPollButton.setClickable(false);
            takeAPollImageView.setVisibility(View.VISIBLE);
        }

        if (hasAskedComputer) {
            askComputerButton.setClickable(false);
            askComputerImageView.setVisibility(View.VISIBLE);
        }

        if (hasMinus2Question) {
            minus2QuestionsButton.setClickable(false);
            minus2QuestionsImageView.setVisibility(View.VISIBLE);
        }

        updateRefreshQuestionState(false);
        updateShouldContinueGame(true);
        updateProgressState(false);
    }

    private String getCorrectLabel() {
        if (questionModel == null){
            parseQuestionJSONArray(questionIndex);
        }
        List<OptionsModel> optionsList = questionModel.getOptions();
        String correctAnswer = questionModel.getCorrectText().trim();

        for (int i = 0; i < optionsList.size(); i++) {
            OptionsModel model = optionsList.get(i);

            if (model.getOptionText().trim().equalsIgnoreCase(correctAnswer)) {
                return getLabelFromList(this, i);
            }
        }

        return "";
    }

    /**
     * Saves game progress, including question history and amount won.
     *
     * @param questionId    The ID of the current question.
     * @param answer        The user's answer to the question.
     * @param correctAnswer The correct answer to the question.
     * @param highScore     The amount won for the current question.
     * @param isCorrect     Indicates whether the user's answer is correct.
     */
    private void saveHistory(String questionId, String answer, String correctAnswer, String highScore, boolean isCorrect) {
        try {
            DateFormat dateFormat = new SimpleDateFormat("EEE, d MMM, HH:mm", Locale.getDefault());
            String datePlayed = dateFormat.format(Calendar.getInstance().getTime());

            dbHelper.saveHistory(
                    questionId,
                    answer,
                    correctAnswer,
                    questionModel.getReasonText(),
                    datePlayed,
                    datePlayed,
                    highScore,
                    isCorrect
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showToast() {
        Toast.makeText(this, getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
    }

    /**
     * Handles the display of various dialogs such as the exit dialog, failure dialog, and explanation dialog.
     */

    private void showExitDialog() {
        AudioManager.darkBlueBlink(this, exitButton);

        ExitGameDialog dialog = new ExitGameDialog(this, String.valueOf(amountWonText));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Window window = dialog.getWindow();
        assert window != null;
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @SuppressLint("VisibleForTests")
    private AdRequest createAdRequest() {
        return new AdRequest.Builder().build();
    }

    private String formatDuration(long durationMillis) {
        long seconds = durationMillis / 1000;
        long minutes = seconds / 60;
        seconds = seconds % 60;
        long hours = minutes / 60;
        minutes = minutes % 60;

        if (hours > 0) {
            return String.format(Locale.getDefault(),
                    getResources().getString(R.string.d_hours_02d_minutes_02d_seconds),
                    hours, minutes, seconds
            );
        } else if (minutes > 0) {
            return String.format(Locale.getDefault(),
                    getResources().getString(R.string.d_minutes_02d_seconds),
                    minutes, seconds
            );
        } else {
            return String.format(Locale.getDefault(),
                    getResources().getString(R.string.d_seconds),
                    seconds
            );
        }
    }

    private void animateViews() {
        View[] views = {minus2QuestionsButton, lifeLineDescriptionTextView1, askComputerButton,
                lifeLineDescriptionTextView2, takeAPollButton, lifeLineDescriptionTextView3,
                resetQuestionButton, lifeLineDescriptionTextView4, questionCardView,
                questionTextView, exitButton, amountContainer, questionProgressTextView
        };

        long[] delays = {1000, 1000, 1200, 1200, 1400, 1400, 1600, 1600, 500, 550, 500, 500, 500};
        long duration = 800; // Default duration

        for (int i = 0; i < views.length; i++) {
            fadeViewIn(views[i], delays[i], duration);
        }
    }

    private void fadeViewIn(View view, long delay, long duration) {
        view.setAlpha(0f);
        view.animate()
                .alpha(1f)
                .setDuration(duration)
                .setStartDelay(delay)
                .start();
    }

    private void saveGameProgress() {
        sharedPreferences = getSharedPreferences("progress", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putInt("questionIndex", questionIndex);
        editor.putInt("numberOfPassed", numberOfPassed);
        editor.putInt("numberOfFailure", numberOfFailure);
        editor.putInt("numberOfAnswered", numberOfAnswered);
        editor.putBoolean("hasAskedComputer", hasAskedComputer);
        editor.putBoolean("hasTakenPoll", hasTakenPoll);
        editor.putBoolean("isMinus2Questions", hasMinus2Question);
        editor.putInt("amountWonText", amountWonText);
        editor.putLong("startTimeMillis", startTimeMillis);
        editor.putString("questionJSONArray", questionJSONArray.toString());
        editor.apply();
    }

    private void restoreSavedProgress() {
        sharedPreferences = getSharedPreferences("progress", MODE_PRIVATE);
        questionIndex = sharedPreferences.getInt("questionIndex", 0);
        numberOfPassed = sharedPreferences.getInt("numberOfPassed", 0);
        numberOfFailure = sharedPreferences.getInt("numberOfFailure", 0);
        numberOfAnswered = sharedPreferences.getInt("numberOfAnswered", 0);
        hasAskedComputer = sharedPreferences.getBoolean("hasAskedComputer", false);
        hasTakenPoll = sharedPreferences.getBoolean("hasTakenPoll", false);
        hasMinus2Question = sharedPreferences.getBoolean("isMinus2Questions", false);
        amountWonText = sharedPreferences.getInt("amountWonText", 0);
        startTimeMillis = sharedPreferences.getLong("startTimeMillis", 0);
        String questionJson = sharedPreferences.getString("questionJSONArray", "");

        disableLifeLines();

        if (!questionJson.isEmpty()) {
            try {
                questionJSONArray = new JSONArray(questionJson);
                showQuestion();
            } catch (Exception e) {
                loadQuestions();
            }
        } else {
            loadQuestions();
        }
    }

    private void clearSavedProgress() {
        getSharedPreferences("progress", MODE_PRIVATE)
                .edit()
                .clear()
                .apply();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntegerArrayList("amountWonList", new ArrayList<>(amountWonList));
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        amountWonList = savedInstanceState.getIntegerArrayList("amountWonList");
    }

    private void startCountdownTimer() {
        countDownTimer = new CountDownTimer(30000, DELAY_INTERVAL_LONG) {
            @Override
            public void onTick(long millisUntilFinished) {
                updateCountdownText(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                vibrate();
                showFailureDialog();
                cancelTimer();
            }
        };

        countDownTimer.start();
    }

    private void updateCountdownText(long millisUntilFinished) {
        long seconds = millisUntilFinished / DELAY_INTERVAL_LONG;
        String timeRemaining = String.valueOf(seconds);
        countdownTextView.setText(timeRemaining);
    }

    private void startCountdownTimerIfGameModeIsTimed() {
        if (gameMode().equals("1")) {
            timerContainer.setVisibility(View.VISIBLE);
            cancelTimer();
            startCountdownTimer();
        } else {
            timerContainer.setVisibility(View.GONE);
        }
    }

    private void cancelTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    private void disposeResources() {
        stopBackgroundMusic();
        releaseMusicResources();
        updateMusicState(false);
        disposeAds();
        cancelTimer();

        long durationMillis = System.currentTimeMillis() - startTimeMillis;
        String durationString = formatDuration(durationMillis);

        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString("duration", durationString);
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (shouldContinueGame()) {
            if (isFromProgress()) {
                showQuestion();
            } else if (isShouldRefreshQuestion()) {
                loadQuestions();
                updateRefreshQuestionState(false);
                updateShouldContinueGame(true);
            } else {
                startCountdownTimerIfGameModeIsTimed();
            }
        } else {
            enableLifeLines();
            loadQuestions();
        }

        if (shouldPlayMusic()) {
            playBackgroundMusic(this);
            updateMusicState(false);
        }

        updateSuggestionAndPollVisibility();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (!shouldPlayMusic()) {
            pauseBackgroundMusic();
            updateMusicState(true);
        }

        updateRefreshQuestionState(false);
        updateShouldContinueGame(true);
        updateProgressState(false);
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposeResources();
        clearSavedProgress();
    }
}