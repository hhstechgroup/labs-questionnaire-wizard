package com.engagepoint.labs.wizard.answers;

import java.util.Date;

/**
 * Created by igor.guzenko on 2/4/14.
 */
public class DateAnswer implements Answer<Date> {
    private Date dateAnswer;
    @Override
    public void setAnswer(Date date) {
        dateAnswer=date;
    }

    @Override
    public Date getAnswer() {
        return dateAnswer;
    }
}
