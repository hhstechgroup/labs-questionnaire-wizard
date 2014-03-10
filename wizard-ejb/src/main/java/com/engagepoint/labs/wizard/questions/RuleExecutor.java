package com.engagepoint.labs.wizard.questions;

import com.engagepoint.labs.wizard.bean.WizardForm;
import com.engagepoint.labs.wizard.bean.WizardTopic;
import com.engagepoint.labs.wizard.values.Value;
import org.primefaces.component.panel.Panel;
import org.primefaces.context.RequestContext;

import javax.faces.context.FacesContext;
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
        boolean show = false;
        WizardQuestion parentQuestion = form.getWizardQuestionById(parentID);
        Value parentQuestionAnswer = parentQuestion.getAnswer();
        String panelVarAndId = "maincontentid-panel_" + question.getId();
        Panel panel = (Panel) FacesContext.getCurrentInstance().getViewRoot().findComponent(panelVarAndId);
        if (!parentQuestion.isIgnored() && parentQuestionAnswer != null && parentQuestionAnswer.getValue() != null) {
            switch (parentQuestionAnswer.getType()) {
                case STRING:
                    show = parentQuestionAnswer.getValue().equals(expectedAnswer[0]);
                    break;
                case DATE:
                    break;
                case FILE:
                    break;
                case LIST:
                    List<String> valueList = (List<String>) parentQuestionAnswer.getValue();
                    show = valueList.containsAll(Arrays.asList(expectedAnswer))
                            && expectedAnswer.length == valueList.size();
                    break;
                case GRID:
                    break;
            }
        }
        System.out.println("+++++++++++++ Question ID = " + question.getId() + "; parent is ignored = "
                + parentQuestion.isIgnored() + " +++++++++++++++++++");
        if (show) {
            panel.setVisible(true);
            question.setIgnored(false);
        } else {
            panel.setVisible(false);
            question.setIgnored(true);
        }
    }

    public void updateAllQuestionsOnTopic(WizardQuestion question) {
        WizardTopic topic = form.findWizardTopicByQuestionId(question.getId());
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
