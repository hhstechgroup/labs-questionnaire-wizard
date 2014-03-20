package com.engagepoint.labs.wizard.ui;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.component.UIPanel;
import javax.faces.component.UISelectItems;
import javax.faces.component.html.HtmlInputFile;
import javax.faces.component.html.HtmlInputHidden;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.component.html.HtmlPanelGroup;
import javax.faces.component.html.HtmlSelectOneListbox;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.validator.Validator;

import org.primefaces.component.behavior.ajax.AjaxBehavior;
import org.primefaces.component.button.Button;
import org.primefaces.component.calendar.Calendar;
import org.primefaces.component.commandbutton.CommandButton;
import org.primefaces.component.inputtext.InputText;
import org.primefaces.component.inputtextarea.InputTextarea;
import org.primefaces.component.message.Message;
import org.primefaces.component.outputlabel.OutputLabel;
import org.primefaces.component.panel.Panel;
import org.primefaces.component.panelgrid.PanelGrid;
import org.primefaces.component.row.Row;
import org.primefaces.component.selectbooleancheckbox.SelectBooleanCheckbox;
import org.primefaces.component.selectmanycheckbox.SelectManyCheckbox;
import org.primefaces.component.slider.Slider;

import com.engagepoint.labs.wizard.controller.UINavigationBean;
import com.engagepoint.labs.wizard.handler.DataGridHandler;
import com.engagepoint.labs.wizard.handler.TimeHandler;
import com.engagepoint.labs.wizard.questions.CheckBoxesQuestion;
import com.engagepoint.labs.wizard.questions.DateQuestion;
import com.engagepoint.labs.wizard.questions.DropDownQuestion;
import com.engagepoint.labs.wizard.questions.GridQuestion;
import com.engagepoint.labs.wizard.questions.MultipleChoiseQuestion;
import com.engagepoint.labs.wizard.questions.RangeQuestion;
import com.engagepoint.labs.wizard.questions.TimeQuestion;
import com.engagepoint.labs.wizard.questions.WizardQuestion;
import com.engagepoint.labs.wizard.ui.ajax.CustomAjaxBehaviorListener;
import com.engagepoint.labs.wizard.ui.converters.ComponentValueConverter;
import com.engagepoint.labs.wizard.ui.validators.ComponentValidator;
import com.engagepoint.labs.wizard.values.Value;
import com.engagepoint.labs.wizard.values.objects.Grid;
import com.engagepoint.labs.wizard.values.objects.Range;

/**
 * Created by igor.guzenko on 2/11/14.
 */
public class UIComponentGenerator {
    private static int MAXIMUM_SIZE_FILE_ANSWER = 1024 * 1024 * 100;
    private UIPanel panel;
    private final int ONE_SELECT_ITEM_HEIGHT = 20;
    private boolean isParent;
    private int pageNumber;
    private int topicNumber;
    private UINavigationBean navigationBean;

    public UIComponentGenerator() {
    }

    public List<UIComponent> getPanelList(
            Map<WizardQuestion, Boolean> wizardQuestionMap, int pageNumber,
            int topicNumber, UINavigationBean navigationBean, DataGridHandler gridHandler, TimeHandler timeHandler) {
        this.navigationBean = navigationBean;
        this.pageNumber = pageNumber;
        this.topicNumber = topicNumber;
        List<UIComponent> panelList = new ArrayList<>();
        for (Map.Entry<WizardQuestion, Boolean> entry : wizardQuestionMap
                .entrySet()) {
            isParent = entry.getValue();
            panelList.add(analyzeQuestion(entry.getKey(), gridHandler,timeHandler));
        }
        return panelList;
    }

