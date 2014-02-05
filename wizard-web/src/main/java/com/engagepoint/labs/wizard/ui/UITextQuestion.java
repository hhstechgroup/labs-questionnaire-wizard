package com.engagepoint.labs.wizard.ui;

import javax.faces.component.UIComponent;

import org.primefaces.component.inputtext.InputText;

public class UITextQuestion extends UIBasicQuestion {

    public static final String TYPE = "textQuestion";
    private InputText inputText;

    public UITextQuestion() {
	super(TYPE);

	inputText = new InputText();
	inputText.setValue(new String("inputText"));
	uiComponent = (UIComponent) inputText;
    }

    @Override
    public void setPf_value(String pf_value) {
	inputText.setValue(pf_value);
	this.pf_value=pf_value;
    }

}
