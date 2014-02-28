package com.engagepoint.labs.wizard.ui.validators;

import com.engagepoint.labs.wizard.questions.WizardQuestion;
import com.engagepoint.labs.wizard.values.ListTextValue;
import com.engagepoint.labs.wizard.values.TextValue;
import com.engagepoint.labs.wizard.values.Value;
import org.primefaces.component.inputtext.InputText;
import org.primefaces.component.inputtextarea.InputTextarea;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by igor.guzenko on 2/26/14.
 */
public class ComponentValidator implements Validator {
    private final WizardQuestion question;
    private QuestionAnswerValidator questionAnswerValidator;

    public ComponentValidator(final WizardQuestion question) {
        this.question = question;
        this.questionAnswerValidator = new QuestionAnswerValidator(question);
    }

    @Override
    public void validate(FacesContext context, UIComponent component,
                         Object value) throws ValidatorException {
        switch (question.getQuestionType()) {
            case TEXT:
                if (question.isRequired() && !questionAnswerValidator.validateTextQuestionComponent(value)) {
                    ((InputText) component).resetValue();
                    question.setValid(false);
                    throw new ValidatorException(new FacesMessage(
                            FacesMessage.SEVERITY_ERROR, "Validation Error",
                            questionAnswerValidator.getErrorMessage()));
                } else if (value != null) {
                    question.setValid(true);
                    saveTextValue(value.toString());
                }
                break;
            case PARAGRAPHTEXT:
                if (question.isRequired() && !questionAnswerValidator.validateTextAreaQuestionComponent(value)) {
                    ((InputTextarea) component).resetValue();
                    question.setValid(false);
                    throw new ValidatorException(new FacesMessage(
                            FacesMessage.SEVERITY_ERROR, "Validation Error",
                            questionAnswerValidator.getErrorMessage()));
                } else if (value != null) {
                    question.setValid(true);
                    saveTextValue(value.toString());
                }
                break;
            case MULTIPLECHOICE:
                if (question.isRequired() && !questionAnswerValidator.validateMultipleChoiseQuestionComponent(value)) {
                    question.setValid(false);
                    throw new ValidatorException(new FacesMessage(
                            FacesMessage.SEVERITY_ERROR, "Validation Error",
                            questionAnswerValidator.getErrorMessage()));
                } else if (value != null) {
                    question.setValid(true);
                    saveTextValue(value.toString());
                }
                break;
            case CHECKBOX:
                if (question.isRequired() && !questionAnswerValidator.validateCheckBoxQuestionComponent(value)) {
                    question.setValid(false);
                    throw new ValidatorException(new FacesMessage(
                            FacesMessage.SEVERITY_ERROR, "Validation Error",
                            questionAnswerValidator.getErrorMessage()));
                } else if (value != null) {
                    question.setValid(true);
                    saveListTextValue((Object[]) value);
                }
                break;
            case CHOOSEFROMLIST:
                if (question.isRequired() && !questionAnswerValidator.validateDropDownQuestionComponent(value)) {
                    question.setValid(false);
                    throw new ValidatorException(new FacesMessage(
                            FacesMessage.SEVERITY_ERROR, "Validation Error",
                            questionAnswerValidator.getErrorMessage()));
                } else if (value != null) {
                    question.setValid(true);
                    if (component.getChildren().get(0).getId().equals("defaultItem")) {
                        component.getChildren().remove(0);
                    }
                    saveTextValue(value.toString());
                }
                break;
            default:
                break;
        }
    }

    private void saveTextValue(String value) {
        Value textValue = new TextValue();
        textValue.setValue(value);
        question.setAnswer(textValue);
    }

    private void saveListTextValue(Object[] values) {
        ListTextValue listTextValue = new ListTextValue();
        List answersList = new ArrayList();
        for (int i = 0; i < values.length; i++) {
            answersList.add(values[i]);
        }
        listTextValue.setValue(answersList);
        question.setAnswer(listTextValue);
    }
}
