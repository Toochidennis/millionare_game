package com.digitalDreams.millionaire_game.alpha.activity;

import static com.digitalDreams.millionaire_game.alpha.AudioManager.pauseBackgroundMusic;
import static com.digitalDreams.millionaire_game.alpha.AudioManager.playBackgroundMusic;
import static com.digitalDreams.millionaire_game.alpha.AudioManager.playFailureSound;
import static com.digitalDreams.millionaire_game.alpha.AudioManager.playSuccessSound;
import static com.digitalDreams.millionaire_game.alpha.AudioManager.releaseAll;
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
import static com.digitalDreams.millionaire_game.alpha.Constants.getRandomSuggestion;
import static com.digitalDreams.millionaire_game.alpha.Constants.labelList;
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
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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
 * <p>
 * Key Components:
 * - Views: Initialize and manage UI components such as buttons, text views, and recycler views.
 * - User Interaction: Handle user selections of options and process selected answers.
 * - Question Data: Retrieve questions from a database and prepare them for display.
 * - Dialogs: Display explanations for correct answers and failure dialogs for incorrect answers.
 * - Game Flow: Control the progression of the game and update the amount won by the user.
 * <p>
 * <p>
 * <p>
 * <p>
 * Usage:
 * GameActivity3 is used as the main activity for the quiz game module within the application.
 * It can be launched from other activities or components using intents.
 * <p>
 * <p>
 * <p>
 * Notes:
 * - This activity is part of a larger application and may interact with other modules or components.
 * - Additional features and enhancements can be implemented based on project requirements and user feedback.
 * <p>
 * <p>
 * <p>
 * Author: [ToochiDennis]
 * Version: 1.0
 * Date: [27th Jan, 2024]
 */

public class GameActivity3 extends AppCompatActivity implements OnOptionsClickListener {


    private RelativeLayout exitButton, timerContainer, amountContainer;
    private TextView questionTextView, amountWonTextView, questionProgressTextView;

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
    private List<Integer> amountList;
    private final List<Integer> amountWonList = new ArrayList<>();

    private int amountWonText;
    private String selectedAnswer;
    private long startTimeMillis;

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

