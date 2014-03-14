package com.engagepoint.labs.wizard.handler;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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

    private Boolean currentCellValue;
    private String currentGridID;
    private String currentCellId;

    private ArrayList<String> checkBoxIDsToUnset;

    private Map<String, GridQuestion> questions;

    public DataGridHandler() {
	questions = new HashMap<String, GridQuestion>();
    }

    @PostConstruct
    public void init() {
	questions = new HashMap<String, GridQuestion>();
	checkBoxIDsToUnset = new ArrayList<String>();
    }

    public Map<String, GridQuestion> getQuestions() {
	return questions;
    }

    public void setQuestions(Map<String, GridQuestion> questions) {
	this.questions = questions;
    }

    public DataGridHandler setCellFromGridByID(String gridID, String cellID) {
	Grid grid = searchForGrid(gridID);
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
	Grid grid = searchForGrid(currentGridID);
	grid.getValues().put(currentCellId, currentCellValue);

	processRowRule();
	RequestContext.getCurrentInstance().update(currentGridID);
    }

    private Grid searchForGrid(String gridID) {
	Grid grid = null;
	Iterator<String> i = questions.keySet().iterator();
	while (i.hasNext()) {
	    String questionID = i.next();
	    GridQuestion dataGridQuestion = questions.get(questionID);
	    if (dataGridQuestion.getId().equals(gridID)) {
		grid = (Grid) dataGridQuestion.getAnswer().getValue();
		break;
	    }
	}
	return grid;
    }

    private void processRowRule() {
	int currentCellNumber = Grid.getCheckBoxNumberFromID(currentCellId);
	int colsCount = questions.get(currentGridID).getColumns().size();
	int currentCellNumberRow = currentCellNumber / colsCount;

	checkBoxIDsToUnset.clear();
	for (int i = 0; i < colsCount; i++) {
	    String idToUnset = Grid.createCheckBoxID(currentGridID,
		    currentCellNumberRow * colsCount + i);
	    {
		if (!idToUnset.equals(currentCellId)) {
		    checkBoxIDsToUnset.add(idToUnset);
		}
	    }
	}
	unsetValues();
    }

    private void unsetValues() {
	Grid grid = searchForGrid(currentGridID);
	for (String cellID : checkBoxIDsToUnset) {
	    grid.getValues().put(cellID, false);
	}
    }
}
