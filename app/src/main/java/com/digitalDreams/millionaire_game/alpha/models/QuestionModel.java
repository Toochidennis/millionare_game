package com.digitalDreams.millionaire_game.alpha.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class QuestionModel implements Parcelable {
    private String questionId;
    private String questionText;
    private String correctText;
    private String reasonText;
    private List<OptionsModel> options;

    public QuestionModel() {
    }

    public QuestionModel(String questionId, String questionText, String correctText, String reasonText, List<OptionsModel> options) {
        this.questionId = questionId;
        this.questionText = questionText;
        this.correctText = correctText;
        this.options = options;
        this.reasonText = reasonText;
    }

    protected QuestionModel(Parcel in) {
        questionId = in.readString();
        questionText = in.readString();
        correctText = in.readString();
        reasonText = in.readString();
        options = new ArrayList<>();
        in.readList(options, OptionsModel.class.getClassLoader());
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(questionId);
        dest.writeString(questionText);
        dest.writeString(correctText);
        dest.writeString(reasonText);
        dest.writeList(options);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<QuestionModel> CREATOR = new Creator<QuestionModel>() {
        @Override
        public QuestionModel createFromParcel(Parcel source) {
            return new QuestionModel(source);
        }

        @Override
        public QuestionModel[] newArray(int size) {
            return new QuestionModel[size];
        }
    };

    @Override
    public int hashCode() {
        return questionId.hashCode();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        QuestionModel that = (QuestionModel) obj;

        return questionId.equals(that.questionId);
    }

    public String getQuestionId() {
        return questionId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String getCorrectText() {
        return correctText;
    }

    public String getReasonText() {
        return reasonText;
    }

    public List<OptionsModel> getOptions() {
        return options;
    }

}
