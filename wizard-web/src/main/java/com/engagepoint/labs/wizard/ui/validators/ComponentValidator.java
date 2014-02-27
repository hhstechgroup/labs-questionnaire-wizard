package com.engagepoint.labs.wizard.ui.validators;

import com.engagepoint.labs.wizard.questions.WizardQuestion;
import com.engagepoint.labs.wizard.values.ListTextValue;
import com.engagepoint.labs.wizard.values.TextValue;
import com.engagepoint.labs.wizard.values.Value;
import org.primefaces.component.inputtext.InputText;
import org.primefaces.component.inputtextarea.InputTextarea;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlSelectOneMenu;
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

    public ComponentValidator(final WizardQuestion question) {
        this.question = question;
    }

    @Override
    public void validate(FacesContext context, UIComponent component,
                         Object value) throws ValidatorException {
        switch (question.getQuestionType()) {
            case TEXT:
                validateTextQuestionComponent((InputText) component, value);
                break;
            case PARAGRAPHTEXT:
                validateTextAreaQuestionComponent((InputTextarea) component, value);
                break;
            case MULTIPLECHOICE:
                validateMultipleChoiseQuestionComponent(value);
                break;
            case CHECKBOX:
                validateCheckBoxQuestionComponent(value);
                break;
            case CHOOSEFROMLIST:
                validateDropDownQuestionComponent((HtmlSelectOneMenu) component,
                        value);
                break;
            default:
                break;
        }
    }

    private void validateDropDownQuestionComponent(HtmlSelectOneMenu component,
                                                   Object value) {
        if (question.isRequired()) {
            if (value == null || value.toString().isEmpty()) {
                question.setValid(false);
                throw new ValidatorException(new FacesMessage(
                        FacesMessage.SEVERITY_ERROR, "Validation Error",
                        "Answer must be selected for this question!"));
            } else {
                question.setValid(true);
                if (component.getChildren().get(0).getId().equals("defaultItem")) {
                    component.getChildren().remove(0);
                }
            }
        }
        System.err.print("CHOOSE CASE VALID");
        question.setValid(true);
        Value textValue = new TextValue();
        textValue.setValue(value.toString());
        question.setAnswer(textValue);

    }

    private void validateCheckBoxQuestionComponent(Object value) {
        if (question.isRequired()) {
            if (value == null || ((Object[]) value).length == 0) {
                question.setValid(false);
                throw new ValidatorException(new FacesMessage(
                        FacesMessage.SEVERITY_ERROR, "Validation Error",
                        "Empty field is not allowed here!"));
            }
        }
        question.setValid(true);
        ListTextValue listTextValue = new ListTextValue();
        Object[] arr = (Object[]) value;
        List answersList = new ArrayList();
        for (int i = 0; i < arr.length; i++) {
            answersList.add(arr[i]);
        }
        listTextValue.setValue(answersList);
        question.setAnswer(listTextValue);

    }

    private void validateMultipleChoiseQuestionComponent(Object value) {
        if (question.isRequired()) {
            if (value == null || value.toString().isEmpty()) {
                question.setValid(false);
                throw new ValidatorException(new FacesMessage(
                        FacesMessage.SEVERITY_ERROR, "Validation Error",
                        "Answer must be selected for this question!"));
            }
        }
        question.setValid(true);
        Value textValue = new TextValue();
        textValue.setValue(value);
        question.setAnswer(textValue);
    }

    private void validateTextAreaQuestionComponent(InputTextarea component,
                                                   Object value) {
        if (question.isRequired()) {
            if (value == null) {
                question.setValid(false);
                throw new ValidatorException(new FacesMessage(
                        FacesMessage.SEVERITY_ERROR, "Validation Error",
                        "Empty field is not allowed here!"));
            } else {
                String currentValue = value.toString();
                currentValue = currentValue.replaceAll("\\s", "");
                if (currentValue.isEmpty()) {
                    question.setValid(false);
                    component.resetValue();
                    throw new ValidatorException(new FacesMessage(
                            FacesMessage.SEVERITY_ERROR, "Validation Error",
                            "Empty field is not allowed here!"));
                }
            }
        }
        question.setValid(true);
        Value textValue = new TextValue();
        textValue.setValue(value);
        question.setAnswer(textValue);
    }

    private void validateTextQuestionComponent(InputText component, Object value) {
        if (question.isRequired()) {
            if (value == null) {
                question.setValid(false);
                throw new ValidatorException(new FacesMessage(
                        FacesMessage.SEVERITY_ERROR, "Validation Error",
                        "Empty field is not allowed here!"));
            } else {
                String currentValue = value.toString();
                currentValue = currentValue.replaceAll("\\s", "");
                if (currentValue.isEmpty()) {
                    question.setValid(false);
                    component.resetValue();
                    throw new ValidatorException(new FacesMessage(
                            FacesMessage.SEVERITY_ERROR, "Validation Error",
                            "Empty field is not allowed here!"));
                }
            }
        }
        question.setValid(true);
        Value textValue = new TextValue();
        textValue.setValue(value);
        question.setAnswer(textValue);
    }
}
