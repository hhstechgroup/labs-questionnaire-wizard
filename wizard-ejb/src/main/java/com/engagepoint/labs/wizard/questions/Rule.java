package com.engagepoint.labs.wizard.questions;

import com.engagepoint.labs.wizard.bean.WizardForm;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by artem.pylypenko on 3/5/14.
 */
public class Rule {

    @Inject
    private WizardForm wizardForm;

    public boolean renderedRule(String parentID, String expectedAnswer) {
//        if (wizardForm.getWizardQuestionById(parentID).getAnswer() == null) {
//            return false;
//        }
//        if(wizardForm.getWizardQuestionById(parentID).getAnswer().getValue() == null){
//            return false;
//        }
//        if (wizardForm.getWizardQuestionById(parentID).getAnswer().getValue().toString().equals(expectedAnswer)) {
//            return true;
//        }
        wizardForm.hashCode();
        return false;
    }

    public WizardForm getWizardForm() {
        return wizardForm;
    }

    public void setWizardForm(WizardForm wizardForm) {
        this.wizardForm = wizardForm;
    }
}
