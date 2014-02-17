package com.engagepoint.labs.wizard.questions;

import java.util.List;

/**
 * Created by igor.guzenko on 2/4/14.
 */
public class MultipleChoiseQuestion extends WizardQuestion {
    private List<String> optionsList;

    public List<String> getOptionsList() {
        return optionsList;
    }

    public void setOptionsList(List<String> optionsList) {
        this.optionsList = optionsList;
    }
}
