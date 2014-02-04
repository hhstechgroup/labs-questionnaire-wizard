package com.engagepoint.labs.wizard.questions;

import com.engagepoint.labs.wizard.answers.RangeAnswer;

/**
 * Created by igor.guzenko on 2/4/14.
 */
public class RangeQuestion extends BasicQuestion implements Question<RangeAnswer> {

    private int startRange;
    private int endRange;
    private RangeAnswer rangeAnswer;

    public void setRange(Integer start,Integer end){
        startRange=start;
        endRange = end;
    }

    public int getStartRange() {
        return startRange;
    }

    public int getEndRange() {
        return endRange;
    }
    @Override
    public RangeAnswer getAnswer() {
        return rangeAnswer;
    }

    @Override
    public void setAnswer(RangeAnswer answers) {
        rangeAnswer=answers;
    }
}
