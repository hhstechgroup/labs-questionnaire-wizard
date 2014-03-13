package com.engagepoint.labs.wizard.handler;

import java.util.ArrayList;

import org.primefaces.component.selectbooleancheckbox.SelectBooleanCheckbox;

public class DataGridStoreObject {

    private String dataGridID;
    private ArrayList<SelectBooleanCheckbox> dataGridItems;

    public DataGridStoreObject(String id) {
	dataGridID = id;
    }

    public String getDataGridID() {
	return dataGridID;
    }

    public void setDataGridID(String dataGridID) {
	this.dataGridID = dataGridID;
    }

    public ArrayList<SelectBooleanCheckbox> getDataGridItems() {
	return dataGridItems;
    }

    public void setDataGridItems(ArrayList<SelectBooleanCheckbox> dataGridItems) {
	this.dataGridItems = dataGridItems;
    }
}
