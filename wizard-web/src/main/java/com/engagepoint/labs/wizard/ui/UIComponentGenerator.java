package com.engagepoint.labs.wizard.ui;

import com.engagepoint.component.UIDatePicker;
import com.engagepoint.component.UIEditor;
import com.engagepoint.component.UIInput;
import com.engagepoint.labs.wizard.questions.*;
import org.primefaces.component.calendar.Calendar;
import org.primefaces.component.outputlabel.OutputLabel;
import org.primefaces.component.panel.Panel;
import org.primefaces.component.selectmanycheckbox.SelectManyCheckbox;
import org.primefaces.component.selectonelistbox.SelectOneListbox;
import org.primefaces.component.selectonemenu.SelectOneMenu;
import org.primefaces.component.slider.Slider;

import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItems;
import javax.faces.model.SelectItem;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by igor.guzenko on 2/11/14.
 */
public class UIComponentGenerator {
    private Panel mainPanel;

    public UIComponentGenerator() {
        mainPanel = new Panel();
    }

    public Panel getMainPanel(List<WizardQuestion> wizardQuestionList) {
        mainPanel.getChildren().clear();
        for (WizardQuestion question : wizardQuestionList) {
            analyzeQuestion(question);
        }
        return mainPanel;
    }

    private void analyzeQuestion(WizardQuestion question) {
        addComponentToMainPanel(getLabel(question));
        UIComponent component = null;

        switch (question.getQuestionType()) {
            case TEXT:
                component = getUIInput();
                break;
            case PARAGRAPHTEXT:
                component = getUIEditor();
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
                component = getUIDatePicker();
                break;
            case TIME:
                component = getCalendar();
            case RANGE:
                component = getSlider(question);
                break;
            //todo

        }
        addComponentToMainPanel(component);

    }

    private Slider getSlider(WizardQuestion question) {
        RangeQuestion rangeQuestion = (RangeQuestion)question;
        Slider slider = new Slider();
        slider.setMinValue(rangeQuestion.getStartRange());
        slider.setMaxValue(rangeQuestion.getEndRange());
        return slider;
    }


    private SelectOneListbox getSelectOneListbox(WizardQuestion question) {
        List<String> optionsList = ((MultipleChoiseQuestion) question).getOptionsList();
        SelectOneListbox selectOneListbox = new SelectOneListbox();
        selectOneListbox.getChildren().add(getSelectItems(optionsList));
        return selectOneListbox;
    }


    private void addComponentToMainPanel(UIComponent component) {
        mainPanel.getChildren().add(component);
    }

    private UIInput getUIInput() {
        return new UIInput();
    }

    private OutputLabel getLabel(WizardQuestion question) {
        OutputLabel label = new OutputLabel();
        label.setValue(question.getTitle());
        return label;
    }

    private SelectOneMenu getSelectOneMenu(WizardQuestion question) {
        List<String> optionsList = ((DropDownQuestion) question).getOptionsList();
        SelectOneMenu selectOneMenu = new SelectOneMenu();
        selectOneMenu.getChildren().add(getSelectItems(optionsList));
        return selectOneMenu;
    }

    private SelectManyCheckbox getSelectManyCheckbox(WizardQuestion question) {
        SelectManyCheckbox checkbox = new SelectManyCheckbox();
        List<String> optionsList = ((CheckBoxesQuestion) question).getOptionsList();
        checkbox.getChildren().add(getSelectItems(optionsList));
        return checkbox;

    }

    private UISelectItems getSelectItems(List<String> optionsList) {
        SelectItem item;
        UISelectItems selectItems = new UISelectItems();
        List<SelectItem> itemsList = new ArrayList<>();
        for (int i = 0; i < optionsList.size(); i++) {
            item = new SelectItem((i + 1), optionsList.get(i));
            itemsList.add(item);
        }
        selectItems.setValue(itemsList);
        return selectItems;
    }

    private UIEditor getUIEditor() {
        return new UIEditor();
    }

    private UIDatePicker getUIDatePicker() {
       return new UIDatePicker();
    }

    private Calendar getCalendar() {
        Calendar timeCalendar = new Calendar();
        timeCalendar.setPattern("HH:mm");
        timeCalendar.setTimeOnly(true);
        return timeCalendar;
    }
}
