package com.engagepoint.labs.wizard.ui;

import javax.faces.component.UIComponent;

import org.primefaces.component.inputtext.InputText;

public class UIBasicQuestion {

    private String type;
    protected UIComponent uiComponent;
    protected String pf_value;

    public UIBasicQuestion(String type) {
	this.type = type;
    }

    public String getType() {
	return type;
    }

    public UIComponent getUiComponent() {
	return uiComponent;
    }

    public String getPf_value() {
	return pf_value;
    }

    public void setPf_value(String pf_value) {
    }

}
