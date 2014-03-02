package com.engagepoint.labs.wizard.ui;

import com.engagepoint.labs.wizard.questions.*;
import com.engagepoint.labs.wizard.ui.validators.ComponentValidator;
import com.engagepoint.labs.wizard.ui.validators.CustomAjaxBehaviorListener;
import com.engagepoint.labs.wizard.values.Value;

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

import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItems;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.component.html.HtmlSelectOneListbox;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.model.SelectItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by igor.guzenko on 2/11/14.
 */
public class UIComponentGenerator {
    private Panel panel;
    private final int ONE_SELECT_ITEM_HEIGHT = 20;

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
                // to do
                component = getSlider(question, answer, defaultAnswer);
                break;
            case FILEUPLOAD:
                component = getFileUpload(question, answer, defaultAnswer);
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

    private Slider getSlider(WizardQuestion question, Value answer, Value defaultAnswer) {
        RangeQuestion rangeQuestion = (RangeQuestion) question;
        Slider slider = new Slider();
        slider.setMinValue(1);
        slider.setMaxValue(13);
        slider.setFor("maincontentid-j_id3");
        return slider;
    }

    private HtmlSelectOneListbox getSelectOneListBox(WizardQuestion question, Value answer, Value defaultAnswer) {
        HtmlSelectOneListbox selectOneListBox = new HtmlSelectOneListbox();

        List<String> optionsList = ((MultipleChoiseQuestion) question)
                .getOptionsList();
        int height = ONE_SELECT_ITEM_HEIGHT * optionsList.size();

        // Creating Listener for Validation and AJAX ClientBehavior
        selectOneListBox.setStyle("height:" + height + "px");
        selectOneListBox.getChildren().add(getSelectItems(optionsList));
        selectOneListBox.addValidator(new ComponentValidator(question));
        selectOneListBox.addClientBehavior("valueChange", getAjaxBehaivor(question));

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
        inputText.addValidator(new ComponentValidator(question));
        inputText.addClientBehavior("valueChange", getAjaxBehaivor(question));

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
        inputTextarea.addValidator(new ComponentValidator(question));
        inputTextarea.addClientBehavior("valueChange", getAjaxBehaivor(question));

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
        selectOneMenu.addValidator(new ComponentValidator(question));
        selectOneMenu.addClientBehavior("valueChange", getAjaxBehaivor(question));

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
        checkbox.addValidator(new ComponentValidator(question));
        checkbox.addClientBehavior("valueChange", getAjaxBehaivor(question));

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
        dateCalendar.setReadonlyInput(true);
        dateCalendar.addValidator(new ComponentValidator(question));
        dateCalendar.addClientBehavior("valueChange", getAjaxBehaivor(question));
        dateCalendar.addClientBehavior("dateSelect", getAjaxBehaivor(question));

        // Showing Answer or Default Answer
        if (defaultAnswer != null && answer == null) {
            dateCalendar.setValue(defaultAnswer.getValue());
        } else if (answer != null) {
            dateCalendar.setValue(answer.getValue());
        }
        return dateCalendar;
    }

    private Calendar getTime(WizardQuestion question, Value answer, Value defaultAnswer) {
        Calendar timeCalendar = new Calendar();

        // Adding all attributes to UIComponent
        timeCalendar.setTimeOnly(true);
        timeCalendar.setPattern(TimeQuestion.TIME_FORMAT);
        timeCalendar.setStyle("padding:1px");
        timeCalendar.setShowOn("both");
        timeCalendar.setReadonlyInput(true);
        timeCalendar.addValidator(new ComponentValidator(question));
        timeCalendar.addClientBehavior("dateSelect", getAjaxBehaivor(question));
//        timeCalendar.addClientBehavior("blur", ajaxBehavior);

        // Showing Answer or Default Answer
        if (defaultAnswer != null && answer == null) {
            timeCalendar.setValue(defaultAnswer.getValue());
        } else if (answer != null) {
            timeCalendar.setValue(answer.getValue());
        }
        return timeCalendar;
    }

    private FileUpload getFileUpload(WizardQuestion question, Value answer, Value defaultAnswer) {
        FileUpload fileUpload = new FileUpload();
        return fileUpload;
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

    private AjaxBehavior getAjaxBehaivor(WizardQuestion question) {
        AjaxBehavior ajaxBehavior = new AjaxBehavior();
        ajaxBehavior.addAjaxBehaviorListener(new CustomAjaxBehaviorListener(question));
        ajaxBehavior.setUpdate("maincontentid-panel_" + question.getId());
        ajaxBehavior.setAsync(true);
        return ajaxBehavior;
    }
}
