package com.engagepoint.labs.wizard.questions;

import com.engagepoint.labs.wizard.answers.TimeAnswer;

/**
 * Created by igor.guzenko on 2/4/14.
 */
public class TimeQuestion extends BasicQuestion<TimeAnswer> {

    private TimeAnswer timeAnswer;

    @Override
    public TimeAnswer getAnswer() {
        return timeAnswer;
    }

    @Override
    public void setAnswer(TimeAnswer answers) {
        timeAnswer = answers;
    }
}
