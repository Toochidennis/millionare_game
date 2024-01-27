package com.digitalDreams.millionaire_game.alpha.models;

import java.io.Serializable;

public class OptionsModel implements Serializable {

    private String optionId;
    private String optionText;


    public OptionsModel(String optionId, String optionText) {
        this.optionId = optionId;
        this.optionText = optionText;
    }

    public String getOptionId() {
        return optionId;
    }

    public void setOptionId(String optionId) {
        this.optionId = optionId;
    }

    public String getOptionText() {
        return optionText;
    }

    public void setOptionText(String optionText) {
        this.optionText = optionText;
    }

}
