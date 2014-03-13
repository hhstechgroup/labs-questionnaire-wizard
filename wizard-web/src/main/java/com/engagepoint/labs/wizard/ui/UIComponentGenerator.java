package com.engagepoint.labs.wizard.ui;

import com.engagepoint.component.primefaces.DataTable;
import com.engagepoint.labs.wizard.handler.DataGridHandler;
import com.engagepoint.labs.wizard.handler.DataGridStoreObject;
import com.engagepoint.labs.wizard.questions.*;
import com.engagepoint.labs.wizard.ui.ajax.CustomAjaxBehaviorListener;
import com.engagepoint.labs.wizard.ui.converters.ComponentValueConverter;
import com.engagepoint.labs.wizard.ui.validators.ComponentValidator;
import com.engagepoint.labs.wizard.values.Value;

import org.primefaces.component.behavior.ajax.AjaxBehavior;
import org.primefaces.component.button.Button;
import org.primefaces.component.calendar.Calendar;
import org.primefaces.component.column.Column;
import org.primefaces.component.commandbutton.CommandButton;
import org.primefaces.component.datagrid.DataGrid;
import org.primefaces.component.inputtext.InputText;
import org.primefaces.component.inputtextarea.InputTextarea;
import org.primefaces.component.message.Message;
import org.primefaces.component.outputlabel.OutputLabel;
import org.primefaces.component.panel.Panel;
import org.primefaces.component.panelgrid.PanelGrid;
import org.primefaces.component.radiobutton.RadioButton;
import org.primefaces.component.row.Row;
import org.primefaces.component.selectbooleancheckbox.SelectBooleanCheckbox;
import org.primefaces.component.selectmanycheckbox.SelectManyCheckbox;
import org.primefaces.component.selectoneradio.SelectOneRadio;
import org.primefaces.component.slider.Slider;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItem;
import javax.faces.component.UISelectItems;
import javax.faces.component.html.*;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by igor.guzenko on 2/11/14.
 */
public class UIComponentGenerator {
    private static int MAXIMUM_SIZE_FILE_ANSWER = 1024 * 1024 * 100;
    private Panel panel;
    private final int ONE_SELECT_ITEM_HEIGHT = 20;
    private boolean isParent;
    private int pageNumber;
    private int topicNumber;

    public UIComponentGenerator() {
    }

    public List<Panel> getPanelList(
	    Map<WizardQuestion, Boolean> wizardQuestionMap, int pageNumber,
	    int topicNumber, DataGridHandler gridHandler) {
	this.pageNumber = pageNumber;
	this.topicNumber = topicNumber;
	List<Panel> panelList = new ArrayList<>();
	for (Map.Entry<WizardQuestion, Boolean> entry : wizardQuestionMap
		.entrySet()) {
	    isParent = entry.getValue();
	    panelList.add(analyzeQuestion(entry.getKey(), gridHandler));
	}
	return panelList;
    }

