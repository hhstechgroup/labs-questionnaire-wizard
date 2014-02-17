package com.engagepoint.labs.wizard.values;

/**
 * Created with IntelliJ IDEA.
 * User: andrii.sotnyk
 * Date: 2/17/14
 * Time: 2:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class TextValue extends Value {

    public TextValue() {
        type = ValueType.STRING;
    }

    @Override
    public void setValue(Object value) {
        if (value instanceof String) {
            super.setValue(value);
        } else {
            throw new ClassCastException("You must provide String for this method!");
        }

    }
}
