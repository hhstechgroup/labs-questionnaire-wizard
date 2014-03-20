package com.engagepoint.labs.wizard.handler;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import org.primefaces.context.RequestContext;

import com.engagepoint.labs.wizard.questions.GridQuestion;
import com.engagepoint.labs.wizard.values.objects.Grid;

@Named("dataGridHandler")
@SessionScoped
public class DataGridHandler implements Serializable {
    private static final long serialVersionUID = -1066387551691144085L;

    private String currentGridID;
    private Boolean currentCellValue;
    private String currentCellId;

    private List<String> checkBoxIDsToUnset;
    private Map<String, GridQuestion> questions;

    @PostConstruct
    public void init() {
	questions = new HashMap<String, GridQuestion>();
	checkBoxIDsToUnset = new ArrayList<String>();
    }

    public void initialValidation() {
	Iterator<String> idIterator = questions.keySet().iterator();
	while (idIterator.hasNext()) {
	    String currentID = idIterator.next();
	    Map<String, Boolean> currentGrid = ((Grid) questions.get(currentID)
		    .getAnswer().getValue()).getValues();
	    Iterator<String> cellIterator = currentGrid.keySet().iterator();
	    while (cellIterator.hasNext()) {
		String cellID = cellIterator.next();
		if (currentGrid.get(cellID)) {
		    currentGridID = currentID;
		    currentCellId = cellID;
		    setCurrentCellValue(true);
		    continue;
		}
	    }
	}
    }

    public Map<String, GridQuestion> getQuestions() {
	return questions;
    }

    public void setQuestions(Map<String, GridQuestion> questions) {
	this.questions = questions;
    }

    public DataGridHandler setCellFromGridByID(String gridID, String cellID) {
	GridQuestion question = questions.get(gridID);
	currentCellValue = ((Grid) question.getAnswer().getValue()).getValues()
		.get(cellID);
	currentGridID = gridID;
	currentCellId = cellID;
	return this;
    }

    public Boolean getCurrentCellValue() {
	return currentCellValue;
    }

    public void setCurrentCellValue(Boolean currentCellValue) {
	this.currentCellValue = currentCellValue;
	GridQuestion question = questions.get(currentGridID);
	Grid grid = (Grid) question.getAnswer().getValue();
	grid.getValues().put(currentCellId, currentCellValue);

	if (currentCellValue) {
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

	    if (!idToUnset.equals(currentCellId)) {
		checkBoxIDsToUnset.add(idToUnset);
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

	    if (!idToUnset.equals(currentCellId)) {
		checkBoxIDsToUnset.add(idToUnset);
	    }

	}
	unsetValues();
    }

    private void unsetValues() {
	GridQuestion question = questions.get(currentGridID);
	Grid grid = (Grid) question.getAnswer().getValue();
	for (String cellID : checkBoxIDsToUnset) {
	    grid.getValues().put(cellID, false);
	}
    }
}
