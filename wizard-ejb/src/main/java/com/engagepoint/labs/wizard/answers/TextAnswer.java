package com.engagepoint.labs.wizard.answers;

/**
 * Created by igor.guzenko on 2/4/14.
 */
public class TextAnswer implements Answer<String> {
    private String textAnswer;

    @Override
    public void setAnswer(String s) {
        textAnswer = s;
    }

    @Override
    public String getAnswer() {
        return textAnswer;
    }
}
