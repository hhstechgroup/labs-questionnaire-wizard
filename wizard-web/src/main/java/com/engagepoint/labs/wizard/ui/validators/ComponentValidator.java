package com.engagepoint.labs.wizard.ui.validators;

import com.engagepoint.labs.wizard.controller.UINavigationBean;
import com.engagepoint.labs.wizard.questions.WizardQuestion;
import com.engagepoint.labs.wizard.values.*;
import com.engagepoint.labs.wizard.values.objects.Range;
import org.apache.log4j.Logger;
import org.primefaces.component.inputtext.InputText;
import org.primefaces.component.inputtextarea.InputTextarea;
import org.primefaces.component.outputlabel.OutputLabel;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.Part;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by igor.guzenko on 2/26/14.
 */
public class ComponentValidator implements Validator {
    private static final boolean VALID = true;
    private static final Logger LOGGER = Logger.getLogger(ComponentValidator.class);
    private static final String DEFAULT_ITEM_ID = "defaultItem";
    private static int cursor = 0;
    static int[] a = new int[2];
    private final WizardQuestion question;
    private boolean isParent;
    private int pageNumber;
    private int topicNumber;
    private UINavigationBean navigationBean;

    public ComponentValidator(WizardQuestion question, UINavigationBean navigationBean) {
        this.navigationBean = navigationBean;
        this.question = question;
    }


    public ComponentValidator(WizardQuestion question, int pageNumber, int topicNumber, boolean isParent, UINavigationBean navBean) {
        this.question = question;
        this.isParent = isParent;
        this.pageNumber = pageNumber;
        this.topicNumber = topicNumber;
        this.navigationBean = navBean;
    }

    @Override
    public void validate(FacesContext context, UIComponent component,
                         Object value) throws ValidatorException {
        switch (question.getQuestionType()) {
            case TEXT:
                validateTextAnswer((InputText) component, value);
                break;
            case PARAGRAPHTEXT:
                validateParagraphTextAnswer((InputTextarea) component, value);
                break;
            case MULTIPLECHOICE:
                validateMultipleChoiseAnswer(value);
                break;
            case CHECKBOX:
                validateCheckBoxAnswer(value);
                break;
            case CHOOSEFROMLIST:
                validateChooseFromListAnswer(component, value);
                break;
            case DATE:
                validateDateAnswer(value);
                break;
            case TIME:
                validateTimeAnswer(value);
                break;
            case RANGE:
                validateRangeAnswer(component, value);
                break;
            case FILEUPLOAD:
                validateFileUploadAnswer(value);
                break;
            default:
                break;
        }
        navigationBean.setCurrentQuestionType(question.getQuestionType());
        updateDependentLimits();

    }

    private void updateDependentLimits() {
        if (isParent) {
            navigationBean.moveLimitIfNecessary(pageNumber, topicNumber);
        }
    }

    private void validateTimeAnswer(Object value) {
        if (question.isRequired() && !validateTimeQuestionComponent(value)) {
            question.setValid(false);
            throw new ValidatorException(new FacesMessage(
                    FacesMessage.SEVERITY_ERROR, "Validation Error",
                    "Empty field is not allowed here!"));
        }
        question.setValid(true);
        saveDateTimeValue((Date) value);
    }

    private void validateDateAnswer(Object value) {
        if (question.isRequired() && !validateDateQuestionComponent(value)) {
            question.setValid(false);
            throw new ValidatorException(new FacesMessage(
                    FacesMessage.SEVERITY_ERROR, "Validation Error",
                    "Empty field is not allowed here!"));
        }
        question.setValid(true);
        saveDateTimeValue((Date) value);
    }

    private void validateChooseFromListAnswer(UIComponent component, Object value) {
        if (question.isRequired() && !validateDropDownQuestionComponent(value)) {
            question.setValid(false);
            throw new ValidatorException(new FacesMessage(
                    FacesMessage.SEVERITY_ERROR, "Validation Error",
                    "Answer must be selected for this question!"));
        }
        question.setValid(true);
        if (component.getChildren().get(0).getId().equals(DEFAULT_ITEM_ID)) {
            component.getChildren().remove(0);
        }
        saveTextValue(value.toString());
    }

    private void validateCheckBoxAnswer(Object value) {
        if (question.isRequired() && !validateCheckBoxQuestionComponent(value)) {
            question.setValid(false);
            throw new ValidatorException(new FacesMessage(
                    FacesMessage.SEVERITY_ERROR, "Validation Error",
                    "Empty field is not allowed here"));
        }
        question.setValid(true);
        saveListTextValue((Object[]) value);
    }

    private void validateRangeAnswer(UIComponent component, Object value) {
        if (question.isRequired() && !validateRangeQuestionComponent(value)) {
            ((javax.faces.component.html.HtmlInputHidden) component).resetValue();
            question.setValid(false);
            throw new ValidatorException(new FacesMessage(
                    FacesMessage.SEVERITY_ERROR, "Validation Error",
                    "Empty field is not allowed here!"));
        }
        question.setValid(true);
        saveRangeValue(value);

    }

