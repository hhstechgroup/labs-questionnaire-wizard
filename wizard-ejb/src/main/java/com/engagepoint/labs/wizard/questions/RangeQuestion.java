package com.engagepoint.labs.wizard.questions;

import com.engagepoint.labs.wizard.answers.RangeAnswer;

/**
 * Created by igor.guzenko on 2/4/14.
 */
public class RangeQuestion extends BasicQuestion<RangeAnswer> {

    private int startRange;
    private int endRange;
    private String value;
    private RangeAnswer rangeAnswer;

    public void setRange(Integer start, Integer end) {
        startRange = start;
        endRange = end;
    }

    public int getStartRange() {
        return startRange;
    }

    public int getEndRange() {
        return endRange;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public RangeAnswer getAnswer() {
        return rangeAnswer;
    }

    @Override
    public void setAnswer(RangeAnswer answers) {
        rangeAnswer = answers;
    }
}
