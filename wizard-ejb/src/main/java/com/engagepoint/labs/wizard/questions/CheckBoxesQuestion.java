package com.engagepoint.labs.wizard.questions;

import com.engagepoint.labs.wizard.answers.ListTextAnswer;

import java.util.ArrayList;

/**
 * Created by igor.guzenko on 2/4/14.
 */
public class CheckBoxesQuestion extends BasicQuestion<ListTextAnswer> {
    private ListTextAnswer listTextAnswer;
    private ArrayList<String> optionsList;
    public ArrayList<String> getOptionsList() {
        return optionsList;
    }

    public void setOptionsList(ArrayList<String> optionsList) {
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
