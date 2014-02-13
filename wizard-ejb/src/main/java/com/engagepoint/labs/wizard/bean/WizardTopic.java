/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.engagepoint.labs.wizard.bean;

import com.engagepoint.labs.wizard.questions.WizardQuestion;
import java.util.List;

/**
 *
 * @author artem.pylypenko
 */
public class WizardTopic {

    private String id;
    private String groupTitle;
    private List<WizardQuestion> wizardQuestionList;

    public WizardTopic() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<WizardQuestion> getWizardQuestionList() {
        return wizardQuestionList;
    }

    public void setWizardQuestionList(List<WizardQuestion> wizardQuestionList) {
        this.wizardQuestionList = wizardQuestionList;
    }

    public String getGroupTitle() {
        return groupTitle;
    }

    public void setGroupTitle(String groupTitle) {
        this.groupTitle = groupTitle;
    }

}
