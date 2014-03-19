package com.engagepoint.labs.wizard.ruleExecutors;

import com.engagepoint.labs.wizard.bean.WizardForm;
import com.engagepoint.labs.wizard.bean.WizardTopic;
import com.engagepoint.labs.wizard.questions.WizardQuestion;
import com.engagepoint.labs.wizard.values.Value;
import org.primefaces.component.menuitem.MenuItem;

/**
 * Created by artem.pylypenko on 3/18/14.
 */
public class TopicRuleExecutor extends RuleExecutorAbstract {
    private WizardForm form;
    private MenuItem menuItem;
    private WizardTopic topic;
    private boolean isAlreadyShowing;

    public TopicRuleExecutor(WizardForm form) {
        this.form = form;
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(MenuItem menuItem) {
        this.menuItem = menuItem;
    }

    public WizardTopic getTopic() {
        return topic;
    }

    public void setTopic(WizardTopic topic) {
        this.topic = topic;
    }

    @Override
    public boolean renderedRule(String parentID, String[] expectedAnswer) {
        boolean change = false;
        boolean show = false;
        WizardQuestion parentQuestion = form.getWizardQuestionById(parentID);
        Value parentQuestionAnswer = parentQuestion.getAnswer();
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
            if (!isAlreadyShowing) {
                change = true;
            }
            showTopic();
            isAlreadyShowing = true;
        } else {
            if (isAlreadyShowing) {
                change = true;
            }
            hideTopic();
            isAlreadyShowing = false;
        }
        return change;
    }

    private void showTopic() {
        menuItem.setRendered(true);
        topic.setIgnored(false);
    }

    private void hideTopic() {
        menuItem.setRendered(false);
        topic.setIgnored(true);
        topic.resetTopic();
    }
}
