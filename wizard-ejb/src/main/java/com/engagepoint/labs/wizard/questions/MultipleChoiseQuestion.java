package com.engagepoint.labs.wizard.questions;

import java.util.ArrayList;

/**
 * Created by igor.guzenko on 2/4/14.
 */
public class MultipleChoiseQuestion extends TextQuestion{
    private ArrayList<String> optionsList;

    public ArrayList<String> getOptionsList() {
        return optionsList;
    }

    public void setOptionsList(ArrayList<String> optionsList) {
        this.optionsList = optionsList;
    }

}
