package com.engagepoint.labs.wizard.ui;

import com.engagepoint.component.UIDatePicker;
import com.engagepoint.component.UIEditor;
import com.engagepoint.component.UIInput;
import com.engagepoint.labs.wizard.questions.CheckBoxesQuestion;
import com.engagepoint.labs.wizard.questions.DropDownQuestion;
import com.engagepoint.labs.wizard.questions.MultipleChoiseQuestion;
import com.engagepoint.labs.wizard.questions.WizardQuestion;
import org.primefaces.component.calendar.Calendar;
import org.primefaces.component.panel.Panel;
import org.primefaces.component.selectmanycheckbox.SelectManyCheckbox;
import org.primefaces.component.selectonelistbox.SelectOneListbox;
import org.primefaces.component.selectonemenu.SelectOneMenu;

import javax.faces.component.UISelectItems;
import javax.faces.model.SelectItem;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by igor.guzenko on 2/11/14.
 */
public class UIComponentGenerator {
    private Panel questionPanel;


    public Panel getComponent(WizardQuestion wizardQuestion) {
        List<String> optionsList;
        questionPanel = new Panel();
        questionPanel.setMenuTitle(wizardQuestion.getTitle());
        questionPanel.setCollapsed(false);

        switch (wizardQuestion.getQuestionType()) {
            case TEXT:
                questionPanel.getChildren().add(new UIInput());
                break;
            case PARAGRAPHTEXT:
                questionPanel.getChildren().add(new UIEditor());
                break;
            case CHECKBOX:
                SelectManyCheckbox checkbox = new SelectManyCheckbox();
                optionsList = ((CheckBoxesQuestion) wizardQuestion).getOptionsList();
                checkbox.getChildren().add(getSelectItems(optionsList));
                questionPanel.getChildren().add(checkbox);
                break;
            case CHOOSEFROMLIST:
                optionsList = ((DropDownQuestion) wizardQuestion).getOptionsList();
                SelectOneMenu selectOneMenu = new SelectOneMenu();
                selectOneMenu.getChildren().add(getSelectItems(optionsList));
                questionPanel.getChildren().add(selectOneMenu);
                break;
            case MULTIPLECHOICE:
                optionsList = ((MultipleChoiseQuestion) wizardQuestion).getOptionsList();
                SelectOneListbox selectOneListbox = new SelectOneListbox();
                selectOneListbox.getChildren().add(getSelectItems(optionsList));
                questionPanel.getChildren().add(selectOneListbox);
                break;
            case DATE:
                questionPanel.getChildren().add(new UIDatePicker());
                break;
            case TIME:
                Calendar timeCalendar = new Calendar();
                timeCalendar.setPattern("HH:mm");
                timeCalendar.setTimeOnly(true);
                questionPanel.getChildren().add(timeCalendar);
                break;
            // todo add: fileupload, grid, range types




        }
        return questionPanel;

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

}
