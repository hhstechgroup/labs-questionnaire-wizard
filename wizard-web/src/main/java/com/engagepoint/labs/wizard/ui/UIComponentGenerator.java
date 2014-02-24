package com.engagepoint.labs.wizard.ui;

import com.engagepoint.labs.wizard.questions.*;
import com.engagepoint.labs.wizard.values.ListTextValue;
import com.engagepoint.labs.wizard.values.TextValue;
import com.engagepoint.labs.wizard.values.Value;
import org.primefaces.component.button.Button;
import org.primefaces.component.calendar.Calendar;
import org.primefaces.component.datagrid.DataGrid;
import org.primefaces.component.fileupload.FileUpload;
import org.primefaces.component.inputtext.InputText;
import org.primefaces.component.inputtextarea.InputTextarea;
import org.primefaces.component.outputlabel.OutputLabel;
import org.primefaces.component.panel.Panel;
import org.primefaces.component.selectmanycheckbox.SelectManyCheckbox;
import org.primefaces.component.slider.Slider;

import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItems;
import javax.faces.component.html.HtmlSelectOneListbox;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ValueChangeEvent;
import javax.faces.event.ValueChangeListener;
import javax.faces.model.SelectItem;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by igor.guzenko on 2/11/14.
 */
public class UIComponentGenerator {
    private Panel panel;

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
                component = getCalendar(question);
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
        panel.getChildren().add(component);

        Button tooltip = new Button();
        tooltip.setId("tooltip_" + question.getId());
        tooltip.setTitle(question.getHelpText());
        tooltip.setIcon("ui-icon-question");
        tooltip.setStyleClass("custom");
        tooltip.setStyle("position: absolute; left: auto; bottom: auto; padding: 1px");
        tooltip.setDisabled(true);
        panel.getChildren().add(tooltip);

        return panel;
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
        // hardcoded default answers
        if(question.getDefaultAnswer()==null){
            TextValue defHardValue = new TextValue();
            defHardValue.setValue(optionsList.get(0));
            question.setDefaultAnswer(defHardValue);
        }
        int height  = 17*optionsList.size();
        sOneListbox.setStyle("height:"+height+"px");

        Value defaultAnswer = question.getDefaultAnswer();
        Value answer = question.getAnswer();

        sOneListbox.addValueChangeListener(new ValueChangeListener() {
            @Override
            public void processValueChange(ValueChangeEvent event) throws AbortProcessingException {
                TextValue value = new TextValue();
                value.setValue(event.getNewValue().toString());
                question.setAnswer(value);
            }
        });

        if (defaultAnswer != null && answer == null) {
            sOneListbox.setValue(defaultAnswer.getValue());
        } else if (answer != null) {

            sOneListbox.setValue(answer.getValue());
        }

