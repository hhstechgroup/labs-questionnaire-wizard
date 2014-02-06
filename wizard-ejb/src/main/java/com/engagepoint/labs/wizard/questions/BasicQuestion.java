package com.engagepoint.labs.wizard.questions;

import com.engagepoint.labs.wizard.answers.Answer;
import java.util.List;
import super_binding.DependentQuestion;
import super_binding.QType;

/**
 * Created by igor.guzenko on 2/4/14.
 */
public abstract class BasicQuestion<B extends Answer> {

    protected Integer id;
    protected String title;
    protected QType questionType;
    protected String helpText;
    private List<DependentQuestion> dependentQuestionsList; // need to be done later!!!
    protected boolean answerRequired;

    public abstract B getAnswer();

    public abstract void setAnswer(B answers);

    public String getHelpText() {
        return helpText;
    }

    public void setHelpText(String helpText) {
        this.helpText = helpText;
    }

    public boolean isRequired() {
        return answerRequired;
    }

    public void setAnswerRequired(boolean answerRequired) {
        this.answerRequired = answerRequired;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public QType getQuestionType() {
        return questionType;
    }

    public void setQuestionType(QType questionType) {
        this.questionType = questionType;
    }

}
