package com.digitalDreams.millionaire_game.alpha.models;

import java.io.Serializable;
import java.util.List;

public class QuestionModel implements Serializable {
    private String questionId;
    private String questionText;
    private String correctText;
    private String reasonText;
    private List<OptionsModel> optionsList;

    public QuestionModel() {
    }

    public QuestionModel(String questionId, String questionText, String correctText, String reasonText, List<OptionsModel> optionsList) {
        this.questionId = questionId;
        this.questionText = questionText;
        this.correctText = correctText;
        this.optionsList = optionsList;
        this.reasonText = reasonText;
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

    public String getReasonText() {
        return reasonText;
    }

    public List<OptionsModel> getOptionsList() {
        return optionsList;
    }

    public void setOptionsList(List<OptionsModel> optionsList) {
        this.optionsList = optionsList;
    }
}
