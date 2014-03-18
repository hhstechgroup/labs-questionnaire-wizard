package com.engagepoint.labs.wizard.ruleExecutors;

import com.engagepoint.labs.wizard.bean.WizardForm;
import com.engagepoint.labs.wizard.bean.WizardTopic;
import com.engagepoint.labs.wizard.questions.DateQuestion;
import com.engagepoint.labs.wizard.questions.TimeQuestion;
import com.engagepoint.labs.wizard.questions.WizardQuestion;
import com.engagepoint.labs.wizard.values.Value;
import org.apache.log4j.Logger;
import org.primefaces.component.menuitem.MenuItem;
import org.primefaces.component.panel.Panel;
import super_binding.QType;

import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by artem.pylypenko on 3/18/14.
 */
public class TopicRuleExecutor extends RuleExecutorAbstract {
    private WizardForm form;
    private MenuItem menuItem;
    private WizardTopic topic;
    private boolean isAlreadyShowing;
    private static final Logger LOGGER = Logger.getLogger(TopicRuleExecutor.class);

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
        System.out.println("*********** ID = " + topic.getId());
        if (show) {
            if (!isAlreadyShowing) {
                change = true;
            }
            showTopic();
            System.out.println("%%%%%%%% show method!!!!!!!!!! show = " + show + " ; menuitem rendered =" + menuItem.isRendered());
            isAlreadyShowing = true;
        } else {
            if (isAlreadyShowing) {
                change = true;
            }
            hideTopic();
            System.out.println("$$$$$$$$$$$$ hide method !!!! show = " + show + " ; menuitem rendered =" + menuItem.isRendered());
            isAlreadyShowing = false;
        }
        return change;
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
            LOGGER.warn(e.getMessage());
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

    private void showTopic() {
        menuItem.setRendered(true);
        topic.setIgnored(false);
    }

    private void hideTopic() {
        menuItem.setRendered(false);
        topic.setIgnored(true);
//        question.resetAnswer();
//        if (question.isRequired()) {
//            question.setValid(false);
//        }
//        resetComponentValue();
    }

//    private void resetComponentValue() {
//        UIOutput component = (UIOutput) FacesContext.getCurrentInstance().getViewRoot().findComponent("maincontentid-" + question.getId());
//        if (question.getDefaultAnswer() != null && component != null) {
//            component.setValue(question.getDefaultAnswer().getValue());
//        } else if (component != null) {
//            component.resetValue();
//        }
//    }
}