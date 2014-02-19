package com.engagepoint.labs.wizard.values;

/**
 * Created with IntelliJ IDEA.
 * User: andrii.sotnyk
 * Date: 2/17/14
 * Time: 2:50 PM
 * To change this template use File | Settings | File Templates.
 */

public abstract class Value {
    protected ValueType type;
    protected Object value;

    public ValueType getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
