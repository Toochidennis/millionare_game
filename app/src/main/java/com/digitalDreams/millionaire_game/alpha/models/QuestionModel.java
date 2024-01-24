package com.digitalDreams.millionaire_game.alpha.models;

import java.util.List;

public class QuestionModel {
    private String questionId;
    private String questionText;
    private String correctText;
    private List<OptionsModel> optionsList;


    public QuestionModel(String questionText, String correctText, List<OptionsModel> optionsList) {
        this.questionText = questionText;
        this.correctText = correctText;
        this.optionsList = optionsList;
    }


    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getCorrectText() {
        return correctText;
    }

    public void setCorrectText(String correctText) {
        this.correctText = correctText;
    }

    public List<OptionsModel> getOptionsList() {
        return optionsList;
    }

    public void setOptionsList(List<OptionsModel> optionsList) {
        this.optionsList = optionsList;
    }
}
