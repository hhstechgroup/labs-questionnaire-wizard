package com.engagepoint.labs.wizard.ui.validators;

import com.engagepoint.labs.wizard.questions.FileUploadQuestion;
import com.engagepoint.labs.wizard.questions.WizardQuestion;
import com.engagepoint.labs.wizard.values.*;
import org.primefaces.component.calendar.Calendar;
import org.primefaces.component.inputtext.InputText;
import org.primefaces.component.inputtextarea.InputTextarea;
import org.primefaces.component.outputlabel.OutputLabel;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by igor.guzenko on 2/26/14.
 */
public class ComponentValidator implements Validator {
    private static final boolean VALID = true;
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
                }
                question.setValid(true);
                saveTextValue(value.toString());
                testingDependet(question);
                break;
            case PARAGRAPHTEXT:
                if (question.isRequired() && !questionAnswerValidator.validateTextAreaQuestionComponent(value)) {
                    ((InputTextarea) component).resetValue();
                    question.setValid(false);
                    throw new ValidatorException(new FacesMessage(
                            FacesMessage.SEVERITY_ERROR, "Validation Error",
                            questionAnswerValidator.getErrorMessage()));
                }
                question.setValid(true);
                saveTextValue(value.toString());
                break;
            case MULTIPLECHOICE:
                if (question.isRequired() && !questionAnswerValidator.validateMultipleChoiseQuestionComponent(value)) {
                    question.setValid(false);
                    throw new ValidatorException(new FacesMessage(
                            FacesMessage.SEVERITY_ERROR, "Validation Error",
                            questionAnswerValidator.getErrorMessage()));
                }
                question.setValid(true);
                saveTextValue(value.toString());
                break;
            case CHECKBOX:
                if (question.isRequired() && !questionAnswerValidator.validateCheckBoxQuestionComponent(value)) {
                    question.setValid(false);
                    throw new ValidatorException(new FacesMessage(
                            FacesMessage.SEVERITY_ERROR, "Validation Error",
                            questionAnswerValidator.getErrorMessage()));
                }
                question.setValid(true);
                saveListTextValue((Object[]) value);
                break;
            case CHOOSEFROMLIST:
                if (question.isRequired() && !questionAnswerValidator.validateDropDownQuestionComponent(value)) {
                    question.setValid(false);
                    throw new ValidatorException(new FacesMessage(
                            FacesMessage.SEVERITY_ERROR, "Validation Error",
                            questionAnswerValidator.getErrorMessage()));
                }
                question.setValid(true);
                if (component.getChildren().get(0).getId().equals("defaultItem")) {
                    component.getChildren().remove(0);
                }
                saveTextValue(value.toString());
                break;
            case DATE:
                if (question.isRequired() && !questionAnswerValidator.validateDateQuestionComponent(value)) {
                    question.setValid(false);
                    throw new ValidatorException(new FacesMessage(
                            FacesMessage.SEVERITY_ERROR, "Validation Error",
                            questionAnswerValidator.getErrorMessage()));
                }
                question.setValid(true);
                saveDateTimeValue((Date) value);
                break;
            case TIME:
                if (question.isRequired() && !questionAnswerValidator.validateTimeQuestion(value)) {
                    question.setValid(false);
                    throw new ValidatorException(new FacesMessage(
                            FacesMessage.SEVERITY_ERROR, "Validation Error",
                            questionAnswerValidator.getErrorMessage()));
                }
                question.setValid(true);
                saveDateTimeValue((Date) value);
                break;
            case FILEUPLOAD:
                if (question.isRequired() && !questionAnswerValidator.validateFileUpload(value)) {
                    question.setValid(!VALID);
                    throw new ValidatorException(new FacesMessage(
                            FacesMessage.SEVERITY_ERROR, "Error, need to choose file",
                            questionAnswerValidator.getErrorMessage()));
                }
                question.setValid(VALID);
//                OutputLabel outputLabel = (OutputLabel) FacesContext.getCurrentInstance().getViewRoot().findComponent("maincontentid-little_" + question.getId());
//                outputLabel.setValue("Your file  uploaded");
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

    private void saveDateTimeValue(Date date) {
        DateValue dateValue = new DateValue();
        dateValue.setValue(date);
        question.setAnswer(dateValue);
    }

    private void saveFileUpload(Object o) {
        Value fileValue = new FileValue();
        fileValue.setValue((InputStream) o);
        question.setAnswer(fileValue);
    }

    private void testingDependet(WizardQuestion question){
//         question.getAnswer().getValue().toString().equals("aaaa");
        Calendar component = (Calendar) FacesContext.getCurrentInstance().getViewRoot().findComponent("maincontentid-hjkhwewewjvv");
        component.setDisabled(false);
    }
}
