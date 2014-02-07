package com.engagepoint.labs.wizard.ui;

import java.util.Date;

import javax.faces.component.UIComponent;

import org.primefaces.component.calendar.Calendar;

public class UITimeQuestion extends UIBasicQuestion {
    public static final String TYPE = "TimeQuestion";
    private Calendar calendar;
    private Date date;

    public UITimeQuestion(String name) {
	super(TYPE,name);

	date=new Date();
	calendar=new Calendar();
	calendar.setTimeOnly(true);
	calendar.setPattern("HH:mm");
	calendar.setValue(date);
	uiComponent = (UIComponent) calendar;
    }

    @Override
    public void setPf_value(Object pf_value) {
	calendar.setValue(pf_value);
	this.pf_value=pf_value;
    }

}
