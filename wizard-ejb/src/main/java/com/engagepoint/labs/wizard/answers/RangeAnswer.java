package com.engagepoint.labs.wizard.answers;

/**
 * Created by igor.guzenko on 2/4/14.
 */
public class RangeAnswer implements Answer<Integer> {
    int rangeAnswer;
    @Override
    public void setAnswer(Integer integer) {
        rangeAnswer = integer;
    }

    @Override
    public Integer getAnswer() {
        return rangeAnswer;
    }
}
