package com.engagepoint.labs.wizard.answers;

/**
 * Created with IntelliJ IDEA.
 * User: andrii.sotnyk
 * Date: 2/17/14
 * Time: 2:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class RangeValue extends Value {
    private int startValue;
    private int endValue;

    public int getStartValue() {
        return startValue;
    }

    public void setStartValue(int startValue) {
        this.startValue = startValue;
    }

    public int getEndValue() {
        return endValue;
    }

    public void setEndValue(int endValue) {
        this.endValue = endValue;
    }
}
