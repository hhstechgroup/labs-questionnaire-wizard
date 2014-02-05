package com.engagepoint.labs.wizard.controller;

import java.io.Serializable;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.html.HtmlForm;
import javax.inject.Named;

import com.engagepoint.labs.wizard.ui.UIBasicQuestion;
import com.engagepoint.labs.wizard.ui.UITextQuestion;

@Named("uiComponentAddController")
@SessionScoped
public class UIComponentAddController implements Serializable {
    private static final long serialVersionUID = -7860806407082286277L;
    private HtmlForm dynaform;
    private ArrayList<UIBasicQuestion> currentUIComponents;
    private int[] currentPointer;

    @PostConstruct
    public void init() {
	currentUIComponents = new ArrayList<UIBasicQuestion>();

	currentPointer = new int[2];
	currentPointer[0] = 0;
	currentPointer[1] = 0;
	// TODO: stub here
    }

    private void populateCurrentUIComponents() {

	UIBasicQuestion q1 = new UITextQuestion();
	q1.setPf_value("input 1");

	UIBasicQuestion q2 = new UITextQuestion();
	q2.setPf_value("input 2");

	currentUIComponents.add(q1);
	currentUIComponents.add(q2);

	// TODO: get model data here and convert to UIComponents
	// wakes up within button from left menu with help of currentPointer

	reloadDynaForm();
    }

    private void reloadDynaForm() {
	for (int i = 0; i < currentUIComponents.size(); i++) {
	    dynaform.getChildren().add(currentUIComponents.get(i).getUiComponent());
	}
    }

    public HtmlForm getDynaform() {
	if (dynaform == null) {
	    dynaform = new HtmlForm();
	}
	return dynaform;
    }

    public void setDynaform(HtmlForm dynaform) {
	this.dynaform = dynaform;
    }

    public void updateDynaform() {
	populateCurrentUIComponents();
    }
}
