package com.engagepoint.labs.wizard.values.objects;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by igor.guzenko on 2/17/14.
 */
public class Grid {
    public static String CELL_PREFIX = "gridcell_";
    private List<String> row;
    private List<String> column;
    private Map<String, Boolean> values;
    private String gridID;

    public Grid(List<String> row, List<String> column, List<String> values,
	    String gridID) {
	this.row = row;
	this.column = column;
	this.values = new LinkedHashMap<String, Boolean>();
	this.gridID = gridID;
	parseStrings(values);
    }

    private void parseStrings(List<String> values) {
	int checkBoxNumber = 0;
	for (String stringRow : values) {
	    String[] cellIDs = stringRow.split(",");
	    for (int i = 0; i < cellIDs.length; i++) {
		this.values.put(createCheckBoxID(gridID, checkBoxNumber),
			Boolean.parseBoolean(cellIDs[i]));
		checkBoxNumber++;
	    }
	}
    }

    public static String createCheckBoxID(String gridID, int cellNumber) {
	String id = CELL_PREFIX + gridID + "_"
		+ Integer.toString(cellNumber, 10);
	return id;
    }

    public static int getCheckBoxNumberFromID(String id) {
	int checkBoxIDNumberStartPosition = id.lastIndexOf("_") + 1;
	String checkBoxNumberString = id
		.substring(checkBoxIDNumberStartPosition);

	int currentCellNumber = Integer.parseInt(checkBoxNumberString);
	return currentCellNumber;
    }

    public List<String> getRow() {
	return row;
    }

    public void setRow(List<String> row) {
	this.row = row;
    }

    public List<String> getColumn() {
	return column;
    }

    public void setColumn(List<String> column) {
	this.column = column;
    }

    public Map<String, Boolean> getValues() {
	return values;
    }

    public void setValues(Map<String, Boolean> values) {
	this.values = values;
    }
}
