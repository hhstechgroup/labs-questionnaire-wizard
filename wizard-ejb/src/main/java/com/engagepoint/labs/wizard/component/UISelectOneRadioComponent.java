package com.engagepoint.labs.wizard.component;

import java.io.IOException;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

@FacesComponent("com.engagepoint.labs.wizard.component")
public class UISelectOneRadioComponent extends UIComponentBase {

    public static final String COMPONENT_TYPE = "com.engagepoint.labs.wizard.component";

    private String value = null;
    private String name = null;
    private String overrideName = null;
    private String styleClass = null;
    private String style = null;
    private String disabled = null;
    private String itemLabel = null;
    private String itemValue = null;
    private String onClick = null;
    private String onMouseOver = null;
    private String onMouseOut = null;
    private String onFocus = null;
    private String onBlur = null;

    public Object saveState(FacesContext context) {
	Object[] values = new Object[13];
	values[0] = super.saveState(context);
	values[1] = styleClass;
	values[2] = style;
	values[3] = disabled;
	values[4] = itemLabel;
	values[5] = itemValue;
	values[6] = onClick;
	values[7] = onMouseOver;
	values[8] = onMouseOut;
	values[9] = onFocus;
	values[10] = onBlur;
	values[11] = name;
	values[12] = overrideName;

	return (values);
    }

    public void restoreState(FacesContext context, Object state) {
	Object[] values = (Object[]) state;
	super.restoreState(context, values[0]);
	styleClass = (String) values[1];
	style = (String) values[2];
	disabled = (String) values[3];
	itemLabel = (String) values[4];
	itemValue = (String) values[5];
	onClick = (String) values[6];
	onMouseOver = (String) values[7];
	onMouseOut = (String) values[8];
	onFocus = (String) values[9];
	onBlur = (String) values[10];
	name = (String) values[11];
	overrideName = (String) values[12];
    }

    @Override
    public void encodeBegin(FacesContext context) throws IOException {
	ResponseWriter writer = context.getResponseWriter();
	writer.startElement("marquee", this);
	writer.write(getValue());
	writer.endElement("marquee");
    }

    @Override
    public void encodeEnd(FacesContext arg0) throws IOException {
	super.encodeEnd(arg0);
    }

    public void setValue(String value) {
	this.value = value;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getOverrideName() {
	return overrideName;
    }

    public void setOverrideName(String overrideName) {
	this.overrideName = overrideName;
    }

    public String getStyleClass() {
	return styleClass;
    }

    public void setStyleClass(String styleClass) {
	this.styleClass = styleClass;
    }

    public String getStyle() {
	return style;
    }

    public void setStyle(String style) {
	this.style = style;
    }

    public String getDisabled() {
	return disabled;
    }

    public void setDisabled(String disabled) {
	this.disabled = disabled;
    }

    public String getItemLabel() {
	return itemLabel;
    }

    public void setItemLabel(String itemLabel) {
	this.itemLabel = itemLabel;
    }

    public String getItemValue() {
	return itemValue;
    }

    public void setItemValue(String itemValue) {
	this.itemValue = itemValue;
    }

    public String getOnClick() {
	return onClick;
    }

    public void setOnClick(String onClick) {
	this.onClick = onClick;
    }

    public String getOnMouseOver() {
	return onMouseOver;
    }

    public void setOnMouseOver(String onMouseOver) {
	this.onMouseOver = onMouseOver;
    }

    public String getOnMouseOut() {
	return onMouseOut;
    }

    public void setOnMouseOut(String onMouseOut) {
	this.onMouseOut = onMouseOut;
    }

    public String getOnFocus() {
	return onFocus;
    }

    public void setOnFocus(String onFocus) {
	this.onFocus = onFocus;
    }

    public String getOnBlur() {
	return onBlur;
    }

    public void setOnBlur(String onBlur) {
	this.onBlur = onBlur;
    }

    public String getValue() {
	return value;
    }

    @Override
    public String getFamily() {
	return COMPONENT_TYPE;
    }

}
