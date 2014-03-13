package com.engagepoint.labs.wizard.handler;

import java.io.Serializable;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import org.primefaces.component.selectbooleancheckbox.SelectBooleanCheckbox;

@Named("dataGridHandler")
@SessionScoped
public class DataGridHandler implements Serializable {

    private static final long serialVersionUID = -1066387551691144085L;

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

    public ArrayList<SelectBooleanCheckbox> getGridByID(String id) {
	ArrayList<SelectBooleanCheckbox> dataGrid = null;
	for (DataGridStoreObject dataGridObject : grids) {
	    if (dataGridObject.getDataGridID().equals(id)) {
		dataGrid = dataGridObject.getDataGridItems();
		break;
	    }
	}
	return dataGrid;
    }
}
