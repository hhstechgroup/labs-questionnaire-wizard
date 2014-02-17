package com.engagepoint.labs.wizard.values;

/**
 * Created by igor.guzenko on 2/17/14.
 */
public class StringValue extends Value {
    private String value;

    public StringValue() {
        type = ValType.STRING;
    }

    @Override
    public String asString() {
        return value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
