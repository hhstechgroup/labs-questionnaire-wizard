package com.engagepoint.labs.wizard.ui;

import com.engagepoint.component.UIDatePicker;
import com.engagepoint.component.UIEditor;
import com.engagepoint.component.UIInput;
import com.engagepoint.labs.wizard.questions.*;
import org.primefaces.component.calendar.Calendar;
import org.primefaces.component.datagrid.DataGrid;
import org.primefaces.component.fileupload.FileUpload;
import org.primefaces.component.outputlabel.OutputLabel;
import org.primefaces.component.panel.Panel;
import org.primefaces.component.selectmanycheckbox.SelectManyCheckbox;
import org.primefaces.component.selectonelistbox.SelectOneListbox;
import org.primefaces.component.selectonemenu.SelectOneMenu;
import org.primefaces.component.slider.Slider;

import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItem;
import javax.faces.component.UISelectItems;
import javax.faces.component.UISelectOne;
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
//                Calendar calendar = new Calendar();
//                calendar.setValue(new Date());
//                calendar.setId("calendarID");
//                calendar.setInView(true);
//
////                calendar.setShowOn("button");
////                calendar.setNavigator(true);
                // to do
                component = new Calendar();
                break;
            case TIME:
                // to do
                component = getCalendar();
                break;
            case RANGE:
                // to do
                component = getSlider(question);
                break;
            case FILEUPLOAD:
                component = new FileUpload();
                break;
            case GRID:
                // to do
                component = new DataGrid();
                break;
        }
        panel.getChildren().add(component);

        return panel;
    }

    private Slider getSlider(WizardQuestion question) {
        RangeQuestion rangeQuestion = (RangeQuestion) question;
        Slider slider = new Slider();
        slider.setMinValue(rangeQuestion.getStartRange());
        slider.setMaxValue(rangeQuestion.getEndRange());
        return slider;
    }


    private UISelectOne getSelectOneListbox(WizardQuestion question) {
        UISelectOne uiSelectOne = new UISelectOne();
        uiSelectOne.setRendererType("javax.faces.Listbox");
        List<String> optionsList = ((MultipleChoiseQuestion) question).getOptionsList();
        uiSelectOne.getChildren().add(getSelectItems(optionsList));
        return uiSelectOne;
    }

    private UIInput getUIInput() {
        return new UIInput();
    }

    private OutputLabel getLabel(WizardQuestion question) {
        OutputLabel label = new OutputLabel();
        label.setValue(question.getTitle());
        return label;
    }

    private UISelectOne getSelectOneMenu(WizardQuestion question) {
        UISelectOne uiSelectOne = new UISelectOne();
        List<String> optionsList = ((DropDownQuestion) question).getOptionsList();
        uiSelectOne.getChildren().add(getSelectItems(optionsList));
        return uiSelectOne;
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
            item = new SelectItem(optionsList.get(i));
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
