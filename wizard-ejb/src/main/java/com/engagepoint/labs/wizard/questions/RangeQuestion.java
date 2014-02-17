package com.engagepoint.labs.wizard.questions;

/**
 * Created by igor.guzenko on 2/4/14.
 */
public class RangeQuestion extends WizardQuestion {
    private int startRange;
    private int endRange;
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setRange(Integer start, Integer end) {
        startRange = start;
        endRange = end;
    }

    public int getStartRange() {
        return startRange;
    }

    public int getEndRange() {
        return endRange;
    }
}
