package com.engagepoint.labs.wizard.ui.ajax;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import org.primefaces.component.behavior.ajax.AjaxBehaviorListenerImpl;
import org.primefaces.component.calendar.Calendar;

import com.engagepoint.labs.wizard.questions.WizardQuestion;
import com.engagepoint.labs.wizard.values.DateValue;
import com.engagepoint.labs.wizard.values.Value;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class CustomAjaxBehaviorListener extends AjaxBehaviorListenerImpl {

    private static final long serialVersionUID = -2696377872787453416L;
    private WizardQuestion question;

    public CustomAjaxBehaviorListener(WizardQuestion question) {
        this.question = question;
    }

    @Override
    public void processAjaxBehavior(AjaxBehaviorEvent event)
            throws AbortProcessingException {

    }
}
