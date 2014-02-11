package com.engagepoint.labs.wizard.answers;

import java.sql.Time;

/**
 * Created by igor.guzenko on 2/4/14.
 */
public class TimeAnswer implements Answer {
    private Time timeAnswer;

    @Override
    public void setAnswer(Object time) {
        timeAnswer = (Time) time;
    }

    @Override
    public Time getAnswer() {
        return timeAnswer;
    }
}
