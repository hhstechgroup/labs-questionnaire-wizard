package com.engagepoint.labs.wizard.ui;

import com.engagepoint.labs.wizard.bean.WizardDataModelGenerator;
import com.engagepoint.labs.wizard.questions.*;
import com.engagepoint.labs.wizard.values.DateValue;
import com.engagepoint.labs.wizard.values.ListTextValue;
import com.engagepoint.labs.wizard.values.TextValue;
import com.engagepoint.labs.wizard.values.Value;

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

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItems;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.component.html.HtmlSelectOneListbox;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ValueChangeEvent;
import javax.faces.event.ValueChangeListener;
import javax.faces.model.SelectItem;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

	switch (question.getQuestionType()) {
	case TEXT:
	    component = getInputText(question);
	    break;
	case PARAGRAPHTEXT:
	    component = getInputTextArea(question);
	    break;
	case MULTIPLECHOICE:
	    component = getSelectOneListbox(question);
	    break;
	case CHECKBOX:
	    component = getSelectManyCheckbox(question);
	    break;
	case CHOOSEFROMLIST:
	    component = getSelectOneMenu(question);
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

    private HtmlSelectOneListbox getSelectOneListbox(final WizardQuestion question) {
	HtmlSelectOneListbox sOneListbox = new HtmlSelectOneListbox();
	sOneListbox.setOnchange("submit()");
	List<String> optionsList = ((MultipleChoiseQuestion) question).getOptionsList();
	sOneListbox.getChildren().add(getSelectItems(optionsList));
	int height = ONE_SELECT_ITEM_HEIGHT * optionsList.size();
	sOneListbox.setStyle("height:" + height + "px");
	Value defaultAnswer = question.getDefaultAnswer();
	Value answer = question.getAnswer();
	// added value change listener
	sOneListbox.addValueChangeListener(new ValueChangeListener() {
	    @Override
	    public void processValueChange(ValueChangeEvent event)
		    throws AbortProcessingException {
		TextValue value = new TextValue();
		value.setValue(event.getNewValue().toString());
		question.setAnswer(value);
	    }
	});
	if (question.isRequired()) {
	    sOneListbox.addValidator(new Validator() {
		@Override
		public void validate(FacesContext context, UIComponent component,
			Object value) throws ValidatorException {
		    if (value == null || value.toString().isEmpty()) {
			question.setValid(false);
			throw new ValidatorException(new FacesMessage(
				FacesMessage.SEVERITY_ERROR, "Validation Error",
				"Answer must be selected for this question!"));
		    } else {
			question.setValid(true);
		    }
		}
	    });
	}

	if (defaultAnswer != null && answer == null) {
	    sOneListbox.setValue(defaultAnswer.getValue());
	} else if (answer != null) {
	    sOneListbox.setValue(answer.getValue());
	}
	return sOneListbox;
    }

    private InputText getInputText(final WizardQuestion question) {
	final InputText inputText = new InputText();
	Value defaultAnswer = question.getDefaultAnswer();
	Value answer = question.getAnswer();
	inputText.setOnchange("submit()");
	inputText.setOnblur("submit()");
	// Creating Listener for Validation
	if (question.isRequired()) {
	    inputText.addValidator(new Validator() {
		@Override
		public void validate(FacesContext context, UIComponent component,
			Object value) throws ValidatorException {
		    if (value == null || value.toString().isEmpty()) {
			question.setValid(false);
			throw new ValidatorException(new FacesMessage(
				FacesMessage.SEVERITY_ERROR, "Validation Error",
				"Empty field is not allowed here!"));
		    } else if (value != null) {
			String currentValue = value.toString();
			currentValue = currentValue.replaceAll("\\s", "");
			if (currentValue.isEmpty()) {
			    question.setValid(false);
			    inputText.resetValue();
			    throw new ValidatorException(new FacesMessage(
				    FacesMessage.SEVERITY_ERROR, "Validation Error",
				    "Empty field is not allowed here!"));
			} else {
			    question.setValid(true);
			    Value textValue = new TextValue();
			    textValue.setValue(value);
			    question.setAnswer(textValue);
			}
		    }
		}
	    });
	}
	// Showing Answer or Default Answer
	if (defaultAnswer != null && answer == null) {
	    inputText.setValue(defaultAnswer.getValue().toString());
	} else if (answer != null) {
	    inputText.setValue(answer.getValue().toString());
	}
	return inputText;
    }

    private InputTextarea getInputTextArea(final WizardQuestion question) {
	final InputTextarea inputTextarea = new InputTextarea();
	Value defaultAnswer = question.getDefaultAnswer();
	Value answer = question.getAnswer();
	inputTextarea.setOnchange("submit()");
	inputTextarea.setOnblur("submit()");
	// Creating Listener for Validation
	if (question.isRequired()) {
	    inputTextarea.addValidator(new Validator() {
		@Override
		public void validate(FacesContext context, UIComponent component,
			Object value) throws ValidatorException {
		    if (value == null || value.toString().isEmpty()) {
			question.setValid(false);
			throw new ValidatorException(new FacesMessage(
				FacesMessage.SEVERITY_ERROR, "Validation Error",
				"Empty field is not allowed here!"));
		    } else if (value != null) {
			String currentValue = value.toString();
			currentValue = currentValue.replaceAll("\\s", "");
			if (currentValue.isEmpty()) {
			    question.setValid(false);
			    inputTextarea.resetValue();
			    throw new ValidatorException(new FacesMessage(
				    FacesMessage.SEVERITY_ERROR, "Validation Error",
				    "Empty field is not allowed here!"));
			} else {
			    question.setValid(true);
			    Value textValue = new TextValue();
			    textValue.setValue(value);
			    question.setAnswer(textValue);
			}
		    }
		}
	    });
	}
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
	HtmlOutputText required=new HtmlOutputText();
	required.setStyle("color:red");
	required.setValue(" *");
	label.setValue(question.getTitle());
	if (question.isRequired()) {
	    label.getChildren().add(required);
	}
	label.getChildren().add(getButtonTooltip(question));
	return label;
    }

    private HtmlSelectOneMenu getSelectOneMenu(final WizardQuestion question) {
	final HtmlSelectOneMenu selectOneMenu = new HtmlSelectOneMenu();
	final UISelectItems defaultItem = new UISelectItems();
	selectOneMenu.setOnchange("submit()");
	List<String> optionsList = ((DropDownQuestion) question).getOptionsList();
	Value defaultAnswer = question.getDefaultAnswer();
	final Value answer = question.getAnswer();
	if (defaultAnswer == null || answer == null) {
	    defaultItem.setValue(new SelectItem("", "Set answer please"));
	    selectOneMenu.getChildren().add(defaultItem);
	}

	selectOneMenu.getChildren().add(getSelectItems(optionsList));
	selectOneMenu.setOnblur("submit()");
	selectOneMenu.addValueChangeListener(new ValueChangeListener() {
	    @Override
	    public void processValueChange(ValueChangeEvent event)
		    throws AbortProcessingException {
		TextValue newValue = new TextValue();
		newValue.setValue(event.getNewValue());
		question.setAnswer(newValue);
	    }
	});
	if (question.isRequired()) {
	    selectOneMenu.addValidator(new Validator() {
		@Override
		public void validate(FacesContext context, UIComponent component,
			Object value) throws ValidatorException {
		    if (value == null || value.toString().isEmpty()) {
			question.setValid(false);
			selectOneMenu.setValue("");
			throw new ValidatorException(new FacesMessage(
				FacesMessage.SEVERITY_ERROR, "Validation Error",
				"Answer must be selected for this question!"));
		    } else {
			selectOneMenu.getChildren().remove(defaultItem);
			question.setValid(true);
		    }
		}
	    });
	}
	if (defaultAnswer != null && answer == null) {
	    selectOneMenu.setValue(defaultAnswer.getValue());
	} else if (answer != null) {
	    selectOneMenu.setValue(answer.getValue());
	}
	return selectOneMenu;
    }

    private SelectManyCheckbox getSelectManyCheckbox(final WizardQuestion question) {
	SelectManyCheckbox checkbox = new SelectManyCheckbox();
	Value defaultAnswer = question.getDefaultAnswer();
	Value answer = question.getAnswer();
	List<String> optionsList = ((CheckBoxesQuestion) question).getOptionsList();
	checkbox.getChildren().add(getSelectItems(optionsList));
	checkbox.setLayout("pageDirection");
	checkbox.setOnchange("submit()");
	checkbox.setOnblur("submit()");
	// Creating Listener for Validation
	if (question.isRequired()) {
	    checkbox.addValidator(new Validator() {
		@Override
		public void validate(FacesContext context, UIComponent component,
			Object value) throws ValidatorException {
		    if (value == null || ((Object[]) value).length == 0) {
			question.setValid(false);
			throw new ValidatorException(new FacesMessage(
				FacesMessage.SEVERITY_ERROR, "Validation Error",
				"You need to choose at least one option!"));
		    } else {
			question.setValid(true);
			ListTextValue listTextValue = new ListTextValue();
			Object[] arr = (Object[]) value;
			List answersList = new ArrayList();
			for (int i = 0; i < arr.length; i++) {
			    answersList.add(arr[i]);
			}
			listTextValue.setValue(answersList);
			question.setAnswer(listTextValue);
		    }
		}
	    });
	}
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

	dateCalendar.setValue(new Date());
	dateCalendar.setStyle("padding:1px");

	dateCalendar.setOnchange("submit()");
	dateCalendar.setOnblur("submit()");
	// Creating Listener for Validation
	if (question.isRequired()) {
	    dateCalendar.addValidator(new Validator() {
		@Override
		public void validate(FacesContext context, UIComponent component,
			Object value) throws ValidatorException {
		    if (value == null || ((Object[]) value).length == 0) {
			question.setValid(false);
			throw new ValidatorException(new FacesMessage(
				FacesMessage.SEVERITY_ERROR, "Validation Error",
				"You need to choose a date!"));
		    } else {
			question.setValid(true);
			Value dateValue = new DateValue();
			dateValue.setValue(value);
			question.setAnswer(dateValue);
		    }
		}
	    });
	}
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
	timeCalendar.setValue(new Date());
	timeCalendar.setPattern(WizardDataModelGenerator.TIME_FORMAT);
	timeCalendar.setTimeOnly(true);
	timeCalendar.setStyle("padding:1px");

	timeCalendar.setOnchange("submit()");
	timeCalendar.setOnblur("submit()");
	// Creating Listener for Validation
	if (question.isRequired()) {
	    timeCalendar.addValidator(new Validator() {
		@Override
		public void validate(FacesContext context, UIComponent component,
			Object value) throws ValidatorException {
		    if (value == null || ((Object[]) value).length == 0) {
			question.setValid(false);
			throw new ValidatorException(new FacesMessage(
				FacesMessage.SEVERITY_ERROR, "Validation Error",
				"You need to choose a time!"));
		    } else {
			question.setValid(true);
			Value dateValue = new DateValue();
			dateValue.setValue(value);
			question.setAnswer(dateValue);
		    }
		}
	    });
	}
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
