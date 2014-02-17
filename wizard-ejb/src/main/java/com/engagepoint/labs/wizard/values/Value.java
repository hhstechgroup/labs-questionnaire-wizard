package com.engagepoint.labs.wizard.values;

/**
 * Created by igor.guzenko on 2/17/14.
 */
public abstract class Value {
    protected ValType type;

    public abstract String asString();

    public ValType getType() {
        return type;
    }
}
