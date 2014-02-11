/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.engagepoint.labs.wizard.questions;

import com.engagepoint.labs.wizard.answers.FileAnswer;

/**
 * @author artem
 */
public class FileUploadQuestion extends WizardQuestion {

    private FileAnswer fileAnswer;

    @Override
    public FileAnswer getAnswer() {
        return fileAnswer;
    }

    @Override
    public void setAnswer(Object answers) {
        this.fileAnswer = (FileAnswer) answers;
    }
}
