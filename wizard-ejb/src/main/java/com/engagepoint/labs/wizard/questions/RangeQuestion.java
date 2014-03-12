package com.engagepoint.labs.wizard.questions;

import com.engagepoint.labs.wizard.values.RangeValue;
import com.engagepoint.labs.wizard.values.Value;
import com.engagepoint.labs.wizard.values.ValueType;

/**
 * Created by igor.guzenko on 2/4/14.
 */
public class RangeQuestion extends WizardQuestion {
    private int startRange;
    private int endRange;
    private RangeValue answer;
    private RangeValue defaultAnswer;


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


    @Override
    public Value getAnswer() {
        return answer;
    }

    @Override
    public void setAnswer(Value answer) {
        if (answer.getType().equals(ValueType.RANGE)) {
            this.answer = (RangeValue) answer;
        }
    }

    @Override
    public Value getDefaultAnswer() {
        return defaultAnswer;
    }

    @Override
    public void setDefaultAnswer(Value defaultAnswer) {
        if (defaultAnswer.getType().equals(ValueType.RANGE)) {
            this.defaultAnswer = (RangeValue) defaultAnswer;
        }
    }

    @Override
    public void resetAnswer() {
        answer = null;
    }
}
