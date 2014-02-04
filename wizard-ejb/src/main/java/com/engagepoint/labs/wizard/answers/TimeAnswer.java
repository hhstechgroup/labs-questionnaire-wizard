package com.engagepoint.labs.wizard.answers;

import java.sql.Time;

/**
 * Created by igor.guzenko on 2/4/14.
 */
public class TimeAnswer implements Answer<Time> {
    private Time timeAnswer;

    @Override
    public void setAnswer(Time time) {
        timeAnswer = time;
    }

    @Override
    public Time getAnswer() {
        return timeAnswer;
    }
}
