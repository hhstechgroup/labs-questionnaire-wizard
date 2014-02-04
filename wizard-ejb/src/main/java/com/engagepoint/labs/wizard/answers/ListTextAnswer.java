package com.engagepoint.labs.wizard.answers;

import java.util.List;

/**
 * Created by igor.guzenko on 2/4/14.
 */
public class ListTextAnswer implements Answer<List<String>> {
    private List<String> textAnswersList;
    @Override
    public void setAnswer(List<String> stringList) {
        textAnswersList = stringList;
    }

    @Override
    public List<String> getAnswer() {
        return textAnswersList;
    }
}
