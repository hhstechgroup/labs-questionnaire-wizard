package com.engagepoint.labs.wizard.answers;

/**
 * Created with IntelliJ IDEA.
 * User: andrii.sotnyk
 * Date: 2/17/14
 * Time: 2:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class GridValue extends Value {
    private String row;
    private String column;

    public String getRow() {
        return row;
    }

    public void setRow(String row) {
        this.row = row;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }
}
