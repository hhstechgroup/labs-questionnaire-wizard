package com.engagepoint.labs.wizard.questions;

import com.engagepoint.labs.wizard.answers.TextAnswer;

import java.util.ArrayList;

/**
 * Created by igor.guzenko on 2/4/14.
 */
public class DropDownQuestion extends BasicQuestion<TextAnswer> {
    private TextAnswer textAnswer;
    private ArrayList<String> optionsList;

    public ArrayList<String> getOptionsList() {
        return optionsList;
    }

    public void setOptionsList(ArrayList<String> optionsList) {
        this.optionsList = optionsList;
    }

     @Override
    public TextAnswer getAnswer() {
        return textAnswer;
    }

    @Override
    public void setAnswer(TextAnswer answers) {
        textAnswer = answers;
    }
}
