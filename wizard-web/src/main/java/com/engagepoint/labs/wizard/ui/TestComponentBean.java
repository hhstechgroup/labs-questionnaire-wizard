package com.engagepoint.labs.wizard.ui;

import com.engagepoint.labs.wizard.questions.CheckBoxesQuestion;
import com.engagepoint.labs.wizard.questions.TextQuestion;
import com.engagepoint.labs.wizard.questions.WizardQuestion;
import org.primefaces.component.panel.Panel;
import super_binding.QType;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by igor.guzenko on 2/12/14.
 */
@ManagedBean(name = "testBean")
@SessionScoped
public class TestComponentBean implements Serializable {
    private Panel mainPanel;
    private WizardQuestion question;
    CheckBoxesQuestion cquestion;
    private UIComponent component;
    private UIComponentGenerator uiComponentGenerator;

    public Panel getMainPanel() {
        return mainPanel;
    }

    public TestComponentBean() {
        mainPanel = new Panel();
        uiComponentGenerator = new UIComponentGenerator();

        question = new TextQuestion();
        question.setId("po100");
        question.setTitle("FirstQuestion");
        question.setQuestionType(QType.TEXT);

        cquestion = new CheckBoxesQuestion();
        cquestion.setId("sdsd");
        cquestion.setTitle("Second");
        cquestion.setQuestionType(QType.CHECKBOX);
        List<String> list = new ArrayList<>(2);
        list.add("Option1");
        list.add("Option2");
        cquestion.setOptionsList(list);
        ArrayList<WizardQuestion> questionArrayList = new ArrayList<>();
        questionArrayList.add(question);
        questionArrayList.add(cquestion);





    }

    public void setMainPanel(Panel mainPanel) {
        this.mainPanel = mainPanel;
    }
}
