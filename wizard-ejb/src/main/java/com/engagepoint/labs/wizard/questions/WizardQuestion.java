package com.engagepoint.labs.wizard.questions;

import com.engagepoint.labs.wizard.values.Value;
import super_binding.QType;

/**
 * Created by igor.guzenko on 2/4/14.
 *
 *
 */
public class WizardQuestion {

    protected String id;
    protected String title;
    protected QType questionType;
    protected String helpText;
    protected Boolean answerRequired;
    protected Value answer;
    protected Value defaultAnswer;

    public Value getAnswer() {
        return answer;
    }

    public void setAnswer(Value answer) {
        this.answer = answer;
    }

    public Value getDefaultAnswer() {
        return defaultAnswer;
    }

    public void setDefaultAnswer(Value defaultAnswer) {
        this.defaultAnswer = defaultAnswer;
    }

    public String getHelpText() {
        return helpText;
    }

    public void setHelpText(String helpText) {
        this.helpText = helpText;
    }

    public boolean isRequired() {
        return answerRequired;
    }

    public void setAnswerRequired(Boolean answerRequired) {
        this.answerRequired = answerRequired;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public QType getQuestionType() {
        return questionType;
    }

    public void setQuestionType(QType questionType) {
        this.questionType = questionType;
    }

}
