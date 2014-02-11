package com.engagepoint.labs.wizard.ui;

import com.engagepoint.component.UIDatePicker;
import com.engagepoint.component.UIEditor;
import com.engagepoint.component.UIInput;
import com.engagepoint.labs.wizard.questions.CheckBoxesQuestion;
import com.engagepoint.labs.wizard.questions.WizardQuestion;
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
                checkbox.getChildren().add(getCheckBoxSelectItems(optionsList));
                questionPanel.getChildren().add(checkbox);
                break;
            case CHOOSEFROMLIST:
                questionPanel.getChildren().add(new SelectOneMenu());
                break;
            case MULTIPLECHOICE:
                questionPanel.getChildren().add(new SelectOneListbox());
            case DATE:
                questionPanel.getChildren().add(new UIDatePicker());



        }
        return questionPanel;

    }

    private UISelectItems getCheckBoxSelectItems(List<String> optionsList) {
        SelectItem item;
        UISelectItems selectItems = new UISelectItems();
        List<SelectItem> itemsList = new ArrayList<>();
        for (int i = 0; i < optionsList.size(); i++) {
            item = new SelectItem((i+1),optionsList.get(i));
            itemsList.add(item);
        }
        selectItems.setValue(itemsList);
        return selectItems;
    }

}
