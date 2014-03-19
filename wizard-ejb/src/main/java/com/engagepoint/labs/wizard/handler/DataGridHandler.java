package com.engagepoint.labs.wizard.handler;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;

import com.engagepoint.labs.wizard.questions.GridQuestion;
import com.engagepoint.labs.wizard.values.objects.Grid;

@Named("dataGridHandler")
@SessionScoped
public class DataGridHandler implements Serializable {
    private static final Logger LOGGER = Logger.getLogger(DataGridHandler.class
	    .getName());
    private static final long serialVersionUID = -1066387551691144085L;

    private Boolean currentCellValue;
    private String currentGridID;
    private String currentCellId;

    private String lastGridID;
    private GridQuestion lastGridQuestion;

    private ArrayList<String> checkBoxIDsToUnset;

    private Map<String, GridQuestion> questions;

    public DataGridHandler() {
	questions = new HashMap<String, GridQuestion>();
    }

    @PostConstruct
    public void init() {
	questions = new HashMap<String, GridQuestion>();
	checkBoxIDsToUnset = new ArrayList<String>();
	lastGridID = "";
    }

    public Map<String, GridQuestion> getQuestions() {
	return questions;
    }

    public void setQuestions(Map<String, GridQuestion> questions) {
	this.questions = questions;
    }

    public DataGridHandler setCellFromGridByID(String gridID, String cellID) {
	GridQuestion question = null;
	if (lastGridID.equals(gridID)) {
	    question = lastGridQuestion;
	} else {
	    question = searchForQuestion(gridID);
	    lastGridID = gridID;
	    lastGridQuestion = question;
	}
	Grid grid = (Grid) question.getAnswer().getValue();
	currentCellValue = grid.getValues().get(cellID);
	currentGridID = gridID;
	currentCellId = cellID;
	return this;
    }

    public Boolean getCurrentCellValue() {
	return currentCellValue;
    }

    public void setCurrentCellValue(Boolean currentCellValue) {
	this.currentCellValue = currentCellValue;
	GridQuestion question = searchForQuestion(currentGridID);
	Grid grid = (Grid) question.getAnswer().getValue();
	grid.getValues().put(currentCellId, currentCellValue);

	if (currentCellValue == true) {
	    if (question.isOneInRow()) {
		processRowRule();
	    }
	    if (question.isOneInCol()) {
		processColumnRule();
	    }
	    if (question.isOneInRow() && question.isOneInCol()) {
		processRowRule();
		processColumnRule();
	    }
	}
	RequestContext.getCurrentInstance().update(currentGridID);
    }

    private GridQuestion searchForQuestion(String gridID) {
	GridQuestion question = null;
	Iterator<String> i = questions.keySet().iterator();
	while (i.hasNext()) {
	    String questionID = i.next();
	    GridQuestion dataGridQuestion = questions.get(questionID);
	    if (dataGridQuestion.getId().equals(gridID)) {
		question = dataGridQuestion;
		break;
	    }
	}
	return question;
    }

    private int[] getCellPosition(int cellNumber, int rowsNumber,
	    int columnNumber) {
	int[] position = new int[2];
	int iter = 0;
	boolean positionFound = false;
	for (int i = 0; i < rowsNumber; i++) {
	    for (int j = 0; j < columnNumber; j++) {
		if (iter == cellNumber) {
		    position[0] = i;
		    position[1] = j;
		    positionFound = true;
		    break;
		}
		iter++;
	    }
	    if (positionFound) {
		break;
	    }
	}
	return position;
    }

    private void processRowRule() {
	int currentCellNumber = Grid.getCheckBoxNumberFromID(currentCellId);
	int rowsCount = questions.get(currentGridID).getRows().size();
	int colsCount = questions.get(currentGridID).getColumns().size();
	int[] cellPosition = getCellPosition(currentCellNumber, rowsCount,
		colsCount);

	checkBoxIDsToUnset.clear();
	for (int i = 0; i < colsCount; i++) {
	    String idToUnset = Grid.createCheckBoxID(currentGridID,
		    cellPosition[0] * colsCount + i);
	    {
		if (!idToUnset.equals(currentCellId)) {
		    checkBoxIDsToUnset.add(idToUnset);
		}
	    }
	}
	unsetValues();
    }

    private void processColumnRule() {
	int currentCellNumber = Grid.getCheckBoxNumberFromID(currentCellId);
	int rowsCount = questions.get(currentGridID).getRows().size();
	int colsCount = questions.get(currentGridID).getColumns().size();
	int[] cellPosition = getCellPosition(currentCellNumber, rowsCount,
		colsCount);

	checkBoxIDsToUnset.clear();
	for (int i = cellPosition[1]; i < rowsCount * colsCount; i = i
		+ colsCount) {
	    String idToUnset = Grid.createCheckBoxID(currentGridID, i);
	    {
		if (!idToUnset.equals(currentCellId)) {
		    checkBoxIDsToUnset.add(idToUnset);
		}
	    }
	}
	unsetValues();
    }

    private void unsetValues() {
	GridQuestion question = searchForQuestion(currentGridID);
	Grid grid = (Grid) question.getAnswer().getValue();
	for (String cellID : checkBoxIDsToUnset) {
	    grid.getValues().put(cellID, false);
	}
    }

    public String getLastGridID() {
	return lastGridID;
    }

    public void setLastGridID(String lastGridID) {
	this.lastGridID = lastGridID;
    }

    public GridQuestion getLastGridQuestion() {
	return lastGridQuestion;
    }

    public void setLastGridQuestion(GridQuestion lastGridQuestion) {
	this.lastGridQuestion = lastGridQuestion;
    }
}