    private UIComponent analyzeQuestion(WizardQuestion question, DataGridHandler gridHandler,TimeHandler timeHandler) {
        panel = new Panel();
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
                component = getTime(question, answer, defaultAnswer, timeHandler);
                break;
            case RANGE:
                return getHtmlPanelGroup(question, answer, defaultAnswer);
            case FILEUPLOAD:
                component = getUploadComponent(question);
                break;
            case GRID:
                component = getGrid(question, gridHandler);
                break;
        }
        component.setId(question.getId());
        panel.getAttributes().put("styleClass", "nonRange");
        panel.getChildren().add(component);
        return panel;
    }

    private UIComponent getUploadComponent(WizardQuestion question) {
        UIComponent component;
        panel.getChildren().add(getLitleLabel(question));
        panel.getChildren().add(getHTMLbr());
        component = getFileUpload(question);
        panel.getChildren().add(getButton());
        return component;
    }

    private PanelGrid getGrid(WizardQuestion question, DataGridHandler gridHandler) {
        GridQuestion gridQuestion = (GridQuestion) question;
        PanelGrid grid = new PanelGrid();
        // Get column names from our model
        ArrayList<String> columns = (ArrayList<String>) gridQuestion
                .getColumns();
        // Get rows names from our model
        ArrayList<String> rows = (ArrayList<String>) gridQuestion.getRows();
        // Get our question ID. Grid will have this id.
        String gridID = gridQuestion.getId();
        // Set rows and column number. +1 because our table must have one upper
        // row with column names and one left row with row names Other rows and
        // columns are used for cells with radiobuttons.
        int rowsNumber = rows.size() + 1;
        int colsNumber = columns.size() + 1;
        grid.setColumns(colsNumber);
        grid.setId(gridID);
        gridHandler.getQuestions().put(gridID, gridQuestion);
        // Cells in datagrid starts numbering from 0. This counter provides cell
        // numbering.
        int checkBoxCellNumber = 0;
        for (int row = 0; row < rowsNumber; row++) {
            for (int col = 0; col < colsNumber; col++) {
                Row cell = new Row();
                if (row == 0 && col == 0) {
                    // Upper left cell does not contain anything, so will be
                    // empty
                    grid.getChildren().add(cell);
                    continue;
                }
                if (row == 0) {
                    // Upper row with has index 0. It contains column headers.
                    // Add it.
                    HtmlOutputText colName = new HtmlOutputText();
                    // col-1 because of column headers starts from cell with
                    // index 1, but first column name entry in arraylist is 0
                    colName.setValue(columns.get(col - 1));
                    cell.getChildren().add(colName);
                    grid.getChildren().add(cell);
                    continue;
                }
                if (row != 0 && col == 0) {
                    // When we are on column with index 0, we must insert to it
                    // cells with row names.
                    HtmlOutputText rowName = new HtmlOutputText();
                    rowName.setValue(rows.get(row - 1));
                    cell.getChildren().add(rowName);
                    grid.getChildren().add(cell);
                    continue;
                }
                if (row != 0 && col != 0) {
                    // Finally add cells with radios to datatable. First cell
                    // with radio will be upper-left and will have
                    // *checkBoxCellNumber* set to 0.
                    SelectBooleanCheckbox checkbox = new SelectBooleanCheckbox();
                    String checkboxID = Grid.createCheckBoxID(gridID,
                            checkBoxCellNumber);
                    checkbox.setId(checkboxID);
                    String valueGetterQuery = "#{dataGridHandler.setCellFromGridByID(\""
                            + gridID
                            + "\",\""
                            + checkboxID
                            + "\").currentCellValue}";
                    checkbox.setValueExpression(
                            "value",
                            createValueExpression(valueGetterQuery,
                                    Boolean.class));
                    checkbox.addClientBehavior("valueChange",
                            getAjaxBehavior(question));
                    cell.getChildren().add(checkbox);
                    grid.getChildren().add(cell);
                    checkBoxCellNumber++;
                    continue;
                }
            }
        }
        gridHandler.initialValidation();
        return grid;
    }

    private HtmlPanelGroup getHtmlPanelGroup(WizardQuestion question, Value answer, Value defaultAnswer) {
        HtmlPanelGroup panelGroup = new HtmlPanelGroup();
        panelGroup.setStyle("padding: 20px; background-color: #EDEDED; border: 1px solid #DDD; border-radius: 3px;");
        panelGroup.setStyleClass("ui-panel-column");
        panelGroup.setLayout("block");
        panelGroup.setId("panelid" + question.getId());
        Range range = getSliderPositions(question, answer, defaultAnswer);

        panelGroup.getChildren().add(getLabel(question));
        panelGroup.getChildren().add(getHTMLbr());
        panelGroup.getChildren().add(getOutputTextForSlider(question, range));
        panelGroup.getChildren().add(getSliderOutputLabelBegin(question));
        panelGroup.getChildren().add(getInputHiddenBegin(question, range));
        panelGroup.getChildren().add(getSliderOutputLabelEnd(question));
        panelGroup.getChildren().add(getInputHiddenEnd(question, range));
        panelGroup.getChildren().add(getSlider(question));
        panelGroup.getChildren().add(getHTMLbr());
        panelGroup.getChildren().add(getCommandButtonForSlider(question));
        return panelGroup;
    }

    private Range getSliderPositions(WizardQuestion question, Value answer, Value defaultAnswer) {
        Range range = new Range();
        if (answer != null) {
            range = (Range) answer.getValue();
        } else if (defaultAnswer != null) {
            range = (Range) defaultAnswer.getValue();
        } else {
            int initBegin = ((RangeQuestion) question).getStartRange();
            int initEnd = ((RangeQuestion) question).getEndRange();
            int begin = (int) (initBegin + (initEnd-initBegin)*0.2);
            int end = (int) (initBegin + (initEnd-initBegin)*0.8);
            range.setStart(begin);
            range.setEnd(end);
        }
        return range;
    }

    private HtmlOutputText getOutputTextForSlider(WizardQuestion question, Range range) {
        int begin = range.getStart();
        int end = range.getEnd();
        HtmlOutputText outputText = new HtmlOutputText();
        outputText.setId("displayRange" + question.getId());
        String rangeText = "Between " + begin + " and " + end;
        outputText.setValue(rangeText);
        return outputText;
    }

    private HtmlInputHidden getInputHiddenEnd(WizardQuestion question, Range range) {
        int end = range.getEnd();
        HtmlInputHidden inputHidden = new HtmlInputHidden();
        inputHidden.setId("txt7" + question.getId());
        inputHidden.setValue(end);
        inputHidden.addValidator(getComponentValidator(question));
        return inputHidden;
    }

    private HtmlInputHidden getInputHiddenBegin(WizardQuestion question, Range range) {
        int begin = range.getStart();
        HtmlInputHidden inputHidden = new HtmlInputHidden();
        inputHidden.setId("txt6" + question.getId());
        inputHidden.setValue(begin);
        inputHidden.addValidator(getComponentValidator(question));
        return inputHidden;
    }

    private OutputLabel getSliderOutputLabelBegin(WizardQuestion question) {
        OutputLabel label = new OutputLabel();
        label.setValue("slider_input");
        label.setFor("txt7" + question.getId());
        label.setStyleClass("for-slider-horizontal");
        label.setStyle("display: none");
        return label;
    }

    private OutputLabel getSliderOutputLabelEnd(WizardQuestion question) {
        OutputLabel label = new OutputLabel();
        label.setValue("slider_input");
        label.setFor("txt6" + question.getId());
        label.setStyleClass("for-slider-horizontal");
        label.setStyle("display: none");
        return label;
    }

    private Slider getSlider(WizardQuestion question) {
        Slider slider = new Slider();
        slider.setFor("sliderinputtext");
        slider.setAnimate(false);
        slider.addClientBehavior("valueChange", getAjaxBehavior(question));
        slider.setStyle("width: 400px;");
        slider.setDisplay("displayRange" + question.getId());
        slider.setStyleClass("ui-slider-with-range");
        slider.setDisplayTemplate("Between {min} and {max}");
        slider.setRange(true);
        String textFor = "txt6" + question.getId() + " , " + "txt7" + question.getId();
        slider.setFor(textFor);
        //setting slider bounds
        int min = ((RangeQuestion) question).getStartRange();
        int max = ((RangeQuestion) question).getEndRange();
        slider.setMinValue(min);
        slider.setMaxValue(max);
        return slider;
    }

    private CommandButton getCommandButtonForSlider(WizardQuestion question) {
        CommandButton commandButton = new CommandButton();
        commandButton.setId("but" + question.getId());
        commandButton.setValue("Submit");
        commandButton.setProcess("maincontentid-panelid"+ question.getId());
        commandButton.setPartialSubmit(true);
        return commandButton;
    }

    private HtmlSelectOneListbox getSelectOneListBox(WizardQuestion question, Value answer, Value defaultAnswer) {
        HtmlSelectOneListbox selectOneListBox = new HtmlSelectOneListbox();

        List<String> optionsList = ((MultipleChoiseQuestion) question)
                .getOptionsList();
        int height = ONE_SELECT_ITEM_HEIGHT * optionsList.size();

        // Creating Listener for Validation and AJAX ClientBehavior
        selectOneListBox.setStyle("height:" + height + "px");
        selectOneListBox.getChildren().add(getSelectItems(optionsList));
        selectOneListBox.addValidator(getComponentValidator(question));
        selectOneListBox.addClientBehavior("valueChange", getAjaxBehavior(question));

        // Showing Answer or Default Answer
        if (defaultAnswer != null && answer == null) {
            selectOneListBox.setValue(defaultAnswer.getValue());
        } else if (answer != null) {
            selectOneListBox.setValue(answer.getValue());
        }
        return selectOneListBox;
    }

    private InputText getInputText(WizardQuestion question, Value answer, Value defaultAnswer) {
        InputText inputText = new InputText();

        // Creating Listener for Validation and AJAX ClientBehavior
        inputText.addValidator(getComponentValidator(question));
        inputText.addClientBehavior("valueChange", getAjaxBehavior(question));
        // Showing Answer or Default Answer
        if (defaultAnswer != null && answer == null) {
            inputText.setValue(defaultAnswer.getValue().toString());
        } else if (answer != null) {
            inputText.setValue(answer.getValue().toString());
        }
        return inputText;
    }

    private InputTextarea getInputTextArea(WizardQuestion question, Value answer, Value defaultAnswer) {
        InputTextarea inputTextarea = new InputTextarea();

        // Creating Listener for Validation and AJAX ClientBehavior
        inputTextarea.addValidator(getComponentValidator(question));
        inputTextarea.addClientBehavior("valueChange", getAjaxBehavior(question));

        // Showing Answer or Default Answer
        if (defaultAnswer != null && answer == null) {
            inputTextarea.setValue(defaultAnswer.getValue().toString());
        } else if (answer != null) {
            inputTextarea.setValue(answer.getValue().toString());
        }
        return inputTextarea;
    }

    private HtmlSelectOneMenu getSelectOneMenu(WizardQuestion question, Value answer, Value defaultAnswer) {
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
        selectOneMenu.addValidator(getComponentValidator(question));
        selectOneMenu.addClientBehavior("valueChange", getAjaxBehavior(question));

        if (defaultAnswer != null && answer == null) {
            selectOneMenu.setValue(defaultAnswer.getValue());
        } else if (answer != null) {
            selectOneMenu.setValue(answer.getValue());
        }
        return selectOneMenu;
    }

    private SelectManyCheckbox getSelectManyCheckbox(WizardQuestion question, Value answer, Value defaultAnswer) {
        SelectManyCheckbox checkbox = new SelectManyCheckbox();

        List<String> optionsList = ((CheckBoxesQuestion) question)
                .getOptionsList();

        // Creating Listener for Validation and AJAX ClientBehavior
        checkbox.getChildren().add(getSelectItems(optionsList));
        checkbox.setLayout("pageDirection");
        checkbox.addValidator(getComponentValidator(question));
        checkbox.addClientBehavior("valueChange", getAjaxBehavior(question));
        // Showing Answer or Default Answer
        if (defaultAnswer != null && answer == null) {
            checkbox.setValue(defaultAnswer.getValue());
        } else if (answer != null) {
            checkbox.setValue(answer.getValue());
        }
        return checkbox;
    }

    private Calendar getDate(WizardQuestion question, Value answer, Value defaultAnswer) {
        Calendar dateCalendar = new Calendar();

        // Adding all attributes to UIComponent
        dateCalendar.setPattern(DateQuestion.DATE_FORMAT);
        dateCalendar.setStyle("padding:1px");
        dateCalendar.setNavigator(true);
        dateCalendar.setShowOn("both");
        dateCalendar.addClientBehavior("valueChange", getAjaxBehavior(question));
        dateCalendar.addClientBehavior("dateSelect", getAjaxBehavior(question));
        dateCalendar.addValidator(getComponentValidator(question));
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
	    Value defaultAnswer, TimeHandler timeHandler) {
	Calendar timeCalendar = new Calendar();

	// Adding all attributes to UIComponent
	String timeID = question.getId();
	timeCalendar.setTimeOnly(true);
	timeCalendar.setPattern(TimeQuestion.TIME_FORMAT);
	timeCalendar.setStyle("padding:1px");
	timeCalendar.setShowOn("both");
	timeCalendar.addClientBehavior("valueChange",
		getTimeAjaxBehavior(question));

	// Showing Answer or Default Answer
	if (defaultAnswer != null && answer == null) {
	    timeCalendar.setValue(defaultAnswer.getValue());
	} else if (answer != null) {
	    timeCalendar.setValue(answer.getValue());
	}

	String valueGetterQuery = "#{timeHandler.setTimeQuestionID(\"" + timeID
		+ "\").currentTimeQuestionValue}";
	timeCalendar.setValueExpression("value",
		createValueExpression(valueGetterQuery, Date.class));
	
	timeCalendar.setReadonlyInput(true);

	timeHandler.getQuestions().put(timeID, (TimeQuestion) question);
	return timeCalendar;
    }

    private AjaxBehavior getTimeAjaxBehavior(WizardQuestion question) {
	AjaxBehavior ajaxBehavior = new AjaxBehavior();
	ajaxBehavior.addAjaxBehaviorListener(new CustomAjaxBehaviorListener(
		question));
	ajaxBehavior.setPartialSubmit(true);
	return ajaxBehavior;
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
        if (isParent) {
            HtmlOutputText outputText = new HtmlOutputText();
            outputText.setValue(" *");
            outputText.setStyle("color:#00CC00");
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
        ajaxBehavior.addAjaxBehaviorListener(new CustomAjaxBehaviorListener(question));
        ajaxBehavior.setUpdate("@(.nonRange)");
        ajaxBehavior.setAsync(true);
        return ajaxBehavior;
    }

    private HtmlInputFile getFileUpload(WizardQuestion question) {
        HtmlInputFile fileUpload = new HtmlInputFile();
        fileUpload.setValue("#{fileUploadController.file}");
        fileUpload.setSize(MAXIMUM_SIZE_FILE_ANSWER);

        fileUpload.setStyle("position: absolute; left: auto; right: 100px; display: inline-block;");
        fileUpload.addValidator(getComponentValidator(question));
        return fileUpload;
    }

    public CommandButton getButton() {
        CommandButton commandButton = new CommandButton();
        commandButton.setValue("Upload");
        commandButton.setAjax(false);
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

    private HtmlOutputText getHTMLbr() {
        HtmlOutputText lineBreak = new HtmlOutputText();
        lineBreak.setValue("<br/>");
        lineBreak.setEscape(false);
        return lineBreak;
    }

    public static MethodExpression createMethodExpression(String expression, Class<?> returnType, Class<?>... parameterTypes) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        return facesContext.getApplication().getExpressionFactory().createMethodExpression(
                facesContext.getELContext(), expression, returnType, parameterTypes);
    }

    private Validator getComponentValidator(WizardQuestion question) {
        if (isParent) {
            return new ComponentValidator(question, pageNumber, topicNumber, isParent, navigationBean);
        } else {
            return new ComponentValidator(question, navigationBean);
        }
    }

    public static ValueExpression createValueExpression(String expression,
                                                        Class<?> returnType) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        return facesContext
                .getApplication()
                .getExpressionFactory()
                .createValueExpression(facesContext.getELContext(), expression,
                        returnType);
    }
}
