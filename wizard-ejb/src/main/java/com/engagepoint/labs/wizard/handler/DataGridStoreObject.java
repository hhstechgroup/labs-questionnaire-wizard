package com.engagepoint.labs.wizard.handler;

import java.util.ArrayList;

public class DataGridStoreObject {

    private String dataGridID;
    private ArrayList<String> dataGridItems;

    public DataGridStoreObject(String id, ArrayList<String> items) {
	dataGridID = id;
	dataGridItems = items;
    }

    public String getDataGridID() {
	return dataGridID;
    }

    public void setDataGridID(String dataGridID) {
	this.dataGridID = dataGridID;
    }

    public ArrayList<String> getDataGridItems() {
	return dataGridItems;
    }

    public void setDataGridItems(ArrayList<String> dataGridItems) {
	this.dataGridItems = dataGridItems;
    }
}