        initializeGame();
    }

    /**
     * Initializes the game by setting up views, background color, preparing questions, and handling view clicks.
     */
    private void initializeGame() {
        startCountDownActivity();
        initializeViews();
        setRootViewBackgroundColor();
        prepareQuestion();
        handleViewsClick();

        gameActivity = this;

        startTimeMillis = System.currentTimeMillis();
    }


    /**
     * Initializes UI components by finding and assigning views from the layout XML file.
     */
    private void initializeViews() {
        setContentView(R.layout.activity_game3);

        exitButton = findViewById(R.id.exitBtn);
        timerContainer = findViewById(R.id.timer_container);
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


        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        dbHelper = new DBHelper(this);
    }

    /**
     * Handles user interactions such as button clicks.
     */
    private void handleViewsClick() {
        exitButton.setOnClickListener(v -> super.onBackPressed());

        timerContainer.setVisibility(View.GONE);

        minus2QuestionsButton.setOnClickListener(minus -> {
            hideTwoQuestions();
            minus2QuestionsButton.setClickable(false);
        });

        resetQuestionButton.setOnClickListener(reset -> showRewardAds());

        askComputerButton.setOnClickListener(ask -> askComputer());

        takeAPollButton.setOnClickListener(poll -> takeAPoll());

        exitButton.setOnClickListener(exit -> showExitDialog());

        amountContainer.setOnClickListener(viewAmount ->
                startActivity(new Intent(this, ProgressActivity2.class)
                        .putIntegerArrayListExtra("amount_won", new ArrayList<>(amountWonList))
                        .putExtra("should_use_timer", false)));

        adView.loadAd(createAdRequest());
    }


    /**
     * Prepares questions for the game from a JSON array.
     */
    private void prepareQuestion() {
        try {
            sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
            String gameLevel = sharedPreferences.getString("game_level", "1");
            int level = Integer.parseInt(gameLevel);
            amountList = generateAmount(level);

            String questionsJson = dbHelper.buildJson();
            JSONArray jsonArray = new JSONArray(questionsJson);
            JSONObject jsonObject = jsonArray.getJSONObject(0).getJSONObject("q");
            questionJSONArray = jsonObject.getJSONArray("0");

            showQuestion();

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

    private void showQuestion() {
        updateAmountWon();
        parseQuestionJSONArray(questionIndex);
        animateViews();
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
        optionsAdapter = new OptionsAdapter(questionModel.getOptionsList(), this);
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
        selectedAnswer = questionModel.getOptionsList().get(position).getOptionText().trim();
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
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(DELAY_INTERVAL_MEDIUM, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                vibrator.vibrate(DELAY_INTERVAL_MEDIUM);
            }
        }
    }


    private void showExplanationDialog(String from) {
        vibrate();
        ExplanationBottomSheetDialog explanationBottomSheetDialog;

        if (Objects.equals(from, PASSED)) {
            explanationBottomSheetDialog = new ExplanationBottomSheetDialog(this, questionModel);
            explanationBottomSheetDialog.show();

            explanationBottomSheetDialog.setOnDismissListener(dialog -> {
                dialog.dismiss();

                new Handler().postDelayed(this::startProgressActivity, DELAY_INTERVAL_LONG);
            });

        } else {
            if (numberOfFailure < 1) {
                showFailureDialog();
                numberOfFailure++;
            } else {
                pauseBackgroundMusic();

                explanationBottomSheetDialog = new ExplanationBottomSheetDialog(this, questionModel);
                explanationBottomSheetDialog.show();

                explanationBottomSheetDialog.setOnDismissListener(dialog -> {
                    dialog.dismiss();
                    numberOfFailure = 0;
                    startActivity(new Intent(this, FailureActivity.class));
                });

                updateRefreshQuestionState(true);
            }

            optionsClickable = true;

            updateProgressState(false);
            saveHistory(questionModel.getQuestionId(), selectedAnswer, questionModel.getCorrectText().trim(), String.valueOf(amountWonText), false);
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

        saveHistory(
                questionModel.getQuestionId(),
                selectedAnswer, questionModel.getCorrectText().trim(),
                String.valueOf(amountWonText),
                true
        );
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
        editor.putBoolean(SHOULD_CONTINUE_GAME, !refreshState);
        editor.apply();
    }


    private void startCountDownActivity() {
        startActivity(new Intent(this, CountDownActivity.class));
    }

    private void initializeAds() {
        AdManager.initInterstitialAd(this);
        AdManager.initRewardedVideo(this);
    }

    private void showRewardAds() {
        if (Utils.isOnline(this)) {
            try {
                initializeAds();
                AdManager.showRewardAd(this);

                AdManager.rewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                        showToast();
                    }

                    @Override
                    public void onAdDismissedFullScreenContent() {
                        updateProgressState(true);
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

        for (int i = 0; i < progressBars.length; i++) {
            animateProgressBar(
                    progressBars[i],
                    textViews[i],
                    (correctLabel.equals(String.valueOf((char) ('A' + i)))) ?
                            correctProgress : incorrectProgress);
        }
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

    private void updateLifeLines() {
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


    private String getCorrectLabel() {
        List<OptionsModel> optionsList = questionModel.getOptionsList();
        String correctAnswer = questionModel.getCorrectText().trim();

        for (int i = 0; i < optionsList.size(); i++) {
            OptionsModel model = optionsList.get(i);

            if (model.getOptionText().trim().equalsIgnoreCase(correctAnswer)) {
                return labelList[i];
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
            dbHelper.saveHistory(questionId, answer, correctAnswer, datePlayed, datePlayed, highScore, isCorrect);
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
        Utils.darkBlueBlink(exitButton, this);

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
                    seconds);
        }
    }

    private void animateViews() {
        View[] views = {minus2QuestionsButton, lifeLineDescriptionTextView1, askComputerButton,
                lifeLineDescriptionTextView2, votingContainer, lifeLineDescriptionTextView3,
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


    @Override
    protected void onResume() {
        super.onResume();

        if (shouldContinueGame()) {
            if (isFromProgress()) {
                showQuestion();
            } else if (isShouldRefreshQuestion()) {
                prepareQuestion();
                updateRefreshQuestionState(false);
            }
        } else {
            updateLifeLines();
            prepareQuestion();
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
        updateProgressState(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopBackgroundMusic();
        releaseAll();
        updateMusicState(false);

        long durationMillis = System.currentTimeMillis() - startTimeMillis;

        String durationString = formatDuration(durationMillis);

        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString("duration", durationString);
        editor.apply();
    }
}