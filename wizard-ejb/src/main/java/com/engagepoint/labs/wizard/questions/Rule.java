package com.engagepoint.labs.wizard.questions;

import com.engagepoint.labs.wizard.bean.WizardForm;
import org.apache.commons.jexl2.Expression;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.MapContext;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

/**
 * Created by artem.pylypenko on 3/5/14.
 */
public class Rule implements Serializable {

    @Inject
    private WizardForm wizardForm;

    public void renderedRule(String parentID, String expectedAnswer) {
//        if (wizardForm.getWizardQuestionById(parentID).getAnswer() == null) {
//            return false;
//        }
//        if(wizardForm.getWizardQuestionById(parentID).getAnswer().getValue() == null){
//            return false;
//        }
//        if (wizardForm.getWizardQuestionById(parentID).getAnswer().getValue().toString().equals(expectedAnswer)) {
//            return true;
//        }
//        wizardForm.hashCode();
//        WizardQuestion question = new CheckBoxesQuestion();
//        JexlEngine jexlEngine = new JexlEngine();
//        String rule = "question.rule.renderedRule('qp123', '123456')";
//        Expression expression = jexlEngine.createExpression(rule);
//        JexlContext context = new MapContext();
//        context.set("question", question);
//        question.setRule(new Rule());
//        boolean rendered = (boolean) expression.evaluate(context);
        FacesContext.getCurrentInstance().getViewRoot().findComponent("maincontentid-qawes123").setRendered(false);
//        return false;
    }

    public WizardForm getWizardForm() {
        return wizardForm;
    }

    public void setWizardForm(WizardForm wizardForm) {
        this.wizardForm = wizardForm;
    }
}
