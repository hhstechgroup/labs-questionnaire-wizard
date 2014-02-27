package com.engagepoint.labs.wizard.ui.validators;

import com.engagepoint.labs.wizard.questions.WizardQuestion;
import com.engagepoint.labs.wizard.values.ListTextValue;
import com.engagepoint.labs.wizard.values.TextValue;
import com.engagepoint.labs.wizard.values.Value;
import org.primefaces.component.inputtext.InputText;
import org.primefaces.component.inputtextarea.InputTextarea;

import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by artem.pylypenko on 2/27/14.
 */
public class QuestionAnswerValidator {

    private WizardQuestion question;
    private String errorMessage;

    public QuestionAnswerValidator(WizardQuestion question) {
        this.question = question;
    }

    public boolean validate(Object value) {
        switch (question.getQuestionType()) {
            case TEXT:
                return validateTextQuestionComponent(value);
            case PARAGRAPHTEXT:
                return validateTextAreaQuestionComponent(value);
            case MULTIPLECHOICE:
                return validateMultipleChoiseQuestionComponent(value);
            case CHECKBOX:
                return validateCheckBoxQuestionComponent(value);
            case CHOOSEFROMLIST:
                return validateDropDownQuestionComponent(value);
        }
        return true;
    }


    public boolean validateDropDownQuestionComponent(Object value) {
        if (value == null || value.toString().isEmpty()) {
            errorMessage = "Answer must be selected for this question!";
            return false;
        }
        return true;
    }

    public boolean validateCheckBoxQuestionComponent(Object value) {
        if (value == null || ((Object[]) value).length == 0) {
            errorMessage = "Empty field is not allowed here";
            return false;
        }
        return true;
    }

    public boolean validateMultipleChoiseQuestionComponent(Object value) {
        if (value == null || value.toString().isEmpty()) {
            errorMessage = "Answer must be selected for this question!";
            return false;
        }
        return true;
    }

    public boolean validateTextAreaQuestionComponent(Object value) {
        if (value == null) {
            errorMessage = "Empty field is not allowed here!";
            return false;
        } else {
            String currentValue = value.toString();
            currentValue = currentValue.replaceAll("\\s", "");
            if (currentValue.isEmpty()) {
                errorMessage = "Empty field is not allowed here!";
                return false;
            }
        }
        return true;
    }

    public boolean validateTextQuestionComponent(Object value) {
        if (value == null) {
            errorMessage = "Empty field is not allowed here!";
            return false;
        } else {
            String currentValue = value.toString();
            currentValue = currentValue.replaceAll("\\s", "");
            if (currentValue.isEmpty()) {
                errorMessage = "Empty field is not allowed here!";
                return false;
            }
        }
        return true;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
