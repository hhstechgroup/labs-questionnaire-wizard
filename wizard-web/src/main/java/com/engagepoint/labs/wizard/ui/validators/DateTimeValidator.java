package com.engagepoint.labs.wizard.ui.validators;

import javax.faces.application.FacesMessage;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.validator.ValidatorException;

import org.primefaces.component.behavior.ajax.AjaxBehaviorListenerImpl;
import org.primefaces.component.calendar.Calendar;

import com.engagepoint.labs.wizard.questions.WizardQuestion;
import com.engagepoint.labs.wizard.values.DateValue;
import com.engagepoint.labs.wizard.values.Value;

public class DateTimeValidator extends AjaxBehaviorListenerImpl {

    private static final long serialVersionUID = -2696377872787453416L;
    private WizardQuestion question;

    public DateTimeValidator(WizardQuestion question) {
	this.question = question;
    }

    @Override
    public void processAjaxBehavior(AjaxBehaviorEvent event)
	    throws AbortProcessingException {
	Calendar component = (Calendar) event.getComponent();
	Object value = component.getValue();
	System.out.println("Listener for component type "
		+ question.getQuestionType().value() + " with ID "
		+ question.getId() + " was called.");

	if (question.isRequired()) {
	    if (value == null) {
		question.setValid(false);
		throw new ValidatorException(new FacesMessage(
			FacesMessage.SEVERITY_ERROR, "Validation Error",
			"Answer must be selected for this question!"));
	    }
	}
	question.setValid(true);
	Value dateValue = new DateValue();
	dateValue.setValue(value);
	question.setAnswer(dateValue);
    }

}