        return sOneListbox;
    }

    private InputText getInputText(final WizardQuestion question) {
        InputText inputText = new InputText();
        Value defaultAnswer = question.getDefaultAnswer();
        Value answer = question.getAnswer();
        inputText.setOnchange("submit()");
        // Creating Listener interface implamintation
        inputText.addValueChangeListener(new ValueChangeListener() {
            @Override
            public void processValueChange(ValueChangeEvent event) throws AbortProcessingException {
                Value value = new TextValue();
                value.setValue(event.getNewValue());
                question.setAnswer(value);
            }
        });
        if (defaultAnswer != null && answer == null) {
            inputText.setValue(defaultAnswer.getValue().toString());
        } else if (answer != null) {
            inputText.setValue(answer.getValue().toString());
        }
        return inputText;
    }

    private OutputLabel getLabel(WizardQuestion question) {
        OutputLabel label = new OutputLabel();
        label.setValue(question.getTitle());
        return label;
    }

    private HtmlSelectOneMenu getSelectOneMenu(final WizardQuestion question) {

        HtmlSelectOneMenu selectOneMenu = new HtmlSelectOneMenu();
        selectOneMenu.setOnchange("submit()");
        List<String> optionsList = ((DropDownQuestion) question).getOptionsList();
        if(question.getDefaultAnswer()==null){
            TextValue defHardValue = new TextValue();
            defHardValue.setValue(optionsList.get(0));
            question.setDefaultAnswer(defHardValue);
        }
        Value defaultAnswer = question.getDefaultAnswer();
        Value answer = question.getAnswer();
        selectOneMenu.getChildren().add(getSelectItems(optionsList));
        selectOneMenu.addValueChangeListener(new ValueChangeListener() {
            @Override
            public void processValueChange(ValueChangeEvent event) throws AbortProcessingException {
                TextValue newValue = new TextValue();
                newValue.setValue(event.getNewValue());
                question.setAnswer(newValue);
            }
        });
        if (defaultAnswer != null && answer == null) {
            selectOneMenu.setValue(defaultAnswer.getValue());
        } else if (answer != null) {

           selectOneMenu.setValue(answer.getValue());
        }


        return selectOneMenu;
    }

    private SelectManyCheckbox getSelectManyCheckbox(final WizardQuestion question) {
        SelectManyCheckbox checkbox = new SelectManyCheckbox();
        checkbox.setLayout("pageDirection");
        checkbox.setOnchange("submit()");
        List<String> optionsList = ((CheckBoxesQuestion) question).getOptionsList();
        checkbox.addValueChangeListener(new ValueChangeListener() {
            @Override
            public void processValueChange(ValueChangeEvent event) throws AbortProcessingException {
                ListTextValue listTextValue = new ListTextValue();
                Object[] arr = (Object[]) event.getNewValue();
                List answersList = new ArrayList();
                for (int i = 0; i < arr.length; i++) {
                    answersList.add(arr[i]);
                }
                listTextValue.setValue(answersList);
                question.setAnswer(listTextValue);
            }
        });
        //creating default answer
        ListTextValue listTextValue;
        List<String> defaultAnswer;
        if (!optionsList.isEmpty()) {
            listTextValue = new ListTextValue();
            defaultAnswer = new ArrayList<>();
            defaultAnswer.add(optionsList.get(0));
            listTextValue.setValue(defaultAnswer);
            question.setDefaultAnswer(listTextValue);
        }
        checkbox.getChildren().add(getSelectItems(optionsList));
        if (question.getDefaultAnswer() != null && question.getAnswer() == null) {
            checkbox.setValue(question.getDefaultAnswer().getValue());
        } else if (question.getAnswer() != null) {
            checkbox.setValue(question.getAnswer().getValue());
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

    private InputTextarea getInputTextArea(final WizardQuestion question) {
        InputTextarea inputTextarea = new InputTextarea();
        Value defaultAnswer = question.getDefaultAnswer();
        Value answer = question.getAnswer();
        inputTextarea.setOnchange("submit()");
        // Creating Listener interface implamintation
        inputTextarea.addValueChangeListener(new ValueChangeListener() {
            @Override
            public void processValueChange(ValueChangeEvent event) throws AbortProcessingException {
                Value value = new TextValue();
                value.setValue(event.getNewValue());
                question.setAnswer(value);
            }
        });
        if (defaultAnswer != null && answer == null) {
            inputTextarea.setValue(defaultAnswer.getValue().toString());
        } else if (answer != null) {
            inputTextarea.setValue(answer.getValue().toString());
        }
        return inputTextarea;
    }

    private Calendar getCalendar(WizardQuestion question) {
        Calendar calendar = new Calendar();
        calendar.setValue(new Date());
        return calendar;
    }

    private Calendar getTime(WizardQuestion question) {
        Calendar timeCalendar = new Calendar();
        timeCalendar.setValue(new Date());
        timeCalendar.setPattern("HH:mm");
        timeCalendar.setTimeOnly(true);
        return timeCalendar;
    }

    private FileUpload getFileUpload(WizardQuestion question) {
        FileUpload fileUpload = new FileUpload();
        return fileUpload;
    }
}
