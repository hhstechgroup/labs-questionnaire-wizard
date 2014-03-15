package com.engagepoint.labs.wizard.values;

import java.io.File;
import java.io.InputStream;

/**
 * Created with IntelliJ IDEA.
 * User: andrii.sotnyk
 * Date: 2/17/14
 * Time: 2:39 PM
 * To change this template use File | Settings | File Templates.
 */
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
