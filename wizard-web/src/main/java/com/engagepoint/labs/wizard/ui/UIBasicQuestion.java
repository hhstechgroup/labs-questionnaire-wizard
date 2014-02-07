package com.engagepoint.labs.wizard.ui;

import javax.faces.component.UIComponent;

import org.primefaces.component.inputtext.InputText;

public class UIBasicQuestion {

    private String type;
    private String id;
    private String name;
    protected UIComponent uiComponent;
    protected Object pf_value;

    public UIBasicQuestion(String type, String Name) {
	this.type = type;
    }

    public String getType() {
	return type;
    }

    public UIComponent getUiComponent() {
	return uiComponent;
    }

    public Object getPf_value() {
	return pf_value;
    }

    public void setPf_value(Object pf_value) {
    }

    public String getId() {
	return uiComponent.getId();
    }

    public void setId(String id) {
	this.id = id;
    }
    
    public void postInit()
    {
	
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

}
