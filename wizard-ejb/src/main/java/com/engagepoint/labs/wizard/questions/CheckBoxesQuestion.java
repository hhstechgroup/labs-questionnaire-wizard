package com.engagepoint.labs.wizard.questions;

import com.engagepoint.labs.wizard.values.ListTextValue;
import com.engagepoint.labs.wizard.values.Value;
import com.engagepoint.labs.wizard.values.ValueType;

import java.util.List;

/**
 * Created by igor.guzenko on 2/4/14.
 */
public class CheckBoxesQuestion extends WizardQuestion {

    private List<String> optionsList;
    private ListTextValue answer;
    private ListTextValue defaultAnswer;

    public List<String> getOptionsList() {
        return optionsList;
    }

    public void setOptionsList(List<String> optionsList) {
        this.optionsList = optionsList;
    }

    @Override
    public Value getAnswer() {
        return answer;
    }

    @Override
    public void setAnswer(Value answer) {
        if (answer.getType().equals(ValueType.LIST)) {
            this.answer = (ListTextValue) answer;
        }

    }

    @Override
    public Value getDefaultAnswer() {
        return defaultAnswer;
    }

    @Override
    public void setDefaultAnswer(Value defaultAnswer) {
        if (defaultAnswer.getType().equals(ValueType.LIST)) {
            this.defaultAnswer = (ListTextValue) defaultAnswer;
        }

    }
}
