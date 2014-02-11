package com.engagepoint.labs.wizard.questions;

import com.engagepoint.labs.wizard.answers.TextAnswer;

import java.util.List;

/**
 * Created by igor.guzenko on 2/4/14.
 */
public class MultipleChoiseQuestion extends WizardQuestion {

    private TextAnswer textAnswer;
    private List<String> optionsList;

    public List<String> getOptionsList() {
        return optionsList;
    }

    public void setOptionsList(List<String> optionsList) {
        this.optionsList = optionsList;
    }

    @Override
    public TextAnswer getAnswer() {
        return textAnswer;
    }

    @Override
    public void setAnswer(Object answers) {
        textAnswer = (TextAnswer) answers;
    }
}
