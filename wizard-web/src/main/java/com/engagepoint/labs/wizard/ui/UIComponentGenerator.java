package com.engagepoint.labs.wizard.ui;

import java.util.ArrayList;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItems;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.component.html.HtmlSelectOneListbox;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.primefaces.component.behavior.ajax.AjaxBehavior;
import org.primefaces.component.button.Button;
import org.primefaces.component.calendar.Calendar;
import org.primefaces.component.datagrid.DataGrid;
import org.primefaces.component.fileupload.FileUpload;
import org.primefaces.component.inputtext.InputText;
import org.primefaces.component.inputtextarea.InputTextarea;
import org.primefaces.component.message.Message;
import org.primefaces.component.outputlabel.OutputLabel;
import org.primefaces.component.panel.Panel;
import org.primefaces.component.selectmanycheckbox.SelectManyCheckbox;
import org.primefaces.component.slider.Slider;

import com.engagepoint.labs.wizard.bean.WizardDataModelGenerator;
import com.engagepoint.labs.wizard.questions.CheckBoxesQuestion;
import com.engagepoint.labs.wizard.questions.DropDownQuestion;
import com.engagepoint.labs.wizard.questions.MultipleChoiseQuestion;
import com.engagepoint.labs.wizard.questions.RangeQuestion;
import com.engagepoint.labs.wizard.questions.WizardQuestion;
import com.engagepoint.labs.wizard.ui.validators.ComponentValidator;
import com.engagepoint.labs.wizard.ui.validators.DateTimeValidator;
import com.engagepoint.labs.wizard.values.Value;

/**
 * Created by igor.guzenko on 2/11/14.
 */
public class UIComponentGenerator {
    private Panel panel;
    private final int ONE_SELECT_ITEM_HEIGHT = 17;

    public UIComponentGenerator() {
    }

    public List<Panel> getPanelList(List<WizardQuestion> wizardQuestionList) {
	List<Panel> panelList = new ArrayList<>();
	for (WizardQuestion question : wizardQuestionList) {
	    panelList.add(analyzeQuestion(question));
	}
	return panelList;
    }

    private Panel analyzeQuestion(WizardQuestion question) {
	panel = new Panel();
	panel.getChildren().add(getLabel(question));
	panel.getChildren().add(getValidationMessage(question));
	UIComponent component = null;
	Value answer = question.getAnswer();
	Value defaultAnswer = question.getDefaultAnswer();

	switch (question.getQuestionType()) {
	case TEXT:
	    component = getInputText(question, answer, defaultAnswer);
	    break;
	case PARAGRAPHTEXT:
	    component = getInputTextArea(question, answer, defaultAnswer);
	    break;
	case MULTIPLECHOICE:
	    component = getSelectOneListbox(question, answer, defaultAnswer);
	    break;
	case CHECKBOX:
	    component = getSelectManyCheckbox(question, answer, defaultAnswer);
	    break;
	case CHOOSEFROMLIST:
	    component = getSelectOneMenu(question, answer, defaultAnswer);
	    break;
	case DATE:
	    component = getDate(question);
	    break;
	case TIME:
	    // to do
	    component = getTime(question);
	    break;
	case RANGE:
	    // to do
	    component = getSlider(question);
	    break;
	case FILEUPLOAD:
	    component = getFileUpload(question);
	    break;
	case GRID:
	    // to do
	    component = new DataGrid();
	    break;
	}
	component.setId(question.getId());
	panel.getChildren().add(component);

	return panel;
    }

    private Message getValidationMessage(WizardQuestion question) {
	Message message = new Message();
	message.setFor("maincontentid-" + question.getId());
	return message;
    }

    private Slider getSlider(WizardQuestion question) {
	RangeQuestion rangeQuestion = (RangeQuestion) question;
	Slider slider = new Slider();
	slider.setMinValue(1);
	slider.setMaxValue(13);
	slider.setFor("maincontentid-j_id3");
	return slider;
    }

    private HtmlSelectOneListbox getSelectOneListbox(
	    final WizardQuestion question, Value answer, Value defaultAnswer) {
	HtmlSelectOneListbox sOneListbox = new HtmlSelectOneListbox();
	sOneListbox.setOnchange("submit()");
	List<String> optionsList = ((MultipleChoiseQuestion) question)
		.getOptionsList();
	sOneListbox.getChildren().add(getSelectItems(optionsList));
	int height = ONE_SELECT_ITEM_HEIGHT * optionsList.size();
	sOneListbox.setStyle("height:" + height + "px");
	sOneListbox.addValidator(new ComponentValidator(question));
	if (defaultAnswer != null && answer == null) {
	    sOneListbox.setValue(defaultAnswer.getValue());
	} else if (answer != null) {
	    sOneListbox.setValue(answer.getValue());
	}
	return sOneListbox;
    }

    private InputText getInputText(final WizardQuestion question, Value answer,
	    Value defaultAnswer) {
	final InputText inputText = new InputText();
	inputText.setOnchange("submit()");
	inputText.addValidator(new ComponentValidator(question));
	// Showing Answer or Default Answer
	if (defaultAnswer != null && answer == null) {
	    inputText.setValue(defaultAnswer.getValue().toString());
	} else if (answer != null) {
	    inputText.setValue(answer.getValue().toString());
	}
	return inputText;
    }

    private InputTextarea getInputTextArea(final WizardQuestion question,
	    Value answer, Value defaultAnswer) {
	final InputTextarea inputTextarea = new InputTextarea();
	inputTextarea.setOnchange("submit()");
	// Creating Listener for Validation
	inputTextarea.addValidator(new ComponentValidator(question));
	// Showing Answer or Default Answer
	if (defaultAnswer != null && answer == null) {
	    inputTextarea.setValue(defaultAnswer.getValue().toString());
	} else if (answer != null) {
	    inputTextarea.setValue(answer.getValue().toString());
	}
	return inputTextarea;
    }

