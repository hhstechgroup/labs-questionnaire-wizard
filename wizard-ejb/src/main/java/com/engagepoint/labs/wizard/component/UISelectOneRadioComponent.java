package com.engagepoint.labs.wizard.component;

import java.io.IOException;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

@FacesComponent("com.engagepoint.labs.wizard.component")
public class UISelectOneRadioComponent extends UIComponentBase {

    public static final String COMPONENT_TYPE = "com.engagepoint.labs.wizard.component";

    String value = null;

    public String getValue() {
	return value;
    }

    @Override
    public String getFamily() {
	return COMPONENT_TYPE;
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

}
