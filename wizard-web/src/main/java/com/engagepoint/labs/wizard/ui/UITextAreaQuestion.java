package com.engagepoint.labs.wizard.ui;

import javax.faces.component.UIComponent;

import org.primefaces.component.inputtextarea.InputTextarea;

public class UITextAreaQuestion extends UIBasicQuestion {

    public static final String TYPE = "TextAreaQuestion";
    private InputTextarea inputTextArea;

    public UITextAreaQuestion() {
	super(TYPE);

	inputTextArea = new InputTextarea();
	inputTextArea.setValue(TYPE);
	uiComponent = (UIComponent) inputTextArea;
    }

    @Override
    public void setPf_value(Object pf_value) {
	inputTextArea.setValue((String)pf_value);
	this.pf_value = pf_value;
    }

}
