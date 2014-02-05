package com.engagepoint.labs.wizard.questions;

import com.engagepoint.labs.wizard.answers.Answer;
import java.util.List;
import super_binding.DependentQuestion;

/**
 * Created by igor.guzenko on 2/4/14.
 */
public abstract class BasicQuestion<B extends Answer> {

    protected long id;
    protected String title;
    protected QuestionType questionType;
    protected boolean required;
    protected String toolTipText;
    private List<DependentQuestion> dependentQuestionsList; // need to be done later!!!

    public String getToolTipText() {
        return toolTipText;
    }

    public void setToolTipText(String toolTipText) {
        this.toolTipText = toolTipText;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
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

    public void setId(long id) {
        this.id = id;
    }

    public abstract B getAnswer();

    public abstract void setAnswer(B answers);
}