    private Panel analyzeQuestion(WizardQuestion question,
	    DataGridHandler gridHandler) {
	panel = new Panel();
	panel.setWidgetVar("panel_" + question.getId());
	panel.setClosable(true);
	panel.setToggleable(true);
	panel.setCloseSpeed(300);
	panel.setId("panel_" + question.getId());
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
	    component = getSelectOneListBox(question, answer, defaultAnswer);
	    break;
	case CHECKBOX:
	    component = getSelectManyCheckbox(question, answer, defaultAnswer);
	    break;
	case CHOOSEFROMLIST:
	    component = getSelectOneMenu(question, answer, defaultAnswer);
	    break;
	case DATE:
	    component = getDate(question, answer, defaultAnswer);
	    break;
	case TIME:
	    // to do
	    component = getTime(question, answer, defaultAnswer);
	    break;
	case RANGE:
	    component = getSlider(question, answer, defaultAnswer);
	    break;
	case FILEUPLOAD:
	    panel.getChildren().add(getLitleLabel(question));
	    panel.getChildren().add(getHtmlText());
	    component = getFileUpload(question);
	    panel.getChildren().add(getButton(question));
	    break;
	case GRID:
	    // to do
	    component = getGrid(question, answer, defaultAnswer, gridHandler);
	    break;
	}
	component.setId(question.getId());
	panel.getChildren().add(component);
	return panel;
    }

    private PanelGrid getGrid(WizardQuestion question, Value answer,
	    Value defaultAnswer, DataGridHandler gridHandler) {
	PanelGrid grid = new PanelGrid();
	GridQuestion gridQuestion = (GridQuestion) question;
	ArrayList<String> columns = (ArrayList<String>) gridQuestion
		.getColumns();
	ArrayList<String> rows = (ArrayList<String>) gridQuestion.getRows();
	String gridID=gridQuestion.getId();
	int rowsNumber = rows.size() + 1;
	int colsNumber = columns.size() + 1;
	grid.setColumns(colsNumber);
	grid.setId(gridID);
	
	gridHandler.getGrids().add(new DataGridStoreObject(gridID));

	int checkBoxCellNumber=0;
	for (int row = 0; row < rowsNumber; row++) {
	    for (int col = 0; col < colsNumber; col++) {
		Row cell = new Row();
		if (row == 0 && col == 0) {
		    grid.getChildren().add(cell);
		    continue;
		}
		if (row == 0) {
		    HtmlOutputText colName = new HtmlOutputText();
		    colName.setValue(columns.get(col - 1));
		    cell.getChildren().add(colName);
		    grid.getChildren().add(cell);
		    continue;
		}
		if (row != 0 && col == 0) {
		    HtmlOutputText rowName = new HtmlOutputText();
		    rowName.setValue(rows.get(row - 1));
		    cell.getChildren().add(rowName);
		    grid.getChildren().add(cell);
		    continue;
		}
		if (row != 0 && col != 0) {
		    SelectBooleanCheckbox checkbox = new SelectBooleanCheckbox();
		    cell.getChildren().add(checkbox);
		    grid.getChildren().add(cell);
		    
		    
		    gridHandler.getGridByID(gridID).add(checkbox);
		    checkBoxCellNumber++;
		    continue;
		}
	    }
	}

	return grid;
    }

    private Slider getSlider(WizardQuestion question, Value answer,
	    Value defaultAnswer) {
	Slider slider = new Slider();
	slider.setMinValue(1);
	slider.setMaxValue(13);
	return slider;
    }

    private HtmlSelectOneListbox getSelectOneListBox(WizardQuestion question,
	    Value answer, Value defaultAnswer) {
	HtmlSelectOneListbox selectOneListBox = new HtmlSelectOneListbox();

	List<String> optionsList = ((MultipleChoiseQuestion) question)
		.getOptionsList();
	int height = ONE_SELECT_ITEM_HEIGHT * optionsList.size();

	// Creating Listener for Validation and AJAX ClientBehavior
	selectOneListBox.setStyle("height:" + height + "px");
	selectOneListBox.getChildren().add(getSelectItems(optionsList));
	selectOneListBox.addValidator(new ComponentValidator(question,
		pageNumber, topicNumber, isParent));
	selectOneListBox.addClientBehavior("valueChange",
		getAjaxBehavior(question));

	// Showing Answer or Default Answer
	if (defaultAnswer != null && answer == null) {
	    selectOneListBox.setValue(defaultAnswer.getValue());
	} else if (answer != null) {
	    selectOneListBox.setValue(answer.getValue());
	}
	return selectOneListBox;
    }

    private InputText getInputText(WizardQuestion question, Value answer,
	    Value defaultAnswer) {
	InputText inputText = new InputText();

	// Creating Listener for Validation and AJAX ClientBehavior
	inputText.addValidator(new ComponentValidator(question, pageNumber,
		topicNumber, isParent));
	inputText.addClientBehavior("valueChange", getAjaxBehavior(question));

	// Showing Answer or Default Answer
	if (defaultAnswer != null && answer == null) {
	    inputText.setValue(defaultAnswer.getValue().toString());
	} else if (answer != null) {
	    inputText.setValue(answer.getValue().toString());
	}
	return inputText;
    }

    private InputTextarea getInputTextArea(WizardQuestion question,
	    Value answer, Value defaultAnswer) {
	InputTextarea inputTextarea = new InputTextarea();

	// Creating Listener for Validation and AJAX ClientBehavior
	inputTextarea.addValidator(new ComponentValidator(question, pageNumber,
		topicNumber, isParent));
	inputTextarea.addClientBehavior("valueChange",
		getAjaxBehavior(question));

	// Showing Answer or Default Answer
	if (defaultAnswer != null && answer == null) {
	    inputTextarea.setValue(defaultAnswer.getValue().toString());
	} else if (answer != null) {
	    inputTextarea.setValue(answer.getValue().toString());
	}
	return inputTextarea;
    }

    private HtmlSelectOneMenu getSelectOneMenu(WizardQuestion question,
	    Value answer, Value defaultAnswer) {
	HtmlSelectOneMenu selectOneMenu = new HtmlSelectOneMenu();

	List<String> optionsList = ((DropDownQuestion) question)
		.getOptionsList();
	UISelectItems defaultItem = new UISelectItems();
	if (defaultAnswer == null && answer == null) {
	    defaultItem.setValue(new SelectItem("", "Set answer please"));
	    defaultItem.setId("defaultItem");
	    selectOneMenu.getChildren().add(defaultItem);
	}

	// Creating Listener for Validation and AJAX ClientBehavior
	selectOneMenu.getChildren().add(getSelectItems(optionsList));
	selectOneMenu.addValidator(new ComponentValidator(question, pageNumber,
		topicNumber, isParent));
	selectOneMenu.addClientBehavior("valueChange",
		getAjaxBehavior(question));

	if (defaultAnswer != null && answer == null) {
	    selectOneMenu.setValue(defaultAnswer.getValue());
	} else if (answer != null) {
	    selectOneMenu.setValue(answer.getValue());
	}
	return selectOneMenu;
    }

    private SelectManyCheckbox getSelectManyCheckbox(WizardQuestion question,
	    Value answer, Value defaultAnswer) {
	SelectManyCheckbox checkbox = new SelectManyCheckbox();

	List<String> optionsList = ((CheckBoxesQuestion) question)
		.getOptionsList();

	// Creating Listener for Validation and AJAX ClientBehavior
	checkbox.getChildren().add(getSelectItems(optionsList));
	checkbox.setLayout("pageDirection");
	checkbox.addValidator(new ComponentValidator(question, pageNumber,
		topicNumber, isParent));
	checkbox.addClientBehavior("valueChange", getAjaxBehavior(question));

	// Showing Answer or Default Answer
	if (defaultAnswer != null && answer == null) {
	    checkbox.setValue(defaultAnswer.getValue());
	} else if (answer != null) {
	    checkbox.setValue(answer.getValue());
	}
	return checkbox;
    }

    private Calendar getDate(WizardQuestion question, Value answer,
	    Value defaultAnswer) {
	Calendar dateCalendar = new Calendar();

	// Adding all attributes to UIComponent
	dateCalendar.setPattern(DateQuestion.DATE_FORMAT);
	dateCalendar.setStyle("padding:1px");
	dateCalendar.setNavigator(true);
	dateCalendar.setShowOn("both");
	dateCalendar
		.addClientBehavior("valueChange", getAjaxBehavior(question));
	dateCalendar.addClientBehavior("dateSelect", getAjaxBehavior(question));
	dateCalendar.addValidator(new ComponentValidator(question, pageNumber,
		topicNumber, isParent));
	dateCalendar.setConverter(new ComponentValueConverter(question));

	// Showing Answer or Default Answer
	if (defaultAnswer != null && answer == null) {
	    dateCalendar.setValue(defaultAnswer.getValue());
	} else if (answer != null) {
	    dateCalendar.setValue(answer.getValue());
	}
	return dateCalendar;
    }

    private Calendar getTime(final WizardQuestion question, Value answer,
	    Value defaultAnswer) {
	Calendar timeCalendar = new Calendar();

	// Adding all attributes to UIComponent
	timeCalendar.setTimeOnly(true);
	timeCalendar.setPattern(TimeQuestion.TIME_FORMAT);
	timeCalendar.setStyle("padding:1px");
	timeCalendar.setShowOn("both");
	timeCalendar
		.addClientBehavior("valueChange", getAjaxBehavior(question));
	// timeCalendar.addClientBehavior("dateSelect",
	// getAjaxBehavior(question));
	timeCalendar.addValidator(new ComponentValidator(question, pageNumber,
		topicNumber, isParent));
	timeCalendar.setConverter(new ComponentValueConverter(question));

	// Showing Answer or Default Answer
	if (defaultAnswer != null && answer == null) {
	    timeCalendar.setValue(defaultAnswer.getValue());
	} else if (answer != null) {
	    timeCalendar.setValue(answer.getValue());
	}
	return timeCalendar;
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

    private OutputLabel getLabel(WizardQuestion question) {
	OutputLabel label = new OutputLabel();
	label.setId("labelIdFor-" + question.getId());
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

    private Message getValidationMessage(WizardQuestion question) {
	Message message = new Message();
	message.setFor("maincontentid-" + question.getId());
	return message;
    }

    private AjaxBehavior getAjaxBehavior(WizardQuestion question) {
	AjaxBehavior ajaxBehavior = new AjaxBehavior();
	ajaxBehavior.addAjaxBehaviorListener(new CustomAjaxBehaviorListener(
		question));
	ajaxBehavior.setUpdate("maincontentid-j_id1");
	ajaxBehavior.setAsync(true);
	return ajaxBehavior;
    }

    private HtmlInputFile getFileUpload(WizardQuestion question) {
	HtmlInputFile fileUpload = new HtmlInputFile();
	fileUpload.setValue("#{fileUploadController.file}");
	fileUpload.setSize(MAXIMUM_SIZE_FILE_ANSWER);

	fileUpload
		.setStyle("position: absolute; left: auto; right: 100px; display: inline-block;");
	fileUpload.addValidator(new ComponentValidator(question, pageNumber,
		topicNumber, isParent));
	return fileUpload;
    }

    public CommandButton getButton(WizardQuestion question) {
	CommandButton commandButton = new CommandButton();
	commandButton.setValue("Upload");
	commandButton.setAjax(false);
	// commandButton.setActionExpression(createMethodExpression(String.format("#{fileUploadController.getAnswerInputStream('"
	// + question.getId() + "')}"), null, String.class));
	return commandButton;
    }

    private OutputLabel getLitleLabel(WizardQuestion question) {
	OutputLabel label = new OutputLabel();
	label.setId("little_" + question.getId());
	if (question.getAnswer() != null) {
	    label.setValue("Your file  uploaded");
	}
	return label;
    }

    private HtmlOutputText getHtmlText() {
	HtmlOutputText lineBreak = new HtmlOutputText();
	lineBreak.setValue("<br/>");
	lineBreak.setEscape(false);
	return lineBreak;
    }

    public static MethodExpression createMethodExpression(String expression,
	    Class<?> returnType, Class<?>... parameterTypes) {
	FacesContext facesContext = FacesContext.getCurrentInstance();
	return facesContext
		.getApplication()
		.getExpressionFactory()
		.createMethodExpression(facesContext.getELContext(),
			expression, returnType, parameterTypes);
    }
}
