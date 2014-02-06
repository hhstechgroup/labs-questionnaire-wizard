package com.engagepoint.labs.wizard.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlForm;
import javax.faces.component.html.HtmlOutputText;
import javax.inject.Named;

import org.primefaces.context.RequestContext;
import org.w3c.dom.html.HTMLBRElement;

import com.engagepoint.labs.wizard.ui.UIBasicQuestion;
import com.engagepoint.labs.wizard.ui.UIMultipleChoiceQuestion;
import com.engagepoint.labs.wizard.ui.UITextAreaQuestion;
import com.engagepoint.labs.wizard.ui.UITextQuestion;
import com.engagepoint.labs.wizard.ui.UITimeQuestion;

@Named("uiComponentAddController")
@SessionScoped
public class UIComponentAddController implements Serializable {
    private static final long serialVersionUID = -7860806407082286277L;
    private HtmlForm dynaform;

    private ArrayList<UIBasicQuestion> currentUIBasicComponents;
    private int[] currentPointer;

    // TODO: Test
    private ArrayList<String> defaultStrings;
    private ArrayList<Integer> defaultInts;

    @PostConstruct
    public void init() {
	currentUIBasicComponents = new ArrayList<UIBasicQuestion>();

	currentPointer = new int[2];
	currentPointer[0] = 0;
	currentPointer[1] = 0;
	// TODO: stub here

	// TODO: Test

	defaultStrings = new ArrayList<String>();
	defaultInts = new ArrayList<Integer>();
	for (int i = 0; i < 4; i++) {
	    defaultInts.add(new Integer(i));
	    defaultStrings.add(new String("Option" + i));
	}
    }

    private void populateCurrentUIComponents() {

	UIBasicQuestion q1 = new UITextQuestion();
	UIBasicQuestion q2 = new UITextAreaQuestion();
	UIBasicQuestion q3 = new UITimeQuestion();
	UIBasicQuestion q4 = new UIMultipleChoiceQuestion(defaultStrings,
		defaultInts);

	currentUIBasicComponents.add(q1);
	currentUIBasicComponents.add(q2);
	currentUIBasicComponents.add(q3);
	currentUIBasicComponents.add(q4);

	// TODO: get model data here and convert to UIComponents
	// wakes up within button from left menu with help of currentPointer

	reloadDynaForm();
    }

    private void reloadDynaForm() {
	dynaform.getChildren().clear();
	for (int i = 0; i < currentUIBasicComponents.size(); i++) {
	    currentUIBasicComponents.get(i).postInit();
	    dynaform.getChildren().add(currentUIBasicComponents.get(i).getUiComponent());
	    HtmlOutputText linebreak = new HtmlOutputText();
	    linebreak.setValue("<br/>");
	    linebreak.setEscape(false);
	    dynaform.getChildren().add(linebreak);
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
