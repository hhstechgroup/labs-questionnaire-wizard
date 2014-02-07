package com.engagepoint.labs.wizard.questions;

import com.engagepoint.labs.wizard.answers.ListTextAnswer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by igor.guzenko on 2/4/14.
 */
public class CheckBoxesQuestion extends WizardQuestion<ListTextAnswer> {

    private ListTextAnswer listTextAnswer;
    private List<String> optionsList;

    public List<String> getOptionsList() {
        return optionsList;
    }

    public void setOptionsList(List<String> optionsList) {
        this.optionsList = optionsList;
    }

    @Override
    public ListTextAnswer getAnswer() {
        return listTextAnswer;
    }

    @Override
    public void setAnswer(ListTextAnswer answers) {
        listTextAnswer = answers;
    }
}
