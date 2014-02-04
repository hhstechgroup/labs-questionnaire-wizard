package com.engagepoint.labs.wizard.answers;

import java.io.File;

/**
 * Created by igor.guzenko on 2/4/14.
 */
public class FileAnswer implements Answer<File> {
    private File fileAnswer;
    @Override
    public void setAnswer(File file) {
        fileAnswer = file;
    }

    @Override
    public File getAnswer() {
        return fileAnswer;
    }
}