    private OutputLabel getLabel(WizardQuestion question) {
	OutputLabel label = new OutputLabel();
	label.setValue(question.getTitle());
	if (question.isRequired()) {
	    HtmlOutputText outputText = new HtmlOutputText();
	    outputText.setValue(" *");
	    outputText.setStyle("color:red");
	    label.getChildren().add(outputText);
	}
	label.getChildren().add(getButtonTooltip(question));
	return label;
    }

    private HtmlSelectOneMenu getSelectOneMenu(final WizardQuestion question,
	    Value answer, Value defaultAnswer) {
	final HtmlSelectOneMenu selectOneMenu = new HtmlSelectOneMenu();
	final UISelectItems defaultItem = new UISelectItems();
	selectOneMenu.setOnchange("submit()");
	List<String> optionsList = ((DropDownQuestion) question)
		.getOptionsList();
	if (defaultAnswer == null && answer == null) {
	    defaultItem.setValue(new SelectItem("", "Set answer please"));
	    defaultItem.setId("defaultItem");
	    selectOneMenu.getChildren().add(defaultItem);
	}
	selectOneMenu.getChildren().add(getSelectItems(optionsList));
	selectOneMenu.addValidator(new ComponentValidator(question));
	if (defaultAnswer != null && answer == null) {
	    selectOneMenu.setValue(defaultAnswer.getValue());
	} else if (answer != null) {
	    selectOneMenu.setValue(answer.getValue());
	}
	return selectOneMenu;
    }

    private SelectManyCheckbox getSelectManyCheckbox(
	    final WizardQuestion question, Value answer, Value defaultAnswer) {
	SelectManyCheckbox checkbox = new SelectManyCheckbox();
	List<String> optionsList = ((CheckBoxesQuestion) question)
		.getOptionsList();
	checkbox.getChildren().add(getSelectItems(optionsList));
	checkbox.setLayout("pageDirection");
	checkbox.setOnchange("submit()");
	// Creating Listener for Validation
	checkbox.addValidator(new ComponentValidator(question));
	// Showing Answer or Default Answer
	if (defaultAnswer != null && answer == null) {
	    checkbox.setValue(defaultAnswer.getValue());
	} else if (answer != null) {
	    checkbox.setValue(answer.getValue());
	}
	return checkbox;
    }

    private UISelectItems getSelectItems(List<String> optionsList) {
	SelectItem item;
	UISelectItems selectItems = new UISelectItems();
	List<SelectItem> itemsList = new ArrayList<>();
	for (int i = 0; i < optionsList.size(); i++) {
	    item = new SelectItem(optionsList.get(i));
	    itemsList.add(item);
	}
	selectItems.setValue(itemsList);
	return selectItems;
    }

    private Calendar getDate(final WizardQuestion question) {
	Calendar dateCalendar = new Calendar();
	Value defaultAnswer = question.getDefaultAnswer();
	Value answer = question.getAnswer();
	AjaxBehavior ajaxBehavior;

	dateCalendar.setPattern(WizardDataModelGenerator.DATE_FORMAT);
	dateCalendar.setStyle("padding:1px");
//	dateCalendar.setOnchange("submit()");

	ajaxBehavior = (AjaxBehavior) FacesContext.getCurrentInstance()
		.getApplication().createBehavior(AjaxBehavior.BEHAVIOR_ID);
	ajaxBehavior.addAjaxBehaviorListener(new DateTimeValidator(question));
	dateCalendar.addClientBehavior("dateSelect", ajaxBehavior);

	// Showing Answer or Default Answer
	if (defaultAnswer != null && answer == null) {
	    dateCalendar.setValue(defaultAnswer.getValue());
	} else if (answer != null) {
	    dateCalendar.setValue(answer.getValue());
	}

	return dateCalendar;
    }

    private Calendar getTime(final WizardQuestion question) {
	Calendar timeCalendar = new Calendar();
	Value defaultAnswer = question.getDefaultAnswer();
	Value answer = question.getAnswer();
	AjaxBehavior ajaxBehavior;

	timeCalendar.setTimeOnly(true);
	timeCalendar.setPattern(WizardDataModelGenerator.TIME_FORMAT);
	timeCalendar.setStyle("padding:1px");
//	dateCalendar.setOnchange("submit()");

	ajaxBehavior = (AjaxBehavior) FacesContext.getCurrentInstance()
		.getApplication().createBehavior(AjaxBehavior.BEHAVIOR_ID);
	ajaxBehavior.addAjaxBehaviorListener(new DateTimeValidator(question));
	timeCalendar.addClientBehavior("dateSelect", ajaxBehavior);

	// Showing Answer or Default Answer
	if (defaultAnswer != null && answer == null) {
	    timeCalendar.setValue(defaultAnswer.getValue());
	} else if (answer != null) {
	    timeCalendar.setValue(answer.getValue());
	}

	return timeCalendar;
    }

    private FileUpload getFileUpload(WizardQuestion question) {
	FileUpload fileUpload = new FileUpload();
	return fileUpload;
    }

    private Button getButtonTooltip(WizardQuestion question) {
	Button tooltip = new Button();
	tooltip.setId("tooltip_" + question.getId());
	tooltip.setTitle(question.getHelpText());
	tooltip.setIcon("ui-icon-help");
	tooltip.setStyleClass("custom");
	tooltip.setStyle("position: absolute; left: auto; right: 15px; bottom: auto; padding: 1px");
	tooltip.setDisabled(true);
	return tooltip;
    }
}
