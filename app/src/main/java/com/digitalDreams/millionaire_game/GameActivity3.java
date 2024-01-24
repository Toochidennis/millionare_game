package com.digitalDreams.millionaire_game;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.digitalDreams.millionaire_game.alpha.adapters.OptionsAdapter;
import com.digitalDreams.millionaire_game.alpha.models.OptionsModel;
import com.digitalDreams.millionaire_game.alpha.models.QuestionModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GameActivity3 extends AppCompatActivity {


    private RelativeLayout exitButton, timerContainer, amountContainer;
    private TextView questionTextView;
    private RecyclerView optionsRecyclerView;

    private SharedPreferences sharedPreferences;
    private DBHelper dbHelper;
    private QuestionModel questionModel;
    private JSONArray questionJSONArray;

    private int questionIndex = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game3);

        initViews();
    }


    private void initViews() {
        exitButton = findViewById(R.id.exitBtn);
        timerContainer = findViewById(R.id.timer_container);
        amountContainer = findViewById(R.id.amount_container);
        questionTextView = findViewById(R.id.question_text);
        optionsRecyclerView = findViewById(R.id.options_recyclerView);

        sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE);
        dbHelper = new DBHelper(this);

        initDisplay();
    }

    private void initDisplay() {
        setRootViewBackgroundColor();
        prepareData();

        handleViewsClick();
    }

    private void handleViewsClick() {
        exitButton.setOnClickListener(v -> super.onBackPressed());

        timerContainer.setVisibility(View.GONE);

        amountContainer.setOnClickListener(amount -> {
            if (questionIndex <= 14) {
                if (questionIndex == 14) {
                    questionIndex = 0;
                }
                parseQuestionJSONArray(questionIndex);
            }

            questionIndex++;
        });
    }


    private void prepareData() {
        try {
            String questionsJson = dbHelper.buildJson();
            Log.d("question", questionsJson);

            JSONArray jsonArray = new JSONArray(questionsJson);
            JSONObject jsonObject = jsonArray.getJSONObject(0).getJSONObject("q");
            questionJSONArray = jsonObject.getJSONArray("0");

            parseQuestionJSONArray(0);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void parseQuestionJSONArray(int questionIndex) {
        try {
            List<OptionsModel> optionsList = new ArrayList<>();

            JSONObject questionObject = questionJSONArray.getJSONObject(questionIndex);
            String question = questionObject.getString("content");
            String correct = questionObject.getString("correct");
            String options = questionObject.getString("answer");

            JSONArray optionsArray = new JSONArray(options);

            for (int i = 0; i < optionsArray.length(); i++) {
                JSONObject optionsObject = optionsArray.getJSONObject(i);
                String option = optionsObject.getString("text");
                optionsList.add(new OptionsModel(String.valueOf(i), option));
            }

            questionModel = new QuestionModel(question, correct, optionsList);

            processQuestionData();

        } catch (Exception e) {
            e.printStackTrace();
        }
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
        OptionsAdapter optionsAdapter = new OptionsAdapter(questionModel.getOptionsList());

        optionsRecyclerView.hasFixedSize();
        optionsRecyclerView.setAdapter(optionsAdapter);
    }

}