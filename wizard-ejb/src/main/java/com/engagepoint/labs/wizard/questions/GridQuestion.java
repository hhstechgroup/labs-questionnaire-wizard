package com.engagepoint.labs.wizard.questions;

import com.engagepoint.labs.wizard.values.GridValue;
import com.engagepoint.labs.wizard.values.Value;
import com.engagepoint.labs.wizard.values.ValueType;
import com.engagepoint.labs.wizard.values.objects.Grid;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by igor.guzenko on 2/4/14.
 */
public class GridQuestion extends WizardQuestion {

    private List<String> columns;
    private List<String> rows;
    private boolean oneInCol;
    private boolean oneInRow;
    private GridValue answer;
    private GridValue defaultAnswer;
    private boolean emptyDefaultAnswer;

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

    public static List<String> createGridEmptyAnswers(int rows, int cols) {
	List<String> answers = new ArrayList<>();
	StringBuilder builder = new StringBuilder();
	for (int i = 0; i < rows; i++) {
	    for (int j = 0; j < cols; j++) {
		builder.append("false");
		if (j < cols - 1) {
		    builder.append(",");
		}
	    }
	    answers.add(builder.toString());
	    builder.setLength(0);
	}
	return answers;
    }

    public List<String> getAnswerAsStrings() {
	List<String> answerList = new ArrayList<>(getRows().size());
	StringBuilder linesBuilder = new StringBuilder();
	Map<String, Boolean> answersMap = ((com.engagepoint.labs.wizard.values.objects.Grid) getAnswer()
		.getValue()).getValues();
	Set keySet = answersMap.keySet();
	Iterator keysIterator = keySet.iterator();
	int valuesInLine = 0;
	boolean lastIterator = false;
	while (keysIterator.hasNext() || lastIterator) {
	    if (valuesInLine < getColumns().size()) {
		String key = keysIterator.next().toString();
		linesBuilder.append(answersMap.get(key));
		if (valuesInLine < getColumns().size() - 1) {
		    linesBuilder.append(",");
		}
		lastIterator = true;
		valuesInLine++;
	    } else {
		lastIterator = false;
		answerList.add(linesBuilder.toString());
		valuesInLine = 0;
		linesBuilder.setLength(0);
	    }
	}
	return answerList;
    }

    public List<String> getDefaultAnswerAsStrings() {
	List<String> answerList = new ArrayList<>(getRows().size());
	StringBuilder linesBuilder = new StringBuilder();
	Map<String, Boolean> answersMap = ((com.engagepoint.labs.wizard.values.objects.Grid) getDefaultAnswer()
		.getValue()).getValues();
	Set keySet = answersMap.keySet();
	Iterator keysIterator = keySet.iterator();
	int valuesInLine = 0;
	boolean lastIterator = false;
	while (keysIterator.hasNext() || lastIterator) {
	    if (valuesInLine < getColumns().size()) {
		String key = keysIterator.next().toString();
		linesBuilder.append(answersMap.get(key));
		if (valuesInLine < getColumns().size() - 1) {
		    linesBuilder.append(",");
		}
		lastIterator = true;
		valuesInLine++;
	    } else {
		lastIterator = false;
		answerList.add(linesBuilder.toString());
		valuesInLine = 0;
		linesBuilder.setLength(0);
	    }
	}
	return answerList;
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
	Grid value = null;
	if (isEmptyDefaultAnswer()) {
	    value = new Grid(getId(), createGridEmptyAnswers(getRows().size(),
		    getColumns().size()), getRows().size()
		    * getColumns().size());
	} else {
	    value = new Grid(getId(), getDefaultAnswerAsStrings(), getRows()
		    .size() * getColumns().size());
	}
	answer.setValue(value);
    }

    public boolean isOneInCol() {
	return oneInCol;
    }

    public void setOneInCol(boolean oneInCol) {
	this.oneInCol = oneInCol;
    }

    public boolean isOneInRow() {
	return oneInRow;
    }

    public void setOneInRow(boolean oneInRow) {
	this.oneInRow = oneInRow;
    }

    public boolean isEmptyDefaultAnswer() {
	return emptyDefaultAnswer;
    }

    public void setEmptyDefaultAnswer(boolean emptyDefaultAnswer) {
	this.emptyDefaultAnswer = emptyDefaultAnswer;
    }

}
