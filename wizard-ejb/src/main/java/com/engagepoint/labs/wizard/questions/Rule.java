package com.engagepoint.labs.wizard.questions;

import com.engagepoint.labs.wizard.bean.WizardForm;
import com.engagepoint.labs.wizard.bean.WizardTopic;
import org.apache.commons.jexl2.Expression;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.MapContext;
import org.primefaces.context.RequestContext;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Alternative;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

/**
 * Created by artem.pylypenko on 3/5/14.
 */
public class Rule implements Serializable {

    private WizardTopic topic;
    private WizardQuestion question;

    public Rule(WizardQuestion question, WizardTopic topic) {
        this.question = question;
        this.topic = topic;
    }

    public void renderedRule(String parentID, String expectedAnswer) {
        boolean rendered = false;
        if (topic.findQuestionById(parentID).getAnswer() == null) {
            rendered = false;
        } else if (topic.findQuestionById(parentID).getAnswer().getValue() == null) {
            rendered = false;
        } else if (topic.findQuestionById(parentID).getAnswer().getValue().toString().equals(expectedAnswer)) {
            rendered = true;
        }
        FacesContext.getCurrentInstance().getViewRoot().findComponent("maincontentid-" + question.getId()).setRendered(rendered);
    }

    public void updateAllQuestionsOnTopic() {
        for (WizardQuestion wizardQuestion : topic.getWizardQuestionList()) {
            wizardQuestion.executeAllRules();
        }
    }
}
