package com.engagepoint.labs.wizard.ruleExecutors;

import com.engagepoint.labs.wizard.bean.WizardForm;
import com.engagepoint.labs.wizard.bean.WizardPage;
import com.engagepoint.labs.wizard.bean.WizardTopic;
import com.engagepoint.labs.wizard.questions.DateQuestion;
import com.engagepoint.labs.wizard.questions.TimeQuestion;
import com.engagepoint.labs.wizard.questions.WizardQuestion;
import com.engagepoint.labs.wizard.values.Value;
import org.apache.log4j.Logger;
import org.primefaces.component.menuitem.MenuItem;
import super_binding.QType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by artem.pylypenko on 3/19/14.
 */
public class PageRuleExecutor extends RuleExecutorAbstract {
    private WizardForm form;
    private MenuItem menuItem;
    private WizardPage page;
    private boolean isAlreadyShowing;
    private static final Logger LOGGER = Logger.getLogger(PageRuleExecutor.class);

    public PageRuleExecutor(WizardForm form) {
        this.form = form;
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(MenuItem menuItem) {
        this.menuItem = menuItem;
    }

    public WizardPage getPage() {
        return page;
    }

    public void setPage(WizardPage page) {
        this.page = page;
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
            showPage();
            isAlreadyShowing = true;
        } else {
            if (isAlreadyShowing) {
                change = true;
            }
            hidePage();
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
        return ((Integer) (parentQuestionAnswer.getValue()) != 0) && (stringToCompareWith.equals("true"));
    }

    private boolean compareList(Value parentQuestionAnswer, String[] stringArrayToCompareWith) {
        List<String> valueList = (List<String>) parentQuestionAnswer.getValue();
        return valueList.containsAll(Arrays.asList(stringArrayToCompareWith))
                && stringArrayToCompareWith.length == valueList.size();
    }

    private void showPage() {
        menuItem.setRendered(true);
        page.setIgnored(false);
    }

    private void hidePage() {
        menuItem.setRendered(false);
        page.setIgnored(true);
        page.resetPage();
    }
}
