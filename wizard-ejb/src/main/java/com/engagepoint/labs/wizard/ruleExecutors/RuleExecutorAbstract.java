package com.engagepoint.labs.wizard.ruleExecutors;

import com.engagepoint.labs.wizard.bean.WizardForm;
import com.engagepoint.labs.wizard.questions.DateQuestion;
import com.engagepoint.labs.wizard.questions.TimeQuestion;
import com.engagepoint.labs.wizard.values.Value;
import org.apache.log4j.Logger;
import super_binding.QType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by artem.pylypenko on 3/18/14.
 */
public abstract class RuleExecutorAbstract {
    private WizardForm form;
    private boolean isAlreadyShowing;
    private static final Logger LOGGER = Logger.getLogger(RuleExecutorAbstract.class.getName());

    protected RuleExecutorAbstract() {
    }

    protected RuleExecutorAbstract(WizardForm form) {
        this.form = form;
    }

    public boolean isAlreadyShowing() {
        return isAlreadyShowing;
    }

    public void setAlreadyShowing(boolean isAlreadyShowing) {
        this.isAlreadyShowing = isAlreadyShowing;
    }

    public WizardForm getForm() {

        return form;
    }

    public void setForm(WizardForm form) {
        this.form = form;
    }

    public abstract boolean renderedRule(String parentID, String[] expectedAnswer);

    protected boolean compareString(Value parentQuestionAnswer, String stringToCompareWith) {
        return parentQuestionAnswer.getValue().equals(stringToCompareWith);
    }

    protected boolean compareDateOrTime(QType parentQuestionType, Value parentQuestionAnswer, String dateToCompareWith) {
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
            LOGGER.warn("ParseException", e);
        }
        return compareResult;
    }

    protected boolean compareFile(Value parentQuestionAnswer, String stringToCompareWith) {
        return ((Integer) (parentQuestionAnswer.getValue()) != 0) && (stringToCompareWith.equals("true"));
    }

    protected boolean compareList(Value parentQuestionAnswer, String[] stringArrayToCompareWith) {
        List<String> valueList = (List<String>) parentQuestionAnswer.getValue();
        return valueList.containsAll(Arrays.asList(stringArrayToCompareWith))
                && stringArrayToCompareWith.length == valueList.size();
    }

}
