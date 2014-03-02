package com.engagepoint.labs.wizard.ui.validators;

import java.util.List;

import com.engagepoint.labs.wizard.questions.WizardQuestion;
import com.engagepoint.labs.wizard.values.DateValue;
import com.engagepoint.labs.wizard.values.ListTextValue;
import com.engagepoint.labs.wizard.values.TextValue;
import com.engagepoint.labs.wizard.values.Value;

/**
 * Created by artem.pylypenko on 2/27/14.
 */
public class QuestionAnswerValidator {

    private WizardQuestion question;
    private String errorMessage;

    public QuestionAnswerValidator(WizardQuestion question) {
        this.question = question;
    }

    public boolean validate(Value value) {
        switch (question.getQuestionType()) {
            case TEXT:
                return validateTextValue(value);
            case PARAGRAPHTEXT:
                return validateTextValue(value);
            case MULTIPLECHOICE:
                return validateTextValue(value);
            case CHECKBOX:
                return validateListTextValue(value);
            case CHOOSEFROMLIST:
                return validateTextValue(value);
            case DATE:
                return validateDateOrTimeValue(value);
            case TIME:
                return validateDateOrTimeValue(value);
            default:
                break;
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

    public boolean validateDateQuestionComponent(Object value) {
        if (value == null) {
            errorMessage = "Empty field is not allowed here! class";
            return false;
        }
        return true;
    }

    public boolean validateTimeQuestion(Object value) {
        if (value == null) {
            errorMessage = "Empty field is not allowed here! class";
            return false;
        }
        return true;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    private boolean validateTextValue(Value value) {
        TextValue textValue = (TextValue) value;
        if (textValue == null || textValue.getValue().toString().isEmpty()) {
            return false;
        }
        return true;
    }

    private boolean validateListTextValue(Value value) {
        ListTextValue listTextValue = (ListTextValue) value;
        if (listTextValue == null
                || ((List) listTextValue.getValue()).isEmpty()) {
            return false;
        }
        return true;
    }

    private boolean validateDateOrTimeValue(Value value) {
        DateValue timeValue = (DateValue) value;
        if (timeValue == null) {
            return false;
        }
        return true;
    }

}
