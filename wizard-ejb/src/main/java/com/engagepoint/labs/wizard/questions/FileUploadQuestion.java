/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.engagepoint.labs.wizard.questions;

import com.engagepoint.labs.wizard.values.FileValue;
import com.engagepoint.labs.wizard.values.Value;
import com.engagepoint.labs.wizard.values.ValueType;

/**
 * @author artem
 */
public class FileUploadQuestion extends WizardQuestion {
    private FileValue answer;
    private FileValue defaultAnswer;
    private String uploadText;

    public String getUploadText() {
        return uploadText;
    }

    public void setUploadText(String uploadText) {
        this.uploadText = uploadText;
    }

    @Override
    public Value getAnswer() {
        return answer;
    }

    @Override
    public void setAnswer(Value answer) {
        if (answer.getType().equals(ValueType.FILE)) {
            this.answer = (FileValue) answer;
        }

    }

    @Override
    public Value getDefaultAnswer() {
        return defaultAnswer;
    }

    @Override
    public void setDefaultAnswer(Value defaultAnswer) {
        if (defaultAnswer.getType().equals(ValueType.FILE)) {
            this.defaultAnswer = (FileValue) defaultAnswer;
        }

    }
}
