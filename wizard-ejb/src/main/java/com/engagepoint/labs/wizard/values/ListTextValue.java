package com.engagepoint.labs.wizard.values;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: andrii.sotnyk
 * Date: 2/17/14
 * Time: 2:41 PM
 * To change this template use File | Settings | File Templates.
 */

public class ListTextValue extends Value {
    private List<String> textAnswersList;

    public List<String> getTextAnswersList() {
        return textAnswersList;
    }

    public void setTextAnswersList(List<String> textAnswersList) {
        this.textAnswersList = textAnswersList;
    }
}
