package com.engagepoint.labs.wizard.rulexecutors;

import com.engagepoint.labs.wizard.bean.WizardForm;
import com.engagepoint.labs.wizard.questions.GridQuestion;
import com.engagepoint.labs.wizard.questions.RangeQuestion;
import com.engagepoint.labs.wizard.questions.WizardQuestion;
import com.engagepoint.labs.wizard.values.Value;
import org.primefaces.component.panel.Panel;
import org.primefaces.component.slider.Slider;
import super_binding.QType;

import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UIOutput;
import javax.faces.component.UIPanel;
import javax.faces.component.html.HtmlPanelGroup;
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
        String componentId = "maincontentid-panelid" + question.getId();
        UIPanel panel = (UIPanel) FacesContext.getCurrentInstance().getViewRoot().findComponent(componentId);
        if (!parentQuestion.isIgnored() && parentQuestionAnswer != null && parentQuestionAnswer.getValue() != null) {
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

    private void showQuestionPanel(UIPanel panel) {
        if (panel instanceof HtmlPanelGroup) {
            ((HtmlPanelGroup) panel).setStyle("visibility:visible");
        } else {
            ((Panel) panel).setVisible(true);
        }
        panel.setRendered(true);
        question.setIgnored(false);
    }

    private void hideQuestionPanel(UIPanel panel) {
        if (panel instanceof HtmlPanelGroup) {
            ((HtmlPanelGroup) panel).setStyle("visibility:hidden");
        } else {
            ((Panel) panel).setVisible(false);
        }
        question.setIgnored(true);
        question.resetAnswer();
        if (question.isRequired()) {
            question.setValid(false);
        }
        resetComponentValue();
    }

    private void resetComponentValue() {
        if (question.getDefaultAnswer() != null && question.getQuestionType() == QType.RANGE) {
            Slider slider = (Slider) FacesContext.getCurrentInstance().getViewRoot().findComponent("maincontentid-" + question.getId());
            if (slider != null) {
                int min = ((RangeQuestion) question).getStartRange();
                int max = ((RangeQuestion) question).getEndRange();
                slider.setMinValue(min);
                slider.setMaxValue(max);
            } else {
                UIOutput component = (UIOutput) FacesContext.getCurrentInstance().getViewRoot().findComponent("maincontentid-" + question.getId());
                if (question.getDefaultAnswer() != null && component != null) {
                    component.setValue(question.getDefaultAnswer().getValue());
                } else if (component != null) {
                    component.resetValue();
                }
            }
        }
    }
}
