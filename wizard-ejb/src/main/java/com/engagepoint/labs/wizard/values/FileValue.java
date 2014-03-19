package com.engagepoint.labs.wizard.values;

import java.io.File;

public class FileValue extends Value {

    public FileValue() {
        type = ValueType.FILE;
    }

    @Override
    public void setValue(Object value) {
        if (value instanceof File) {
            super.setValue(value);
        } else {
            throw new ClassCastException("You must provide File for this method!");
        }

    }
}
