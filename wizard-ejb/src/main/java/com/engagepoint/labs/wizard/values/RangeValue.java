package com.engagepoint.labs.wizard.values;

import com.engagepoint.labs.wizard.values.oblects.Range;

/**
 * Created with IntelliJ IDEA.
 * User: andrii.sotnyk
 * Date: 2/17/14
 * Time: 2:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class RangeValue extends Value {


    public RangeValue() {
        type = ValueType.RANGE;
    }

    @Override
    public void setValue(Object value) {
        if (value instanceof Range) {
            super.setValue(value);
        } else {
            throw new ClassCastException("You must provide Range for this method!");
        }

    }
}
