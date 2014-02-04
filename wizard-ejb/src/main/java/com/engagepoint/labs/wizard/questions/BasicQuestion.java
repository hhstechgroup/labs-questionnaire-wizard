package com.engagepoint.labs.wizard.questions;

/**
 * Created by igor.guzenko on 2/4/14.
 */
public class BasicQuestion {
    protected long id;
    protected String title;
    protected boolean required;
    protected String toolTipText;

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


}
