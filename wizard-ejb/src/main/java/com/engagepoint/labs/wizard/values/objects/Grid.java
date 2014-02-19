package com.engagepoint.labs.wizard.values.objects;

import java.util.List;

/**
 * Created by igor.guzenko on 2/17/14.
 */
public class Grid {
    private List<String> row;
    private List<String> column;

    public List<String> getRow() {
        return row;
    }

    public void setRow(List<String> row) {
        this.row = row;
    }

    public List<String> getColumn() {
        return column;
    }

    public void setColumn(List<String> column) {
        this.column = column;
    }
}
