package com.engagepoint.labs.wizard.handler;

import java.util.ArrayList;

public class DataGridStoreObject {

    private String dataGridID;
    private ArrayList<Boolean> dataGridItems;

    public DataGridStoreObject(String id) {
	dataGridID = id;
    }

    public String getDataGridID() {
	return dataGridID;
    }

    public void setDataGridID(String dataGridID) {
	this.dataGridID = dataGridID;
    }

    public ArrayList<Boolean> getDataGridItems() {
	if (dataGridItems == null) {
	    dataGridItems = new ArrayList<Boolean>();
	}
	return dataGridItems;
    }

    public void setDataGridItems(ArrayList<Boolean> dataGridItems) {
	this.dataGridItems = dataGridItems;
    }
}
