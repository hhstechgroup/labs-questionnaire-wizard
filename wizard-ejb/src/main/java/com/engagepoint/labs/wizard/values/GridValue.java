package com.engagepoint.labs.wizard.values;

import java.util.HashMap;

import com.engagepoint.labs.wizard.values.objects.Grid;

/**
 * Created with IntelliJ IDEA.
 * User: andrii.sotnyk
 * Date: 2/17/14
 * Time: 2:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class GridValue extends Value {

    public GridValue() {
        type = ValueType.GRID;
    }

    @Override
    public void setValue(Object value) {
        if (value instanceof Grid) {
            super.setValue(value);
        } else {
            throw new ClassCastException("You must provide Grid for this method!");
        }
    }
}
