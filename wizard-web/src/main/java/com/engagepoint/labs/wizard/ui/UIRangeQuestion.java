package com.engagepoint.labs.wizard.ui;

import javax.faces.component.html.HtmlForm;
import javax.faces.component.html.HtmlOutputText;

import org.primefaces.component.slider.Slider;

public class UIRangeQuestion extends UIBasicQuestion {

    public static final String TYPE = "RangeQuestion";
    private Slider slider;

    public UIRangeQuestion(String type, HtmlForm dynaform, int start_id,
	    int finish_id) {
	super(TYPE);

	HtmlOutputText hidden_input_start = new HtmlOutputText();
	hidden_input_start.setValue("<h:inputHidden id=\"" + TYPE + start_id
		+ "\" value=\"#{uiComponentAddController.sliderStart(" + start_id
		+ ")}\" />");
	hidden_input_start.setEscape(false);

	HtmlOutputText hidden_input_finish = new HtmlOutputText();
	hidden_input_finish.setValue("<h:inputHidden id=\"" + TYPE + finish_id
		+ "\" value=\"#{uiComponentAddController.sliderFinish(" + finish_id
		+ ")}\" />");
	hidden_input_finish.setEscape(false);

	dynaform.getChildren().add(hidden_input_start);
	dynaform.getChildren().add(hidden_input_finish);

	slider = new Slider();
    }

}
