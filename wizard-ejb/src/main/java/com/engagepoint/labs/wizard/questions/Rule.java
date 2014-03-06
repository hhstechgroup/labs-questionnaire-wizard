package com.engagepoint.labs.wizard.questions;

import com.engagepoint.labs.wizard.bean.WizardForm;
import com.engagepoint.labs.wizard.bean.WizardTopic;

import javax.faces.context.FacesContext;
import java.io.Serializable;

/**
 * Created by artem.pylypenko on 3/5/14.
 */
public class Rule implements Serializable {

    private WizardForm form;
    private WizardTopic parentTopic;
    private WizardQuestion question;


    public Rule(WizardForm form, WizardTopic parentTopic, WizardQuestion question) {
        this.form = form;
        this.parentTopic = parentTopic;
        this.question = question;
    }

    public void renderedRule(String parentID, String expectedAnswer) {
        boolean rendered = false;
        if (form.getWizardQuestionById(parentID).getAnswer() == null) {
            rendered = false;
        } else if (form.getWizardQuestionById(parentID).getAnswer().getValue() == null) {
            rendered = false;
        } else if (form.getWizardQuestionById(parentID).getAnswer().getValue().toString().equals(expectedAnswer)) {
            rendered = true;
        }
        FacesContext.getCurrentInstance().getViewRoot().findComponent("maincontentid-" + question.getId()).setRendered(rendered);
    }

    public void updateAllQuestionsOnTopic() {
        for (WizardQuestion wizardQuestion : parentTopic.getWizardQuestionList()) {
            wizardQuestion.executeAllRules();
        }
    }
}
