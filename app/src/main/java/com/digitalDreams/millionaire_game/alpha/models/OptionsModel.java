package com.digitalDreams.millionaire_game.alpha.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class OptionsModel implements Parcelable {

    private String optionId;
    private String optionText;


    public OptionsModel(String optionId, String optionText) {
        this.optionId = optionId;
        this.optionText = optionText;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    protected OptionsModel(Parcel in) {
        optionId = in.readString();
        optionText = in.readString();
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(optionId);
        dest.writeString(optionText);
    }

    public static final Creator<OptionsModel> CREATOR = new Creator<OptionsModel>() {
        @Override
        public OptionsModel createFromParcel(Parcel source) {
            return new OptionsModel(source);
        }

        @Override
        public OptionsModel[] newArray(int size) {
            return new OptionsModel[size];
        }
    };

    @Override
    public int hashCode() {
        return optionId.hashCode();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        OptionsModel that = (OptionsModel) obj;

        return optionId.equals(that.optionId);
    }

    public String getOptionText() {
        return optionText;
    }


}
