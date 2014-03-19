package com.engagepoint.labs.wizard.rulexecutors;

import com.engagepoint.labs.wizard.bean.WizardForm;
import com.engagepoint.labs.wizard.bean.WizardPage;
import com.engagepoint.labs.wizard.questions.WizardQuestion;
import com.engagepoint.labs.wizard.values.Value;
import org.primefaces.component.menuitem.MenuItem;

/**
 * Created by artem.pylypenko on 3/19/14.
 */
public class PageRuleExecutor extends RuleExecutorAbstract {
    private WizardForm form;
    private MenuItem menuItem;
    private WizardPage page;
    private boolean isAlreadyShowing;

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