    private void validateFileUploadAnswer(Object value) {
        if (question.isRequired() && !validateFileUploadComponent(value)) {
            question.setValid(false);
            throw new ValidatorException(new FacesMessage(
                    FacesMessage.SEVERITY_ERROR, "Error, need to choose file",
                    "Error, need to choose file"));
        }
        question.setValid(VALID);
        saveFileUpload((Part) value);
        OutputLabel outputLabel = (OutputLabel) FacesContext.getCurrentInstance().getViewRoot().findComponent("maincontentid-little_" + question.getId());
        outputLabel.setValue("Your file  uploaded");
    }

    private void validateMultipleChoiseAnswer(Object value) {
        if (question.isRequired() && !validateMultipleChoiseQuestionComponent(value)) {
            question.setValid(false);
            throw new ValidatorException(new FacesMessage(
                    FacesMessage.SEVERITY_ERROR, "Validation Error",
                    "Answer must be selected for this question!"));
        }
        question.setValid(true);
        saveTextValue(value.toString());
    }

    private void validateParagraphTextAnswer(InputTextarea component, Object value) {
        if (question.isRequired() && !validateTextAreaQuestionComponent(value)) {
            component.resetValue();
            question.setValid(false);
            throw new ValidatorException(new FacesMessage(
                    FacesMessage.SEVERITY_ERROR, "Validation Error",
                    "Empty field is not allowed here!"));
        }
        question.setValid(true);
        saveTextValue(value.toString());
    }

    private void validateTextAnswer(InputText component, Object value) {
        if (question.isRequired() && !validateTextQuestionComponent(value)) {
            component.resetValue();
            question.setValid(false);
            throw new ValidatorException(new FacesMessage(
                    FacesMessage.SEVERITY_ERROR, "Validation Error",
                    "Empty field is not allowed here!"));
        }
        question.setValid(true);
        saveTextValue(value.toString());
    }

    public boolean validateDropDownQuestionComponent(Object value) {
        return !(value == null || value.toString().isEmpty());
    }


    public boolean validateCheckBoxQuestionComponent(Object value) {
        return !(value == null || ((Object[]) value).length == 0);
    }

    public boolean validateMultipleChoiseQuestionComponent(Object value) {
        return !(value == null || value.toString().isEmpty());
    }

    public boolean validateTextAreaQuestionComponent(Object value) {
        if (value == null) {
            return false;
        } else {
            String currentValue = value.toString();
            currentValue = currentValue.replaceAll("\\s", "");
            if (currentValue.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public boolean validateTextQuestionComponent(Object value) {
        if (value == null) {
            return false;
        } else {
            String currentValue = value.toString();
            currentValue = currentValue.replaceAll("\\s", "");
            if (currentValue.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public boolean validateRangeQuestionComponent(Object value) {
        return !(value == null);
    }

    public boolean validateDateQuestionComponent(Object value) {
        return value != null;
    }

    public boolean validateTimeQuestionComponent(Object value) {
        return value != null;
    }


    public boolean validateFileUploadComponent(Object value) {
        if (value != null) {
            long size = ((Part) value).getSize();
            if (size == 0) {
                return false;
            }
        }

        return !(value == null && question.getAnswer() == null);
    }

    private void saveTextValue(String value) {
        Value textValue = new TextValue();
        textValue.setValue(value);
        question.setAnswer(textValue);
    }

    private void saveListTextValue(Object[] values) {
        ListTextValue listTextValue = new ListTextValue();
        List answersList = new ArrayList(Arrays.asList(values));
        listTextValue.setValue(answersList);
        question.setAnswer(listTextValue);
    }

    private void saveRangeValue(Object value) {
        if (cursor < 2) {
            a[cursor++] = Integer.parseInt((String) value);
        }
        if (cursor == 2) {
            RangeValue valueRange = new RangeValue();
            Range range = new Range();
            range.setStart(a[0]);
            range.setEnd(a[1]);
            valueRange.setValue(range);
            question.setAnswer(valueRange);
            cursor = 0;
        }
    }

    private void saveDateTimeValue(Date date) {
        DateValue dateValue = new DateValue();
        dateValue.setValue(date);
        question.setAnswer(dateValue);
    }

    private void saveFileUpload(Part file) {
        File fileCopied = null;
        try {
            fileCopied = copyFile(file);
        } catch (IOException e) {
            LOGGER.warn("Can't copy file", e);
        }
        FileValue fileValue = new FileValue();
        fileValue.setValue(fileCopied);
        question.setAnswer(fileValue);
    }

    public File copyFile(Part file) throws IOException {
        String uploadName = file.getSubmittedFileName();
        String sourcePath;
        try {
            sourcePath = getClass().getClassLoader().getResource(".").getPath();
        } catch (Exception npe){
            sourcePath = "\\";
            LOGGER.warn("NullPointerException was caught,sourcePath will set to '\\'  ", npe);
        }

        String path = sourcePath + uploadName;
        File uploadFile = null;
        InputStream inStream = null;
        OutputStream outStream = null;
        try {
            inStream = file.getInputStream();
            outStream = new FileOutputStream(new File(path));
            byte[] buffer = new byte[4096];
            int length;
            while (true) {
                length = inStream.read(buffer);
                if (length > 0) {
                    outStream.write(buffer, 0, length);
                } else {
                    break;
                }
            }
            uploadFile = new File(path);

        } catch (IOException e) {
            LOGGER.warn("IO Exception when you try copy file", e);
        } finally {
            if (inStream != null) {
                inStream.close();
            }
            if (outStream != null) {
                outStream.close();
            }
        }
        return uploadFile;
    }
}
