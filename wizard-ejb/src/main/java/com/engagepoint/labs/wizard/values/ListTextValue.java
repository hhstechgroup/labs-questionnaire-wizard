package com.engagepoint.labs.wizard.values;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: andrii.sotnyk
 * Date: 2/17/14
 * Time: 2:41 PM
 * To change this template use File | Settings | File Templates.
 */

public class ListTextValue extends Value {

    public ListTextValue() {
        type = ValueType.LIST;
    }

    @Override
    public void setValue(Object value) {
        if (value instanceof List) {
            super.setValue(value);
        } else {
            throw new ClassCastException("You must provide List for this method!");
        }

    }
}
