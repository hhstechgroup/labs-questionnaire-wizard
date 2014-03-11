package com.engagepoint.labs.wizard.questions;

import com.engagepoint.labs.wizard.bean.WizardForm;
import com.engagepoint.labs.wizard.bean.WizardTopic;
import com.engagepoint.labs.wizard.values.Value;
import org.primefaces.component.panel.Panel;
import super_binding.QType;

import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
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
                    show = compareString(parentQuestionAnswer, expectedAnswer[0]);
                    break;
                case DATE:
                    show = compareDateOrTime(parentQuestion.getQuestionType(), parentQuestionAnswer, expectedAnswer[0]);
                    break;
                case FILE:
                    show = compareFile(parentQuestionAnswer, expectedAnswer[0]);
                    break;
                case LIST:
                    show = compareList(parentQuestionAnswer, expectedAnswer);
                    break;
                case GRID:
                    break;
            }
        }
        if (show) {
            panel.setVisible(true);
            question.setIgnored(false);
        } else {
            panel.setVisible(false);
            question.setIgnored(true);
            question.resetAnswer();
            if (question.isRequired()) {
                question.setValid(false);
            }
            ((UIOutput) FacesContext.getCurrentInstance().getViewRoot().findComponent("maincontentid-" + question.getId())).resetValue();
        }
    }

    public void executeAllRulesOnCurrentTopic(WizardQuestion question) {
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

    private boolean compareString(Value parentQuestionAnswer, String stringToCompareWith) {
        return parentQuestionAnswer.getValue().equals(stringToCompareWith);
    }

    private boolean compareDateOrTime(QType parentQuestionType, Value parentQuestionAnswer, String dateToCompareWith) {
        SimpleDateFormat format;
        boolean compareResult = false;
        if (parentQuestionType.equals(QType.DATE)) {
            format = new SimpleDateFormat(DateQuestion.DATE_FORMAT);
        } else {
            format = new SimpleDateFormat(TimeQuestion.TIME_FORMAT);
        }
        try {
            Date date = format.parse(dateToCompareWith);
            compareResult = date.compareTo((Date) parentQuestionAnswer.getValue()) == 0;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return compareResult;
    }

    private boolean compareFile(Value parentQuestionAnswer, String stringToCompareWith) {
        return ((parentQuestionAnswer.getValue()) != 0) && (stringToCompareWith.equals("true"));
    }

    private boolean compareList(Value parentQuestionAnswer, String[] stringArrayToCompareWith) {
        List<String> valueList = (List<String>) parentQuestionAnswer.getValue();
        return valueList.containsAll(Arrays.asList(stringArrayToCompareWith))
                && stringArrayToCompareWith.length == valueList.size();
    }

}
