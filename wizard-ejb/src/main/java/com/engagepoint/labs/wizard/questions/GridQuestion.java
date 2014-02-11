package com.engagepoint.labs.wizard.questions;

import com.engagepoint.labs.wizard.answers.GridAnswer;

import java.util.List;

/**
 * Created by igor.guzenko on 2/4/14.
 */
public class GridQuestion extends WizardQuestion {

    private List<String> columns;
    private List<String> rows;
    private GridAnswer gridAnswer;

    public List<String> getColumns() {
        return columns;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }

    public List<String> getRows() {
        return rows;
    }

    public void setRows(List<String> rows) {
        this.rows = rows;
    }

    @Override
    public GridAnswer getAnswer() {
        return gridAnswer;
    }

    @Override
    public void setAnswer(Object answers) {
        this.gridAnswer = (GridAnswer) answers;
    }
}
