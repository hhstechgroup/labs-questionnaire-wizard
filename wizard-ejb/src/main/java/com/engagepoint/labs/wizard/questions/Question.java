package com.engagepoint.labs.wizard.questions;

import com.engagepoint.labs.wizard.answers.Answer;

/**
 * Created with IntelliJ IDEA.
 * User: andrii.sotnyk
 * Date: 2/4/14
 * Time: 11:36 AM
 * To change this template use File | Settings | File Templates.
 */

public interface Question<B extends Answer> {
    public long getId();
    public void setId(long id);
    public String getTitle();
    public void setTitle(String title);
    public boolean isRequired();
    public void setRequired(boolean required);
    public String getToolTipText();
    public void setToolTipText(String toolTipText);
    public B getAnswer();
    public void setAnswer(B answers);
}
