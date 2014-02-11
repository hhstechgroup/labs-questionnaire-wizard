package com.engagepoint.labs.wizard.answers;

/**
 * Created by igor.guzenko on 2/4/14.
 */
public class TextAnswer implements Answer {
    private String textAnswer;

    @Override
    public void setAnswer(Object s) {
        textAnswer = s.toString();
    }

    @Override
    public String getAnswer() {
        return textAnswer;
    }
}
