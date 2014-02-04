package com.engagepoint.labs.wizard.questions;

import com.engagepoint.labs.wizard.answers.TextAnswer;

/**
 * Created with IntelliJ IDEA.
 * User: andrii.sotnyk
 * Date: 2/4/14
 * Time: 12:05 PM
 * To change this template use File | Settings | File Templates.
 */

public class TextQuestion extends BasicQuestion implements Question<TextAnswer> {
    private TextAnswer textAnswer;
   @Override
    public TextAnswer getAnswer() {
        return textAnswer;
    }

    @Override
    public void setAnswer(TextAnswer answers) {
        textAnswer = answers;
    }
}
