package com.engagepoint.labs.wizard.ui.ajax;

import javax.faces.event.AbortProcessingException;
import javax.faces.event.AjaxBehaviorEvent;

import org.primefaces.component.behavior.ajax.AjaxBehaviorListenerImpl;

import com.engagepoint.labs.wizard.questions.WizardQuestion;

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
