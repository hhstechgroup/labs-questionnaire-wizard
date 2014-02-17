package com.engagepoint.labs.wizard.questions;

import com.engagepoint.labs.wizard.values.TextValue;
import com.engagepoint.labs.wizard.values.Value;
import com.engagepoint.labs.wizard.values.ValueType;

/**
 * Created with IntelliJ IDEA.
 * User: andrii.sotnyk
 * Date: 2/4/14
 * Time: 12:05 PM
 * To change this template use File | Settings | File Templates.
 */

public class TextQuestion extends WizardQuestion {
    private TextValue answer;
    private TextValue defaultAnswer;


    @Override
    public Value getAnswer() {
        return answer;
    }

    @Override
    public void setAnswer(Value answer) {
        if(answer.getType().equals(ValueType.STRING)){
            this.answer = (TextValue)answer;

        }
    }


    @Override
    public Value getDefaultAnswer() {
        return defaultAnswer;
    }

    @Override
    public void setDefaultAnswer(Value defaultAnswer) {
        if(defaultAnswer.getType().equals(ValueType.STRING)){
            this.defaultAnswer = (TextValue)defaultAnswer;
        }
    }
}
