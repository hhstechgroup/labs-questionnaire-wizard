package com.engagepoint.labs.wizard.ui.validators;

import javax.faces.event.AbortProcessingException;
import javax.faces.event.AjaxBehaviorEvent;

import org.primefaces.component.behavior.ajax.AjaxBehaviorListenerImpl;
import org.primefaces.event.SlideEndEvent;

public class SliderOrdinaryValidator extends AjaxBehaviorListenerImpl {

    private static final long serialVersionUID = 4476671252320960130L;

    public SliderOrdinaryValidator() {

    }

    @Override
    public void processAjaxBehavior(AjaxBehaviorEvent event)
	    throws AbortProcessingException {
	SlideEndEvent slideEvent = (SlideEndEvent) event;

	System.out.println(slideEvent.getValue()
		+ " ===========================================");
    }

}
