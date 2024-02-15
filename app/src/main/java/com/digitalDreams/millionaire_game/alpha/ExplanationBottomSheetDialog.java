package com.digitalDreams.millionaire_game.alpha;

import static com.digitalDreams.millionaire_game.alpha.Constants.getLabelFromList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.digitalDreams.millionaire_game.R;
import com.digitalDreams.millionaire_game.WebViewActivity;
import com.digitalDreams.millionaire_game.alpha.models.OptionsModel;
import com.digitalDreams.millionaire_game.alpha.models.QuestionModel;
import com.digitalDreams.millionaire_game.alpha.testing.database.Question;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

public class ExplanationBottomSheetDialog extends BottomSheetDialog {

    private QuestionModel questionModel;
    private Question question;

    private TextView labelTextView, optionTextView, reasonTextView;
    RelativeLayout exitButton, readMoreButton, nextQuestionButton;

    public ExplanationBottomSheetDialog(@NonNull Context context, QuestionModel questionModel) {
        super(context);
        this.questionModel = questionModel;
    }

    public ExplanationBottomSheetDialog(@NonNull Context context, Question question) {
        super(context);
        this.question = question;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_explanation_bottom_sheet);

        if (getWindow() != null) {
            getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        initViews();
    }


    private void initViews() {
        labelTextView = findViewById(R.id.label_text);
        optionTextView = findViewById(R.id.option_text);
        reasonTextView = findViewById(R.id.reason);
        exitButton = findViewById(R.id.close_dialog);
        readMoreButton = findViewById(R.id.read_more);
        nextQuestionButton = findViewById(R.id.next_question);

        initDisplay();
    }


    private void initDisplay() {
        if (questionModel != null) {
            labelTextView.setText(getLabel());
            optionTextView.setText(questionModel.getCorrectText());
            reasonTextView.setText(questionModel.getReasonText());
        } else {
            labelTextView.setText(getLabel());
            optionTextView.setText(question.getCorrectAnswer());
            reasonTextView.setText(question.getReason());
        }

        handleViewClicks();
    }

    private void handleViewClicks() {
        exitButton.setOnClickListener(exit -> dismiss());

        readMoreButton.setOnClickListener(read -> {
            Intent intent = new Intent(getContext(), WebViewActivity.class);
            if (question == null) {
                intent.putExtra("questionId", questionModel.getQuestionId());
            } else {
                intent.putExtra("questionId", question.getQuestionId());
            }

            getContext().startActivity(intent);
        });

        nextQuestionButton.setOnClickListener(next -> dismiss());
    }

    private String getLabel() {
        List<OptionsModel> optionsList = new ArrayList<>();
        String correctAnswer;

        if (question != null) {
            Question.Options options = question.getOptions();
            optionsList.add(new OptionsModel(options.getA()));
            optionsList.add(new OptionsModel(options.getB()));
            optionsList.add(new OptionsModel(options.getC()));
            optionsList.add(new OptionsModel(options.getD()));

            correctAnswer = question.getCorrectAnswer().trim();
        } else {
            optionsList = questionModel.getOptions();
            correctAnswer = questionModel.getCorrectText().trim();
        }

        for (int i = 0; i < optionsList.size(); i++) {
            OptionsModel model = optionsList.get(i);

            if (model.getOptionText().trim().equalsIgnoreCase(correctAnswer)) {
                return getLabelFromList(getContext(), i);
            }
        }

        return "";
    }

}