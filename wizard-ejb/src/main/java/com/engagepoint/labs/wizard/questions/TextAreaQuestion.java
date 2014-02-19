package com.engagepoint.labs.wizard.questions;

import com.engagepoint.labs.wizard.values.TextValue;
import com.engagepoint.labs.wizard.values.Value;
import com.engagepoint.labs.wizard.values.ValueType;

/**
 * Created by igor.guzenko on 2/4/14.
 */
public class TextAreaQuestion extends WizardQuestion {
    private TextValue answer;
    private TextValue defaultAnswer;

    @Override
    public Value getAnswer() {
        return answer;
    }

    @Override
    public void setAnswer(Value answer) {
        if (answer.getType().equals(ValueType.STRING)) {
            this.answer = (TextValue) answer;

        }
    }


    @Override
    public Value getDefaultAnswer() {
        return defaultAnswer;
    }

    @Override
    public void setDefaultAnswer(Value defaultAnswer) {
        if (defaultAnswer.getType().equals(ValueType.STRING)) {
            this.defaultAnswer = (TextValue) defaultAnswer;
        }
    }
}
