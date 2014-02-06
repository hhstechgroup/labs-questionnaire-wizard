package com.engagepoint.labs.wizard.ui;

import java.util.ArrayList;

import javax.faces.component.UIComponent;

import org.primefaces.component.selectonelistbox.SelectOneListbox;

public class UIMultipleChoiceQuestion extends UIBasicQuestion {
    public static final String TYPE = "MultipleChoiceQuestion";
    private SelectOneListbox selectOneListbox;
    private ArrayList<String> labels;
    private ArrayList<Integer> values;

    public UIMultipleChoiceQuestion(ArrayList<String> labels, ArrayList<Integer> values) {
	super(TYPE);
	this.labels = labels;
	this.values = values;

	selectOneListbox = new SelectOneListbox();

	uiComponent = (UIComponent) selectOneListbox;

	for (int i = 0; i < labels.size(); i++) {
	}
    }

    @Override
    public void setPf_value(Object pf_value) {
	selectOneListbox.setValue(pf_value);
	this.pf_value = pf_value;
    }

    @Override
    public UIComponent getUiComponent() {
	return selectOneListbox;
    }

}
