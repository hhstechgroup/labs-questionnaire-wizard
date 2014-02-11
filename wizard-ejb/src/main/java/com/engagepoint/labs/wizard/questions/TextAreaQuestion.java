package com.engagepoint.labs.wizard.questions;

import com.engagepoint.labs.wizard.answers.TextAnswer;

/**
 * Created by igor.guzenko on 2/4/14.
 */
public class TextAreaQuestion extends WizardQuestion {

    private TextAnswer textAnswer;

    @Override
    public TextAnswer getAnswer() {
        return textAnswer;
    }

    @Override
    public void setAnswer(Object answers) {
        textAnswer = (TextAnswer) answers;
    }
}
