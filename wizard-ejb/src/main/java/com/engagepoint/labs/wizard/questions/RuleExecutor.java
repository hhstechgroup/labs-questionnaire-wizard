package com.engagepoint.labs.wizard.questions;

import com.engagepoint.labs.wizard.bean.WizardForm;
import com.engagepoint.labs.wizard.bean.WizardTopic;
import com.engagepoint.labs.wizard.values.Value;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * Created by artem.pylypenko on 3/5/14.
 */
public class RuleExecutor implements Serializable {

    private WizardForm form;
    private WizardQuestion question;


    public RuleExecutor(WizardForm form) {
        this.form = form;
    }

    public void renderedRule(String parentID, String[] expectedAnswer) {
        boolean rendered = false;
        Value answer = form.getWizardQuestionById(parentID).getAnswer();
        if (answer != null && answer.getValue() != null) {
            switch (answer.getType()) {
                case STRING:
                    rendered = answer.getValue().equals(expectedAnswer[0]);
                    break;
                case DATE:
                    break;
                case FILE:
                    break;
                case LIST:
                    List<String> valueList = (List<String>) answer.getValue();
                    rendered = valueList.containsAll(Arrays.asList(expectedAnswer));
                    break;
                case GRID:
                    break;
            }
        }
        FacesContext.getCurrentInstance().getViewRoot().findComponent("maincontentid-panel_" + question.getId()).setRendered(rendered);
    }

    public void updateAllQuestionsOnTopic() {
        WizardTopic topic = form.findWizardTopicVyQuestionId(question.getId());
        for (WizardQuestion wizardQuestion : topic.getWizardQuestionList()) {
            wizardQuestion.executeAllRules();
        }
    }

    public WizardQuestion getQuestion() {
        return question;
    }

    public void setQuestion(WizardQuestion question) {
        this.question = question;
    }
}
