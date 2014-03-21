package com.engagepoint.labs.wizard.rulexecutors;

import com.engagepoint.labs.wizard.bean.WizardForm;
import com.engagepoint.labs.wizard.questions.GridQuestion;
import com.engagepoint.labs.wizard.questions.WizardQuestion;
import com.engagepoint.labs.wizard.values.Value;

import org.primefaces.component.panel.Panel;

import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;

/**
 * Created by artem.pylypenko on 3/18/14.
 */
public class QuestionRuleExecutor extends RuleExecutorAbstract {
    private WizardForm form;
    private WizardQuestion question;
    private boolean isAlreadyShowing;

    public QuestionRuleExecutor(WizardForm form) {
        this.form = form;
    }

    public WizardQuestion getQuestion() {
        return question;
    }

    public void setQuestion(WizardQuestion question) {
        this.question = question;
    }

    @Override
    public boolean renderedRule(String parentID, String[] expectedAnswer) {
        boolean change = false;
        boolean show = false;
        WizardQuestion parentQuestion = form.getWizardQuestionById(parentID);
        Value parentQuestionAnswer = parentQuestion.getAnswer();
        String componentId = "maincontentid-panel_" + question.getId();
        Panel panel = (Panel) FacesContext.getCurrentInstance().getViewRoot()
                .findComponent(componentId);
        if (!parentQuestion.isIgnored() && parentQuestionAnswer != null
                && parentQuestionAnswer.getValue() != null) {
            switch (parentQuestionAnswer.getType()) {
                case STRING:
                    show = compareString(parentQuestionAnswer, expectedAnswer[0]);
                    break;
                case DATE:
                    show = compareDateOrTime(parentQuestion.getQuestionType(),
                            parentQuestionAnswer, expectedAnswer[0]);
                    break;
                case FILE:
                    show = compareFile(parentQuestionAnswer, expectedAnswer[0]);
                    break;
                case LIST:
                    show = compareList(parentQuestionAnswer, expectedAnswer);
                    break;
                case GRID:
                    GridQuestion gridQuestion = (GridQuestion) parentQuestion;
                    show = compareGrid(gridQuestion.getAnswerAsStrings(),
                            expectedAnswer);
                    break;
                default:
                    break;
            }
        }
        if (show) {
            if (!isAlreadyShowing) {
                change = true;
            }
            showQuestionPanel(panel);
            isAlreadyShowing = true;
        } else {
            if (isAlreadyShowing) {
                change = true;
            }
            hideQuestionPanel(panel);
            isAlreadyShowing = false;
        }
        return change;
    }

    private void showQuestionPanel(Panel panel) {
        panel.setVisible(true);
        question.setIgnored(false);
    }

    private void hideQuestionPanel(Panel panel) {
        panel.setVisible(false);
        question.setIgnored(true);
        question.resetAnswer();
        if (question.isRequired()) {
            question.setValid(false);
        }
        resetComponentValue();
    }

    private void resetComponentValue() {
        UIOutput component = (UIOutput) FacesContext.getCurrentInstance()
                .getViewRoot()
                .findComponent("maincontentid-" + question.getId());
        if (question.getDefaultAnswer() != null && component != null) {
            component.setValue(question.getDefaultAnswer().getValue());
        } else if (component != null) {
            component.resetValue();
        }
    }
}
