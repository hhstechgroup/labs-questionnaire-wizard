package com.engagepoint.labs.wizard.ui.validators;

import com.engagepoint.labs.wizard.questions.WizardQuestion;
import com.engagepoint.labs.wizard.ui.WizardLimits;
import com.engagepoint.labs.wizard.values.*;
import org.primefaces.component.inputtext.InputText;
import org.primefaces.component.inputtextarea.InputTextarea;
import org.primefaces.component.outputlabel.OutputLabel;
import org.primefaces.context.RequestContext;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.Part;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by igor.guzenko on 2/26/14.
 */
public class ComponentValidator implements Validator {
    private static final boolean VALID = true;
    private final WizardQuestion question;
    private boolean isParent;
    private int pageNumber;
    private int topicNumber;
    private Set<WizardQuestion> questionSet;

    public ComponentValidator(WizardQuestion question, int pageNumber, int topicNumber, boolean isParent) {
        this.question = question;
        this.isParent = isParent;
        this.pageNumber = pageNumber;
        this.topicNumber = topicNumber;
    }

    @Override
    public void validate(FacesContext context, UIComponent component,
                         Object value) throws ValidatorException {
        switch (question.getQuestionType()) {
            case TEXT:
                if (question.isRequired() && !validateTextQuestionComponent(value)) {
                    ((InputText) component).resetValue();
                    question.setValid(false);
                    throw new ValidatorException(new FacesMessage(
                            FacesMessage.SEVERITY_ERROR, "Validation Error",
                            "Empty field is not allowed here!"));
                }
                question.setValid(true);
                saveTextValue(value.toString());
                break;
            case PARAGRAPHTEXT:
                if (question.isRequired() && !validateTextAreaQuestionComponent(value)) {
                    ((InputTextarea) component).resetValue();
                    question.setValid(false);
                    throw new ValidatorException(new FacesMessage(
                            FacesMessage.SEVERITY_ERROR, "Validation Error",
                            "Empty field is not allowed here!"));
                }
                question.setValid(true);
                saveTextValue(value.toString());
                break;
            case MULTIPLECHOICE:
                if (question.isRequired() && !validateMultipleChoiseQuestionComponent(value)) {
                    question.setValid(false);
                    throw new ValidatorException(new FacesMessage(
                            FacesMessage.SEVERITY_ERROR, "Validation Error",
                            "Answer must be selected for this question!"));
                }
                question.setValid(true);
                saveTextValue(value.toString());
                break;
            case CHECKBOX:
                if (question.isRequired() && !validateCheckBoxQuestionComponent(value)) {
                    question.setValid(false);
                    throw new ValidatorException(new FacesMessage(
                            FacesMessage.SEVERITY_ERROR, "Validation Error",
                            "Empty field is not allowed here"));
                }
                question.setValid(true);
                saveListTextValue((Object[]) value);
                break;
            case CHOOSEFROMLIST:
                if (question.isRequired() && !validateDropDownQuestionComponent(value)) {
                    question.setValid(false);
                    throw new ValidatorException(new FacesMessage(
                            FacesMessage.SEVERITY_ERROR, "Validation Error",
                            "Answer must be selected for this question!"));
                }
                question.setValid(true);
                if (component.getChildren().get(0).getId().equals("defaultItem")) {
                    component.getChildren().remove(0);
                }
                saveTextValue(value.toString());
                break;
            case DATE:
                if (question.isRequired() && !validateDateQuestionComponent(value)) {
                    question.setValid(false);
                    throw new ValidatorException(new FacesMessage(
                            FacesMessage.SEVERITY_ERROR, "Validation Error",
                            "Empty field is not allowed here!"));
                }
                question.setValid(true);
                saveDateTimeValue((Date) value);
                break;
            case TIME:
                if (question.isRequired() && !validateTimeQuestionComponent(value)) {
                    question.setValid(false);
                    throw new ValidatorException(new FacesMessage(
                            FacesMessage.SEVERITY_ERROR, "Validation Error",
                            "Empty field is not allowed here!"));
                }
                question.setValid(true);
                saveDateTimeValue((Date) value);
                break;
            case FILEUPLOAD:
                if (question.isRequired() && !validateFileUploadComponent(value)) {
                    question.setValid(!VALID);
                    throw new ValidatorException(new FacesMessage(
                            FacesMessage.SEVERITY_ERROR, "Error, need to choose file",
                            "Error, need to choose file"));
                }
                question.setValid(VALID);
                saveFileUpload((Part) value);
                OutputLabel outputLabel = (OutputLabel) FacesContext.getCurrentInstance().getViewRoot().findComponent("maincontentid-little_" + question.getId());
                outputLabel.setValue("Your file  uploaded");
                break;
            default:
                break;
        }
        moveWallIfNecessary();
    }

    public boolean validateDropDownQuestionComponent(Object value) {
        if (value == null || value.toString().isEmpty()) {
            return false;
        }
        return true;
    }


    public boolean validateCheckBoxQuestionComponent(Object value) {
        if (value == null || ((Object[]) value).length == 0) {
            return false;
        }
        return true;
    }

    public boolean validateMultipleChoiseQuestionComponent(Object value) {
        if (value == null || value.toString().isEmpty()) {
            return false;
        }
        return true;
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

    public boolean validateDateQuestionComponent(Object value) {
        if (value == null) {
            return false;
        }
        return true;
    }

    public boolean validateTimeQuestionComponent(Object value) {
        if (value == null) {
            return false;
        }
        return true;
    }


    public boolean validateFileUploadComponent(Object value) {
        if (value != null) {
            long size = ((Part) value).getSize();
            if (size == 0) return false;
        }
        if (value == null) {
            if (question.getAnswer() == null) return false;
        }
        return true;
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

    private void saveFileUpload(Part file) {
        File fileCopied = null;
        try {
            fileCopied = copyFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileValue fileValue = new FileValue();
        fileValue.setValue(fileCopied);
        question.setAnswer(fileValue);
    }

    public File copyFile(Part file) throws IOException {
        String uploadName = file.getSubmittedFileName();
        String path = new String("D:\\" + uploadName);
        File uploadFile = null;
        InputStream inStream = null;
        OutputStream outStream = null;
        try {
            inStream = file.getInputStream();
            outStream = new FileOutputStream(new File(path));
            byte[] buffer = new byte[4096];
            int length;
            while ((length = inStream.read(buffer)) > 0) {
                outStream.write(buffer, 0, length);
            }
            System.err.println("File is copied successful!");
            uploadFile = new File(path);

        } catch (IOException e) {
            e.printStackTrace();
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

    private void moveWallIfNecessary() {
        if (isParent) {
            WizardLimits.pageLimit = pageNumber;
            WizardLimits.topicLimit = topicNumber;
            if (question.getAnswer() != null) {
                RequestContext.getCurrentInstance().execute("dialogDependentQuestion.show()");
            }
        }
    }
}
