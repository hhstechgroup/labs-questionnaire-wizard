package com.engagepoint.labs.wizard.answers;

/**
 * Created by igor.guzenko on 2/4/14.
 */
public class TextAnswer implements Answer<String> {
    private String dropDownAnswer;

    @Override
    public void setAnswer(String s) {
        dropDownAnswer = s;
    }

    @Override
    public String getAnswer() {
        return dropDownAnswer;
    }
}
