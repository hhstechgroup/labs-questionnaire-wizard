package com.engagepoint.labs.wizard.questions;

import com.engagepoint.labs.wizard.values.GridValue;
import com.engagepoint.labs.wizard.values.Value;
import com.engagepoint.labs.wizard.values.ValueType;

import java.util.List;

/**
 * Created by igor.guzenko on 2/4/14.
 */
public class GridQuestion extends WizardQuestion {

    private List<String> columns;
    private List<String> rows;
    private GridValue answer;
    private GridValue defaultAnswer;

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
    public Value getAnswer() {
        return answer;
    }

    @Override
    public void setAnswer(Value answer) {
        if (answer.getType().equals(ValueType.GRID)) {
            this.answer = (GridValue) answer;
        }
    }

    @Override
    public Value getDefaultAnswer() {
        return defaultAnswer;
    }

    @Override
    public void setDefaultAnswer(Value defaultAnswer) {
        if (defaultAnswer.getType().equals(ValueType.GRID)) {
            this.defaultAnswer = (GridValue) defaultAnswer;
        }

    }

    @Override
    public void resetAnswer() {
        answer = null;
    }

}
