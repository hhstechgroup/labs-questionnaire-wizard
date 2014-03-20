package com.engagepoint.labs.wizard.ui.validators;

import com.engagepoint.labs.wizard.questions.WizardQuestion;
import com.engagepoint.labs.wizard.values.*;

import java.util.List;

/**
 * Created by artem.pylypenko on 2/27/14.
 */
public class QuestionAnswerValidator {

    private WizardQuestion question;

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
            case FILEUPLOAD:
                return validateFileUpload(value);
            default:
                break;
        }
        return true;
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

    private boolean validateFileUpload(Value value) {
        FileValue fileValue = (FileValue) value;
        if (fileValue == null) {
            return false;
        }
        return true;
    }
}
