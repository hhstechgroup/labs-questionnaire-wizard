package com.engagepoint.labs.wizard.component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UIData;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.ValueBinding;

@FacesComponent("com.engagepoint.labs.wizard.component")
public class UISelectOneRadioComponent extends UIInput {

    public static final String COMPONENT_TYPE = "com.engagepoint.labs.wizard.component";

    private String action = null;
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

    @Override
    public Object saveState(FacesContext context) {
	Object[] values = new Object[14];
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
	values[13] = action;
	return (values);
    }

    @Override
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
	action = (String) values[13];
    }

    @Override
    public void decode(FacesContext context) {
	if ((context == null)) {
	    throw new NullPointerException();
	}

	Map map = context.getExternalContext().getRequestParameterMap();
	String name = getRName(context);
	if (map.containsKey(name)) {
	    String value = (String) map.get(name);
	    if (value != null) {
		setSubmittedValue(value);
	    }

	}
    }

    @Override
    public void encodeBegin(FacesContext context) throws IOException {
	if ((context == null)) {
	    throw new NullPointerException();
	}
    }

    /**
     * <p>
     * No children encoding is required.
     * </p>
     *
     * @param context
     *            <code>FacesContext</code>for the current request
     * @param component
     *            <code>UIComponent</code> to be decoded
     */
    @Override
    public void encodeChildren(FacesContext context) throws IOException {
	if ((context == null)) {
	    throw new NullPointerException();
	}
    }

    /**
     * <p>
     * Encode this component.
     * </p>
     *
     * @param context
     *            <code>FacesContext</code>for the current request
     * @param component
     *            <code>UIComponent</code> to be decoded
     */

    @Override
    public void encodeEnd(FacesContext context) throws IOException {
	if ((context == null)) {
	    throw new NullPointerException();
	}

	if (isRendered()) {
	    ResponseWriter writer = context.getResponseWriter();

	    writer.write("<input type=\"radio\"");
	    writer.write(" id=\"" + getClientId(context) + "\"");
	    writer.write(" name=\"" + getRName(context) + "\"");
	    if (getAction() != null && getAction().trim().length() > 0) {
		writer.write(" action=\"" + getAction().trim() + "\"");
	    }
	    if (getStyleClass() != null && getStyleClass().trim().length() > 0) {
		writer.write(" class=\"" + getStyleClass().trim() + "\"");
	    }
	    if (getStyle() != null && getStyle().trim().length() > 0) {
		writer.write(" style=\"" + getStyle().trim() + "\"");
	    }
	    if (getDisabled() != null && getDisabled().trim().length() > 0
		    && getDisabled().trim().equals("true")) {
		writer.write(" disabled=\"disabled\"");
	    }
	    if (getItemValue() != null) {
		writer.write(" value=\"" + getItemValue().trim() + "\"");
	    }
	    if (getOnClick() != null && getOnClick().trim().length() > 0) {
		writer.write(" onclick=\"" + getOnClick().trim() + "\"");
	    }
	    if (getOnMouseOver() != null
		    && getOnMouseOver().trim().length() > 0) {
		writer.write(" onmouseover=\"" + getOnMouseOver().trim() + "\"");
	    }
	    if (getOnMouseOut() != null && getOnMouseOut().trim().length() > 0) {
		writer.write(" onmouseout=\"" + getOnMouseOut().trim() + "\"");
	    }
	    if (getOnFocus() != null && getOnFocus().trim().length() > 0) {
		writer.write(" onfocus=\"" + getOnFocus().trim() + "\"");
	    }
	    if (getOnBlur() != null && getOnBlur().trim().length() > 0) {
		writer.write(" onblur=\"" + getOnBlur().trim() + "\"");
	    }
	    if (getValue() != null && getValue().equals(getItemValue())) {
		writer.write(" checked=\"checked\"");
	    }
	    writer.write(">");
	    if (getItemLabel() != null) {
		writer.write(getItemLabel());
	    }
	    writer.write("</input>");
	}
    }

    private String getRName(FacesContext context) {

	UIComponent parentUIComponent = getParentDataTableFromHierarchy(this);
	if (parentUIComponent == null) {
	    return getClientId(context);
	} else {
	    if (getOverrideName() != null && getOverrideName().equals("true")) {
		return getName();
	    } else {

		String id = getClientId(context);
		int lastIndexOfColon = id.lastIndexOf(":");
		String partName = "";
		if (lastIndexOfColon != -1) {
		    partName = id.substring(0, lastIndexOfColon + 1);
		    if (getName() == null) {
			partName = partName + "generatedRad";
		    } else
			partName = partName + getName();
		}

		return partName;
	    }
	}
    }

    private UIComponent getParentDataTableFromHierarchy(UIComponent uiComponent) {
	if (uiComponent == null) {
	    return null;
	}
	if (uiComponent instanceof UIData) {
	    return uiComponent;
	} else {
	    // try to find recursively in the Component tree hierarchy
	    return getParentDataTableFromHierarchy(uiComponent.getParent());
	}
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
	System.out.println("AAAAAAAAAAAAAAAAAAAAAAA");
	return itemValue;
    }

    public void setItemValue(String itemValue) {
	System.out.println(itemValue+" "+this.toString());
	this.itemValue = itemValue;
    }

    public String getOnClick() {
	if (onClick != null) {
	    return onClick;
	}
	return returnValueBindingAsString("submit()");
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

    public String getAction() {
	if (action != null) {
	    return action;
	}
	return returnValueBindingAsString("action");
    }

    public void setAction(String action) {
	this.action = action;
    }

    public String returnValueBindingAsString(String attr) {
	FacesContext facesContext = FacesContext.getCurrentInstance();
	ELContext elContext = facesContext.getELContext();
	ExpressionFactory exFactory = facesContext.getApplication()
		.getExpressionFactory();
	ValueExpression valueExpression = exFactory.createValueExpression(
		elContext, attr, String.class);
	return valueExpression.getExpressionString();
    }

    @Override
    public String getFamily() {
	return COMPONENT_TYPE;
    }

}
