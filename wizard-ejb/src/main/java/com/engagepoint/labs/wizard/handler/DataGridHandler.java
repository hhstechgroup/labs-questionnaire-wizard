package com.engagepoint.labs.wizard.handler;

import java.io.Serializable;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import org.primefaces.context.RequestContext;

@Named("dataGridHandler")
@SessionScoped
public class DataGridHandler implements Serializable {

    private static final long serialVersionUID = -1066387551691144085L;

    private Boolean currentCellValue;
    private String currentGridID;
    private int currentCellIndex;

    private ArrayList<DataGridStoreObject> grids;

    public DataGridHandler() {
	grids = new ArrayList<DataGridStoreObject>();
    }

    @PostConstruct
    public void init() {
	grids = new ArrayList<DataGridStoreObject>();
    }

    public ArrayList<DataGridStoreObject> getGrids() {
	return grids;
    }

    public void setGrids(ArrayList<DataGridStoreObject> grids) {
	this.grids = grids;
    }

    public DataGridHandler setCellFromGridByID(String gridID, int cellIndex) {
	ArrayList<Boolean> dataGrid = null;
	for (DataGridStoreObject dataGridObject : grids) {
	    if (dataGridObject.getDataGridID().equals(gridID)) {
		dataGrid = dataGridObject.getDataGridItems();
		break;
	    }
	}
	currentCellValue = dataGrid.get(cellIndex);
	currentGridID = gridID;
	currentCellIndex = cellIndex;
	test(gridID, cellIndex);
	RequestContext.getCurrentInstance().update(currentGridID);
	return this;
    }

    public Boolean getCurrentCellValue() {
	return currentCellValue;
    }

    public void setCurrentCellValue(Boolean currentCellValue) {
	this.currentCellValue = currentCellValue;
	for (DataGridStoreObject dataGridObject : grids) {
	    if (dataGridObject.getDataGridID().equals(currentGridID)) {
		dataGridObject.getDataGridItems().set(currentCellIndex,
			currentCellValue);
		break;
	    }
	}
    }

    private void test(String gridID, int cellIndex) {
	System.out.println(gridID + " " + cellIndex);
	for (DataGridStoreObject dataGridObject : grids) {
	    if (dataGridObject.getDataGridID().equals(currentGridID)) {
		dataGridObject.getDataGridItems().set(
			dataGridObject.getDataGridItems().size() - 1,
			currentCellValue);
		break;
	    }
	}
    }
}
