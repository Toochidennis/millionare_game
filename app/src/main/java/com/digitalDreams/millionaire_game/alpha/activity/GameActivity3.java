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
import static com.digitalDreams.millionaire_game.alpha.Constants.prettyCount;

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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.digitalDreams.millionaire_game.CountDownActivity;
import com.digitalDreams.millionaire_game.DBHelper;
import com.digitalDreams.millionaire_game.FailureActivity;
import com.digitalDreams.millionaire_game.R;
import com.digitalDreams.millionaire_game.WrongAnswerDialog;
import com.digitalDreams.millionaire_game.alpha.ExplanationBottomSheetDialog;
import com.digitalDreams.millionaire_game.alpha.adapters.OnOptionsClickListener;
import com.digitalDreams.millionaire_game.alpha.adapters.OptionsAdapter;
import com.digitalDreams.millionaire_game.alpha.models.OptionsModel;
import com.digitalDreams.millionaire_game.alpha.models.QuestionModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;


public class GameActivity3 extends AppCompatActivity implements OnOptionsClickListener {


    private RelativeLayout exitButton, timerContainer, amountContainer;
    private TextView questionTextView, amountWonTextView, questionProgressTextView;

    RelativeLayout minus2QuestionsButton, askComputerButton, takeAPollButton, resetQuestionButton;
    ImageView minus2QuestionsImageView, askComputerImageView, takeAPollImageView;
    private RecyclerView optionsRecyclerView;

    private SharedPreferences sharedPreferences;
    private DBHelper dbHelper;
    private QuestionModel questionModel;
    private JSONArray questionJSONArray;
    private OptionsAdapter optionsAdapter;

    private int questionIndex = 0;
    private int numberOfFailure = 0;
    private int numberOfPassed = 0;
    private boolean optionsClickable = true;
    private boolean isMoveToNextQuestion = true;
    private List<Integer> amountList;
    private List<Integer> amountWonList = new ArrayList<>();

    private int amountWonText;


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
        optionsRecyclerView = findViewById(R.id.options_recyclerView);
        questionProgressTextView = findViewById(R.id.question_progress);


        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        dbHelper = new DBHelper(this);
    }

    private void handleViewsClick() {
        exitButton.setOnClickListener(v -> super.onBackPressed());

        timerContainer.setVisibility(View.GONE);

        minus2QuestionsButton.setOnClickListener(minus -> {
            hideTwoQuestions();
            minus2QuestionsButton.setClickable(false);
        });
    }


    private void prepareQuestion() {
        try {
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
            String question = questionObject.getString("content");
            String correct = questionObject.getString("correct");
            String reason = questionObject.getString("reason");
            String options = questionObject.getString("answer");

            JSONArray optionsArray = new JSONArray(options);

            for (int i = 0; i < optionsArray.length(); i++) {
                JSONObject optionsObject = optionsArray.getJSONObject(i);
                String option = optionsObject.getString("text");
                optionsList.add(new OptionsModel(String.valueOf(i), option));
            }

            questionModel = new QuestionModel(questionId, question, correct, reason, optionsList);

            processQuestionData();
        } catch (Exception e) {
            e.printStackTrace();
        }
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


    private void processQuestionData() {
        questionTextView.setText(questionModel.getQuestionText());
        inflateOptionsView();
    }

    private void inflateOptionsView() {
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
        String selectedAnswer = questionModel.getOptionsList().get(position).getOptionText().trim();
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

    private void hideTwoQuestions() {
        String correctAnswer = questionModel.getCorrectText();
        optionsAdapter.hideRandomOptions(correctAnswer);
        minus2QuestionsImageView.setVisibility(View.VISIBLE);
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

            isMoveToNextQuestion = false;
        }

        optionsClickable = true;
    }

    private void startProgressActivity() {
        amountWonList.add(amountList.get(numberOfPassed));
        amountWonText = amountList.get(numberOfPassed);

        saveAmountWon();
        updateSoundState(true);
        updateRefreshQuestionState(false);
        isMoveToNextQuestion = true;

        startActivity(new Intent(this, ProgressActivity2.class)
                .putIntegerArrayListExtra("amount_won", new ArrayList<>(amountWonList))
                .putExtra("should_use_timer", true));

        questionIndex++;
        numberOfPassed++;
    }


    private void showFailureDialog() {
        pauseBackgroundMusic();
        WrongAnswerDialog wrongAnswerDialog = new WrongAnswerDialog(this, questionModel);
        wrongAnswerDialog.setCancelable(false);
        wrongAnswerDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (shouldContinueGame()) {
            if (isMoveToNextQuestion) {
                showQuestion();
            } else if (isShouldRefreshQuestion()) {
                prepareQuestion();
                updateRefreshQuestionState(false);
            }
        } else {
            amountWonList.clear();
            numberOfFailure = 0;
            numberOfPassed = 0;
            questionIndex = 0;
            prepareQuestion();
        }

        if (shouldPlaySound()) {
            playBackgroundMusic(this);
            updateSoundState(false);
        }
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

    private void saveAmountWon() {
        sharedPreferences = getSharedPreferences(APPLICATION_DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("noOfCorrectAnswer", numberOfPassed);
        editor.putString("amountWon", String.valueOf(amountWonText));
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

    private boolean shouldPlaySound() {
        return sharedPreferences.getBoolean(SOUND, false);
    }

    private void updateSoundState(boolean soundState) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(SOUND, soundState);
        editor.apply();
    }

    private void updateRefreshQuestionState(boolean refreshState) {
        sharedPreferences = getSharedPreferences(APPLICATION_DATA, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(SHOULD_REFRESH_QUESTION, refreshState);
        editor.putBoolean(SHOULD_CONTINUE_GAME, !refreshState);
        editor.apply();
    }


    private void startCountDownActivity() {
        startActivity(new Intent(this, CountDownActivity.class));
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (!shouldPlaySound()) {
            pauseBackgroundMusic();
            updateSoundState(true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopBackgroundMusic();
        releaseAll();
        updateSoundState(false);
    }
}