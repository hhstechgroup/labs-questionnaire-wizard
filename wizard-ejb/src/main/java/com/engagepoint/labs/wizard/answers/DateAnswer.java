package com.engagepoint.labs.wizard.answers;

import java.util.Date;

/**
 * Created by igor.guzenko on 2/4/14.
 */
public class DateAnswer implements Answer {
    private Date dateAnswer;

    @Override
    public void setAnswer(Object date) {
        dateAnswer = (Date) date;
    }

    @Override
    public Date getAnswer() {
        return dateAnswer;
    }
}
