package com.engagepoint.labs.wizard.ui.validators;

import javax.faces.event.AbortProcessingException;
import javax.faces.event.AjaxBehaviorEvent;

import org.primefaces.component.behavior.ajax.AjaxBehaviorListenerImpl;

import com.engagepoint.labs.wizard.questions.WizardQuestion;

public class DateTimeValidator extends AjaxBehaviorListenerImpl {

    private static final long serialVersionUID = -2696377872787453416L;
    private WizardQuestion question;

    public DateTimeValidator(WizardQuestion question) {
	this.question = question;
    }

    @Override
    public void processAjaxBehavior(AjaxBehaviorEvent event) throws AbortProcessingException {
	System.out.println("Listener for component type " + question.getQuestionType().value() + " with ID "
		+ question.getId() + " was called.");
	
    }

}
