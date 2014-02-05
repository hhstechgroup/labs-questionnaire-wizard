package com.engagepoint.labs.wizard.ui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.faces.component.UIComponent;

import org.primefaces.component.calendar.Calendar;
import org.primefaces.component.inputtext.InputText;

public class UITimeQuestion extends UIBasicQuestion {
    public static final String TYPE = "TimeQuestion";
    private Calendar calendar;
    private Date date;

    public UITimeQuestion() {
	super(TYPE);

	date=new Date();
	calendar=new Calendar();
	calendar.setTimeOnly(true);
	calendar.setPattern("HH:mm");
	calendar.setValue(date);
	uiComponent = (UIComponent) calendar;
    }

    @Override
    public void setPf_value(Object pf_value) {
	calendar.setValue(date);
	this.pf_value=pf_value;
    }

}
