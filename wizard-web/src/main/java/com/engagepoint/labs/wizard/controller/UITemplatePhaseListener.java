package com.engagepoint.labs.wizard.controller;

import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpServletResponse;

public class UITemplatePhaseListener implements PhaseListener {

    private static final long serialVersionUID = -3163849709829451995L;
    private final String FORM_MENU = "form_menu";
    private final String FORM_CONTENT = "form_content";
    
    @Override
    public void afterPhase(PhaseEvent event) {
	// TODO Auto-generated method stub
	
	System.out.println("After phase");
    }

    @Override
    public void beforePhase(PhaseEvent event) {
	FacesContext facesContext = event.getFacesContext();
	HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
	response.addHeader("Pragma", "no-cache");
	response.addHeader("Cache-Control", "no-cache");
	// Stronger according to blog comment below that references HTTP spec
	response.addHeader("Cache-Control", "no-store");
	response.addHeader("Cache-Control", "must-revalidate");
	// some date in the past
	response.addHeader("Expires", "Mon, 8 Aug 2006 10:00:00 GMT");
	
	System.out.println("Before phase");
    }

    @Override
    public PhaseId getPhaseId() {
	return PhaseId.RENDER_RESPONSE;
    }

}
