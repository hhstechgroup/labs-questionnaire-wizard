package com.engagepoint.labs.wizard.values;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: andrii.sotnyk
 * Date: 2/17/14
 * Time: 2:31 PM
 * To change this template use File | Settings | File Templates.
 */

public class DateValue extends Value {

    public DateValue() {
        type = ValueType.DATE;
    }

    @Override
    public void setValue(Object value) {
        if (value instanceof Date) {
            super.setValue(value);
        } else {
            throw new ClassCastException("You must provide Date for this method!");
        }
    }
}
