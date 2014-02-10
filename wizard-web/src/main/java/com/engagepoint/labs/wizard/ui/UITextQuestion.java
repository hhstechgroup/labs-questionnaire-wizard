package com.engagepoint.labs.wizard.ui;

import javax.faces.component.UIComponent;

import org.primefaces.component.inputtext.InputText;

public class UITextQuestion extends UIBasicQuestion {

    public static final String TYPE = "TextQuestion";
    private InputText inputText;

    public UITextQuestion(String name) {
	super(TYPE,name);

	inputText = new InputText();
	inputText.setValue(name);
	uiComponent = (UIComponent) inputText;
    }

    @Override
    public void setPf_value(Object pf_value) {
	inputText.setValue((String)pf_value);
	this.pf_value=pf_value;
    }

}
