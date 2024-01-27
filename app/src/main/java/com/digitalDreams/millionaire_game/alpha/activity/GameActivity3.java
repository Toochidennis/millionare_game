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
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
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


public class GameActivity3 extends AppCompatActivity implements OnOptionsClickListener {


    private RelativeLayout exitButton, timerContainer, amountContainer;
    private TextView questionTextView, amountWonTextView, questionProgressTextView;

    RelativeLayout minus2QuestionsButton, askComputerButton, takeAPollButton, resetQuestionButton;
    RelativeLayout askComputerContainer;
    TextView suggestionTextView, optionTextView;
    LinearLayout votingContainer;
    ProgressBar progressBarA, progressBarB, progressBarC, progressBarD;
    TextView textViewA, textViewB, textViewC, textViewD;
    ImageView minus2QuestionsImageView, askComputerImageView, takeAPollImageView, refreshImageView, refreshVideoImageView;
    private RecyclerView optionsRecyclerView;

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

    private void initializeGame() {
        startCountDownActivity();
        initializeViews();
        setRootViewBackgroundColor();
        prepareQuestion();
        handleViewsClick();
    }


    private void initializeViews() {
        setContentView(R.layout.activity_game3);

        exitButton = findViewById(R.id.exitBtn);
        timerContainer = findViewById(R.id.timer_container);
        amountContainer = findViewById(R.id.amount_container);
        amountWonTextView = findViewById(R.id.amount_won_text);
        questionTextView = findViewById(R.id.question_text);
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
        refreshImageView = findViewById(R.id.refresh_imageview);
        refreshVideoImageView = findViewById(R.id.video_imageview);
        optionsRecyclerView = findViewById(R.id.options_recyclerView);
        questionProgressTextView = findViewById(R.id.question_progress);


        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        dbHelper = new DBHelper(this);
    }

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
    }


    private void prepareQuestion() {
        try {
            sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
            String gameLevel = sharedPreferences.getString("game_level", "1");
            int level = Integer.parseInt(gameLevel);
            Log.d("level", gameLevel);
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


    @Override
    public void onOptionClick(int position, View itemView) {
        if (optionsClickable) {
            itemView.setBackground(getBackgroundDrawable(ORANGE, itemView));
            itemView.postDelayed(() -> handleUserSelection(position, itemView), DELAY_INTERVAL_LONG);
            optionsClickable = false;
        }
    }

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
        Log.d("amount", String.valueOf(amountWonText));

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

    @Override
    protected void onResume() {
        super.onResume();

        if (shouldContinueGame()) {
            if (isFromProgress()) {
                showQuestion();
                Log.d("Game", "2");
            } else if (isShouldRefreshQuestion()) {
                prepareQuestion();
                updateRefreshQuestionState(false);
                Log.d("Game", "3");
            }
            Log.d("Game", "1");
        } else {
            updateLifeLines();
            prepareQuestion();
            Log.d("Game", "4");
        }
        Log.d("Game", "5");

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
    }
}