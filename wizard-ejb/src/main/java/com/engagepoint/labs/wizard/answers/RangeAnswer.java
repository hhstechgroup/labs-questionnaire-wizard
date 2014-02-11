package com.engagepoint.labs.wizard.answers;

/**
 * Created by igor.guzenko on 2/4/14.
 */
public class RangeAnswer implements Answer {
    Integer rangeAnswer;
    @Override
    public void setAnswer(Object integer) {
        rangeAnswer = (Integer) integer;
    }

    @Override
    public Integer getAnswer() {
        return rangeAnswer;
    }
}
