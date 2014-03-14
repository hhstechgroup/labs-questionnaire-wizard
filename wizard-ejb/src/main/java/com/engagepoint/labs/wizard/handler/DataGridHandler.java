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

    private Map<String,GridQuestion> questions;

    public DataGridHandler() {
	questions = new HashMap<String,GridQuestion>();
    }

    @PostConstruct
    public void init() {
	questions = new HashMap<String,GridQuestion>();
    }

    public Map<String,GridQuestion> getQuestions() {
	return questions;
    }

    public void setQuestions(Map<String,GridQuestion> questions) {
	this.questions = questions;
    }

    public DataGridHandler setCellFromGridByID(String gridID, String cellID) {
	Map<String, Boolean> dataGridValues = null;
	Iterator<String> i=questions.keySet().iterator();
	while(i.hasNext()) {
	    String questionID=i.next();
	    GridQuestion dataGridQuestion=questions.get(questionID);
	    if (dataGridQuestion.getId().equals(gridID)) {
		Grid grid = (Grid) dataGridQuestion.getAnswer().getValue();
		dataGridValues = grid.getValues();
		break;
	    }
	}
	currentCellValue = dataGridValues.get(cellID);
	currentGridID = gridID;
	currentCellId = cellID;
	return this;
    }

    public Boolean getCurrentCellValue() {
	return currentCellValue;
    }

    public void setCurrentCellValue(Boolean currentCellValue) {
	this.currentCellValue = currentCellValue;
	Iterator<String> i=questions.keySet().iterator();
	while(i.hasNext()) {
	    String questionID=i.next();
	    GridQuestion dataGridQuestion=questions.get(questionID);
	    if (dataGridQuestion.getId().equals(currentGridID)) {
		Grid grid = (Grid) dataGridQuestion.getAnswer().getValue();
		grid.getValues().put(currentCellId, currentCellValue);
		break;
	    }
	}
	System.out.println(currentCellValue+" "+currentCellId+" "+currentGridID);
	RequestContext.getCurrentInstance().update(currentGridID);
    }
}
