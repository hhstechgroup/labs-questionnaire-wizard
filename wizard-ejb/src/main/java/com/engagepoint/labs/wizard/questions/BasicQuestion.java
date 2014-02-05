package com.engagepoint.labs.wizard.questions;

import com.engagepoint.labs.wizard.answers.Answer;
import java.util.List;
import super_binding.DependentQuestion;

/**
 * Created by igor.guzenko on 2/4/14.
 */
public abstract class BasicQuestion<B extends Answer> {

    protected Integer id;
    protected String title;
    protected QuestionType questionType;
    protected String helpText;
    private List<DependentQuestion> dependentQuestionsList; // need to be done later!!!
    protected boolean answerRequired;

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

    public QuestionType getQuestionType() {
        return questionType;
    }

    public void setQuestionType(QuestionType questionType) {
        this.questionType = questionType;
    }

    public abstract B getAnswer();

    public abstract void setAnswer(B answers);
}
